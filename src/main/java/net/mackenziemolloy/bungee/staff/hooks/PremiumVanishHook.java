package net.mackenziemolloy.bungee.staff.hooks;

import java.util.UUID;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

import de.myzelyam.api.vanish.VanishAPI;
import net.mackenziemolloy.bungee.staff.BungeeStaff;

public final class PremiumVanishHook extends Hook {
    public PremiumVanishHook(BungeeStaff plugin) {
        super(plugin);
    }
    
    public boolean isEnabled() {
        PluginManager pluginManager = getPluginManager();
        Plugin premiumVanish = pluginManager.getPlugin("PremiumVanish");
        return (premiumVanish != null);
    }
    
    public boolean isVanished(UUID playerId) {
        return VanishAPI.isInvisibleOffline(playerId);
    }
}
