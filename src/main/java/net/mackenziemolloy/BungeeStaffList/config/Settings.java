package net.mackenziemolloy.BungeeStaffList.config;

import net.mackenziemolloy.BungeeStaffList.BungeeStaffList;
import net.mackenziemolloy.BungeeStaffList.core.PluginConfig;
import net.mackenziemolloy.BungeeStaffList.provider.group.GroupProvider;
import net.md_5.bungee.config.Configuration;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Settings {

  private static PluginConfig config;
  public static List<InternalGroup> internalGroupList;
  public static String groupHandler;
  public static String vanishHandler;
  public static HashMap<String, String> serverAliases;


  public static void setConfig(PluginConfig pluginConfig) {
    config = pluginConfig;
  }

  public static void load() {

    GroupProvider groupProvider = BungeeStaffList.getInstance().getGroupManager().getProviderById(config.getConfig().getString("options.groupHandler"));
    groupHandler = groupProvider != null ? groupProvider.getProviderId() : "INTERNAL";

  }

  public static void loadInternalGroups() {
    // Groups
    internalGroupList = new LinkedList<>();
    for(String key: config.getConfig().getSection("groups").getKeys()) {
      Configuration groupSection = config.getConfig().getSection("groups." + key);
      internalGroupList.add(new InternalGroup(key, groupSection.getString("prefix"), groupSection.getString("suffix"), groupSection.getInt("weight")));
    }
  }

}
