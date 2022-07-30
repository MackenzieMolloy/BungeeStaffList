package net.mackenziemolloy.bungee.staff.hooks;

import java.util.Objects;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.PluginManager;

import net.mackenziemolloy.bungee.staff.BungeeStaff;

public abstract class Hook {
    private final BungeeStaff plugin;
    
    public Hook(BungeeStaff plugin) {
        this.plugin = Objects.requireNonNull(plugin, "plugin must not be null!");
    }
    
    protected final BungeeStaff getPlugin() {
        return this.plugin;
    }
    
    protected final ProxyServer getProxy() {
        BungeeStaff plugin = getPlugin();
        return plugin.getProxy();
    }
    
    protected final PluginManager getPluginManager() {
        ProxyServer proxy = getProxy();
        return proxy.getPluginManager();
    }
    
    public abstract boolean isEnabled();
}
