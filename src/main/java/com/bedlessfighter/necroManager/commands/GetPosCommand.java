package com.bedlessfighter.necroManager.commands;

import com.bedlessfighter.necroManager.managers.MessageManager;
import com.bedlessfighter.necroManager.managers.PermissionManager;
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
import java.util.Locale;

/** Reports a player's current world, position, and rotation. */
public final class GetPosCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                            @NotNull String @NotNull [] args) {
        if (args.length > 1) {
            MessageManager.sendRaw(sender, MessageManager.prefix() + " &cUsage: /" + command.getName() + " [player]");
            return true;
        }
        if (!PermissionManager.has(sender, "necromanager.getpos")) {
            MessageManager.send(sender, "messages.no-perm",
                    "&cYou don't have enough permissions to issue this command!");
            return true;
        }

        Player target;
        if (args.length == 1) {
            target = Bukkit.getPlayerExact(args[0]);
            if (target == null) {
                MessageManager.send(sender, "messages.player-not-found", "&cThat player is not online.");
                return true;
            }
        } else if (sender instanceof Player player) {
            target = player;
        } else {
            MessageManager.send(sender, "messages.player-required",
                    "&cConsole must specify an online player.");
            return true;
        }

        Location location = target.getLocation();
        MessageManager.send(sender, "messages.getpos",
                "&a%player% &7is in &e%world%&7 at &e%x%, %y%, %z%&7 (yaw &e%yaw%&7, pitch &e%pitch%&7).",
                java.util.Map.of(
                        "player", target.getName(),
                        "world", location.getWorld().getName(),
                        "x", String.format(Locale.ROOT, "%.2f", location.getX()),
                        "y", String.format(Locale.ROOT, "%.2f", location.getY()),
                        "z", String.format(Locale.ROOT, "%.2f", location.getZ()),
                        "yaw", String.format(Locale.ROOT, "%.1f", location.getYaw()),
                        "pitch", String.format(Locale.ROOT, "%.1f", location.getPitch())));
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
                                     @NotNull String alias, @NotNull String @NotNull [] args) {
        if (args.length != 1 || !PermissionManager.has(sender, "necromanager.getpos")) {
            return List.of();
        }
        String input = args[0].toLowerCase(Locale.ROOT);
        List<String> matches = new ArrayList<>();
        for (Player online : Bukkit.getOnlinePlayers()) {
            if (online.getName().toLowerCase(Locale.ROOT).startsWith(input)) {
                matches.add(online.getName());
            }
        }
        matches.sort(String.CASE_INSENSITIVE_ORDER);
        return matches;
    }
}