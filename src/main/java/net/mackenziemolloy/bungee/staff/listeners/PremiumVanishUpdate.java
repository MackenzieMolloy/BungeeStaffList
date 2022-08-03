package net.mackenziemolloy.bungee.staff.listeners;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.event.EventHandler;

import com.github.sirblobman.api.bungeecord.core.CorePlugin;
import com.github.sirblobman.api.bungeecord.hook.vanish.IVanishHook;

import de.myzelyam.api.vanish.BungeePlayerHideEvent;
import de.myzelyam.api.vanish.BungeePlayerShowEvent;
import net.mackenziemolloy.bungee.staff.BungeeStaff;

public final class PremiumVanishUpdate implements Listener {
    private final BungeeStaff plugin;

    public PremiumVanishUpdate(BungeeStaff plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onVanish(BungeePlayerHideEvent e) {
        ProxiedPlayer player = e.getPlayer();
        getDefaultVanishHook().setHidden(player, true);
    }

    @EventHandler
    public void onShow(BungeePlayerShowEvent e) {
        ProxiedPlayer player = e.getPlayer();
        getDefaultVanishHook().setHidden(player, false);
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
}
