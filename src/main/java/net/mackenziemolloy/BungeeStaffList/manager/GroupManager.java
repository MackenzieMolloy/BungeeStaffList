package net.mackenziemolloy.BungeeStaffList.manager;

import net.mackenziemolloy.BungeeStaffList.config.Settings;
import net.mackenziemolloy.BungeeStaffList.exception.group.GroupProviderAlreadyRegistered;
import net.mackenziemolloy.BungeeStaffList.provider.group.GroupProvider;
import net.mackenziemolloy.BungeeStaffList.provider.group.InternalProvider;
import net.mackenziemolloy.BungeeStaffList.provider.group.LuckPermsProvider;

import java.util.HashMap;

public class GroupManager {

  private HashMap<String, GroupProvider> groupProviders;

  public GroupManager() {
    groupProviders = new HashMap<>();

    // Register build-in group providers
    try {

      // Internal Group Provider
      InternalProvider internalProvider = new InternalProvider();
      registerProvider(internalProvider.getProviderId(), internalProvider);

      // LuckPerms Group Provider
      LuckPermsProvider luckPermsProvider = new LuckPermsProvider();
      registerProvider(luckPermsProvider.getProviderId(), luckPermsProvider);

    } catch (GroupProviderAlreadyRegistered ex) {
      ex.printStackTrace();
    }

  }

  /**
   * Registers a GroupProvider for use within the plugin
   *
   * @param providerId Unique ID for the group provider
   * @param groupProvider GroupProvider instance
   * @throws GroupProviderAlreadyRegistered when a provider with the unique id is already registered
   */
  public void registerProvider(String providerId, GroupProvider groupProvider) throws GroupProviderAlreadyRegistered {
    providerId = providerId.toUpperCase();

    if(groupProviders.containsKey(providerId)) throw new GroupProviderAlreadyRegistered(providerId);
    groupProviders.put(providerId, groupProvider);
  }

  /**
   * Returns the primary GroupProvider specified in the config.yml
   */
  public GroupProvider getPrimaryGroupProvider() {
    return groupProviders.get(Settings.groupHandler);
  }

  /**
   * Returns the GroupProvider with the specified id
   *
   * @param providerId The ProviderID for the provider you're after
   */
  public GroupProvider getProviderById(String providerId) {
    return groupProviders.get(providerId);
  }

}
