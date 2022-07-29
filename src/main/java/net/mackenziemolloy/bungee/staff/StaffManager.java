package net.mackenziemolloy.bungee.staff;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.config.Configuration;

import com.github.sirblobman.api.utility.Validate;

public final class StaffManager {
    private final BungeeStaff plugin;

    public StaffManager(BungeeStaff plugin) {
        this.plugin = Validate.notNull(plugin, "plugin must not be null!");
    }

    public List<StaffMember> getOnlineStaff(boolean includeVanished) {
        ProxyServer proxy = getProxy();
        Collection<ProxiedPlayer> playerCollection = proxy.getPlayers();
        List<StaffMember> staffList = new ArrayList<>();

        for (ProxiedPlayer player : playerCollection) {
            if (!player.hasPermission("stafflist.staff")) {
                continue;
            }

            StaffMember staffMember = new StaffMember(this.plugin, player);
            if (includeVanished || !staffMember.isHidden()) {
                staffList.add(staffMember);
            }
        }

        Collections.sort(staffList);
        return staffList;
    }

    public String getServerAlias(String serverName) {
        BungeeStaff plugin = getPlugin();
        Configuration configuration = plugin.getConfig().getConfiguration();
        Configuration configuredAliases = configuration.getSection("server-aliases");

        if (configuredAliases.getKeys().contains(serverName)) {
            return configuredAliases.getString(serverName);
        }

        return serverName;
    }

    public String processStaffList(List<StaffMember> staffList) {
        BungeeStaff plugin = getPlugin();
        if (staffList.isEmpty()) {
            return plugin.getMessage("stafflist-none");
        }

        List<String> lineList = new ArrayList<>();
        for (StaffMember staffMember : staffList) {
            String prefix = staffMember.getPrefix();
            String username = staffMember.getUsername();
            String server = staffMember.getServer();

            String lineFormat = plugin.getMessage("staff-format");
            String line = lineFormat.replace("{prefix}", prefix)
                    .replace("{username}", username)
                    .replace("{server}", server);
            lineList.add(line);
        }

        String staff = String.join("\n", lineList);
        String messageFormat = plugin.getMessage("stafflist-online");
        String message = messageFormat.replace("{staff}", staff);
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    private BungeeStaff getPlugin() {
        return this.plugin;
    }

    private ProxyServer getProxy() {
        BungeeStaff plugin = getPlugin();
        return plugin.getProxy();
    }
}
