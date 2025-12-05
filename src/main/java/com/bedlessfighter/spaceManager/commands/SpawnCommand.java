package com.bedlessfighter.spaceManager.commands;

import com.bedlessfighter.spaceManager.managers.SpawnManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {


        if (sender instanceof Player player) {
            SpawnManager.sendToSpawn(player);
        }

        return true;
    }
}
