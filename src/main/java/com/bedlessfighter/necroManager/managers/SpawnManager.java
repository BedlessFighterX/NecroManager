package com.bedlessfighter.necroManager.managers;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public final class SpawnManager {

    private SpawnManager() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Resolves the configured plugin spawn without relying on Bukkit's world-spawn
     * setting. The structured format survives configuration reloads and stores
     * both a world UUID and name so renamed worlds can still be resolved.
     */
    public static Location getSpawn() {
        if (!ConfigManager.contains("spawn.world") && !ConfigManager.contains("spawn.world-uuid")) {
            // Backwards compatibility with the legacy serialized Bukkit Location.
            Location legacy = ConfigManager.getLocation("spawn");
            return isValid(legacy) ? legacy : null;
        }

        World world = null;
        String worldUuid = ConfigManager.getString("spawn.world-uuid", "");
        if (!worldUuid.isBlank()) {
            try {
                world = Bukkit.getWorld(UUID.fromString(worldUuid));
            } catch (IllegalArgumentException ignored) {
                // Fall back to the readable world name for malformed/older data.
            }
        }

        if (world == null) {
            String worldName = ConfigManager.getString("spawn.world", "");
            if (!worldName.isBlank()) {
                world = Bukkit.getWorld(worldName);
            }
        }
        if (world == null) {
            return null;
        }

        double x = ConfigManager.getDouble("spawn.x", Double.NaN);
        double y = ConfigManager.getDouble("spawn.y", Double.NaN);
        double z = ConfigManager.getDouble("spawn.z", Double.NaN);
        double yaw = ConfigManager.getDouble("spawn.yaw", 0.0D);
        double pitch = ConfigManager.getDouble("spawn.pitch", 0.0D);
        if (!Double.isFinite(x) || !Double.isFinite(y) || !Double.isFinite(z)
                || !Double.isFinite(yaw) || !Double.isFinite(pitch)
                || Math.abs(x) > 30_000_000D || Math.abs(z) > 30_000_000D
                || y < world.getMinHeight() - 64D || y > world.getMaxHeight() + 64D) {
            return null;
        }

        return new Location(world, x, y, z, (float) yaw, (float) pitch);
    }

    public static void sendToSpawn(Player player) {
        Location spawn = getSpawn();
        if (!isValid(spawn)) {
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
        if (!PermissionManager.isAdmin(player)) {
            MessageManager.send(player, "messages.no-perm",
                    "&cYou don't have enough permissions to issue this command!");
            return;
        }
        if (!isValid(location)) {
            MessageManager.send(player, "messages.spawn-failed",
                    "&cCould not save this spawn location.");
            return;
        }

        Location saved = location.clone();
        World world = saved.getWorld();

        // Remove the legacy serialized Location before writing the structured section.
        ConfigManager.set("spawn", null);
        ConfigManager.set("spawn.world", world.getName());
        ConfigManager.set("spawn.world-uuid", world.getUID().toString());
        ConfigManager.set("spawn.x", saved.getX());
        ConfigManager.set("spawn.y", saved.getY());
        ConfigManager.set("spawn.z", saved.getZ());
        ConfigManager.set("spawn.yaw", saved.getYaw());
        ConfigManager.set("spawn.pitch", saved.getPitch());
        ConfigManager.save();

        // Plugin spawn and Minecraft's world spawn are independent by default.
        if (ConfigManager.getBoolean("spawn.update-world-spawn", false)) {
            world.setSpawnLocation(saved.getBlockX(), saved.getBlockY(), saved.getBlockZ());
        }
        MessageManager.send(player, "messages.setspawn",
                "&aSpawn location has been updated.");
    }

    private static boolean isValid(Location location) {
        if (location == null || location.getWorld() == null) {
            return false;
        }
        return Double.isFinite(location.getX())
                && Double.isFinite(location.getY())
                && Double.isFinite(location.getZ())
                && Float.isFinite(location.getYaw())
                && Float.isFinite(location.getPitch())
                && Math.abs(location.getX()) <= 30_000_000D
                && Math.abs(location.getZ()) <= 30_000_000D
                && location.getY() >= location.getWorld().getMinHeight() - 64D
                && location.getY() <= location.getWorld().getMaxHeight() + 64D;
    }
}
