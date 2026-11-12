package com.bedlessfighter.necroManager.commands;

import com.bedlessfighter.necroManager.managers.ConfigManager;
import com.bedlessfighter.necroManager.managers.MessageManager;
import com.bedlessfighter.necroManager.managers.PermissionManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public final class ChatLockCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {
        if (!PermissionManager.has(sender, "necromanager.chatlock")) {
            MessageManager.send(sender, "no-perm", "&cYou do not have permission.");
            return true;
        }
        boolean locked = !ConfigManager.getBoolean("chat.locked", false);
        ConfigManager.set("chat.locked", locked);
        ConfigManager.save();
        MessageManager.send(sender, locked ? "chat-lock-enabled" : "chat-lock-disabled",
                locked ? "&aChat is now locked." : "&aChat is now unlocked.");
        return true;
    }
}
