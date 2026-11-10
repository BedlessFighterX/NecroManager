package com.bedlessfighter.necroManager.commands;

import com.bedlessfighter.necroManager.managers.PermissionManager;
import com.bedlessfighter.necroManager.managers.MessageManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class SetFoodAllCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!PermissionManager.has(sender, "necromanager.setfoodall")) {
            MessageManager.sendRaw(sender, "&cYou do not have permission.");
            return true;
        }
        if (args.length != 1) {
            sender.sendMessage("§cUsage: /setfoodall <0-20>");
            return true;
        }
        int food;
        try {
            food = Integer.parseInt(args[0]);
        } catch (NumberFormatException exception) {
            sender.sendMessage("§cFood level must be a number from 0 to 20.");
            return true;
        }
        if (food < 0 || food > 20) {
            sender.sendMessage("§cFood level must be from 0 to 20.");
            return true;
        }
        for (Player player : sender.getServer().getOnlinePlayers()) {
            player.setFoodLevel(food);
            if (food == 20) player.setSaturation(20.0f);
        }
        MessageManager.sendRaw(sender, "&aSet food level to &f" + food + " &afor all online players.");
        return true;
    }
}