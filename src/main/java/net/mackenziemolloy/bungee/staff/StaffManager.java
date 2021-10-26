package net.mackenziemolloy.bungee.staff;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.config.Configuration;

public final class StaffManager {
    private final BungeeStaff plugin;
    
    public StaffManager(BungeeStaff plugin) {
        this.plugin = Objects.requireNonNull(plugin, "plugin must not be null!");
    }
    
    public List<StaffMember> getOnlineStaff(boolean includeVanished) {
        ProxyServer proxy = getProxy();
        Collection<ProxiedPlayer> playerCollection = proxy.getPlayers();
        List<StaffMember> staffList = new ArrayList<>();
        
        for(ProxiedPlayer player : playerCollection) {
            if(!player.hasPermission("stafflist.staff")) {
                continue;
            }
            
            StaffMember staffMember = new StaffMember(this.plugin, player);
            if(includeVanished || !staffMember.isHidden()) {
                staffList.add(staffMember);
            }
        }
        
        Collections.sort(staffList);
        return staffList;
    }
    
    public String getServerAlias(String serverName) {
        BungeeStaff plugin = getPlugin();
        Configuration configuration = plugin.getFromConfig();
        Configuration configuredAliases = configuration.getSection("server-aliases");
        
        if(configuredAliases.getKeys().contains(serverName)) {
            return configuredAliases.getString(serverName);
        }
        
        return serverName;
    }
    
    public String processStaffList(List<StaffMember> staffList) {
        if(staffList.isEmpty()) {
            return this.plugin.getFromConfig().getString("stafflist-none");
        }
        
        List<String> lineList = new ArrayList<>();
        for(StaffMember staffMember : staffList) {
            String staffMemberMsg = this.plugin.getFromConfig().getString("staff-format")
                    .replace("{prefix}", staffMember.getPrefix())
                    .replace("{username}", staffMember.getUsername())
                    .replace("{server}", staffMember.getServer());
            lineList.add(staffMemberMsg);
        }
        
        return ChatColor.translateAlternateColorCodes('&', this.plugin.getFromConfig()
                .getString("stafflist-online").replace("{staff}", String.join("\n", lineList)));
    }
    
    private BungeeStaff getPlugin() {
        return this.plugin;
    }
    
    private ProxyServer getProxy() {
        BungeeStaff plugin = getPlugin();
        return plugin.getProxy();
    }
}
