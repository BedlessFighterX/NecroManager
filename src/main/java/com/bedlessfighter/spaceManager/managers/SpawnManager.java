package com.bedlessfighter.spaceManager.managers;

import com.bedlessfighter.spaceManager.Main;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class SpawnManager {

    public static void sendToSpawn(Player player) {
        var cfg = Main.getPlugin().getConfig();

        Location location = cfg.getLocation("spawn");
        String prefix = ChatColor.translateAlternateColorCodes('&', cfg.getString("messages.prefix"));
        String message = ChatColor.translateAlternateColorCodes('&', cfg.getString("messages.spawnmessage"));

        if (location != null) {
            player.teleport(location);
            player.sendMessage("%prefix% %message%"
                    .replace("%prefix%", prefix)
                    .replace("%message%", message));
        } else {
            player.sendMessage(prefix + ChatColor.RED + " Spawn is not set yet.");
        }
    }

    public static void setSpawn(Player player, Location location) {
        var cfg = Main.getPlugin().getConfig();

        boolean isStaff = player.hasPermission("spacemanager.setspawn")
                || player.hasPermission("spacemanager.admin")
                || player.isOp();

        String prefix = ChatColor.translateAlternateColorCodes('&', cfg.getString("messages.prefix"));
        String message = ChatColor.translateAlternateColorCodes('&', cfg.getString("messages.setspawn"));

        if (isStaff) {
            cfg.set("spawn", location);
            player.getWorld().setSpawnLocation(location.getBlockX(), location.getBlockY(), location.getBlockZ());
            Main.getPlugin().saveConfig();
            Main.getPlugin().reloadConfig();
            player.sendMessage("%prefix% %message%"
                    .replace("%prefix%", prefix)
                    .replace("%message%", message));
        } else {
            player.sendMessage(prefix + ChatColor.RED + " You don't have permission to set spawn.");
        }
    }
}
