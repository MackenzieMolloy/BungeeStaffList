package net.mackenziemolloy.BungeeStaffList;

import net.mackenziemolloy.BungeeStaffList.command.BaseCommand;
import net.mackenziemolloy.BungeeStaffList.config.Lang;
import net.mackenziemolloy.BungeeStaffList.config.Settings;
import net.mackenziemolloy.BungeeStaffList.core.PluginConfig;
import net.mackenziemolloy.BungeeStaffList.manager.GroupManager;
import net.mackenziemolloy.BungeeStaffList.manager.PlayerManager;
import net.mackenziemolloy.BungeeStaffList.manager.VanishManager;
import net.mackenziemolloy.BungeeStaffList.provider.group.GroupProvider;
import net.mackenziemolloy.BungeeStaffList.provider.vanish.VanishProvider;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import org.bstats.bungeecord.Metrics;

public class BungeeStaffList extends Plugin {

  static BungeeStaffList bungeeStaffList;
  private GroupManager groupManager;
  private VanishManager vanishManager;
  private PlayerManager playerManager;

  @Override
  public void onEnable() {
    bungeeStaffList = this;

    groupManager = new GroupManager();
    playerManager = new PlayerManager();
    vanishManager = new VanishManager();

    loadConfig();
    loadLang();
    loadGroupProvider();
    loadVanishProvider();

    ProxyServer proxy = getProxy();
    PluginManager pluginManager = proxy.getPluginManager();
    pluginManager.registerCommand(this, new BaseCommand(this));

    // BStats
    int pluginId = 13319;
    Metrics metrics = new Metrics(this, pluginId);

  }

  public void loadConfig() {
    // Load the YAML file and InputStream
    Settings.setConfig(new PluginConfig(bungeeStaffList, "config.yml"));
    Settings.load();
  }

  public void loadLang() {
    Lang.setConfig(new PluginConfig(bungeeStaffList, "lang.yml"));
    Lang.load();
  }

  public void loadGroupProvider() {
    GroupProvider groupProvider = groupManager.getProviderById(Settings.groupHandler);
    if(groupProvider == null || !groupProvider.validate()) {
      String providerName = groupProvider == null ? Settings.groupHandler : groupProvider.getProviderName();
      getLogger().warning("Failed to validate " + providerName + " group provider. Loading Internal provider instead...");
      Settings.groupHandler = "INTERNAL";
      groupProvider = groupManager.getProviderById(Settings.groupHandler);
    }

    groupProvider.initGroups();
    getLogger().info("Successfully loaded " + groupProvider.getProviderName() + " group provider with " + groupProvider.getGroups().size() + " groups.");
  }

  public void loadVanishProvider() {
    VanishProvider vanishProvider = vanishManager.getProviderById(Settings.vanishHandler);
    if(vanishProvider == null || !vanishProvider.validate()) {
      if(Settings.vanishHandler.equals("NONE")) {
        getLogger().info("A Vanish Provider wasn't loaded due to the configured provider being none or invalid.");
        return;
      }

      String providerName = vanishProvider == null ? Settings.vanishHandler : vanishProvider.getProviderName();
      getLogger().warning("Failed to validate " + providerName + " vanish provider. Skipping vanish provider hook...");
      Settings.groupHandler = "INTERNAL";
      return;
    }

    getLogger().info("Successfully loaded " + vanishProvider.getProviderName() + " vanish provider.");
  }

  public GroupManager getGroupManager() { return groupManager; }
  public PlayerManager getPlayerManager() { return playerManager; }

  public VanishManager getVanishManager() { return vanishManager; }

  public static BungeeStaffList getInstance() {
    return bungeeStaffList;
  }

}
