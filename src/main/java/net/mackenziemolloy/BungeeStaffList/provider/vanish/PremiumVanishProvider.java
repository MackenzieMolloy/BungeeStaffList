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

  // PremiumVanish Event Listener - Listens for players enabling their Vanish
  @EventHandler
  public void onVanishEnable(BungeePlayerHideEvent event) {
    this.setVanishState(event.getPlayer().getUniqueId(), true);
  }

  // PremiumVanish Event Listener - Listens for players disabling their Vanish
  @EventHandler
  public void onVanishDisable(BungeePlayerShowEvent event) {
    this.setVanishState(event.getPlayer().getUniqueId(), false);
  }

}
