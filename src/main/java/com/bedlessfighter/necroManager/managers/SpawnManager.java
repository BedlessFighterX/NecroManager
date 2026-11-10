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
        var cfg = plugin.getConfig();
        Location location = cfg.getLocation("spawn");
        String prefix = color(cfg.getString("messages.prefix", "&a&lNECRO&f&lMC &8|&r"));

        if (location == null || location.getWorld() == null) {
            player.sendMessage(prefix + ChatColor.RED + " Spawn is not set yet.");
            return;
        }

        if (player.teleport(location)) {
            player.sendMessage(prefix + color(cfg.getString("messages.spawnmessage",
                    "&rYou have been &ateleported&r to the &espawn!")));
        }
    }

    public static void setSpawn(Player player, Location location) {
        var plugin = Main.getPlugin();
        var cfg = plugin.getConfig();
        String prefix = color(cfg.getString("messages.prefix", "&a&lNECRO&f&lMC &8|&r"));

        if (!player.hasPermission("necromanager.admin")) {
            player.sendMessage(prefix + ChatColor.RED + " You don't have permission to set spawn.");
            return;
        }

        Location saved = location.clone();
        cfg.set("spawn", saved);
        plugin.saveConfig();
        player.getWorld().setSpawnLocation(saved.getBlockX(), saved.getBlockY(), saved.getBlockZ());
        player.sendMessage(prefix + color(cfg.getString("messages.setspawn",
                "&aSpawn location has been updated.")));
    }

    private static String color(String message) {
        return ChatColor.translateAlternateColorCodes('&', message == null ? "" : message);
    }
}
