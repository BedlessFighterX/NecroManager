package com.bedlessfighter.necroManager.commands;

import com.bedlessfighter.necroManager.managers.MessageManager;
import com.bedlessfighter.necroManager.managers.PermissionManager;
import com.bedlessfighter.necroManager.managers.SpawnManager;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

public final class SpawnCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length > 1) {
            MessageManager.sendRaw(sender, MessageManager.prefix()
                    + " &cUsage: /" + label + " [player]");
            return true;
        }

        if (args.length == 1) {
            if (!PermissionManager.isAdmin(sender)) {
                MessageManager.send(sender, "messages.no-perm",
                        "&cYou don't have enough permissions to issue this command!");
                return true;
            }

            Player target = Bukkit.getPlayerExact(args[0]);
            if (target == null) {
                MessageManager.send(sender, "messages.player-not-found",
                        "&cThat player is not online.");
                return true;
            }

            Location spawn = SpawnManager.getSpawn();
            if (spawn == null || spawn.getWorld() == null) {
                MessageManager.send(sender, "messages.spawn-not-set",
                        "&cSpawn has not been set yet.");
                return true;
            }
            if (!target.teleport(spawn)) {
                MessageManager.send(sender, "messages.spawn-failed",
                        "&cCould not complete the spawn operation.");
                return true;
            }

            MessageManager.send(target, "messages.spawnmessage",
                    "&rYou have been &ateleported&r to the &espawn!");
            MessageManager.send(sender, "messages.spawn-other-success",
                    "&aTeleported %player% to spawn.",
                    java.util.Map.of("%player%", target.getName()));
            return true;
        }

        if (!(sender instanceof Player player)) {
            MessageManager.sendRaw(sender, MessageManager.prefix()
                    + " &cOnly players can teleport themselves to spawn.");
            return true;
        }

        if (!PermissionManager.has(player, "necromanager.spawn")) {
            MessageManager.send(sender, "messages.no-perm",
                    "&cYou don't have enough permissions to issue this command!");
            return true;
        }

        SpawnManager.sendToSpawn(player);
        return true;
    }
}
