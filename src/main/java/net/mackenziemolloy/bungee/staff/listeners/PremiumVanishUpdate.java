package net.mackenziemolloy.bungee.staff.listeners;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import de.myzelyam.api.vanish.BungeePlayerHideEvent;
import de.myzelyam.api.vanish.BungeePlayerShowEvent;
import net.mackenziemolloy.bungee.staff.BungeeStaff;
import net.mackenziemolloy.bungee.staff.StaffManager;

public class PremiumVanishUpdate implements Listener {
    private final BungeeStaff plugin;

    public PremiumVanishUpdate(BungeeStaff plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onVanish(BungeePlayerHideEvent e) {
        ProxiedPlayer player = e.getPlayer();
        StaffManager staffManager = plugin.getStaffManager();
        staffManager.setPlayerVisibility(player, true);
    }

    @EventHandler
    public void onShow(BungeePlayerShowEvent e) {
        ProxiedPlayer player = e.getPlayer();
        StaffManager staffManager = plugin.getStaffManager();
        staffManager.setPlayerVisibility(player, false);
    }
}
