package net.mackenziemolloy.BungeeStaffList;

import net.mackenziemolloy.BungeeStaffList.commands.BaseCommand;
import net.mackenziemolloy.BungeeStaffList.config.InternalGroup;
import net.mackenziemolloy.BungeeStaffList.config.Settings;
import net.mackenziemolloy.BungeeStaffList.core.PluginConfig;
import net.mackenziemolloy.BungeeStaffList.manager.GroupManager;
import net.mackenziemolloy.BungeeStaffList.manager.PlayerManager;
import net.mackenziemolloy.BungeeStaffList.manager.VanishManager;
import net.mackenziemolloy.BungeeStaffList.provider.group.GroupProvider;
import net.mackenziemolloy.BungeeStaffList.provider.group.InternalProvider;
import net.mackenziemolloy.BungeeStaffList.provider.group.LuckPermsProvider;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.event.PlayerHandshakeEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.event.EventHandler;

public class BungeeStaffList extends Plugin {

  static BungeeStaffList bungeeStaffList;
  private GroupManager groupManager;
  private PlayerManager playerManager;

  @Override
  public void onEnable() {
    bungeeStaffList = this;
    groupManager = new GroupManager();

    loadConfig();
    loadGroupProvider();

    playerManager = new PlayerManager();

    ProxyServer proxy = getProxy();
    PluginManager pluginManager = proxy.getPluginManager();
    pluginManager.registerCommand(this, new BaseCommand(this));

  }

  public void loadConfig() {
    // Load the YAML file and InputStream
    Settings.setConfig(new PluginConfig(bungeeStaffList, "config.yml"));
    Settings.load();
  }

  public void loadGroupProvider() {
    GroupProvider groupProvider = groupManager.getProviderById(Settings.groupHandler);
    if(!groupProvider.validate()) {
      getLogger().warning("Failed to validate " + groupProvider.getProviderName() + " group provider. Loading Internal provider instead...");
      Settings.groupHandler = "INTERNAL";
      groupProvider = groupManager.getProviderById(Settings.groupHandler);
    }

    groupProvider.initGroups();
    getLogger().info("Successfully loaded " + groupProvider.getProviderName() + " group provider with " + groupProvider.getGroups().size() + " groups.");
  }

  public GroupManager getGroupManager() { return groupManager; }
  public PlayerManager getPlayerManager() { return playerManager; }

  public static BungeeStaffList getInstance() {
    return bungeeStaffList;
  }

}
