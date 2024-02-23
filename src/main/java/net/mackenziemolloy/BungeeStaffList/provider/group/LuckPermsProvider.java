package net.mackenziemolloy.BungeeStaffList.provider.group;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.NodeType;
import net.mackenziemolloy.BungeeStaffList.config.InternalGroup;
import net.md_5.bungee.api.ProxyServer;


public class LuckPermsProvider extends GroupProvider {

  public LuckPermsProvider() {
    this.providerName = "LuckPerms";
    this.providerId = "LUCKPERMS";
  }
  @Override
  public void initGroups() {
    LuckPerms api = net.luckperms.api.LuckPermsProvider.get();

    for(Group group: api.getGroupManager().getLoadedGroups()) {
      for(Node node: group.getNodes()) {
        if(!node.getKey().startsWith("bungeestafflist.group.")) continue;

        String groupPrefix = group.getNodes(NodeType.PREFIX).stream().findFirst().map(Node::getKey).orElse("");
        String groupSuffix = group.getNodes(NodeType.SUFFIX).stream().findFirst().map(Node::getKey).orElse("");
        Integer groupWeight = Integer.valueOf(group.getNodes(NodeType.WEIGHT).stream().findFirst().map(Node::getKey).orElse("0").replace("weight.", ""));

        InternalGroup internalGroup = new InternalGroup(group.getName(), groupPrefix, groupSuffix, groupWeight);
        super.internalGroups.add(internalGroup);
        break;
      }

    }

  }

  @Override
  public boolean validate() {
    return ProxyServer.getInstance().getPluginManager().getPlugin("LuckPerms") != null;
  }
}
