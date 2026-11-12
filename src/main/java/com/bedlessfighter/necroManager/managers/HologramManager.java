package com.bedlessfighter.necroManager.managers;

import com.bedlessfighter.necroManager.Main;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.Display;
import org.bukkit.entity.Entity;
import org.bukkit.entity.TextDisplay;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

/** Persistent text holograms stored as location and editable line data. */
public final class HologramManager {
    private static final Map<String, Hologram> HOLOGRAMS = new LinkedHashMap<>();
    private static final Map<String, TextDisplay> DISPLAYS = new LinkedHashMap<>();
    private static NamespacedKey marker;
    private static File file;
    private static YamlConfiguration data;

    private HologramManager() { throw new UnsupportedOperationException("Utility class"); }

    public static void load() {
        marker = new NamespacedKey(Main.getPlugin(), "hologram_id");
        file = new File(Main.getPlugin().getDataFolder(), "holograms.yml");
        data = YamlConfiguration.loadConfiguration(file);
        HOLOGRAMS.clear();
        for (World world : Bukkit.getWorlds()) {
            for (Entity entity : world.getEntities()) {
                if (entity instanceof TextDisplay display
                        && display.getPersistentDataContainer().has(marker, PersistentDataType.STRING)) {
                    display.remove();
                }
            }
        }
        for (String id : data.getKeys(false)) {
            String worldName = data.getString(id + ".world");
            World world = worldName == null ? null : Bukkit.getWorld(worldName);
            if (world == null) {
                Main.getPlugin().getLogger().warning("Skipping hologram '" + id + "': world '" + worldName + "' is unavailable.");
                continue;
            }
            List<String> lines = data.getStringList(id + ".lines");
            if (lines.isEmpty()) lines = new ArrayList<>(List.of("&e" + id));
            Location location = new Location(world,
                    data.getDouble(id + ".x"), data.getDouble(id + ".y"), data.getDouble(id + ".z"),
                    (float) data.getDouble(id + ".yaw"), (float) data.getDouble(id + ".pitch"));
            HOLOGRAMS.put(id, new Hologram(location, new ArrayList<>(lines)));
            spawn(id);
        }
    }

    public static boolean create(String id, Location location) {
        if (!validId(id) || HOLOGRAMS.containsKey(id) || location.getWorld() == null) return false;
        HOLOGRAMS.put(id, new Hologram(location.clone(), new ArrayList<>(List.of("&e" + id))));
        save();
        spawn(id);
        return true;
    }

    public static boolean delete(String id) {
        Hologram removed = HOLOGRAMS.remove(id);
        if (removed == null) return false;
        TextDisplay display = DISPLAYS.remove(id);
        if (display != null && display.isValid()) display.remove();
        save();
        return true;
    }

    public static boolean addLine(String id, String line) {
        Hologram hologram = HOLOGRAMS.get(id);
        if (hologram == null || hologram.lines.size() >= 20 || line == null || line.isBlank()) return false;
        hologram.lines.add(line);
        refresh(id);
        save();
        return true;
    }

    public static boolean setLine(String id, int line, String value) {
        Hologram hologram = HOLOGRAMS.get(id);
        if (hologram == null || line < 1 || line > hologram.lines.size() || value == null || value.isBlank()) return false;
        hologram.lines.set(line - 1, value);
        refresh(id);
        save();
        return true;
    }

    public static boolean removeLine(String id, int line) {
        Hologram hologram = HOLOGRAMS.get(id);
        if (hologram == null || line < 1 || line > hologram.lines.size()) return false;
        hologram.lines.remove(line - 1);
        if (hologram.lines.isEmpty()) hologram.lines.add("&e");
        refresh(id);
        save();
        return true;
    }

    public static Set<String> ids() { return new TreeSet<>(HOLOGRAMS.keySet()); }
    public static List<String> lines(String id) {
        Hologram hologram = HOLOGRAMS.get(id);
        return hologram == null ? List.of() : List.copyOf(hologram.lines);
    }
    public static int lineCount(String id) { return lines(id).size(); }
    public static boolean exists(String id) { return HOLOGRAMS.containsKey(id); }

    public static void unload() {
        for (TextDisplay display : DISPLAYS.values()) {
            if (display.isValid()) display.remove();
        }
        DISPLAYS.clear();
    }

    private static void refresh(String id) {
        TextDisplay old = DISPLAYS.remove(id);
        if (old != null && old.isValid()) old.remove();
        spawn(id);
    }

    private static void spawn(String id) {
        Hologram hologram = HOLOGRAMS.get(id);
        if (hologram == null || hologram.location.getWorld() == null) return;
        TextDisplay display = hologram.location.getWorld().spawn(hologram.location, TextDisplay.class, entity -> {
            entity.text(Component.text(MessageManager.color(String.join("\n", hologram.lines))));
            entity.setBillboard(Display.Billboard.CENTER);
            entity.setSeeThrough(true);
            entity.setShadowed(true);
            entity.setInvulnerable(true);
            entity.setPersistent(true);
            entity.getPersistentDataContainer().set(marker, PersistentDataType.STRING, id);
        });
        DISPLAYS.put(id, display);
    }

    private static void save() {
        YamlConfiguration next = new YamlConfiguration();
        for (Map.Entry<String, Hologram> entry : HOLOGRAMS.entrySet()) {
            String path = entry.getKey();
            Hologram hologram = entry.getValue();
            Location location = hologram.location;
            next.set(path + ".world", location.getWorld().getName());
            next.set(path + ".x", location.getX());
            next.set(path + ".y", location.getY());
            next.set(path + ".z", location.getZ());
            next.set(path + ".yaw", location.getYaw());
            next.set(path + ".pitch", location.getPitch());
            next.set(path + ".lines", hologram.lines);
        }
        try {
            next.save(file);
            data = next;
        } catch (IOException exception) {
            Main.getPlugin().getLogger().severe("Could not save holograms.yml: " + exception.getMessage());
        }
    }

    private static boolean validId(String id) {
        return id != null && id.matches("[A-Za-z0-9_-]{1,32}");
    }

    private record Hologram(Location location, List<String> lines) { }
}
