package com.bedlessfighter.necroManager.commands;

import com.bedlessfighter.necroManager.managers.MessageManager;
import com.bedlessfighter.necroManager.managers.SpawnManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class SpawnCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            MessageManager.sendRaw(sender, MessageManager.prefix()
                    + " &cOnly players can teleport to spawn.");
            return true;
        }

        if (!player.hasPermission("necromanager.spawn")
                && !player.hasPermission("necromanager.admin") && !player.isOp()) {
            MessageManager.send(sender, "messages.no-perm",
                    "&cYou don't have enough permissions to issue this command!");
            return true;
        }

        SpawnManager.sendToSpawn(player);
        return true;
    }
}
