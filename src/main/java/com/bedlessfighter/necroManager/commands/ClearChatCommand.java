package com.bedlessfighter.necroManager.commands;

import com.bedlessfighter.necroManager.managers.ConfigManager;
import com.bedlessfighter.necroManager.managers.MessageManager;
import com.bedlessfighter.necroManager.managers.PermissionManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public final class ClearChatCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length != 0) { MessageManager.sendRaw(sender, MessageManager.prefix() + " &cUsage: /clearchat"); return true; }
        if (!PermissionManager.has(sender, "necromanager.clearchat")) {
            MessageManager.send(sender, "messages.no-perm", "&cYou don't have enough permissions to issue this command!");
            return true;
        }
        int lines = ConfigManager.getInt("chat.clear-lines", 100, 20, 300);
        String notice = MessageManager.color(ConfigManager.getString("chat.cleared-message", "&cChat was cleared by staff."));
        for (var player : Bukkit.getOnlinePlayers()) {
            for (int i = 0; i < lines; i++) player.sendMessage("");
            player.sendMessage(notice);
        }
        return true;
    }
}
