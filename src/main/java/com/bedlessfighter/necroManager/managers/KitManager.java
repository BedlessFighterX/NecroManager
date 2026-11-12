package com.bedlessfighter.necroManager.managers;

import com.bedlessfighter.necroManager.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class KitManager {
    private static YamlConfiguration data;
    private static File file;
    private KitManager() { }

    public static synchronized void load() {
        file = new File(Main.getPlugin().getDataFolder(), "kits.yml");
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
        data = YamlConfiguration.loadConfiguration(file);
    }
    private static void ready() { if (data == null) load(); }
    private static void save() {
        try { data.save(file); }
        catch (IOException ex) { throw new IllegalStateException("Could not save kits.yml", ex); }
    }
    public static synchronized List<String> kits() {
        ready();
        ConfigurationSection section = data.getConfigurationSection("kits");
        if (section == null) return List.of();
        return section.getKeys(false).stream().sorted(String.CASE_INSENSITIVE_ORDER).toList();
    }
    public static synchronized boolean exists(String name) { ready(); return data.isConfigurationSection("kits." + name.toLowerCase()); }
    public static synchronized boolean create(String name, Player player, long cooldown) {
        ready();
        String id = name.toLowerCase();
        if (id.isBlank() || !id.matches("[a-z0-9_-]{1,32}") || exists(id)) return false;
        data.set("kits." + id + ".contents", player.getInventory().getStorageContents());
        data.set("kits." + id + ".armor", player.getInventory().getArmorContents());
        data.set("kits." + id + ".offhand", player.getInventory().getItemInOffHand());
        data.set("kits." + id + ".cooldown-seconds", Math.max(0, cooldown));
        save();
        return true;
    }
    public static synchronized boolean delete(String name) {
        ready();
        if (!exists(name)) return false;
        data.set("kits." + name.toLowerCase(), null);
        save();
        return true;
    }
    public static synchronized long cooldown(String name) {
        ready();
        return Math.max(0, data.getLong("kits." + name.toLowerCase() + ".cooldown-seconds", 0));
    }
    public static synchronized long remaining(UUID uuid, String name) {
        ready();
        long until = data.getLong("cooldowns." + uuid + "." + name.toLowerCase(), 0);
        return Math.max(0, until - System.currentTimeMillis() / 1000L);
    }
    public static synchronized boolean claim(Player player, String name) {
        ready();
        String path = "kits." + name.toLowerCase();
        if (!data.isConfigurationSection(path)) return false;
        if (remaining(player.getUniqueId(), name) > 0) return false;
        ItemStack[] contents = data.getList(path + ".contents", new ArrayList<>()).toArray(new ItemStack[0]);
        ItemStack[] armor = data.getList(path + ".armor", new ArrayList<>()).toArray(new ItemStack[0]);
        player.getInventory().addItem(contents);
        player.getInventory().setArmorContents(armor);
        Object offhand = data.get(path + ".offhand");
        if (offhand instanceof ItemStack item && item.getType() != Material.AIR) {
            ItemStack current = player.getInventory().getItemInOffHand();
            if (current.getType() == Material.AIR) player.getInventory().setItemInOffHand(item.clone());
            else player.getWorld().dropItemNaturally(player.getLocation(), item.clone());
        }
        long seconds = cooldown(name);
        if (seconds > 0) data.set("cooldowns." + player.getUniqueId() + "." + name.toLowerCase(), System.currentTimeMillis() / 1000L + seconds);
        save();
        return true;
    }
    public static synchronized List<ItemStack> preview(String name) {
        ready();
        if (!exists(name)) return List.of();
        List<ItemStack> items = new ArrayList<>();
        for (Object value : data.getList("kits." + name.toLowerCase() + ".contents", List.of())) if (value instanceof ItemStack item && item.getType() != Material.AIR) items.add(item.clone());
        for (Object value : data.getList("kits." + name.toLowerCase() + ".armor", List.of())) if (value instanceof ItemStack item && item.getType() != Material.AIR) items.add(item.clone());
        Object offhand = data.get("kits." + name.toLowerCase() + ".offhand");
        if (offhand instanceof ItemStack item && item.getType() != Material.AIR) items.add(item.clone());
        return items;
    }
    public static synchronized boolean reset(UUID uuid, String name) {
        ready();
        if (!exists(name)) return false;
        data.set("cooldowns." + uuid + "." + name.toLowerCase(), null);
        save();
        return true;
    }
}
