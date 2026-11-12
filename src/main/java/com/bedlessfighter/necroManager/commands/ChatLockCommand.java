package com.bedlessfighter.necroManager.commands;

import com.bedlessfighter.necroManager.managers.ConfigManager;
import com.bedlessfighter.necroManager.managers.MessageManager;
import com.bedlessfighter.necroManager.managers.PermissionManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.Locale;

public final class ChatLockCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!PermissionManager.has(sender, "necromanager.chatlock")) {
            MessageManager.send(sender, "no-perm", "&cYou do not have permission.");
            return true;
        }
        if (args.length > 1) { MessageManager.sendRaw(sender, "&cUsage: /chatlock [on|off|status]"); return true; }
        if (args.length == 1 && args[0].equalsIgnoreCase("status")) {
            boolean locked = ConfigManager.getBoolean("chat.locked", false);
            MessageManager.sendRaw(sender, locked ? "&eGlobal chat is currently &clocked&e." : "&eGlobal chat is currently &aunlocked&e.");
            return true;
        }
        boolean locked;
        if (args.length == 0) locked = !ConfigManager.getBoolean("chat.locked", false);
        else if (args[0].equalsIgnoreCase("on")) locked = true;
        else if (args[0].equalsIgnoreCase("off")) locked = false;
        else { MessageManager.sendRaw(sender, "&cUsage: /chatlock [on|off|status]"); return true; }
        ConfigManager.set("chat.locked", locked);
        ConfigManager.save();
        MessageManager.send(sender, locked ? "chat-lock-enabled" : "chat-lock-disabled", locked ? "&aChat is now locked." : "&aChat is now unlocked.");
        return true;
    }
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (!PermissionManager.has(sender, "necromanager.chatlock") || args.length != 1) return List.of();
        String prefix = args[0].toLowerCase(Locale.ROOT);
        return List.of("on", "off", "status").stream().filter(s -> s.startsWith(prefix)).toList();
    }
}