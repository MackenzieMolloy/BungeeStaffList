package net.mackenziemolloy.BungeeStaffList.exception.group;

public class GroupProviderAlreadyRegistered extends Exception {
  public GroupProviderAlreadyRegistered(String providerId) {
    super("A GroupProvider with the id '" + providerId + "' is already registered.");
  }
}
