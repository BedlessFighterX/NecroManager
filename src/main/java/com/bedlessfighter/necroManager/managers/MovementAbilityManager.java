package com.bedlessfighter.necroManager.managers;

import com.bedlessfighter.necroManager.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class MovementAbilityManager {
    private static final Map<UUID, Long> LAUNCH_COOLDOWNS = new ConcurrentHashMap<>();
    private static final Map<UUID, Long> JUMP_COOLDOWNS = new ConcurrentHashMap<>();

    private MovementAbilityManager() { throw new UnsupportedOperationException("Utility class"); }

    public static boolean launchpad(Player player, Material material) {
        FileConfiguration c = Main.getPlugin().getConfig();
        if (!c.getBoolean("movement.launchpads.enabled", false)
                || !player.hasPermission("necromanager.launchpad")) return false;
        List<String> configured = c.getStringList("movement.launchpads.blocks");
        if (configured.stream().noneMatch(s -> s.equalsIgnoreCase(material.name()))) return false;
        if (!ready(LAUNCH_COOLDOWNS, player, c.getLong("movement.launchpads.cooldown-seconds", 2))) return false;
        double horizontal = clamp(c.getDouble("movement.launchpads.horizontal", 1.8), 0, 5);
        double vertical = clamp(c.getDouble("movement.launchpads.vertical", 1.1), 0.1, 4);
        Vector direction = player.getLocation().getDirection().setY(0);
        if (direction.lengthSquared() > 0) direction.normalize().multiply(horizontal);
        direction.setY(vertical);
        player.setVelocity(direction);
        play(player, c.getString("movement.launchpads.sound", "ENTITY_SLIME_JUMP"));
        return true;
    }

    public static boolean doubleJump(Player player) {
        FileConfiguration c = Bukkit.getPluginManager().getPlugin("NecroManager").getConfig();
        if (!c.getBoolean("movement.double-jump.enabled", false)
                || !player.hasPermission("necromanager.doublejump")) return false;
        if (!ready(JUMP_COOLDOWNS, player, c.getLong("movement.double-jump.cooldown-seconds", 3))) return false;
        Vector velocity = player.getVelocity();
        velocity.setY(clamp(c.getDouble("movement.double-jump.vertical", 0.85), 0.1, 3));
        player.setVelocity(velocity);
        play(player, c.getString("movement.double-jump.sound", "ENTITY_BAT_TAKEOFF"));
        return true;
    }

    public static boolean doubleJumpEnabled() {
        return Bukkit.getPluginManager().getPlugin("NecroManager").getConfig()
                .getBoolean("movement.double-jump.enabled", false);
    }

    private static boolean ready(Map<UUID, Long> cooldowns, Player player, long seconds) {
        long now = System.currentTimeMillis();
        long until = cooldowns.getOrDefault(player.getUniqueId(), 0L);
        if (now < until) return false;
        cooldowns.put(player.getUniqueId(), now + Math.max(0, seconds) * 1000L);
        return true;
    }

    private static void play(Player player, String configured) {
        try {
            player.playSound(player.getLocation(), Sound.valueOf(configured.toUpperCase(Locale.ROOT)), 1f, 1f);
        } catch (IllegalArgumentException ignored) {
            // Invalid optional sound configuration should not interrupt movement.
        }
    }

    private static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    public static void clear(Player player) {
        LAUNCH_COOLDOWNS.remove(player.getUniqueId());
        JUMP_COOLDOWNS.remove(player.getUniqueId());
    }

    public static void clearAll() {
        LAUNCH_COOLDOWNS.clear();
        JUMP_COOLDOWNS.clear();
    }
}
