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
        super("bungeestaff", "", "staff", "stafflist", "liststaff");
        this.plugin = Validate.notNull(plugin, "plugin must not be null!");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
            if (sender.hasPermission("stafflist.reload")) {
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

        String color = MessageUtility.color(messageFormat);
        BaseComponent[] message = TextComponent.fromLegacyText(color);
        sender.sendMessage(message);
    }
    
    private void showStaffList(CommandSender sender) {
        StaffManager staffManager = getStaffManager();
        List<StaffMember> staffList = staffManager.getOnlineStaff(false);

        String processStaffList = staffManager.processStaffList(staffList);
        String messageFormat = MessageUtility.color(processStaffList);

        BaseComponent[] message = TextComponent.fromLegacyText(messageFormat);
        sender.sendMessage(message);

        if(!plugin.getFromConfig().getBoolean("notice-understood")) {

            Boolean notified = false;

            LuckPermsHook luckPermsHook = new LuckPermsHook(plugin);
            if(luckPermsHook.isEnabled() && !plugin.getFromConfig().getBoolean("hooks.luckperms")) {
                sender.sendMessage(MessageUtility.color("&c&lHEY! &7It appears you are running &eLuckPerms&7, for ranks from the plugin to be displayed in the stafflist - please enable the hook in the &f&nconfig.yml&7."));
                notified = true;
            }

            PremiumVanishHook premiumVanishHook = new PremiumVanishHook(plugin);
            if(premiumVanishHook.isEnabled() && !plugin.getFromConfig().getBoolean("hooks.premiumvanish")) {
                sender.sendMessage(MessageUtility.color("&c&lHEY! &7It appears you are running &ePremiumVanish&7, for vanished players to be automatically hidden from stafflist - please enable the hook in the &f&nconfig.yml&7."));
                notified = true;
            }

            if(notified) sender.sendMessage(MessageUtility.color("&7\n&cTo hide the above hook messages, set &7understood-notices&c to true or enable the hook in the config.yml.&7"));

        }
    }
}
