package net.mackenziemolloy.BungeeStaffList.config;

public class InternalGroup {
  // MIGRATE THISS SHIT TO THE PROVIDERS
  // Eligible groups have bsl.staff (to indict staff group)
  // So can't cache for luckperms? hmm..
  private final String groupName;
  private final String groupPrefix;
  private final String groupSuffix;
  private final Integer groupWeight;

  public InternalGroup(String groupName, String groupPrefix, String groupSuffix, Integer groupWeight) {
    this.groupName = groupName;
    this.groupPrefix = groupPrefix;
    this.groupSuffix = groupSuffix;
    this.groupWeight = groupWeight;
  }

  public String getGroupName() {
    return groupName;
  }

  public String getGroupPrefix() {
    return groupPrefix;
  }

  public String getGroupSuffix() {
    return groupSuffix;
  }

  public Integer getGroupWeight() {
    return groupWeight;
  }

  public String getGroupPermissionNode() {
    return "bungeestafflist.group." + this.groupName.toLowerCase();
  }
}
