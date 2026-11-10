package com.bedlessfighter.necroManager.commands;

import com.bedlessfighter.necroManager.managers.ConfigManager;
import com.bedlessfighter.necroManager.managers.SpawnManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class SetSpawnCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String prefix = color(ConfigManager.getString("messages.prefix", "&7[NecroManager]&r"));
        if (!(sender instanceof Player player)) {
            sender.sendMessage(prefix + " " + color("&cOnly players can set the spawn location."));
            return true;
        }
        if (!player.hasPermission("necromanager.admin") && !player.isOp()) {
            player.sendMessage(prefix + " " + color(ConfigManager.getString("messages.no-perm",
                    "&cYou don't have enough permissions to issue this command!")));
            return true;
        }
        SpawnManager.setSpawn(player, player.getLocation());
        return true;
    }
    private String color(String message) {
        return ChatColor.translateAlternateColorCodes('&', message == null ? "" : message);
    }
}
