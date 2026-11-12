package com.bedlessfighter.necroManager.managers;

import com.bedlessfighter.necroManager.Main;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Locale;
import java.util.Map;

/** Executes ordered, configurable join actions on the server thread. */
public final class JoinActionManager {
    private JoinActionManager() { throw new UnsupportedOperationException("Utility class"); }

    public static void execute(Player player) {
        if (WorldFeatureManager.isDisabled(player, "join-actions")) return;
        if (!ConfigManager.getBoolean("joins.actions.enabled", false)) return;
        List<Map<?, ?>> actions = Main.getPlugin().getConfig().getMapList("joins.actions.entries");
        for (Map<?, ?> action : actions) {
            String type = string(action, "type", "").toLowerCase(Locale.ROOT);
            String value = string(action, "value", "");
            long delay = Math.max(0, number(action, "delay-ticks", 0));
            String requiredPermission = string(action, "permission", "");
            Runnable task = () -> {
                if (!player.isOnline()) return;
                if (!requiredPermission.isBlank() && !player.hasPermission(requiredPermission)) return;
                runAction(player, type, value, action);
            };
            if (delay == 0) task.run();
            else Bukkit.getScheduler().runTaskLater(Main.getPlugin(), task, delay);
        }
    }

    private static void runAction(Player player, String type, String value, Map<?, ?> action) {
        Map<String, String> placeholders = MessageManager.playerPlaceholders(player);
        String formatted = MessageManager.format(value, placeholders);
        try {
            switch (type) {
                case "message" -> player.sendMessage(formatted);
                case "broadcast" -> Bukkit.broadcastMessage(formatted);
                case "title" -> player.sendTitle(formatted,
                        MessageManager.format(string(action, "subtitle", ""), placeholders),
                        (int) number(action, "fade-in", 10),
                        (int) number(action, "stay", 60),
                        (int) number(action, "fade-out", 20));
                case "actionbar" -> player.sendActionBar(Component.text(org.bukkit.ChatColor.stripColor(formatted)));
                case "sound" -> player.playSound(player.getLocation(),
                        Sound.valueOf(value.toUpperCase(Locale.ROOT)),
                        (float) decimal(action, "volume", 1.0),
                        (float) decimal(action, "pitch", 1.0));
                case "command" -> Bukkit.dispatchCommand(player, stripSlash(formatted));
                case "console" -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), stripSlash(formatted));
                case "gamemode" -> player.setGameMode(GameMode.valueOf(value.toUpperCase(Locale.ROOT)));
                case "effect" -> {
                    String[] parts = value.split(":", 3);
                    PotionEffectType effect = PotionEffectType.getByName(parts[0].toUpperCase(Locale.ROOT));
                    if (effect == null) throw new IllegalArgumentException("Unknown potion effect: " + parts[0]);
                    int seconds = parts.length > 1 ? Integer.parseInt(parts[1]) : 30;
                    int amplifier = parts.length > 2 ? Integer.parseInt(parts[2]) : 0;
                    player.addPotionEffect(new PotionEffect(effect, Math.max(1, seconds) * 20,
                            Math.max(0, amplifier)));
                }
                case "close" -> player.closeInventory();
                default -> Main.getPlugin().getLogger().warning("Unknown joins.actions type: " + type);
            }
        } catch (IllegalArgumentException exception) {
            Main.getPlugin().getLogger().warning("Invalid join action '" + type + "' value '" + value + "': " + exception.getMessage());
        }
    }

    private static String stripSlash(String command) {
        return command.startsWith("/") ? command.substring(1) : command;
    }

    private static String string(Map<?, ?> map, String key, String fallback) {
        Object value = map.get(key);
        return value == null ? fallback : String.valueOf(value);
    }

    private static long number(Map<?, ?> map, String key, long fallback) {
        Object value = map.get(key);
        return value instanceof Number n ? n.longValue() : fallback;
    }

    private static double decimal(Map<?, ?> map, String key, double fallback) {
        Object value = map.get(key);
        return value instanceof Number n ? n.doubleValue() : fallback;
    }
}
