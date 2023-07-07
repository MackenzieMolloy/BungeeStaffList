package net.mackenziemolloy.BungeeStaffList.provider.vanish;

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

}
