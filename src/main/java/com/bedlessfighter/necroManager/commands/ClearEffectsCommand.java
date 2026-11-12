package com.bedlessfighter.necroManager.commands;

import com.bedlessfighter.necroManager.managers.MessageManager;
import com.bedlessfighter.necroManager.managers.PermissionManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class ClearEffectsCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length > 1) {
            MessageManager.sendRaw(sender, MessageManager.prefix() + " &cUsage: /" + label + " [player]");
            return true;
        }
        if (!PermissionManager.has(sender, "necromanager.cleareffects")) {
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

        int removed = 0;
        for (PotionEffect effect : target.getActivePotionEffects()) {
            target.removePotionEffect(effect.getType());
            removed++;
        }

        if (target.equals(sender)) {
            MessageManager.send(target, "messages.cleareffects-self",
                    "&aRemoved %count% active potion effects.",
                    java.util.Map.of("count", Integer.toString(removed)));
        } else {
            MessageManager.send(target, "messages.cleareffects-target",
                    "&eAn administrator cleared your active potion effects.");
            MessageManager.send(sender, "messages.cleareffects-other",
                    "&aRemoved %count% potion effects from %player%.",
                    java.util.Map.of("count", Integer.toString(removed), "player", target.getName()));
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
                                      @NotNull String alias, @NotNull String @NotNull [] args) {
        if (args.length != 1 || !PermissionManager.has(sender, "necromanager.cleareffects")) {
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
