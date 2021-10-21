package net.mackenziemolloy.bungee.staff;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class ListCommand extends Command {

    private final BungeeStaff bungeeStaff;

    public ListCommand(final BungeeStaff bungeeStaff) {
        super("bungeestaff","","staff","stafflist","liststaff");
        this.bungeeStaff = bungeeStaff;
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if(commandSender instanceof ProxiedPlayer) {
            ProxiedPlayer sender = (ProxiedPlayer) commandSender;

            if(args.length > 0 && args[0].equalsIgnoreCase("reload") && sender.hasPermission("stafflist.reload")) { bungeeStaff.reloadConfig(commandSender); return; }

            String staffListMessage = StaffManager.processStaffList(StaffManager.getOnlineStaff(false));
            sender.sendMessage(staffListMessage);

        }

        else {

            if(args.length > 0 && args[0].equalsIgnoreCase("reload")) { bungeeStaff.reloadConfig(commandSender); return; }

            String msg = StaffManager.processStaffList(StaffManager.getOnlineStaff(false));
            ProxyServer.getInstance().getConsole().sendMessage(msg);

        }
    }
}
