package net.mackenziemolloy.bungee.staff;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.logging.Logger;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

import net.mackenziemolloy.bungee.staff.utility.CommentedConfiguration;

public final class BungeeStaff extends Plugin {
    private CommentedConfiguration configuration;

    public BungeeStaff() {
        this.configuration = null;
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
        pluginManager.registerCommand(this, new ListCommand(this));
        
        Logger logger = getLogger();
        logger.info("Loaded successfully, enjoy!");
    }
    
    public void reloadConfig() {
        File dataFolder = getDataFolder();
        File configFile = new File(dataFolder, "config.yml");
        this.configuration = CommentedConfiguration.loadConfiguration(configFile);
    }
    
    public CommentedConfiguration getConfig() {
        return this.configuration;
    }
    
    private void saveDefaultConfig() {
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
                throw new IOException("Failed to create file 'config.ym'.");
            }
        
            Path configPath = configFile.toPath();
            Files.copy(jarStream, configPath, StandardCopyOption.REPLACE_EXISTING);
        } catch(IOException ex) {
            throw new IllegalStateException("Failed to create 'config.yml' because an error occurred:", ex);
        }
    }
}
