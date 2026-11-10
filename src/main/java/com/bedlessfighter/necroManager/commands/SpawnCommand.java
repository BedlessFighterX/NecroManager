package com.bedlessfighter.necroManager.commands;

import com.bedlessfighter.necroManager.managers.MessageManager;
import com.bedlessfighter.necroManager.managers.PermissionManager;
import com.bedlessfighter.necroManager.managers.SpawnManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class SpawnCommand implements CommandExecutor, TabCompleter {

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

            Location configuredSpawn = SpawnManager.getSpawn();
            if (configuredSpawn == null || configuredSpawn.getWorld() == null) {
                MessageManager.send(sender, "messages.spawn-not-set",
                        "&cSpawn has not been set yet.");
                return true;
            }

            Location spawn = SpawnManager.getSafeSpawn();
            if (spawn == null || spawn.getWorld() == null) {
                MessageManager.send(sender, "messages.spawn-unsafe",
                        "&cNo safe location could be found near the configured spawn.");
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
                    Map.of("player", target.getName()));
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

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
                                      @NotNull String alias, @NotNull String @NotNull [] args) {
        if (args.length != 1 || !PermissionManager.isAdmin(sender)) {
            return List.of();
        }

        String input = args[0].toLowerCase(java.util.Locale.ROOT);
        List<String> matches = new ArrayList<>();
        for (Player online : Bukkit.getOnlinePlayers()) {
            String name = online.getName();
            if (name.toLowerCase(java.util.Locale.ROOT).startsWith(input)) {
                matches.add(name);
            }
        }
        matches.sort(String.CASE_INSENSITIVE_ORDER);
        return matches;
    }
}
