package com.bedlessfighter.necroManager.managers;

import com.bedlessfighter.necroManager.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class HomeManager {
    private static File file;
    private static YamlConfiguration data;
    private HomeManager() { }

    public static synchronized void load() {
        file = new File(Main.getPlugin().getDataFolder(), "homes.yml");
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
        data = YamlConfiguration.loadConfiguration(file);
    }
    private static void ready() { if (data == null) load(); }
    private static void save() {
        try { data.save(file); }
        catch (IOException ex) { throw new IllegalStateException("Could not save homes.yml", ex); }
    }
    private static String base(UUID id) { return "players." + id; }
    public static synchronized List<String> list(UUID id) {
        ready();
        ConfigurationSection section = data.getConfigurationSection(base(id));
        if (section == null) return List.of();
        return section.getKeys(false).stream().sorted(String.CASE_INSENSITIVE_ORDER).toList();
    }
    public static synchronized int limit(Player player) {
        int configured = Math.max(1, Main.getPlugin().getConfig().getInt("homes.default-limit", 3));
        if (player.hasPermission("necromanager.homes.unlimited")) return Integer.MAX_VALUE;
        for (int n = 100; n >= 1; n--) if (player.hasPermission("necromanager.homes." + n)) return n;
        return configured;
    }
    public static synchronized boolean set(Player player, String name) {
        ready();
        String key = name.toLowerCase();
        if (!key.matches("[a-z0-9_-]{1,32}")) return false;
        String path = base(player.getUniqueId()) + "." + key;
        if (!data.contains(path) && list(player.getUniqueId()).size() >= limit(player)) return false;
        Location loc = player.getLocation();
        data.set(path + ".world", loc.getWorld().getName());
        data.set(path + ".x", loc.getX());
        data.set(path + ".y", loc.getY());
        data.set(path + ".z", loc.getZ());
        data.set(path + ".yaw", loc.getYaw());
        data.set(path + ".pitch", loc.getPitch());
        save();
        return true;
    }
    public static synchronized Location get(UUID id, String name) {
        ready();
        String path = base(id) + "." + name.toLowerCase();
        if (!data.isConfigurationSection(path)) return null;
        World world = Bukkit.getWorld(data.getString(path + ".world", ""));
        if (world == null) return null;
        return new Location(world, data.getDouble(path + ".x"), data.getDouble(path + ".y"),
                data.getDouble(path + ".z"), (float) data.getDouble(path + ".yaw"),
                (float) data.getDouble(path + ".pitch"));
    }
    public static synchronized boolean delete(UUID id, String name) {
        ready();
        String path = base(id) + "." + name.toLowerCase();
        if (!data.contains(path)) return false;
        data.set(path, null);
        if (list(id).isEmpty()) data.set(base(id), null);
        save();
        return true;
    }
    public static synchronized boolean rename(UUID id, String oldName, String newName) {
        ready();
        String oldPath = base(id) + "." + oldName.toLowerCase();
        String newKey = newName.toLowerCase();
        if (!data.isConfigurationSection(oldPath) || !newKey.matches("[a-z0-9_-]{1,32}")
                || data.contains(base(id) + "." + newKey)) return false;
        data.set(base(id) + "." + newKey, data.getConfigurationSection(oldPath).getValues(true));
        data.set(oldPath, null);
        save();
        return true;
    }
}
