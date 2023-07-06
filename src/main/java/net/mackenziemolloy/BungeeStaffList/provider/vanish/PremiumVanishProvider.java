package net.mackenziemolloy.BungeeStaffList.provider.vanish;

import de.myzelyam.api.vanish.BungeePlayerShowEvent;
import de.myzelyam.api.vanish.PlayerVanishStateChangeEvent;
import net.md_5.bungee.api.plugin.Event;
import net.md_5.bungee.api.plugin.Listener;

public class PremiumVanishProvider extends VanishProvider {

  public PremiumVanishProvider() {
    this.providerId = "PREMIUMVANISH";
    this.providerName = "PremiumVanish";
  }

  @Override
  public void vanishChangeEvent(BungeePlayerShowEvent event) {

  }

  /*

  - Need to create some sort of event listener provider, not sure how.
  - PremiumVanish Bungee has: BungeePlayerShowEvent and BungeePlayerHideEvent, no toggle events.
  - This event listening system needs to work with the VanishManager.

   */

}
