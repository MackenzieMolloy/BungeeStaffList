package net.mackenziemolloy.BungeeStaffList.exception.vanish;

public class VanishProviderAlreadyRegistered extends Exception {
  public VanishProviderAlreadyRegistered(String providerId) {
    super("A VanishProvider with the id '" + providerId + "' is already registered.");
  }
}
