package net.mackenziemolloy.bungee.staff;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.config.Configuration;

import com.github.sirblobman.api.bungeecord.core.CorePlugin;
import com.github.sirblobman.api.bungeecord.hook.vanish.IVanishHook;
import com.github.sirblobman.api.bungeecord.premiumvanish.PremiumVanishHook;

import net.mackenziemolloy.bungee.staff.command.CommandList;
import net.mackenziemolloy.bungee.staff.listeners.PremiumVanishUpdate;
import net.mackenziemolloy.bungee.staff.utility.CommentedConfiguration;
import net.mackenziemolloy.bungee.staff.utility.MessageUtility;
import org.jetbrains.annotations.NotNull;

public final class BungeeStaff extends Plugin {
    private final StaffManager staffManager;
    private final Map<String, CommentedConfiguration> configurationMap;

    public BungeeStaff() {
        this.staffManager = new StaffManager(this);
        this.configurationMap = new HashMap<>();
    }

    @Override
    public void onLoad() {
        saveDefaultConfig();
    }

    @Override
    public void onEnable() {
        reloadConfig();
        syncConfig();

        ProxyServer proxy = getProxy();
        PluginManager pluginManager = proxy.getPluginManager();
        pluginManager.registerCommand(this, new CommandList(this));

        Logger logger = getLogger();
        logger.info("Loaded successfully, enjoy!");

        CorePlugin corePlugin = (CorePlugin) pluginManager.getPlugin("SirBlobmanBungeeCore");
        IVanishHook vanishHook = corePlugin.getVanishHook();
        if(vanishHook instanceof PremiumVanishHook) {
            pluginManager.registerListener(this, new PremiumVanishUpdate(this));
        }
    }

    public void syncConfig() {
        try {
            File dataFolder = getDataFolder();
            File configFile = new File(dataFolder, "config.yml");
            InputStream jarConfig = getResourceAsStream("config.yml");

            CommentedConfiguration configuration = getConfig();
            configuration.syncWithConfig(configFile, jarConfig);
        } catch (IOException ex) {
            throw new IllegalStateException("Failed to sync 'config.yml' because an error occurred:", ex);
        }
    }

    public void reloadConfig() {
        reloadConfig("config.yml");
        reloadConfig("data.yml");
    }

    public StaffManager getStaffManager() {
        return this.staffManager;
    }

    public CommentedConfiguration getDataStorage() {
        return getConfig("data.yml");
    }

    private CommentedConfiguration getConfig(String configName) {
        CommentedConfiguration configuration = this.configurationMap.getOrDefault(configName, null);
        if(configuration != null) {
            return configuration;
        }

        reloadConfig(configName);
        return getConfig(configName);
    }

    private void reloadConfig(String configName) {
        try {
            File dataFolder = getDataFolder();
            File configFile = new File(dataFolder, configName);
            if(!configFile.exists()) {
                Logger logger = getLogger();
                logger.info(configName + " does not exist, loading as empty configuration.");

                CommentedConfiguration emptyConfig = new CommentedConfiguration();
                emptyConfig.loadFromString("");

                this.configurationMap.put(configName, emptyConfig);
            }

            InputStream jarConfigStream = getResourceAsStream(configName);
            CommentedConfiguration configuration = CommentedConfiguration.loadConfiguration(configFile);
            configuration.syncWithConfig(configFile, jarConfigStream);

            this.configurationMap.put(configName, configuration);

        } catch(IOException ex) {
            Logger logger = getLogger();
            logger.log(Level.WARNING, "An error occurred while reloading '" + configName
                    + "', using an empty config.", ex);

            CommentedConfiguration emptyConfig = new CommentedConfiguration();
            emptyConfig.loadFromString("");

            this.configurationMap.put(configName, emptyConfig);
        }
    }

