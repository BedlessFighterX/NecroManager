package com.bedlessfighter.spaceManager.commands;

import com.bedlessfighter.spaceManager.Main;
import com.bedlessfighter.spaceManager.managers.SpawnManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCommand implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        var cfg = Main.getPlugin().getConfig();
        Location location = cfg.getLocation("spawn");
        String prefix = ChatColor.translateAlternateColorCodes('&',cfg.getString("messages.prefix"));
        String message = ChatColor.translateAlternateColorCodes('&',cfg.getString("messages.spawnmessage"));

        if (sender instanceof Player player) {
            SpawnManager.sendToSpawn(player);
        }

        return true;
    }
}
