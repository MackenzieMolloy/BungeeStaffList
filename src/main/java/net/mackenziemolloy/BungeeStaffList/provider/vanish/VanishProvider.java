package net.mackenziemolloy.BungeeStaffList.provider.vanish;

import net.mackenziemolloy.BungeeStaffList.BungeeStaffList;

import java.util.UUID;

public abstract class VanishProvider {

  protected String providerName = "";
  protected String providerId = "";

  public VanishProvider() {
    registerListeners();
  }

  public abstract void registerListeners();

  public abstract boolean validate();

  public String getProviderName() {
    return providerName;
  }
  public String getProviderId() {return providerId;}

  /**
   * Changes the hidden (vanish) state of a player
   *
   * @param uuid Player's UUID you wish to change
   * @param state The desired Hidden (vanish) state
   * @return Boolean of successful state set
   */
  boolean setVanishState(UUID uuid, boolean state) {
    return BungeeStaffList.getInstance().getVanishManager().setPlayerState(uuid, state);
  }

}
