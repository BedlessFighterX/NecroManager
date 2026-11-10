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

public final class FeedCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length > 1) {
            MessageManager.sendRaw(sender, MessageManager.prefix() + " &cUsage: /" + label + " [player]");
            return true;
        }
        if (!PermissionManager.has(sender, "necromanager.feed")) {
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

        target.setFoodLevel(20);
        target.setSaturation(20.0F);
        target.setExhaustion(0.0F);
        if (target.equals(sender)) {
            MessageManager.send(target, "messages.feed-self", "&aYour hunger has been restored.");
        } else {
            MessageManager.send(target, "messages.fed-by-admin", "&aAn administrator restored your hunger.");
            MessageManager.send(sender, "messages.feed-other", "&aRestored hunger for %player%.",
                    MessageManager.playerPlaceholders(target));
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
                                      @NotNull String alias, @NotNull String @NotNull [] args) {
        if (args.length != 1 || !PermissionManager.has(sender, "necromanager.feed")) {
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
