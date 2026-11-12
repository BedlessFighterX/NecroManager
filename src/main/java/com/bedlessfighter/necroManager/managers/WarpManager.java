package com.bedlessfighter.necroManager.managers;

import com.bedlessfighter.necroManager.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;

public final class WarpManager {
    private static File file;
    private static YamlConfiguration data;
    private WarpManager() { }

    public static synchronized void load() {
        file = new File(Main.getPlugin().getDataFolder(), "warps.yml");
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
        data = YamlConfiguration.loadConfiguration(file);
    }
    private static void ready() { if (data == null) load(); }
    private static void save() {
        try { data.save(file); }
        catch (IOException ex) { throw new IllegalStateException("Could not save warps.yml", ex); }
    }
    public static boolean validName(String name) { return name != null && name.matches("[a-zA-Z0-9_-]{1,32}"); }
    public static synchronized List<String> list() {
        ready();
        ConfigurationSection section = data.getConfigurationSection("warps");
        if (section == null) return List.of();
        return section.getKeys(false).stream().sorted(String.CASE_INSENSITIVE_ORDER).toList();
    }
    public static synchronized boolean set(String name, Location location) {
        ready();
        if (!validName(name) || location.getWorld() == null) return false;
        String path = "warps." + name.toLowerCase();
        data.set(path + ".world", location.getWorld().getName());
        data.set(path + ".x", location.getX());
        data.set(path + ".y", location.getY());
        data.set(path + ".z", location.getZ());
        data.set(path + ".yaw", location.getYaw());
        data.set(path + ".pitch", location.getPitch());
        save();
        return true;
    }
    public static synchronized Location get(String name) {
        ready();
        String path = "warps." + name.toLowerCase();
        if (!data.isConfigurationSection(path)) return null;
        World world = Bukkit.getWorld(data.getString(path + ".world", ""));
        if (world == null) return null;
        return new Location(world, data.getDouble(path + ".x"), data.getDouble(path + ".y"),
                data.getDouble(path + ".z"), (float) data.getDouble(path + ".yaw"),
                (float) data.getDouble(path + ".pitch"));
    }
    public static synchronized boolean delete(String name) {
        ready();
        String path = "warps." + name.toLowerCase();
        if (!data.contains(path)) return false;
        data.set(path, null);
        save();
        return true;
    }
}
