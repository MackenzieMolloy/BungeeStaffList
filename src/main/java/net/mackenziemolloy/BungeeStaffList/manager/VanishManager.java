package net.mackenziemolloy.BungeeStaffList.manager;

import net.mackenziemolloy.BungeeStaffList.BungeeStaffList;
import net.mackenziemolloy.BungeeStaffList.config.Settings;

import net.mackenziemolloy.BungeeStaffList.exception.vanish.VanishProviderAlreadyRegistered;
import net.mackenziemolloy.BungeeStaffList.provider.vanish.PremiumVanishProvider;
import net.mackenziemolloy.BungeeStaffList.provider.vanish.VanishProvider;

import java.util.HashMap;
import java.util.UUID;

public class VanishManager {

  private HashMap<String, VanishProvider> vanishProviders;

  public VanishManager() {
    vanishProviders = new HashMap<>();

    // Register build-in vanish providers
    try {

      // PremiumVanish Group Provider
      PremiumVanishProvider premiumVanishProvider = new PremiumVanishProvider();
      registerProvider(premiumVanishProvider.getProviderId(), premiumVanishProvider);


    } catch (VanishProviderAlreadyRegistered ex) {
      ex.printStackTrace();
    }

  }

  /**
   * Registers a VanishProvider for use within the plugin
   *
   * @param providerId Unique ID for the vanish provider
   * @param vanishProvider VanishProvider instance
   * @throws VanishProviderAlreadyRegistered when a provider with the unique id is already registered
   */
  public void registerProvider(String providerId, VanishProvider vanishProvider) throws VanishProviderAlreadyRegistered {
    providerId = providerId.toUpperCase();

    if(vanishProviders.containsKey(providerId)) throw new VanishProviderAlreadyRegistered(providerId);
    vanishProviders.put(providerId, vanishProvider);
  }

  /**
   * Returns the primary VanishProvider specified in the config.yml
   */
  public VanishProvider getPrimaryVanishProvider() {
    return vanishProviders.get(Settings.vanishHandler);
  }

  /**
   * Returns the VanishProvider with the specified id
   *
   * @param providerId The ProviderID for the provider you're after
   */
  public VanishProvider getProviderById(String providerId) {
    return vanishProviders.get(providerId);
  }

  //
  // PLAYER SPECIFIC
  //

  /**
   * Gets the vanished state of a player from their UUID
   *
   * @param uuid Unique ID of the player
   * @return boolean vanished state
   */
  public boolean getPlayerState(UUID uuid) {
    Object state = BungeeStaffList.getInstance().getPlayerManager().get(uuid, "hidden");
    return state != null ? (boolean) state : false;
  }

  /**
   * Sets the vanished state of a player from their UUID
   *
   * @param uuid Unique ID of the player
   * @param state boolean vanished state
   * @return boolean success state
   */
  public boolean setPlayerState(UUID uuid, boolean state) {
    return BungeeStaffList.getInstance().getPlayerManager().set(uuid, "hidden", state);
  }

}
