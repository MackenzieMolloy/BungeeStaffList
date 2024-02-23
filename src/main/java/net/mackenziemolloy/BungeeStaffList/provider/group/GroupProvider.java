package net.mackenziemolloy.BungeeStaffList.provider.group;

import net.mackenziemolloy.BungeeStaffList.config.InternalGroup;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public abstract class GroupProvider {

  protected String providerName = "";
  protected String providerId = "";

  protected List<InternalGroup> internalGroups = new LinkedList<InternalGroup>() {
    public boolean add(InternalGroup internalGroup) {
      super.add(internalGroup);
      internalGroups.sort(Comparator.comparing(InternalGroup::getGroupWeight).reversed());
      return true;
    }
  };

  public GroupProvider() {
  }

  /**
  * Initializes all the InternalGroups for the staff list
  */
  public abstract void initGroups();

  /**
  * Returns a List of all the loaded InternalGroups
  * @return List<InternalGroup>
   */
  public List<InternalGroup> getGroups() {
    return internalGroups;
  };

  public String getProviderName() {
    return providerName;
  }
  public String getProviderId() {return providerId;}

  public InternalGroup getGroupByName(String name) {
    return internalGroups.stream().filter(internalGroup -> internalGroup.getGroupName().equals(name)).findFirst().get();
  };

  public abstract boolean validate();

  public InternalGroup getPrimaryGroup(ProxiedPlayer player) {
    for(InternalGroup internalGroup: internalGroups) {
      if(player.hasPermission(internalGroup.getGroupPermissionNode())) return internalGroup;
    }
    return null;
  }

}
