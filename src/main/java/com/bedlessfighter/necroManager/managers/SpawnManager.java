package com.bedlessfighter.necroManager.managers;

import com.bedlessfighter.necroManager.Main;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public final class SpawnManager {
    private SpawnManager() {
    }

    public static void sendToSpawn(Player player) {
        var plugin = Main.getPlugin();
        var config = plugin.getConfig();
        Location spawn = config.getLocation("spawn");
        String prefix = color(config.getString("messages.prefix", "&a&lNECRO&f&lMC &8|&r"));

        if (spawn == null || spawn.getWorld() == null) {
            player.sendMessage(prefix + " " + color(config.getString("messages.spawn-not-set",
                    "&cSpawn has not been set yet.")));
            return;
        }

        if (!player.teleport(spawn)) {
            player.sendMessage(prefix + " " + color(config.getString("messages.spawn-failed",
                    "&cCould not teleport you to spawn.")));
            return;
        }

        player.sendMessage(prefix + " " + color(config.getString("messages.spawnmessage",
                "&rYou have been &ateleported&r to the &espawn!")));
    }

    public static void setSpawn(Player player, Location location) {
        var plugin = Main.getPlugin();
        var config = plugin.getConfig();
        String prefix = color(config.getString("messages.prefix", "&a&lNECRO&f&lMC &8|&r"));

        if (!player.isOp() && !player.hasPermission("necromanager.admin")) {
            player.sendMessage(prefix + " " + color(config.getString("messages.no-perm",
                    "&cYou don't have enough permissions to issue this command!")));
            return;
        }
        if (location == null || location.getWorld() == null) {
            player.sendMessage(prefix + " " + color(config.getString("messages.spawn-failed",
                    "&cCould not save this spawn location.")));
            return;
        }

        Location saved = location.clone();
        config.set("spawn", saved);
        plugin.saveConfig();
        saved.getWorld().setSpawnLocation(saved.getBlockX(), saved.getBlockY(), saved.getBlockZ());
        player.sendMessage(prefix + " " + color(config.getString("messages.setspawn",
                "&aSpawn location has been updated.")));
    }

    private static String color(String message) {
        return ChatColor.translateAlternateColorCodes('&', message == null ? "" : message);
    }
}
