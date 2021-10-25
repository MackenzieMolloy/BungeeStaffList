package net.mackenziemolloy.bungee.staff;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.logging.Logger;

import net.mackenziemolloy.bungee.staff.command.CommandStaffHide;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

import net.mackenziemolloy.bungee.staff.command.CommandList;
import net.mackenziemolloy.bungee.staff.utility.CommentedConfiguration;
import net.md_5.bungee.config.Configuration;

public final class BungeeStaff extends Plugin {
    private final StaffManager staffManager;
    
    private CommentedConfiguration configuration;
    private CommentedConfiguration dataStorage;

    public BungeeStaff() {
        this.staffManager = new StaffManager(this);
        
        this.configuration = null;
        this.dataStorage = null;
    }
    
    @Override
    public void onLoad() {
        saveDefaultConfig();
    }
    
    @Override
    public void onEnable() {
        reloadConfig();
    
        ProxyServer proxy = getProxy();
        PluginManager pluginManager = proxy.getPluginManager();
        pluginManager.registerCommand(this, new CommandList(this));
        pluginManager.registerCommand(this, new CommandStaffHide(this));
        
        Logger logger = getLogger();
        logger.info("Loaded successfully, enjoy!");
    }
    
    public void reloadConfig() {
        File dataFolder = getDataFolder();
        File configFile = new File(dataFolder, "config.yml");
        File dataFile = new File(dataFolder, "data.yml");
        this.configuration = CommentedConfiguration.loadConfiguration(configFile);
        this.dataStorage = CommentedConfiguration.loadConfiguration(dataFile);
    }
    
    public StaffManager getStaffManager() {
        return this.staffManager;
    }
    
    public CommentedConfiguration getConfig() {
        return this.configuration;
    }
    public Configuration getFromConfig() { return this.configuration.getConfiguration(); }

    public CommentedConfiguration getDataStorage() {
        return this.dataStorage;
    }
    public Configuration getFromDataStorage() { return this.dataStorage.getConfiguration(); }
    
    public void saveDefaultConfig() {
        File dataFolder = getDataFolder();
        if(!dataFolder.exists()) {
            boolean makeFolder = dataFolder.mkdirs();
            if(!makeFolder) {
                throw new IllegalStateException("Failed to create plugin folder.");
            }
        }
        
        File configFile = new File(dataFolder, "config.yml");
        if(configFile.exists()) {
            return;
        }
        
        try (InputStream jarStream = getResourceAsStream("config.yml")) {
            boolean makeFile = configFile.createNewFile();
            if(!makeFile) {
                throw new IOException("Failed to create file 'config.yml'.");
            }
        
            Path configPath = configFile.toPath();
            Files.copy(jarStream, configPath, StandardCopyOption.REPLACE_EXISTING);
        } catch(IOException ex) {
            throw new IllegalStateException("Failed to create 'config.yml' because an error occurred:", ex);
        }

        File dataFile = new File(dataFolder, "data.yml");
        if(dataFile.exists()) {
            return;
        }

        try (InputStream jarStream = getResourceAsStream("data.yml")) {
            boolean makeFile = dataFile.createNewFile();
            if(!makeFile) {
                throw new IOException("Failed to create file 'config.ym'.");
            }

            Path dataPath = dataFile.toPath();
            Files.copy(jarStream, dataPath, StandardCopyOption.REPLACE_EXISTING);
        } catch(IOException ex) {
            throw new IllegalStateException("Failed to create 'data.yml' because an error occurred:", ex);
        }
    }
}
