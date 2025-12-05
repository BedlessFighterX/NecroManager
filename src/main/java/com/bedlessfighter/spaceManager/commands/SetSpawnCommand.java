package com.bedlessfighter.spaceManager.commands;

import com.bedlessfighter.spaceManager.managers.SpawnManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetSpawnCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this shit command.");
            return true;
        }

        if (!player.hasPermission("spacemanager.setspawn")
                && !player.hasPermission("spacemanager.admin")
                && !player.isOp()) {
            player.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
            return true;
        }

        Location location = player.getLocation().toCenterLocation();
        SpawnManager.setSpawn(player, location);
        return true;
    }
}
