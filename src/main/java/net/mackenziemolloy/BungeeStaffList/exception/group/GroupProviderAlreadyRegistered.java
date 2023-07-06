package net.mackenziemolloy.BungeeStaffList.exception.group;

public class GroupProviderAlreadyRegistered extends Exception {
  public GroupProviderAlreadyRegistered(String provideerId) {
    super("A GroupProvider with the id '" + provideerId + "' is already registed.");
  }
}
