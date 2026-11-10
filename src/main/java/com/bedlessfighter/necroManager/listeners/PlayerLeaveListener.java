package com.bedlessfighter.necroManager.listeners;

import com.bedlessfighter.necroManager.managers.ConfigManager;
import com.bedlessfighter.necroManager.managers.MessageManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Map;

public final class PlayerLeaveListener implements Listener {

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (!ConfigManager.getBoolean("quit.enabled", true)) {
            event.setQuitMessage(null);
            return;
        }

        boolean silentStaff = ConfigManager.getBoolean("quit.silent-staffs", true);
        boolean staff = player.isOp()
                || player.hasPermission("necromanager.admin")
                || player.hasPermission("necromanager.silent-leave");
        if (staff && silentStaff) {
            event.setQuitMessage(null);
            return;
        }

        event.setQuitMessage(MessageManager.format(
                ConfigManager.getString("quit.message", "&7%player% left the server."),
                Map.of("player", player.getName())));
    }
}
