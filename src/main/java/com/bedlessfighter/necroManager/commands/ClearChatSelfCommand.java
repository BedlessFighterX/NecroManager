package com.bedlessfighter.necroManager.commands;

import com.bedlessfighter.necroManager.managers.ConfigManager;
import com.bedlessfighter.necroManager.managers.MessageManager;
import com.bedlessfighter.necroManager.managers.PermissionManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class ClearChatSelfCommand implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) { MessageManager.sendRaw(sender, "&cPlayers only."); return true; }
        if (!PermissionManager.has(sender, "necromanager.clearmychat")) {
            MessageManager.send(sender, "messages.no-permission", "&cYou do not have permission."); return true;
        }
        int lines = ConfigManager.getInt("chat.clear-lines", 100, 20, 300);
        for (int i = 0; i < lines; i++) player.sendMessage("");
        MessageManager.sendRaw(sender, ConfigManager.getString("chat.self-cleared-message", "&aYour chat has been cleared."));
        return true;
    }
}
