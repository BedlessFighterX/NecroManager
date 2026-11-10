package com.bedlessfighter.necroManager.managers;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public final class SpawnManager {

    private SpawnManager() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static void sendToSpawn(Player player) {
        Location spawn = ConfigManager.getLocation("spawn");

        if (spawn == null || spawn.getWorld() == null) {
            MessageManager.send(player, "messages.spawn-not-set",
                    "&cSpawn has not been set yet.");
            return;
        }

        if (!player.teleport(spawn)) {
            MessageManager.send(player, "messages.spawn-failed",
                    "&cCould not teleport you to spawn.");
            return;
        }

        MessageManager.send(player, "messages.spawnmessage",
                "&rYou have been &ateleported&r to the &espawn!");
    }

    public static void setSpawn(Player player, Location location) {
        if (!player.isOp() && !player.hasPermission("necromanager.admin")) {
            MessageManager.send(player, "messages.no-perm",
                    "&cYou don't have enough permissions to issue this command!");
            return;
        }
        if (location == null || location.getWorld() == null) {
            MessageManager.send(player, "messages.spawn-failed",
                    "&cCould not save this spawn location.");
            return;
        }

        Location saved = location.clone();
        ConfigManager.set("spawn", saved);
        ConfigManager.save();
        saved.getWorld().setSpawnLocation(saved.getBlockX(), saved.getBlockY(), saved.getBlockZ());
        MessageManager.send(player, "messages.setspawn",
                "&aSpawn location has been updated.");
    }
}
