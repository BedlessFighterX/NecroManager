package com.bedlessfighter.spaceManager.listeners;

import com.bedlessfighter.spaceManager.Main;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLeaveListener implements Listener {

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        var cfg = Main.getPlugin().getConfig();

        String msg = cfg.getString("quit.message").replace("%player%", p.getName());
        boolean quitEnabled = cfg.getBoolean("quit.enabled");
        boolean silentStaff = cfg.getBoolean("quit.silent-staffs");
        boolean staff = p.hasPermission("spacemanager.admin") || p.hasPermission("spacemanager.silent-leave") || p.isOp();

        if (quitEnabled) {
            if (staff && !silentStaff) e.setQuitMessage(ChatColor.translateAlternateColorCodes('&', msg));
            else if (!staff) e.setQuitMessage(ChatColor.translateAlternateColorCodes('&', msg));
            else if (staff && silentStaff) e.setQuitMessage(null);
        }

        if (staff && !silentStaff) e.setQuitMessage(ChatColor.translateAlternateColorCodes('&', msg));
        else if (!staff) e.setQuitMessage(ChatColor.translateAlternateColorCodes('&', msg));
        else if (staff && silentStaff) e.setQuitMessage(null);
    }
}
