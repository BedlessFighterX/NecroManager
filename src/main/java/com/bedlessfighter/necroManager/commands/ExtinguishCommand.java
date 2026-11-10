package com.bedlessfighter.necroManager.commands;

import com.bedlessfighter.necroManager.Main;
import com.bedlessfighter.necroManager.managers.MessageManager;
import com.bedlessfighter.necroManager.managers.PermissionManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/** Extinguishes the sender or an explicitly selected online player. */
public final class ExtinguishCommand implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!PermissionManager.has(sender, "necromanager.extinguish")) {
            MessageManager.send(sender, "messages.no-perm");
            return true;
        }

        Player target;
        boolean self;
        if (args.length == 0) {
            if (!(sender instanceof Player player)) {
                MessageManager.send(sender, "messages.player-required");
                return true;
            }
            target = player;
            self = true;
        } else if (args.length == 1) {
            target = Bukkit.getPlayerExact(args[0]);
            if (target == null) {
                MessageManager.send(sender, "messages.player-not-found");
                return true;
            }
            self = sender instanceof Player player && player.getUniqueId().equals(target.getUniqueId());
        } else {
            sender.sendMessage(MessageManager.color("&cUsage: /" + label + " [player]"));
            return true;
        }

        target.setFireTicks(0);
        if (self) {
            MessageManager.send(sender, "messages.extinguish-self");
        } else {
            MessageManager.send(target, "messages.extinguish-target");
            MessageManager.send(sender, "messages.extinguish-other",
                    java.util.Map.of("player", target.getName()));
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length != 1 || !PermissionManager.has(sender, "necromanager.extinguish")) {
            return List.of();
        }
        String prefix = args[0].toLowerCase(java.util.Locale.ROOT);
        List<String> names = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getName().toLowerCase(java.util.Locale.ROOT).startsWith(prefix)) {
                names.add(player.getName());
            }
        }
        return names;
    }
}