package net.mackenziemolloy.bungee.staff.command;

import net.mackenziemolloy.bungee.staff.BungeeStaff;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.Objects;

public class CommandStaffHide extends Command {
    private final BungeeStaff plugin;

    public CommandStaffHide(BungeeStaff plugin) {
        super("staffhide", "stafflist.staff", "stafflisthide");
        this.plugin = Objects.requireNonNull(plugin, "plugin must not be null!");
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {

        if(commandSender instanceof ProxiedPlayer) {
            String noPermission = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getConfiguration().getString("no-permission"));
            if(commandSender.hasPermission("stafflist.staff") == false) commandSender.sendMessage(noPermission);

            else if(args.length > 0 && commandSender.hasPermission("stafflist.hideothers")) {
                ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[0]);
                if(target == null) ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getConfiguration().getString("player-not-found"));
                togglePlayerVisibility(target, (ProxiedPlayer)commandSender);
            }

            else togglePlayerVisibility((ProxiedPlayer)commandSender);
        }

    }

    public void togglePlayerVisibility(ProxiedPlayer player) {
        Boolean playerHideState = plugin.getFromDataStorage().getBoolean(player.getUniqueId().toString());
        String playerHideToggledMsg = ChatColor.translateAlternateColorCodes('&', plugin.getFromConfig().getString("staffhide.toggle"));
        if(playerHideState == null || playerHideState == false) {
            plugin.getFromDataStorage().set(player.getUniqueId().toString(), true);
            player.sendMessage(playerHideToggledMsg.replace("{state}",ChatColor.translateAlternateColorCodes('&', plugin.getFromConfig().getString("staffhide.enabled-placeholder"))));
            plugin.generateFiles();
        }
        else {
            plugin.getFromDataStorage().set(player.getUniqueId().toString(), false);
            player.sendMessage(playerHideToggledMsg.replace("{state}",ChatColor.translateAlternateColorCodes('&', plugin.getFromConfig().getString("staffhide.disabled-placeholder"))));

        }
    }

    public void togglePlayerVisibility(ProxiedPlayer target, ProxiedPlayer sender) {
        if(target == sender) togglePlayerVisibility(sender);
    }

}