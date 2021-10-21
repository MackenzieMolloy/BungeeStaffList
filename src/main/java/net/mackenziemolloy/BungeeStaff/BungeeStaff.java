package net.mackenziemolloy.BungeeStaff;

import net.mackenziemolloy.BungeeStaff.Utils.CommentedConfiguration;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.concurrent.CompletableFuture;

public class BungeeStaff extends Plugin {

    public static BungeeStaff bungeeStaff;
    public CommentedConfiguration configFile;

    public void onEnable() {
        bungeeStaff = this;

        generateFiles();
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new ListCommand(this));
        this.getLogger().info("[BungeeStaffList] Loaded Successfully - Enjoy!");

    }

    public void generateFiles() {
        if (!getDataFolder().exists()) getDataFolder().mkdir();

        File file = new File(getDataFolder(), "config.yml");
        if(!file.exists()) {
            try (InputStream in = getResourceAsStream("config.yml")) { Files.copy(in, file.toPath()); }
            catch (IOException ex) { ex.printStackTrace(); }
        }

        configFile = CommentedConfiguration.loadConfiguration(file);
        try { configFile.syncWithConfig(file, getResourceAsStream("config.yml"), "server-aliases"); }
        catch (IOException e) {e.printStackTrace();}

    }

    public void reloadConfig(CommandSender commandSender) {

        CompletableFuture<Void> future = CompletableFuture.runAsync(bungeeStaff::generateFiles);
        future.whenComplete((success, error) -> {
            if(error != null) {
                commandSender.sendMessage("An error occurred, check the console!");
                error.printStackTrace();
            } else {
                String configReloadedMsg = ChatColor.translateAlternateColorCodes('&', BungeeStaff.bungeeStaff.configFile.getConfiguration().getString("config-reloaded"));
                commandSender.sendMessage(configReloadedMsg);
            }
        });

    }

}
