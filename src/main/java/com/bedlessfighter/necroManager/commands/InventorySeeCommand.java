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
import java.util.Map;

public final class InventorySeeCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length != 1) {
            MessageManager.sendRaw(sender, MessageManager.prefix() + " &cUsage: /" + label + " <player>");
            return true;
        }
        if (!PermissionManager.has(sender, "necromanager.invsee")) {
            MessageManager.send(sender, "messages.no-perm",
                    "&cYou don't have enough permissions to issue this command!");
            return true;
        }

        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null) {
            MessageManager.send(sender, "messages.player-not-found", "&cThat player is not online.");
            return true;
        }
        if (!(sender instanceof Player viewer)) {
            MessageManager.send(sender, "messages.invsee-player-only",
                    "&cOnly players can inspect an inventory.");
            return true;
        }
        if (viewer.equals(target)) {
            MessageManager.send(sender, "messages.invsee-self",
                    "&cYou cannot inspect your own inventory with this command.");
            return true;
        }

        viewer.openInventory(target.getInventory());
        MessageManager.send(viewer, "messages.invsee-opened",
                "&aOpened the inventory of %player%.", Map.of("player", target.getName()));
        MessageManager.send(target, "messages.invsee-target",
                "&eAn administrator is viewing your inventory.");
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
                                      @NotNull String alias, @NotNull String @NotNull [] args) {
        if (args.length != 1 || !PermissionManager.has(sender, "necromanager.invsee")) {
            return List.of();
        }
        String input = args[0].toLowerCase(java.util.Locale.ROOT);
        List<String> matches = new ArrayList<>();
        for (Player online : Bukkit.getOnlinePlayers()) {
            if (online.getName().toLowerCase(java.util.Locale.ROOT).startsWith(input)) {
                matches.add(online.getName());
            }
        }
        matches.sort(String.CASE_INSENSITIVE_ORDER);
        return matches;
    }
}
