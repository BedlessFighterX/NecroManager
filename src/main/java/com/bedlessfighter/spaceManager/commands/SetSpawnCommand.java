package com.bedlessfighter.spaceManager.commands;

import com.bedlessfighter.spaceManager.Main;
import com.bedlessfighter.spaceManager.managers.SpawnManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetSpawnCommand implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        var cfg = Main.getPlugin().getConfig();

        if (sender instanceof Player player) {
            Location location = player.getLocation().toCenterLocation();

            SpawnManager.setSpawn(player, location);
        }

        return true;
    }
}
