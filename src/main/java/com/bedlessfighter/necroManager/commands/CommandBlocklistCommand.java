package com.bedlessfighter.necroManager.commands;

import com.bedlessfighter.necroManager.managers.ConfigManager;
import com.bedlessfighter.necroManager.managers.MessageManager;
import com.bedlessfighter.necroManager.managers.PermissionManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/** Administrative CRUD for the player command blocklist. */
public final class CommandBlocklistCommand implements CommandExecutor, TabCompleter {
    private static final String PATH = "command-blocklist.commands";

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {
        if (!PermissionManager.has(sender, "necromanager.commandblocklist.manage")) {
            MessageManager.sendRaw(sender, "&cYou do not have permission.");
            return true;
        }
        if (args.length == 0 || args[0].equalsIgnoreCase("list")) {
            List<String> blocked = ConfigManager.getStringList(PATH);
            MessageManager.sendRaw(sender, "&eBlocked player commands (&f" + blocked.size() + "&e): "
                    + (blocked.isEmpty() ? "&7none" : "&f" + String.join("&7, &f", blocked)));
            return true;
        }
        if (args.length != 2 || !(args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("remove"))) {
            MessageManager.sendRaw(sender, "&cUsage: /commandblocklist <add|remove|list> [command]");
            return true;
        }
        String value = args[1].trim().toLowerCase(Locale.ROOT);
        while (value.startsWith("/")) value = value.substring(1);
        final String commandLabel = value;
        if (!commandLabel.matches("[a-z0-9_:-]+") || commandLabel.endsWith(":")) {
            MessageManager.sendRaw(sender, "&cEnter a valid command label without arguments.");
            return true;
        }
        List<String> blocked = new ArrayList<>(ConfigManager.getStringList(PATH));
        if (args[0].equalsIgnoreCase("add")) {
            if (blocked.stream().anyMatch(entry -> normalize(entry).equals(commandLabel))) {
                MessageManager.sendRaw(sender, "&eThat command is already blocked.");
                return true;
            }
            blocked.add(commandLabel);
            ConfigManager.set(PATH, blocked);
            ConfigManager.save();
            MessageManager.sendRaw(sender, "&aAdded &f/" + value + " &ato the player command blocklist.");
        } else {
            boolean removed = blocked.removeIf(entry -> normalize(entry).equals(commandLabel));
            if (!removed) {
                MessageManager.sendRaw(sender, "&cThat command is not in the blocklist.");
                return true;
            }
            ConfigManager.set(PATH, blocked);
            ConfigManager.save();
            MessageManager.sendRaw(sender, "&aRemoved &f/" + value + " &afrom the player command blocklist.");
        }
        return true;
    }

    private static String normalize(String value) {
        String normalized = value.toLowerCase(Locale.ROOT).trim();
        while (normalized.startsWith("/")) normalized = normalized.substring(1);
        return normalized;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
                                      @NotNull String alias, @NotNull String[] args) {
        if (!PermissionManager.has(sender, "necromanager.commandblocklist.manage")) return List.of();
        if (args.length == 1) return List.of("add", "remove", "list").stream()
                .filter(value -> value.startsWith(args[0].toLowerCase(Locale.ROOT))).toList();
        if (args.length == 2 && args[0].equalsIgnoreCase("remove")) {
            return ConfigManager.getStringList(PATH).stream()
                    .filter(value -> value.toLowerCase(Locale.ROOT).startsWith(args[1].toLowerCase(Locale.ROOT))).toList();
        }
        return List.of();
    }
}
