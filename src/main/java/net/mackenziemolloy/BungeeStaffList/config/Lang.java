package net.mackenziemolloy.BungeeStaffList.config;

import net.mackenziemolloy.BungeeStaffList.core.PluginConfig;

import java.util.List;

public class Lang {

    private static PluginConfig config;

    public static String noPermission;
    public static String mustBePlayer;

    public static String hideNotStaff;
    public static String hideError;
    public static String hideHidden;
    public static String hideRevealed;

    public static String listFormat;
    public static List<String> listHeader;
    public static List<String> listFooter;

    public static void setConfig(PluginConfig pluginConfig) {
        config = pluginConfig;
    }

    public static void load() {
        noPermission = config.getConfig().getString("noPermission");
        mustBePlayer = config.getConfig().getString("mustBePlayer");
        hideNotStaff = config.getConfig().getString("hide.notStaff");
        hideError = config.getConfig().getString("hide.error");
        hideHidden = config.getConfig().getString("hide.hidden");
        hideRevealed = config.getConfig().getString("hide.revealed");


        listFormat = config.getConfig().getString("list.playerFormat");
        listHeader = config.getConfig().getStringList("list.header");
        listFooter = config.getConfig().getStringList("list.footer");
    }

}
