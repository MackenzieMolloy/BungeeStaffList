package net.mackenziemolloy.bungee.staff.command;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.PluginManager;

import com.github.sirblobman.api.bungeecord.core.CorePlugin;
import com.github.sirblobman.api.bungeecord.hook.vanish.IVanishHook;
import com.github.sirblobman.api.utility.Validate;

import net.mackenziemolloy.bungee.staff.BungeeStaff;
import net.mackenziemolloy.bungee.staff.utility.MessageUtility;

public final class CommandStaffHide extends Command {
    private final BungeeStaff plugin;

    public CommandStaffHide(BungeeStaff plugin) {
        super("staffhide", "stafflist.staff", "stafflisthide");
        this.plugin = Validate.notNull(plugin, "plugin must not be null!");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof ProxiedPlayer player)) {
            String message = MessageUtility.color("&cOnly players can execute this command.");
            sendMessage(sender, message);
            return;
        }

        if(!player.hasPermission("stafflist.staff")) {
            BungeeStaff plugin = getPlugin();
            String message = MessageUtility.color(plugin.getMessage("no-permission"));
            sendMessage(player, message);
            return;
        }

        IVanishHook localVanishHook = getDefaultVanishHook();
        if(args.length > 0 && player.hasPermission("stafflist.hideothers")) {
            ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[0]);
            if(target == null) {
                String message = MessageUtility.color(plugin.getMessage("player-not-found"));
                sendMessage(sender, message);
            } else {
                boolean hidden = localVanishHook.isHidden(target);
                localVanishHook.setHidden(target, !hidden);
            }
        } else {
            boolean hidden = localVanishHook.isHidden(player);
            localVanishHook.setHidden(player, !hidden);
        }
    }

    private BungeeStaff getPlugin() {
        return this.plugin;
    }

    private ProxyServer getProxy() {
        BungeeStaff plugin = getPlugin();
        return plugin.getProxy();
    }

    private IVanishHook getDefaultVanishHook() {
        ProxyServer proxy = getProxy();
        PluginManager pluginManager = proxy.getPluginManager();
        CorePlugin corePlugin = (CorePlugin) pluginManager.getPlugin("SirBlobmanBungeeCore");
        return corePlugin.getDefaultVanishHook();
    }

    private void sendMessage(CommandSender sender, String messageString) {
        BaseComponent[] message = TextComponent.fromLegacyText(messageString);
        sender.sendMessage(message);
    }
}
