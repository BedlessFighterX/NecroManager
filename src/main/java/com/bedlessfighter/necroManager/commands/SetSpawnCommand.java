package com.bedlessfighter.necroManager.commands;

import com.bedlessfighter.necroManager.managers.SpawnManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class SetSpawnCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can set the spawn location.");
            return true;
        }

        if (!player.hasPermission("necromanager.admin")) {
            player.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
            return true;
        }

        SpawnManager.setSpawn(player, player.getLocation());
        return true;
    }
}