    private void reloadConfig(String configName, ProxiedPlayer player) {
        try {
            File dataFolder = getDataFolder();
            File configFile = new File(dataFolder, configName);
            if(!configFile.exists()) {
                Logger logger = getLogger();
                logger.info(configName + " does not exist, loading as empty configuration.");

                CommentedConfiguration emptyConfig = new CommentedConfiguration();
                emptyConfig.loadFromString("");

                this.configurationMap.put(configName, emptyConfig);
            }

            InputStream jarConfigStream = getResourceAsStream(configName);
            CommentedConfiguration configuration = CommentedConfiguration.loadConfiguration(configFile);
            configuration.syncWithConfig(configFile, jarConfigStream);

            this.configurationMap.put(configName, configuration);
            sendMessage(player, getFromConfig().getString("config-reloaded"));
        } catch(IOException ex) {
            sendMessage(player, "&cAn error has occurred whilst reloading the config, " +
                    "please check console.");

            Logger logger = getLogger();
            logger.log(Level.WARNING, "An error occurred while reloading '" + configName
                    + "', using an empty config.", ex);

            CommentedConfiguration emptyConfig = new CommentedConfiguration();
            emptyConfig.loadFromString("");

            this.configurationMap.put(configName, emptyConfig);
        }

    }

    public void saveConfig(String configName) {
        try {
            CommentedConfiguration configuration = getConfig(configName);
            String configurationString = configuration.saveToString();

            File dataFolder = getDataFolder();
            File configFile = new File(dataFolder, configName);

            Path configPath = configFile.toPath();
            Files.writeString(configPath, configurationString, StandardCharsets.UTF_8,
                    StandardOpenOption.WRITE, StandardOpenOption.CREATE);
        } catch(IOException ex) {
            Logger logger = getLogger();
            logger.log(Level.WARNING, "An error occurred while saving a configuration file:", ex);
        }
    }

    public CommentedConfiguration getConfig() {
        return getConfig("config.yml");
    }

    public Configuration getFromConfig() {
        CommentedConfiguration config = getConfig();
        return config.getConfiguration();
    }

    public Configuration getFromDataStorage() {
        CommentedConfiguration config = getDataStorage();
        return config.getConfiguration();
    }

    private void saveDefaultConfig() {
        File dataFolder = getDataFolder();
        if (!dataFolder.exists()) {
            boolean makeFolder = dataFolder.mkdirs();
            if (!makeFolder) {
                throw new IllegalStateException("Failed to create plugin folder.");
            }
        }

        File configFile = new File(dataFolder, "config.yml");
        if (configFile.exists()) {
            return;
        }

        try (InputStream jarStream = getResourceAsStream("config.yml")) {
            boolean makeFile = configFile.createNewFile();
            if (!makeFile) {
                throw new IOException("Failed to create file 'config.ym'.");
            }

            Path configPath = configFile.toPath();
            Files.copy(jarStream, configPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            throw new IllegalStateException("Failed to create 'config.yml' because an error occurred:", ex);
        }
    }

    @NotNull
    public String getMessage(String path) {
        CommentedConfiguration config = getConfig();
        Configuration configuration = config.getConfiguration();
        String missingPath = String.format(Locale.US, "{%s}", path);

        // If the config is missing the message, show the missing path.
        if (!configuration.contains(path)) {
            return missingPath;
        }

        // Get the object at the config path.
        Object object = configuration.get(path);

        // If the object is already a string, use it.
        if (object instanceof String string) {
            return string;
        }

        // If the object is a list, turn it into a string with `\n`.
        if (object instanceof List objectList) {
            List<String> messageList = new ArrayList<>();
            for (Object messageObject : objectList) {
                String messageLine = String.valueOf(messageObject);
                messageList.add(messageLine);
            }

            return String.join("\n", messageList);
        }

        // If the object is not a string or a list, show it as a missing path.
        return missingPath;
    }

    public void sendMessage(CommandSender sender, String messageString) {
        if(messageString.isEmpty()) {
            return;
        }

        String messageColored = MessageUtility.color(messageString);
        BaseComponent[] message = TextComponent.fromLegacyText(messageColored);
        sender.sendMessage(message);
    }
}
