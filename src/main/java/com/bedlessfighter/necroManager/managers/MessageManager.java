package com.bedlessfighter.necroManager.managers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

/**
 * Shared message formatting and delivery for commands and event handlers.
 *
 * Message templates are read from the existing configuration paths, support
 * legacy ampersand color codes, and accept simple %placeholder% tokens.
 */
public final class MessageManager {

    private MessageManager() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static String color(String message) {
        return ChatColor.translateAlternateColorCodes('&', message == null ? "" : message);
    }

    public static String prefix() {
        return color(ConfigManager.getString("messages.prefix", "&7[NecroManager]&r"));
    }

    public static String format(String template, Map<String, String> placeholders) {
        String message = template == null ? "" : template;
        if (placeholders != null) {
            for (Map.Entry<String, String> entry : placeholders.entrySet()) {
                if (entry.getKey() == null || entry.getValue() == null) continue;
                message = message.replace("%" + entry.getKey() + "%", entry.getValue());
            }
        }
        return color(message);
    }

    /**
     * Common player placeholders for join, quit, death, and command messages.
     */
    public static Map<String, String> playerPlaceholders(Player player) {
        if (player == null) return Map.of();
        return Map.of(
                "player", player.getName(),
                "displayname", player.getDisplayName(),
                "world", player.getWorld().getName(),
                "online", Integer.toString(Bukkit.getOnlinePlayers().size())
        );
    }

    public static String configured(String path, String fallback) {
        return format(ConfigManager.getString(path, fallback), Map.of());
    }

    public static String prefixed(String path, String fallback) {
        return prefix() + " " + configured(path, fallback);
    }

    public static void send(CommandSender recipient, String path) { send(recipient, path, ""); }\n\n    public static void send(CommandSender recipient, String path, Map<String, String> placeholders) {\n        send(recipient, path, \"\", placeholders);\n    }\n\n    public static void send(CommandSender recipient, String path, String fallback) {
        recipient.sendMessage(prefixed(path, fallback));
    }

    public static void sendRaw(CommandSender recipient, String message) {
        recipient.sendMessage(color(message));
    }

    public static void send(CommandSender recipient, String path, String fallback,
                            Map<String, String> placeholders) {
        recipient.sendMessage(prefix() + " "
                + format(ConfigManager.getString(path, fallback), placeholders));
    }
}
