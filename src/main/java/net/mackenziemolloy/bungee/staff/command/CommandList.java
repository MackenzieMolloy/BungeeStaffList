package net.mackenziemolloy.bungee.staff.command;

import java.util.List;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

import com.github.sirblobman.api.utility.Validate;

import net.mackenziemolloy.bungee.staff.BungeeStaff;
import net.mackenziemolloy.bungee.staff.StaffManager;
import net.mackenziemolloy.bungee.staff.StaffMember;

public final class CommandList extends Command {
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

    private void sendConfigReloadMessage(CommandSender sender) {
        BungeeStaff plugin = getPlugin();
        String messageFormat = plugin.getMessage("config-reloaded");
        if (messageFormat.isBlank()) {
            return;
        }

        String color = ChatColor.translateAlternateColorCodes('&', messageFormat);
        BaseComponent[] message = TextComponent.fromLegacyText(color);
        sender.sendMessage(message);
    }

    private void showStaffList(CommandSender sender) {
        StaffManager staffManager = getStaffManager();
        List<StaffMember> staffList = staffManager.getOnlineStaff(false);

        String processStaffList = staffManager.processStaffList(staffList);
        String messageFormat = ChatColor.translateAlternateColorCodes('&', processStaffList);
        BaseComponent[] message = TextComponent.fromLegacyText(messageFormat);
        sender.sendMessage(message);
    }
}
