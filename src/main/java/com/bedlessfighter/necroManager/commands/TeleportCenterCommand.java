package com.bedlessfighter.necroManager.commands;

import com.bedlessfighter.necroManager.managers.MessageManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class TeleportCenterCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            MessageManager.sendRaw(sender, MessageManager.prefix()
                    + " &cThis command can only be used by a player.");
            return true;
        }

        if (!player.hasPermission("necromanager.admin") && !player.isOp()) {
            MessageManager.send(sender, "messages.no-perm",
                    "&cYou don't have enough permissions to issue this command!");
            return true;
        }

        if (!player.teleport(player.getLocation().toCenterLocation())) {
            MessageManager.sendRaw(player, MessageManager.prefix()
                    + " &cTeleportation could not be completed.");
            return true;
        }
        MessageManager.send(player, "messages.tpcenter",
                "&rYou have been centered &asuccessfully!");
        return true;
    }
}
