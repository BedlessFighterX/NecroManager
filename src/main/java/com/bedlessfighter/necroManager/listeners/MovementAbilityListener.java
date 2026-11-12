package com.bedlessfighter.necroManager.listeners;

import com.bedlessfighter.necroManager.managers.MovementAbilityManager;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class MovementAbilityListener implements Listener {
    private final Set<UUID> managedFlight = ConcurrentHashMap.newKeySet();

    @EventHandler(ignoreCancelled = true)
    public void onMove(PlayerMoveEvent event) {
        if (event.getTo() == null) return;
        Player player = event.getPlayer();
        if (event.getFrom().getBlockX() == event.getTo().getBlockX()
                && event.getFrom().getBlockY() == event.getTo().getBlockY()
                && event.getFrom().getBlockZ() == event.getTo().getBlockZ()) return;

        Material underfoot = event.getTo().clone().subtract(0, 1, 0).getBlock().getType();
        MovementAbilityManager.launchpad(player, underfoot);

        if (managedFlight.contains(player.getUniqueId()) && player.isOnGround()
                && !player.isFlying() && !player.getAllowFlight()) {
            player.setAllowFlight(true);
        }
    }

    @EventHandler
    public void onToggleFlight(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();
        if (!managedFlight.contains(player.getUniqueId()) || !MovementAbilityManager.doubleJumpEnabled()
                || (player.getGameMode() != GameMode.SURVIVAL && player.getGameMode() != GameMode.ADVENTURE)) return;
        event.setCancelled(true);
        player.setFlying(false);
        player.setAllowFlight(false);
        if (!MovementAbilityManager.doubleJump(player)) {
            // Keep the second jump unavailable until the player lands again.
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!MovementAbilityManager.doubleJumpEnabled() || !eligible(player)) return;
        managedFlight.add(player.getUniqueId());
        player.setAllowFlight(true);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        managedFlight.remove(player.getUniqueId());
        MovementAbilityManager.clear(player);
    }

    private boolean eligible(Player player) {
        return player.hasPermission("necromanager.doublejump")
                && (player.getGameMode() == GameMode.SURVIVAL || player.getGameMode() == GameMode.ADVENTURE)
                && !player.getAllowFlight();
    }
}
