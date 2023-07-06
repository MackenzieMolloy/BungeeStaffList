package net.mackenziemolloy.BungeeStaffList.provider.vanish;

import net.md_5.bungee.api.plugin.Event;
import net.md_5.bungee.event.EventHandler;

public abstract class VanishProvider {

  protected String providerName = "";
  protected String providerId = "";

  public VanishProvider() {

  }

  @EventHandler
  public abstract void vanishChangeEvent(Class<? extends Event> event);
}
