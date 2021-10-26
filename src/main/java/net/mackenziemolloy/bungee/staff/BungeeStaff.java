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

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.config.Configuration;

import net.mackenziemolloy.bungee.staff.command.CommandList;
import net.mackenziemolloy.bungee.staff.command.CommandStaffHide;
import net.mackenziemolloy.bungee.staff.utility.CommentedConfiguration;

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
        ProxyServer proxy = getProxy();
        PluginManager pluginManager = proxy.getPluginManager();
        pluginManager.registerCommand(this, new CommandList(this));
        pluginManager.registerCommand(this, new CommandStaffHide(this));
        
        Logger logger = getLogger();
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
}
