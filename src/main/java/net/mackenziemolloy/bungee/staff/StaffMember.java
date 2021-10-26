package net.mackenziemolloy.bungee.staff;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.config.Configuration;

import de.myzelyam.api.vanish.BungeeVanishAPI;
import net.mackenziemolloy.bungee.staff.hooks.LuckPermsHook;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class StaffMember implements Comparable<StaffMember> {
    private final BungeeStaff plugin;
    
    private final UUID playerId;
    private final String username;
    private final String displayName;
    
    public StaffMember(BungeeStaff plugin, ProxiedPlayer player) {
        this.plugin = Objects.requireNonNull(plugin, "plugin must not be null!");
        Objects.requireNonNull(player, "player must not be null!");
        
        this.playerId = player.getUniqueId();
        this.username = player.getName();
        this.displayName = player.getDisplayName();
    }
    
    private BungeeStaff getPlugin() {
        return this.plugin;
    }
    
    public UUID getPlayerId() {
        return this.playerId;
    }
    
    public String getUsername() {
        return this.username;
    }
    
    @Nullable
    public ProxiedPlayer getProxiedPlayer() {
        UUID playerId = getPlayerId();
        ProxyServer proxy = ProxyServer.getInstance();
        return proxy.getPlayer(playerId);
    }
    
    public boolean isHidden() {
        ProxiedPlayer player = getProxiedPlayer();
        if(player == null) {
            return false;
        }
        return BungeeVanishAPI.isInvisible(player);
    }
    
    @NotNull
    public String getPrefix() {
        BungeeStaff plugin = getPlugin();
        Configuration configuration = plugin.getFromConfig();
        if(configuration.getBoolean("hooks.luckperms")) {
            LuckPermsHook luckPermsHook = new LuckPermsHook(plugin);
            if(luckPermsHook.isEnabled()) {
                UUID playerId = getPlayerId();
                return luckPermsHook.getPrefix(playerId);
            }
        }
        
        ProxiedPlayer player = getProxiedPlayer();
        Configuration group = getGroup(player);
        return group.getString("prefix", "");
    }
    
    public int getGroupWeight() {
        BungeeStaff plugin = getPlugin();
        Configuration configuration = plugin.getFromConfig();
        if(configuration.getBoolean("hooks.luckperms")) {
            LuckPermsHook luckPermsHook = new LuckPermsHook(plugin);
            if(luckPermsHook.isEnabled()) {
                UUID playerId = getPlayerId();
                return luckPermsHook.getWeight(playerId);
            }
        }
        
        ProxiedPlayer player = getProxiedPlayer();
        Configuration group = getGroup(player);
        return group.getInt("weight", 0);
    }
    
    public Configuration getGroup(ProxiedPlayer player) {
        Configuration ranks = plugin.getFromConfig().getSection("ranks");
        List<String> rankKeys = new ArrayList<>(ranks.getKeys());
        
        int highestGroup = 0;
        for(int i = 0; i < rankKeys.size(); i++) {
            if(player.hasPermission("stafflist.rank." + rankKeys.get(i))) {
                if(ranks.get(rankKeys.get(i) + ".weight") == null) continue;
                if(ranks.getInt(rankKeys.get(i) + ".weight") > ranks.getSection(rankKeys.get(highestGroup)).getInt("weight"))
                    highestGroup = i;
            }
        }
        
        ProxyServer.getInstance().getLogger().info(String.valueOf(rankKeys.size()));
        return (rankKeys.size() == 0 ? ranks : ranks.getSection(rankKeys.get(highestGroup)));
    }
    
    @NotNull
    public String getServer() {
        ProxiedPlayer player = getProxiedPlayer();
        if(player == null) {
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
        if(weight1 == weight2) {
            String name1 = this.getUsername();
            String name2 = other.getUsername();
            return name1.compareTo(name2);
        }
        
        return Integer.compare(weight2, weight1);
    }
}
