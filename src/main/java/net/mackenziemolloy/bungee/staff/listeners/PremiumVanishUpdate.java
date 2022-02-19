package net.mackenziemolloy.bungee.staff.listeners;

import de.myzelyam.api.vanish.BungeePlayerHideEvent;
import de.myzelyam.api.vanish.BungeePlayerShowEvent;
import net.mackenziemolloy.bungee.staff.BungeeStaff;
import net.mackenziemolloy.bungee.staff.StaffManager;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PremiumVanishUpdate implements Listener {


        private final BungeeStaff plugin;
        public PremiumVanishUpdate(BungeeStaff plugin) {
            this.plugin = plugin;

        }

        @EventHandler
        public void onVanish(BungeePlayerHideEvent e) {
            ProxyServer.getInstance().broadcast("hidden");
            ProxiedPlayer player = e.getPlayer();
            StaffManager staffManager = plugin.getStaffManager();

            staffManager.setPlayerVisibility(e.getPlayer(), true);
        }

        @EventHandler
        public void onShow(BungeePlayerShowEvent e) {
            ProxyServer.getInstance().broadcast("shown");
            ProxiedPlayer player = e.getPlayer();
            StaffManager staffManager = plugin.getStaffManager();

            staffManager.setPlayerVisibility(e.getPlayer(), false);
        }

}

