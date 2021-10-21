package net.mackenziemolloy.bungee.staff;

import net.mackenziemolloy.bungee.staff.utility.PlayerComparator;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.config.Configuration;
import java.util.ArrayList;
import java.util.List;

import static net.mackenziemolloy.bungee.staff.BungeeStaff.bungeeStaff;

public class StaffManager{

    public static List<StaffMember> getOnlineStaff(boolean includingVanished) {
        List<StaffMember> staff = new ArrayList<>();
        for(ProxiedPlayer player: bungeeStaff.getProxy().getPlayers()) {
            if(player.hasPermission("stafflist.staff")) {
                StaffMember staffMember = new StaffMember(player.getUniqueId());
                if(!staffMember.isHidden() || includingVanished) {
                    staff.add(staffMember);
                }
            }
        }
        PlayerComparator.sort(staff);
        return staff;
    }

    public static String getServerAlias(String serverName) {
        Configuration configuredAliases = bungeeStaff.configFile.getConfiguration().getSection("server-aliases");
        if(configuredAliases.getKeys().contains(serverName)) return configuredAliases.getString(serverName);
        return serverName;
    }

    public static String processStaffList(List<StaffMember> staff) {
        if(staff.isEmpty()) return bungeeStaff.configFile.getConfiguration().getString("stafflist-none");
        List<String> staffList = new ArrayList<>();
        for(StaffMember staffMember: staff) {
            String staffMemberMsg = bungeeStaff.configFile.getConfiguration().getString("staff-format")
                    .replace("{prefix}", staffMember.getPrefix())
                    .replace("{username}", staffMember.getUsername())
                    .replace("{server}", staffMember.getServer());
            staffList.add(staffMemberMsg);
        }
        return ChatColor.translateAlternateColorCodes('&', bungeeStaff.configFile.getConfiguration().getString("stafflist-online").replace("{staff}", String.join("\n", staffList)));
    }

}
