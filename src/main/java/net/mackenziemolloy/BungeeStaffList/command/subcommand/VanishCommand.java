package net.mackenziemolloy.BungeeStaffList.command.subcommand;

import net.mackenziemolloy.BungeeStaffList.BungeeStaffList;
import net.mackenziemolloy.BungeeStaffList.command.SubCommand;
import net.mackenziemolloy.BungeeStaffList.config.Lang;
import net.mackenziemolloy.BungeeStaffList.util.sirblobman.MessageUtility;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class VanishCommand extends SubCommand {

    public VanishCommand() {
        this.name = "hide";
    }

    @Override
    public void execute(CommandSender commandSender, String[]... args) {
        if(!(commandSender instanceof ProxiedPlayer)) {
            commandSender.sendMessage(MessageUtility.colorize(Lang.mustBePlayer));
            return;
        }

        ProxiedPlayer sender = (ProxiedPlayer) commandSender;
        if(BungeeStaffList.getInstance().getGroupManager().getPrimaryGroupProvider().getPrimaryGroup(sender) == null) {
            sender.sendMessage(MessageUtility.colorize(Lang.hideNotStaff));
            return;
        }

        boolean newHiddenState = !BungeeStaffList.getInstance().getVanishManager().getPlayerState(sender.getUniqueId());
        boolean success = BungeeStaffList.getInstance().getVanishManager().setPlayerState(sender.getUniqueId(), newHiddenState);
        if(!success) {
            sender.sendMessage(MessageUtility.colorize(Lang.hideError));
            return;
        }

        String reply = newHiddenState ? Lang.hideHidden : Lang.hideRevealed;
        sender.sendMessage(reply);
    }
}
