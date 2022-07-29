package net.mackenziemolloy.bungee.staff;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.config.Configuration;

import com.github.sirblobman.api.bungeecord.luckperms.ILuckPermsHookPlugin;
import com.github.sirblobman.api.bungeecord.luckperms.LuckPermsHook;

import net.mackenziemolloy.bungee.staff.command.CommandList;
import net.mackenziemolloy.bungee.staff.utility.CommentedConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class BungeeStaff extends Plugin implements ILuckPermsHookPlugin {
    private final StaffManager staffManager;

    private CommentedConfiguration configuration;
    private LuckPermsHook luckPermsHook;

    public BungeeStaff() {
        this.staffManager = new StaffManager(this);

        this.configuration = null;
        this.luckPermsHook = null;
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
    }

    @Override
    public void setupLuckPermsHook() {
        CommentedConfiguration config = getConfig();
        Configuration configuration = config.getConfiguration();
        if (configuration.getBoolean("hooks.luckperms")) {
            this.luckPermsHook = new LuckPermsHook(this);
            if (this.luckPermsHook.isDisabled()) {
                this.luckPermsHook = null;
            }
        } else {
            this.luckPermsHook = null;
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
        File dataFolder = getDataFolder();
        File configFile = new File(dataFolder, "config.yml");
        this.configuration = CommentedConfiguration.loadConfiguration(configFile);

        setupLuckPermsHook();
    }

    public StaffManager getStaffManager() {
        return this.staffManager;
    }

    public CommentedConfiguration getConfig() {
        return this.configuration;
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

        // If the config is missing the message, show the missing path.
        if (!configuration.contains(path)) {
            return String.format(Locale.US, "{%s}", path);
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
        return String.format(Locale.US, "{%s}", path);
    }

    @Nullable
    public LuckPermsHook getLuckPermsHook() {
        return this.luckPermsHook;
    }
}
