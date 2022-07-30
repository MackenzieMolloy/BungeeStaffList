package net.mackenziemolloy.bungee.staff.hooks;

import java.util.List;
import java.util.UUID;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

import com.github.sirblobman.api.utility.Validate;

import de.myzelyam.api.vanish.BungeeVanishAPI;
import net.mackenziemolloy.bungee.staff.BungeeStaff;

public final class PremiumVanishHook {
    private final BungeeStaff plugin;

    public PremiumVanishHook(BungeeStaff plugin) {
        this.plugin = Validate.notNull(plugin, "plugin must not be null!");
    }

    public Plugin getPlugin() {
        return this.plugin;
    }

    /**
     * Check if PremiumVanish is disabled or does not exist.
     * @return {@code true} if LuckPerms doesn't exist on the proxy, otherwise {@code false}.
     */
    public boolean isDisabled() {
        Plugin plugin = getPlugin();
        ProxyServer proxy = plugin.getProxy();
        PluginManager pluginManager = proxy.getPluginManager();

        Plugin luckPerms = pluginManager.getPlugin("PremiumVanish");
        return (luckPerms == null);
    }

    /**
     * Check if a player is currently vanished.
     * @param player The {@link ProxiedPlayer} to check.
     * @return {@code true} if the player is vanished, otherwise {@code false}.
     */
    public boolean isHidden(ProxiedPlayer player) {
        return BungeeVanishAPI.isInvisible(player);
    }

    /**
     * Check if a player is currently vanished.
     * @param playerId The {@link UUID} of the player to check.
     * @return {@code true} if the player is vanished, otherwise {@code false}.
     */
    public boolean isHidden(UUID playerId) {
        List<UUID> invisiblePlayerList = BungeeVanishAPI.getInvisiblePlayers();
        return invisiblePlayerList.contains(playerId);
    }
}
