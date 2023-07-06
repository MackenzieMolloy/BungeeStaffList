package net.mackenziemolloy.BungeeStaffList.provider.group;


import net.mackenziemolloy.BungeeStaffList.BungeeStaffList;
import net.mackenziemolloy.BungeeStaffList.config.Settings;

public class InternalProvider extends GroupProvider {

  public InternalProvider() {
    this.providerName = "Internal";
    this.providerId = "INTERNAL";
  }

  @Override
  public void initGroups() {
    Settings.loadInternalGroups();
    this.internalGroups = Settings.internalGroupList;
  }

  public boolean validate() { return true; }

}
