package net.mackenziemolloy.bungee.staff.command;

import java.util.Objects;

import net.mackenziemolloy.bungee.staff.StaffManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import net.mackenziemolloy.bungee.staff.BungeeStaff;

public final class CommandStaffHide extends Command {
    private final BungeeStaff plugin;

    public CommandStaffHide(BungeeStaff plugin) {
        super("staffhide", "stafflist.staff", "stafflisthide");
        this.plugin = Objects.requireNonNull(plugin, "plugin must not be null!");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) sender;

            String noPermissionFormat = plugin.getConfig().getConfiguration().getString("no-permission");
            String noPermission = ChatColor.translateAlternateColorCodes('&', noPermissionFormat);
            if(!sender.hasPermission("stafflist.staff")) {
                sendMessage(sender, noPermission);
            } else if(args.length > 0 && sender.hasPermission("stafflist.hideothers")) {
                ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[0]);
                if(target == null) {
                    String messageFormat = plugin.getConfig().getConfiguration().getString("player-not-found");
                    String color = ChatColor.translateAlternateColorCodes('&', messageFormat);
                    sendMessage(sender, color);
                } else {
                    plugin.getStaffManager().setPlayerVisibility(target, player, !plugin.getFromDataStorage().getBoolean(target.getUUID()));
                }
            } else plugin.getStaffManager().setPlayerVisibility(player, !plugin.getFromDataStorage().getBoolean(player.getUniqueId().toString()));
        }

    }

    private void sendMessage(CommandSender sender, String messageString) {
        BaseComponent[] message = TextComponent.fromLegacyText(messageString);
        sender.sendMessage(message);
    }
}
