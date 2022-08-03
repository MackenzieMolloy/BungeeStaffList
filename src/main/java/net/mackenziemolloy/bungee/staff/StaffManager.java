package net.mackenziemolloy.bungee.staff;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.config.Configuration;

import com.github.sirblobman.api.bungeecord.core.CorePlugin;
import com.github.sirblobman.api.bungeecord.hook.vanish.IVanishHook;
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

    public String processStaffList(List<StaffMember> normalStaffList) {
        BungeeStaff plugin = getPlugin();
        List<StaffMember> nonHiddenStaffList = normalStaffList.stream().filter(this::canShowInStaffList).toList();
        if (nonHiddenStaffList.isEmpty()) {
            return plugin.getMessage("stafflist-none");
        }

        List<String> lineList = new ArrayList<>();
        for (StaffMember staffMember : nonHiddenStaffList) {
            String prefix = staffMember.getPrefix();
            String username = staffMember.getUsername();
            String displayName = staffMember.getDisplayName();
            String server = staffMember.getServer();

            String lineFormat = plugin.getMessage("staff-format");
            String line = lineFormat.replace("{prefix}", prefix)
                    .replace("{username}", username)
                    .replace("{display_name}", displayName)
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

    private IVanishHook getDefaultVanishHook() {
        ProxyServer proxy = getProxy();
        PluginManager pluginManager = proxy.getPluginManager();
        CorePlugin corePlugin = (CorePlugin) pluginManager.getPlugin("SirBlobmanBungeeCore");
        return corePlugin.getDefaultVanishHook();
    }

    public boolean canShowInStaffList(StaffMember player) {
        IVanishHook vanishHook = getDefaultVanishHook();
        UUID playerId = player.getPlayerId();
        return vanishHook.isHidden(playerId);
    }
}
