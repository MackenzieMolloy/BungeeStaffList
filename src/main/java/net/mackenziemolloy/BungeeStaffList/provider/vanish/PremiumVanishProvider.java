package net.mackenziemolloy.BungeeStaffList.provider.vanish;

import de.myzelyam.api.vanish.BungeePlayerHideEvent;
import de.myzelyam.api.vanish.BungeePlayerShowEvent;
import net.mackenziemolloy.BungeeStaffList.BungeeStaffList;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PremiumVanishProvider extends VanishProvider implements Listener {

  public PremiumVanishProvider() {
    this.providerId = "PREMIUMVANISH";
    this.providerName = "PremiumVanish";
  }

  // Maybe this could be moved into VanishProvider?? idk how "timing" works
  @Override
  public void registerListeners() {
    ProxyServer.getInstance().getPluginManager().registerListener(BungeeStaffList.getInstance(), this);
  }

  @Override
  public boolean validate() {
    return ProxyServer.getInstance().getPluginManager().getPlugin("PremiumVanish") != null;
  }

  @EventHandler
  public void onVanishEnable(BungeePlayerHideEvent event) {
    BungeeStaffList.getInstance().getVanishManager().setPlayerState(event.getPlayer().getUniqueId(), true);
  }

  @EventHandler
  public void onVanishDisable(BungeePlayerShowEvent event) {
    BungeeStaffList.getInstance().getVanishManager().setPlayerState(event.getPlayer().getUniqueId(), false);
  }

}
