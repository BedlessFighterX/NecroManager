package com.bedlessfighter.necroManager.commands;

import com.bedlessfighter.necroManager.managers.MessageManager;
import com.bedlessfighter.necroManager.managers.VanishManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;

public final class VanishCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("list")) {
            if (!sender.hasPermission("necromanager.vanish.list")) {
                MessageManager.sendRaw(sender, "&cYou do not have permission.");
                return true;
            }
            List<String> names = Bukkit.getOnlinePlayers().stream()
                    .filter(VanishManager::isVanished).map(Player::getName).sorted().toList();
            MessageManager.sendRaw(sender, names.isEmpty()
                    ? "&7No players are currently vanished."
                    : "&aVanished players: &f" + String.join("&7, &f", names));
            return true;
        }

        Player target;
        if (args.length == 0) {
            if (!(sender instanceof Player player)) {
                MessageManager.sendRaw(sender, "&cConsole must specify an online player.");
                return true;
            }
            target = player;
        } else {
            if (!sender.hasPermission("necromanager.vanish.others")) {
                MessageManager.sendRaw(sender, "&cYou do not have permission to vanish other players.");
                return true;
            }
            target = Bukkit.getPlayerExact(args[0]);
            if (target == null) {
                MessageManager.sendRaw(sender, "&cThat player is not online.");
                return true;
            }
        }

        boolean vanished = VanishManager.toggle(target);
        MessageManager.sendRaw(target, vanished ? "&7You are now vanished." : "&aYou are visible again.");
        if (!target.equals(sender)) {
            MessageManager.sendRaw(sender, vanished
                    ? "&aVanished &f" + target.getName() + "&a."
                    : "&aMade &f" + target.getName() + "&a visible.");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
                                      @NotNull String alias, @NotNull String[] args) {
        if (args.length != 1) return List.of();
        String prefix = args[0].toLowerCase(Locale.ROOT);
        if (sender.hasPermission("necromanager.vanish.list") && "list".startsWith(prefix)) {
            return List.of("list");
        }
        if (!sender.hasPermission("necromanager.vanish.others")) return List.of();
        return Bukkit.getOnlinePlayers().stream().map(Player::getName)
                .filter(name -> name.toLowerCase(Locale.ROOT).startsWith(prefix))
                .sorted(String.CASE_INSENSITIVE_ORDER).toList();
    }
}
