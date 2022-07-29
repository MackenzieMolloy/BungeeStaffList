package net.mackenziemolloy.bungee.staff;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.config.Configuration;

import com.github.sirblobman.api.bungeecord.luckperms.LuckPermsHook;
import com.github.sirblobman.api.utility.Validate;

import de.myzelyam.api.vanish.BungeeVanishAPI;
import net.mackenziemolloy.bungee.staff.utility.CommentedConfiguration;
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

    private Configuration getConfiguration() {
        BungeeStaff plugin = getPlugin();
        CommentedConfiguration config = plugin.getConfig();
        return config.getConfiguration();
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

        BungeeStaff plugin = getPlugin();
        LuckPermsHook luckPermsHook = plugin.getLuckPermsHook();
        if (luckPermsHook != null) {
            UUID playerId = getPlayerId();
            return (this.prefix = luckPermsHook.getPrefix(playerId));
        }

        ProxiedPlayer player = getProxiedPlayer();
        Configuration group = getGroup(player);
        String prefix = group.getString("prefix");
        return (this.prefix = (prefix == null || prefix.isBlank() ? "" : prefix));
    }

    public int getGroupWeight() {
        if (this.groupWeight != null) {
            return this.groupWeight;
        }

        BungeeStaff plugin = getPlugin();
        LuckPermsHook luckPermsHook = plugin.getLuckPermsHook();
        if (luckPermsHook != null) {
            UUID playerId = getPlayerId();
            return (this.groupWeight = luckPermsHook.getWeight(playerId, 0));
        }

        ProxiedPlayer player = getProxiedPlayer();
        Configuration group = getGroup(player);
        return (this.groupWeight = group.getInt("weight", 0));
    }

    public Configuration getGroup(ProxiedPlayer player) {
        Configuration configuration = getConfiguration();
        Configuration sectionRanks = configuration.getSection("ranks");
        List<String> rankKeys = new ArrayList<>(sectionRanks.getKeys());

        int rankKeysSize = rankKeys.size();
        if (rankKeysSize <= 0) {
            return sectionRanks;
        }

        String highestRank = null;
        int highestWeight = Integer.MIN_VALUE;
        for (String rank : rankKeys) {
            String permissionName = ("stafflist.rank." + rank);
            if (!player.hasPermission(permissionName)) {
                continue;
            }

            String weightPath = (rank + ".weight");
            if (!sectionRanks.contains(weightPath)) {
                continue;
            }

            int weight = sectionRanks.getInt(weightPath);
            if (weight > highestWeight) {
                highestWeight = weight;
                highestRank = rank;
            }
        }

        if (highestRank == null) {
            return sectionRanks;
        }

        Configuration section = sectionRanks.getSection(highestRank);
        return (section == null ? sectionRanks : section);
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
