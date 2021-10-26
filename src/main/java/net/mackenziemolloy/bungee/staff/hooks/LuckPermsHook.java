package net.mackenziemolloy.bungee.staff.hooks;

import java.util.OptionalInt;
import java.util.UUID;

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
import org.jetbrains.annotations.Nullable;

public final class LuckPermsHook extends Hook {
    public LuckPermsHook(BungeeStaff plugin) {
        super(plugin);
    }
    
    @Override
    public boolean isEnabled() {
        PluginManager pluginManager = getPluginManager();
        Plugin luckPerms = pluginManager.getPlugin("LuckPerms");
        return (luckPerms != null);
    }

    public String getPrefix(UUID playerId) {
        User user = getUser(playerId);
        if(user == null) {
            return "";
        }
        
        CachedDataManager cachedData = user.getCachedData();
        CachedMetaData metaData = cachedData.getMetaData();
        String prefix = metaData.getPrefix();
        return (prefix == null ? "" : prefix);
    }

    public Integer getWeight(UUID playerId) {
        User user = getUser(playerId);
        if(user == null) {
            return 0;
        }
    
        LuckPerms api = getAPI();
        String groupName = user.getPrimaryGroup();
        GroupManager groupManager = api.getGroupManager();
        Group group = groupManager.getGroup(groupName);
        if(group == null) {
            return 0;
        }
    
        OptionalInt optionalWeight = group.getWeight();
        return optionalWeight.orElse(0);
    }
    
    private LuckPerms getAPI() {
        return LuckPermsProvider.get();
    }
    
    @Nullable
    private User getUser(UUID playerId) {
        LuckPerms api = getAPI();
        UserManager userManager = api.getUserManager();
        return userManager.getUser(playerId);
    }
}
