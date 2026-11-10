package com.bedlessfighter.necroManager.managers;

import org.bukkit.World;

/**
 * Resolves global gameplay restrictions with optional per-world overrides.
 * World UUIDs are used as keys so overrides remain stable if a world is renamed.
 */
public final class RestrictionManager {

    private RestrictionManager() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static boolean isEnabled(World world, String settingPath, boolean fallback) {
        if (world != null) {
            String worldPath = "world-overrides." + world.getUID() + "." + settingPath;
            if (ConfigManager.contains(worldPath)) {
                return ConfigManager.getBoolean(worldPath, fallback);
            }
        }
        return ConfigManager.getBoolean(settingPath, fallback);
    }
}
