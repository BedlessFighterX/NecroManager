package com.bedlessfighter.necroManager.commands;

import com.bedlessfighter.necroManager.managers.ConfigManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public final class ReloadCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                            @NotNull String label, @NotNull String @NotNull [] args) {
        String prefix = color(ConfigManager.getString("messages.prefix", "&7[NecroManager]&r"));

        if (!sender.hasPermission("necromanager.admin") && !sender.isOp()) {
            sender.sendMessage(prefix + " " + color(config.getString("messages.no-perm",
                    "&cYou don't have enough permissions to issue this command!")));
            return true;
        }

        ConfigManager.reload();
        sender.sendMessage(color(config.getString("messages.prefix", "&7[NecroManager]&r")) + " "
                + color(config.getString("messages.reload", "&aConfiguration reloaded successfully.")));
        return true;
    }

    private String color(String message) {
        return ChatColor.translateAlternateColorCodes('&', message == null ? "" : message);
    }
}
