/*

    TO-DO
    - Unregister and re-register PremiumVanish hook on config reload
    - Test staff hiding others
    - Clean-up code

 */


package net.mackenziemolloy.bungee.staff;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.mackenziemolloy.bungee.staff.hooks.LuckPermsHook;
import net.mackenziemolloy.bungee.staff.hooks.PremiumVanishHook;
import net.mackenziemolloy.bungee.staff.listeners.PremiumVanishUpdate;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.config.Configuration;

import net.mackenziemolloy.bungee.staff.command.CommandList;
import net.mackenziemolloy.bungee.staff.command.CommandStaffHide;
import net.mackenziemolloy.bungee.staff.utility.CommentedConfiguration;
import org.bstats.bungeecord.Metrics;

public final class BungeeStaff extends Plugin {
    private final StaffManager staffManager;
    private final Map<String, CommentedConfiguration> configurationMap;
    
    public BungeeStaff() {
        this.staffManager = new StaffManager(this);
        this.configurationMap = new HashMap<>();
    }
    
    @Override
    public void onLoad() {
        saveDefaultConfig("config.yml");
        saveDefaultConfig("data.yml");
    }
    
    @Override
    public void onEnable() {
        Logger logger = getLogger();

        int pluginId = 13319;
        Metrics metrics = new Metrics(this, pluginId);

        reloadConfig("config.yml");
        reloadConfig("data.yml");
        
        ProxyServer proxy = getProxy();
        PluginManager pluginManager = proxy.getPluginManager();
        pluginManager.registerCommand(this, new CommandList(this));
        pluginManager.registerCommand(this, new CommandStaffHide(this));

        if(getFromConfig().getBoolean("hooks.premiumvanish")) {
            PremiumVanishHook premiumVanishHook = new PremiumVanishHook(this);
            if(premiumVanishHook.isEnabled()) {
                pluginManager.registerListener(this, new PremiumVanishUpdate(this));
            }
            else {
                logger.log(Level.SEVERE, "You have the PremiumVanish hook enabled, but the plugin wasn't detected so the hook wasn't enabled.");

            }
        }

        if(getFromConfig().getBoolean("hooks.luckperms")) {
            LuckPermsHook luckPermsHook = new LuckPermsHook(this);
            if(luckPermsHook.isEnabled()) {
                //pluginManager.registerListener(this, new PremiumVanishUpdate(this));
            }
            else {
                logger.log(Level.SEVERE, "You have the LuckPerms hook enabled, but the plugin wasn't detected so the hook wasn't enabled.");

            }
        }

        logger.info("Loaded successfully, enjoy!");
    }
    
    public StaffManager getStaffManager() {
        return this.staffManager;
    }
    
    public CommentedConfiguration getConfig() {
        return getConfig("config.yml");
    }
    
    public Configuration getFromConfig() {
        return getConfig().getConfiguration();
    }
    
    public void reloadConfig() {
        reloadConfig("config.yml");
    }

    public void reloadConfig(ProxiedPlayer player) {
        reloadConfig("config.yml", player);
    }
    
    public CommentedConfiguration getDataStorage() {
        return getConfig("data.yml");
    }
    
    public Configuration getFromDataStorage() {
        return getDataStorage().getConfiguration();
    }
    
    private void saveResource(String name) {
        try(InputStream stream = getResourceAsStream(name)) {
            File dataFolder = getDataFolder();
            File file = new File(dataFolder, name);
            
            if(!file.exists()) {
                boolean createFile = file.createNewFile();
                if(!createFile) {
                    String absolutePath = file.getAbsolutePath();
                    throw new IOException("createFile returned false for '" + absolutePath + "'.");
                }
            }
            
            Path filePath = file.toPath();
            Files.copy(stream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch(IOException ex) {
            Logger logger = getLogger();
            logger.log(Level.WARNING, "An error occurred while saving a resource:", name);
        }
    }
    
    private void saveDefaultConfig(String configName) {
        File dataFolder = getDataFolder();
        if(!dataFolder.exists()) {
            boolean makeFolder = dataFolder.mkdirs();
            if(!makeFolder) {
                throw new IllegalStateException("Failed to create plugin data folder.");
            }
        }
        
        File configFile = new File(dataFolder, configName);
        if(!configFile.exists()) {
            saveResource(configName);
        }
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

            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cAn error has occurred whilst reloading the config, please check console."));
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
            byte[] configurationData = configurationString.getBytes(StandardCharsets.UTF_8);
            
            File dataFolder = getDataFolder();
            File configFile = new File(dataFolder, configName);
            
            Path configPath = configFile.toPath();
            Files.write(configPath, configurationData, StandardOpenOption.WRITE, StandardOpenOption.CREATE);
        } catch(IOException ex) {
            Logger logger = getLogger();
            logger.log(Level.WARNING, "An error occurred while saving a configuration file:", ex);
        }
    }

    private void sendMessage(CommandSender sender, String messageString) {
        if(messageString.isEmpty()) return;
        BaseComponent[] message = TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', messageString));
        sender.sendMessage(message);
    }
}
