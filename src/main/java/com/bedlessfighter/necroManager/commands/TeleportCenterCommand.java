package com.bedlessfighter.necroManager.commands;

import com.bedlessfighter.necroManager.managers.ConfigManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TeleportCenterCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                           @NotNull String label, @NotNull String @NotNull [] args) {
        String prefix = color(ConfigManager.getString("messages.prefix", "&7[NecroManager]&r"));

        if (!(sender instanceof Player player)) {
            sender.sendMessage(prefix + " " + color("&cThis command can only be used by a player."));
            return true;
        }

        if (!player.hasPermission("necromanager.admin") && !player.isOp()) {
            player.sendMessage(prefix + " " + color(ConfigManager.getString("messages.no-perm",
                    "&cYou don't have enough permissions to issue this command!")));
            return true;
        }

        player.teleport(player.getLocation().toCenterLocation());
        player.sendMessage(prefix + " " + color(config.getString("messages.tpcenter",
                "&rYou have been centered &asuccessfully!")));
        return true;
    }

    private String color(String message) {
        return ChatColor.translateAlternateColorCodes('&', message == null ? "" : message);
    }
}
