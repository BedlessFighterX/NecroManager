package com.bedlessfighter.necroManager.listeners;

import com.bedlessfighter.necroManager.managers.ConfigManager;
import com.bedlessfighter.necroManager.managers.SpawnManager;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public final class PlayerRespawnListener implements Listener {

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        if (!ConfigManager.getBoolean("respawn.use-plugin-spawn", false)) {
            return;
        }

        Location safeSpawn = SpawnManager.getSafeSpawn();
        if (safeSpawn == null || safeSpawn.getWorld() == null) {
            return;
        }

        event.setRespawnLocation(safeSpawn);
    }
}
