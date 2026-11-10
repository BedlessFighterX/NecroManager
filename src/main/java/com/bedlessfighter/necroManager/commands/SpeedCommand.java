package com.bedlessfighter.necroManager.commands;

import com.bedlessfighter.necroManager.managers.MessageManager;
import com.bedlessfighter.necroManager.managers.PermissionManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/** Changes a player's walking or flying speed using safe Bukkit speed bounds. */
public final class SpeedCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                            @NotNull String @NotNull [] args) {
        if (args.length < 1 || args.length > 3) {
            MessageManager.sendRaw(sender, MessageManager.prefix() + " &cUsage: /speed <1-10> [walk|fly] [player]");
            return true;
        }
        if (!PermissionManager.has(sender, "necromanager.speed")) {
            MessageManager.send(sender, "messages.no-perm", "&cYou don't have enough permissions to issue this command!");
            return true;
        }

        int amount;
        try {
            amount = Integer.parseInt(args[0]);
        } catch (NumberFormatException exception) {
            MessageManager.sendRaw(sender, MessageManager.prefix() + " &cSpeed must be a number from 1 to 10.");
            return true;
        }
        if (amount < 1 || amount > 10) {
            MessageManager.sendRaw(sender, MessageManager.prefix() + " &cSpeed must be from 1 to 10.");
            return true;
        }

        String type = "walk";
        String targetName = null;
        if (args.length >= 2) {
            if (args[1].equalsIgnoreCase("walk") || args[1].equalsIgnoreCase("fly")) {
                type = args[1].toLowerCase(Locale.ROOT);
                if (args.length == 3) targetName = args[2];
            } else {
                targetName = args[1];
                if (args.length == 3) {
                    MessageManager.sendRaw(sender, MessageManager.prefix() + " &cUsage: /speed <1-10> [walk|fly] [player]");
                    return true;
                }
            }
        }

        Player target;
        if (targetName != null) {
            if (!PermissionManager.has(sender, "necromanager.speed.others")) {
                MessageManager.send(sender, "messages.no-perm", "&cYou don't have enough permissions to issue this command!");
                return true;
            }
            target = Bukkit.getPlayerExact(targetName);
            if (target == null) {
                MessageManager.send(sender, "messages.player-not-found", "&cThat player is not online.");
                return true;
            }
        } else if (sender instanceof Player player) {
            target = player;
        } else {
            MessageManager.send(sender, "messages.player-required", "&cConsole must specify an online player.");
            return true;
        }

        float value = amount / 10.0f;
        if (type.equals("fly")) target.setFlySpeed(value);
        else target.setWalkSpeed(value);
        MessageManager.send(sender, "messages.speed-other", "&aSet %player%'s %type% speed to %speed%.",
                java.util.Map.of("player", target.getName(), "type", type, "speed", Integer.toString(amount)));
        if (target != sender) {
            MessageManager.send(target, "messages.speed-target", "&eYour %type% speed was set to %speed%.",
                    java.util.Map.of("type", type, "speed", Integer.toString(amount)));
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
                                     @NotNull String alias, @NotNull String @NotNull [] args) {
        if (args.length == 1) return List.of("1","2","3","4","5","6","7","8","9","10");
        if (args.length == 2) {
            List<String> values = new ArrayList<>(List.of("walk", "fly"));
            if (PermissionManager.has(sender, "necromanager.speed.others")) {
                for (Player player : Bukkit.getOnlinePlayers()) values.add(player.getName());
            }
            String input = args[1].toLowerCase(Locale.ROOT);
            return values.stream().filter(v -> v.toLowerCase(Locale.ROOT).startsWith(input)).toList();
        }
        if (args.length == 3 && PermissionManager.has(sender, "necromanager.speed.others")) {
            String input = args[2].toLowerCase(Locale.ROOT);
            return Bukkit.getOnlinePlayers().stream().map(Player::getName)
                    .filter(n -> n.toLowerCase(Locale.ROOT).startsWith(input)).toList();
        }
        return List.of();
    }
}