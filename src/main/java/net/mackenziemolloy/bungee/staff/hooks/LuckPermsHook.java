package net.mackenziemolloy.bungee.staff.hooks;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.cacheddata.CachedDataManager;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.group.GroupManager;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;
import net.mackenziemolloy.bungee.staff.BungeeStaff;
import net.md_5.bungee.api.ProxyServer;

import java.util.Objects;
import java.util.OptionalInt;
import java.util.UUID;

public class LuckPermsHook {

    public static BungeeStaff plugin;

    public LuckPermsHook(BungeeStaff plugin) {
        this.plugin = Objects.requireNonNull(plugin, "plugin must not be null!");
    }

    public static String getPrefix(UUID playerId) {

        String prefix = null;

        LuckPerms luckPerms = LuckPermsProvider.get();
        UserManager userManager = luckPerms.getUserManager();

        User user = userManager.getUser(playerId);
        if(user != null) {
            CachedDataManager cachedData = user.getCachedData();
            CachedMetaData metaData = cachedData.getMetaData();
            prefix = metaData.getPrefix();
        }

        return (prefix == null ? "" : prefix);
    }

    public static Integer getWeight(UUID playerId, Integer groupWeight) {

        if(groupWeight == null) {
            LuckPerms luckPerms = LuckPermsProvider.get();
            UserManager userManager = luckPerms.getUserManager();
            GroupManager groupManager = luckPerms.getGroupManager();

            User user = userManager.getUser(playerId);
            if(user != null) {
                String groupName = user.getPrimaryGroup();
                Group group = groupManager.getGroup(groupName);
                if(group != null) {
                    OptionalInt weight = group.getWeight();
                    groupWeight = weight.orElse(0);
                } else {
                    groupWeight = 0;
                }
            } else {
                groupWeight = 0;
            }
        }

        return groupWeight;

    }

    public static boolean isEnabled() {
        return ProxyServer.getInstance().getPluginManager().getPlugin("LuckPerms") != null;

    }

}
