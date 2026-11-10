package com.bedlessfighter.necroManager.listeners;

import com.bedlessfighter.necroManager.managers.ConfigManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public final class PlayerLeaveListener implements Listener {
    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if (!ConfigManager.getBoolean("quit.enabled", true)) {
            event.setQuitMessage(null);
            return;
        }

        boolean silentStaff = config.getBoolean("quit.silent-staffs", true);
        boolean staff = player.isOp()
                || player.hasPermission("necromanager.admin")
                || player.hasPermission("necromanager.silent-leave");
        if (staff && silentStaff) {
            event.setQuitMessage(null);
            return;
        }

        String raw = ConfigManager.getString("quit.message", "&7%player% left the server.");
        String message = raw == null ? "" : raw.replace("%player%", player.getName());
        event.setQuitMessage(color(message));
    }

    private String color(String message) {
        return ChatColor.translateAlternateColorCodes('&', message == null ? "" : message);
    }
}
