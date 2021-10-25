package net.mackenziemolloy.bungee.staff.command;

import java.util.List;
import java.util.Objects;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.config.Configuration;

import net.mackenziemolloy.bungee.staff.BungeeStaff;
import net.mackenziemolloy.bungee.staff.StaffManager;
import net.mackenziemolloy.bungee.staff.StaffMember;
import net.mackenziemolloy.bungee.staff.utility.CommentedConfiguration;

public class CommandList extends Command {
    private final BungeeStaff plugin;

    public CommandList(BungeeStaff plugin) {
        super("bungeestaff","","staff","stafflist","liststaff");
        this.plugin = Objects.requireNonNull(plugin, "plugin must not be null!");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length > 0 && args[0].equalsIgnoreCase("reload")) {
            if(sender.hasPermission("stafflist.reload")) {
                getPlugin().reloadConfig();
                sendConfigReloadMessage(sender);
                return;
            }
        }
        
        showStaffList(sender);
    }
    
    private BungeeStaff getPlugin() {
        return this.plugin;
    }
    
    private StaffManager getStaffManager() {
        BungeeStaff plugin = getPlugin();
        return plugin.getStaffManager();
    }
    
    private Configuration getConfiguration() {
        BungeeStaff plugin = getPlugin();
        CommentedConfiguration config = plugin.getConfig();
        return config.getConfiguration();
    }
    
    private void sendConfigReloadMessage(CommandSender sender) {
        Configuration configuration = getConfiguration();
        String messageFormat = configuration.getString("config-reloaded");
        if(messageFormat == null || messageFormat.isEmpty()) {
            return;
        }
        
        String color = ChatColor.translateAlternateColorCodes('&', messageFormat);
        BaseComponent[] message = TextComponent.fromLegacyText(color);
        sender.sendMessage(message);
    }
    
    private void showStaffList(CommandSender sender) {
        StaffManager staffManager = getStaffManager();
        List<StaffMember> staffList = staffManager.getOnlineStaff(false);
        
        String messageFormat = ChatColor.translateAlternateColorCodes('&', staffManager.processStaffList(staffList));
        BaseComponent[] message = TextComponent.fromLegacyText(messageFormat);
        sender.sendMessage(message);
    }
}
