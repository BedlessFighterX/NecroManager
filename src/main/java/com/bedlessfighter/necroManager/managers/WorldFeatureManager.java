package com.bedlessfighter.necroManager.managers;

import org.bukkit.entity.Player;
import java.util.List;

/** Centralized world exclusions for optional NecroManager features. */
public final class WorldFeatureManager {
    private WorldFeatureManager() { throw new UnsupportedOperationException("Utility class"); }

    /**
     * Returns true when a feature is excluded in the player's current world.
     * World names are case-insensitive; an empty/missing list means no exclusion.
     */
    public static boolean isDisabled(Player player, String feature) {
        if (player == null || feature == null || feature.isBlank()) return false;
        List<String> worlds = ConfigManager.getStringList("world-exclusions." + feature + ".disabled-worlds");
        return worlds.stream().anyMatch(world -> world.equalsIgnoreCase(player.getWorld().getName()));
    }
}
