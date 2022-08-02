package net.mackenziemolloy.bungee.staff;

import java.util.UUID;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.plugin.PluginManager;

import com.github.sirblobman.api.bungeecord.core.CorePlugin;
import com.github.sirblobman.api.bungeecord.hook.permission.IPermissionHook;
import com.github.sirblobman.api.utility.Validate;

import de.myzelyam.api.vanish.BungeeVanishAPI;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class StaffMember implements Comparable<StaffMember> {
    private final BungeeStaff plugin;

    private final UUID playerId;
    private final String username;
    private final String displayName;

    private transient String prefix;
    private transient Integer groupWeight;

    public StaffMember(BungeeStaff plugin, ProxiedPlayer player) {
        this.plugin = Validate.notNull(plugin, "plugin must not be null!");
        Validate.notNull(player, "player must not be null!");

        this.playerId = player.getUniqueId();
        this.username = player.getName();
        this.displayName = player.getDisplayName();

        this.prefix = null;
        this.groupWeight = null;
    }

    private BungeeStaff getPlugin() {
        return this.plugin;
    }

    private IPermissionHook getPermissionHook() {
        BungeeStaff plugin = getPlugin();
        ProxyServer proxy = plugin.getProxy();
        PluginManager pluginManager = proxy.getPluginManager();
        CorePlugin corePlugin = (CorePlugin) pluginManager.getPlugin("SirBlobmanBungeeCore");
        return corePlugin.getPermissionHook();
    }

    public UUID getPlayerId() {
        return this.playerId;
    }

    public String getUsername() {
        return this.username;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    @Nullable
    public ProxiedPlayer getProxiedPlayer() {
        UUID playerId = getPlayerId();
        ProxyServer proxy = ProxyServer.getInstance();
        return proxy.getPlayer(playerId);
    }

    public boolean isHidden() {
        ProxiedPlayer player = getProxiedPlayer();
        if (player == null) {
            return false;
        }
        return BungeeVanishAPI.isInvisible(player);
    }

    @NotNull
    public String getPrefix() {
        if (this.prefix != null) {
            return this.prefix;
        }

        UUID playerId = getPlayerId();
        IPermissionHook permissionHook = getPermissionHook();
        return (this.prefix = permissionHook.getPrefix(playerId));
    }

    public int getGroupWeight() {
        if (this.groupWeight != null) {
            return this.groupWeight;
        }

        UUID playerId = getPlayerId();
        IPermissionHook permissionHook = getPermissionHook();
        return (this.groupWeight = permissionHook.getPrimaryGroupWeight(playerId, 0));
    }

    @NotNull
    public String getServer() {
        ProxiedPlayer player = getProxiedPlayer();
        if (player == null) {
            return "";
        }

        Server server = player.getServer();
        String serverName = server.getInfo().getName();
        return this.plugin.getStaffManager().getServerAlias(serverName);
    }

    @Override
    public int compareTo(StaffMember other) {
        int weight1 = this.getGroupWeight();
        int weight2 = other.getGroupWeight();
        if (weight1 == weight2) {
            String name1 = this.getUsername();
            String name2 = other.getUsername();
            return name1.compareTo(name2);
        }

        return Integer.compare(weight2, weight1);
    }
}
