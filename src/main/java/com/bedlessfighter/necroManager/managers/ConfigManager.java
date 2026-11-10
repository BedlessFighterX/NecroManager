package com.bedlessfighter.necroManager.managers;

import com.bedlessfighter.necroManager.Main;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * Centralized, default-safe access to NecroManager's configuration.
 *
 * Keep configuration paths stable; this facade provides fallback values when
 * a key is absent or has an unexpected value.
 */
public final class ConfigManager {

    private ConfigManager() {
    }

    private static FileConfiguration config() {
        return Main.getPlugin().getConfig();
    }

    public static boolean getBoolean(String path, boolean fallback) {
        return config().getBoolean(path, fallback);
    }

    public static String getString(String path, String fallback) {
        String value = config().getString(path);
        return value == null ? fallback : value;
    }

    public static int getInt(String path, int fallback) {
        return config().getInt(path, fallback);
    }

    public static double getDouble(String path, double fallback) {
        return config().getDouble(path, fallback);
    }

    public static Location getLocation(String path) {
        Location location = config().getLocation(path);
        return location == null ? null : location.clone();
    }

    public static void set(String path, Object value) {
        config().set(path, value);
    }

    public static void save() {
        Main.getPlugin().saveConfig();
    }

    public static void reload() {
        Main.getPlugin().reloadConfig();
    }
}
