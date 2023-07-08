package net.mackenziemolloy.BungeeStaffList.command;

import net.md_5.bungee.api.CommandSender;

public abstract class SubCommand {

    protected String name;

    public abstract void execute(CommandSender commandSender, String[] ...args);

}
