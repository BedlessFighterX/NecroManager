package com.bedlessfighter.necroManager.managers;

import com.bedlessfighter.necroManager.Main;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

/**
 * Centralized, default-safe access to NecroManager's configuration.
 *
 * Keep configuration paths stable. Callers should use this facade instead of
 * reaching into JavaPlugin#getConfig() directly so defaults and validation
 * remain consistent throughout the plugin.
 */
public final class ConfigManager {

    private ConfigManager() {
        throw new UnsupportedOperationException("Utility class");
    }

    private static FileConfiguration config() {
        return Main.getPlugin().getConfig();
    }

    /**
     * Reads a boolean setting, returning the supplied fallback when the path
     * is absent or cannot be interpreted as a boolean.
     */
    public static boolean getBoolean(String path, boolean fallback) {
        return config().getBoolean(path, fallback);
    }

    /**
     * Reads a string setting. An absent or non-string value uses the fallback.
     * An intentionally empty string remains empty.
     */
    public static String getString(String path, String fallback) {
        String value = config().getString(path);
        return value == null ? fallback : value;
    }

    public static int getInt(String path, int fallback) {
        return config().getInt(path, fallback);
    }

    public static int getInt(String path, int fallback, int minimum, int maximum) {
        if (minimum > maximum) {
            throw new IllegalArgumentException("minimum cannot be greater than maximum");
        }
        int value = getInt(path, fallback);
        return value < minimum || value > maximum ? fallback : value;
    }

    public static long getLong(String path, long fallback) {
        return config().getLong(path, fallback);
    }

    public static double getDouble(String path, double fallback) {
        return config().getDouble(path, fallback);
    }

    public static double getDouble(String path, double fallback, double minimum, double maximum) {
        if (minimum > maximum) {
            throw new IllegalArgumentException("minimum cannot be greater than maximum");
        }
        double value = getDouble(path, fallback);
        return !Double.isFinite(value) || value < minimum || value > maximum ? fallback : value;
    }

    /**
     * Returns a defensive copy so callers cannot mutate the live config list.
     */
    public static List<String> getStringList(String path, List<String> fallback) {\n        return config().contains(path) ? List.copyOf(config().getStringList(path)) : List.copyOf(fallback);\n    }\n\n    public static List<String> getStringList(String path) {
        return List.copyOf(config().getStringList(path));
    }

    /**
     * Returns a defensive copy because Bukkit Location instances are mutable.
     */
    public static Location getLocation(String path) {
        Location location = config().getLocation(path);
        return location == null ? null : location.clone();
    }

    public static boolean contains(String path) {
        return config().contains(path);
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
