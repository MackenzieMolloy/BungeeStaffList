package net.mackenziemolloy.BungeeStaffList.commands;

import net.mackenziemolloy.BungeeStaffList.BungeeStaffList;
import net.mackenziemolloy.BungeeStaffList.config.InternalGroup;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.UUID;

public class BaseCommand extends Command {
  private final BungeeStaffList bungeeStaffList;

  public BaseCommand(BungeeStaffList bungeeStaffList) {
    super("bungeestaff", "", "staff", "bsl");
    this.bungeeStaffList = bungeeStaffList;
  }

  @Override
  public void execute(CommandSender commandSender, String[] args) {
    //bungeeStaffList.getPlayerManager().createFile(((ProxiedPlayer) commandSender).getUniqueId());

    if(!(commandSender instanceof ProxiedPlayer)) return;

    if(args.length == 0) commandStaffList(commandSender);
    else {

      switch (args[0]) {
        case "hide":
          commandStaffHide(commandSender);
        case "reload":
          commandStaffReload(commandSender);
        default:
          commandSender.sendMessage("Sub-command unknown.");
      }
    }
  }

  private void commandStaffHide(CommandSender commandSender) {
    ProxiedPlayer player = (ProxiedPlayer) commandSender;

    boolean currentObj = bungeeStaffList.getPlayerManager().getVanishManager().getPlayerState(player.getUniqueId());

    boolean success = bungeeStaffList.getPlayerManager().getVanishManager().setPlayerState(player.getUniqueId(), !currentObj);
    player.sendMessage("Success: " + success);
  }
  private void commandStaffList(CommandSender commandSender) {
    InternalGroup internalGroup = bungeeStaffList.getGroupManager().getPrimaryGroupProvider().getPrimaryGroup((ProxiedPlayer) commandSender);
    commandSender.sendMessages("Your group is: " + (internalGroup != null ? internalGroup.getGroupName() : "none :("));
  }
  private void commandStaffReload(CommandSender commandSender) {}
}
