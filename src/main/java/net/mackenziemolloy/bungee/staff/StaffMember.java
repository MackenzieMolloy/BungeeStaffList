package net.mackenziemolloy.bungee.staff;

import java.util.UUID;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import de.myzelyam.api.vanish.BungeeVanishAPI;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;

public class StaffMember {
    private UUID uuid;
    private String username;
    private String prefix;
    private int groupWeight;
    private static LuckPerms api = LuckPermsProvider.get();

//    static {
//        StaffMember.api = LuckPermsProvider.get();
//    }

    public StaffMember(final UUID uuid) {
        this.uuid = uuid;
        this.username = ProxyServer.getInstance().getPlayer(this.uuid).getDisplayName();
        try { this.prefix = StaffMember.api.getUserManager().getUser(this.uuid).getCachedData().getMetaData().getPrefix(); }
        catch (NullPointerException e) { this.prefix = ""; }
        try {
            this.groupWeight = StaffMember.api.getGroupManager().getGroup(StaffMember.api.getUserManager().getUser(this.uuid).getPrimaryGroup()).getWeight().getAsInt();
        }
        catch (NullPointerException e) {
            this.groupWeight = 0;
        }
    }

    public StaffMember(final String username) {
        this.username = username;
        final ProxiedPlayer player = ProxyServer.getInstance().getPlayer(this.username);
        if (player != null) {
            this.uuid = ProxyServer.getInstance().getPlayer(username).getUniqueId();
            this.prefix = StaffMember.api.getUserManager().getUser(this.uuid).getCachedData().getMetaData().getPrefix();
            this.groupWeight = StaffMember.api.getGroupManager().getGroup(StaffMember.api.getUserManager().getUser(this.uuid).getPrimaryGroup()).getWeight().getAsInt();
        }
    }

    public boolean isHidden() {
        return BungeeVanishAPI.isInvisible(ProxyServer.getInstance().getPlayer(this.uuid));
    }
    public UUID getUuid() {
        return this.uuid;
    }
    public String getUsername() {
        return this.username;
    }
    public String getPrefix() {
        return this.prefix;
    }
    public int getGroupWeight() {
        return this.groupWeight;
    }
    public String getServer() { return StaffManager.getServerAlias(ProxyServer.getInstance().getPlayer(this.uuid).getServer().getInfo().getName()); }

}
