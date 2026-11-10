package com.bedlessfighter.necroManager.managers;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
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

    /**
     * Finds a safe standing position for a spawn teleport. The configured point is
     * preferred; nearby positions are considered only when it is obstructed or hazardous.
     */
    public static Location getSafeSpawn() {
        Location configured = getSpawn();
        if (!isValid(configured)) {
            return null;
        }
        if (!ConfigManager.getBoolean("spawn.safe-teleport", true)) {
            return configured;
        }

        World world = configured.getWorld();
        int radius = ConfigManager.getInt("spawn.safe-search-radius", 4, 0, 16);
        int verticalRange = ConfigManager.getInt("spawn.safe-search-height", 3, 0, 16);
        int baseX = configured.getBlockX();
        int baseY = configured.getBlockY();
        int baseZ = configured.getBlockZ();
        Location best = null;
        double bestDistance = Double.MAX_VALUE;

        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                for (int dy = -verticalRange; dy <= verticalRange; dy++) {
                    int x = baseX + dx;
                    int y = baseY + dy;
                    int z = baseZ + dz;
                    if (y <= world.getMinHeight() || y + 1 >= world.getMaxHeight()) {
                        continue;
                    }

                    Location candidate = new Location(world, x + 0.5D, y, z + 0.5D,
                            configured.getYaw(), configured.getPitch());
                    if (!world.getWorldBorder().isInside(candidate)) {
                        continue;
                    }

                    Block feet = world.getBlockAt(x, y, z);
                    Block head = world.getBlockAt(x, y + 1, z);
                    Block floor = world.getBlockAt(x, y - 1, z);
                    if (!isSafeStandingBlock(feet) || !isSafeStandingBlock(head)
                            || !isSafeFloor(floor)) {
                        continue;
                    }

                    double distance = dx * dx + dz * dz + dy * dy;
                    if (distance < bestDistance) {
                        best = candidate;
                        bestDistance = distance;
                        if (distance == 0.0D) {
                            return best;
                        }
                    }
                }
            }
        }
        return best;
    }

    private static boolean isSafeStandingBlock(Block block) {
        Material type = block.getType();
        return block.isPassable() && !block.isLiquid() && !isHazardous(type);
    }

    private static boolean isSafeFloor(Block block) {
        Material type = block.getType();
        return type.isSolid() && !block.isLiquid() && !isHazardous(type);
    }

    private static boolean isHazardous(Material type) {
        return switch (type) {
            case LAVA, FIRE, SOUL_FIRE, CACTUS, MAGMA_BLOCK, CAMPFIRE, SOUL_CAMPFIRE,
                    POWDER_SNOW, SWEET_BERRY_BUSH, WITHER_ROSE, POINTED_DRIPSTONE,
                    BIG_DRIPLEAF_STEM -> true;
            default -> false;
        };
    }

    public static void sendToSpawn(Player player) {
        teleportToSpawn(player, true);
    }

    /**
     * Teleports a player to the configured plugin spawn.
     *
     * @param player player to teleport
     * @param notify whether command-style feedback should be sent
     * @return true when the teleport succeeds
     */
    public static boolean teleportToSpawn(Player player, boolean notify) {
        Location configuredSpawn = getSpawn();
        if (!isValid(configuredSpawn)) {
            if (notify) {
                MessageManager.send(player, "messages.spawn-not-set",
                        "&cSpawn has not been set yet.");
            }
            return false;
        }

        Location spawn = getSafeSpawn();
        if (!isValid(spawn)) {
            if (notify) {
                MessageManager.send(player, "messages.spawn-unsafe",
                        "&cNo safe location could be found near the configured spawn.");
            }
            return false;
        }

        if (!player.teleport(spawn)) {
            if (notify) {
                MessageManager.send(player, "messages.spawn-failed",
                        "&cCould not teleport you to spawn.");
            }
            return false;
        }

        if (notify) {
            MessageManager.send(player, "messages.spawnmessage",
                    "&rYou have been &ateleported&r to the &espawn!");
        }
        return true;
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
        boolean updateWorldSpawn = ConfigManager.getBoolean("spawn.update-world-spawn", false);

        // Remove the legacy serialized Location before writing the structured section.
        // Preserve the opt-in setting because clearing the old root also clears its children.
        ConfigManager.set("spawn", null);
        ConfigManager.set("spawn.update-world-spawn", updateWorldSpawn);
        ConfigManager.set("spawn.world", world.getName());
        ConfigManager.set("spawn.world-uuid", world.getUID().toString());
        ConfigManager.set("spawn.x", saved.getX());
        ConfigManager.set("spawn.y", saved.getY());
        ConfigManager.set("spawn.z", saved.getZ());
        ConfigManager.set("spawn.yaw", saved.getYaw());
        ConfigManager.set("spawn.pitch", saved.getPitch());
        ConfigManager.save();

        // Plugin spawn and Minecraft's world spawn are independent by default.
        if (updateWorldSpawn) {
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
