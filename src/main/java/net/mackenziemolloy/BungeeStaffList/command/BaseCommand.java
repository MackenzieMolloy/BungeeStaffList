package net.mackenziemolloy.BungeeStaffList.command;

import net.mackenziemolloy.BungeeStaffList.BungeeStaffList;
import net.mackenziemolloy.BungeeStaffList.command.subcommand.VanishCommand;
import net.mackenziemolloy.BungeeStaffList.config.InternalGroup;
import net.mackenziemolloy.BungeeStaffList.config.Lang;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.HashMap;
import java.util.Map;

public class BaseCommand extends Command {
  private final BungeeStaffList bungeeStaffList;

  HashMap<String, SubCommand> subCommands;

  public BaseCommand(BungeeStaffList bungeeStaffList) {
    super("bungeestaff", "", "staff", "bsl");
    this.bungeeStaffList = bungeeStaffList;

    loadSubCommands();
  }

  private void loadSubCommands() {
    this.subCommands = new HashMap<>();

    SubCommand vanishCommand = new VanishCommand();
    subCommands.put(vanishCommand.name, vanishCommand);
  }

  @Override
  public void execute(CommandSender commandSender, String[] args) {
    //bungeeStaffList.getPlayerManager().createFile(((ProxiedPlayer) commandSender).getUniqueId());

    if(!(commandSender instanceof ProxiedPlayer)) return;

    if(args.length == 0) commandStaffList(commandSender);
    else {

      SubCommand subCommand = subCommands.get(args[0].toLowerCase());
      if(subCommand == null) {
        commandStaffList(commandSender);
        return;
      }

      subCommand.execute(commandSender);
    }
  }

  private void commandStaffList(CommandSender commandSender) {
    ProxiedPlayer sender = (ProxiedPlayer) commandSender;

    HashMap<ProxiedPlayer, InternalGroup> staffMembers = BungeeStaffList.getInstance().getPlayerManager().getOnlineStaff();
    if(staffMembers.isEmpty()) sender.sendMessage("no staff online :o");

    staffMembers.forEach((key, value) -> {
      commandSender.sendMessage(Lang.listFormat.replace("%prefix%", value.getGroupPrefix()).replace("%username%", key.getDisplayName()).replace("%server%", key.getServer().getInfo().getName()));
    });

    InternalGroup internalGroup = bungeeStaffList.getGroupManager().getPrimaryGroupProvider().getPrimaryGroup(sender);
    commandSender.sendMessages("Your group is: " + (internalGroup != null ? internalGroup.getGroupName() : "none :("));

    boolean vanishState = bungeeStaffList.getVanishManager().getPlayerState(sender.getUniqueId());
    commandSender.sendMessages("Your vanish state is: " + vanishState);
  }

}
