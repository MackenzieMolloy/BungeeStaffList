package net.mackenziemolloy.bungee.staff.hooks;

import java.util.Objects;
import java.util.OptionalInt;
import java.util.UUID;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.cacheddata.CachedDataManager;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.group.GroupManager;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;
import net.mackenziemolloy.bungee.staff.BungeeStaff;
import org.jetbrains.annotations.NotNull;

public final class LuckPermsHook {
    private final BungeeStaff plugin;

    public LuckPermsHook(BungeeStaff plugin) {
        this.plugin = Objects.requireNonNull(plugin, "plugin must not be null!");
    }

    public BungeeStaff getPlugin() {
        return this.plugin;
    }

    public boolean isEnabled() {
        BungeeStaff plugin = getPlugin();
        ProxyServer proxy = plugin.getProxy();
        PluginManager pluginManager = proxy.getPluginManager();

        Plugin luckPerms = pluginManager.getPlugin("LuckPerms");
        return (luckPerms != null);
    }

    @NotNull
    public String getPrefix(UUID playerId) {
        LuckPerms luckPerms = LuckPermsProvider.get();
        UserManager userManager = luckPerms.getUserManager();

        User user = userManager.getUser(playerId);
        if (user == null) {
            return "";
        }

        CachedDataManager dataManager = user.getCachedData();
        CachedMetaData metaData = dataManager.getMetaData();
        String prefix = metaData.getPrefix();
        return (prefix == null || prefix.isBlank() ? "" : prefix);
    }

    public int getWeight(UUID playerId, Integer groupWeight) {
        if (groupWeight != null) {
            return groupWeight;
        }

        LuckPerms luckPerms = LuckPermsProvider.get();
        UserManager userManager = luckPerms.getUserManager();
        GroupManager groupManager = luckPerms.getGroupManager();

        User user = userManager.getUser(playerId);
        if (user == null) {
            return 0;
        }

        String groupName = user.getPrimaryGroup();
        Group group = groupManager.getGroup(groupName);
        if (group == null) {
            return 0;
        }

        OptionalInt optionalWeight = group.getWeight();
        return optionalWeight.orElse(0);
    }
}
