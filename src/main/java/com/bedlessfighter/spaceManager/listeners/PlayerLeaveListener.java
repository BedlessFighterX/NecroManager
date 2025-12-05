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

        boolean quitEnabled = cfg.getBoolean("quit.enabled");
        if (!quitEnabled) {
            e.setQuitMessage(null);
            return;
        }


        boolean silentStaff = cfg.getBoolean("quit.silent-staffs");
        boolean staff = p.hasPermission("spacemanager.admin")
                || p.hasPermission("spacemanager.silent-leave")
                || p.isOp();

        String msgRaw = cfg.getString("quit.message", "&7%player% left the server.");
        String msg = ChatColor.translateAlternateColorCodes('&', msgRaw.replace("%player%", p.getName()));

        if (staff && silentStaff) e.setQuitMessage(null);
        else e.setQuitMessage(msg);
    }
}
