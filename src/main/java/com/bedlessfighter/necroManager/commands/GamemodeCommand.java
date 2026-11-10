package com.bedlessfighter.necroManager.commands;

import com.bedlessfighter.necroManager.managers.MessageManager;
import com.bedlessfighter.necroManager.managers.PermissionManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/** Changes a player's game mode with explicit self/other permission handling. */
public final class GamemodeCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                            @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length < 1 || args.length > 2) {
            MessageManager.sendRaw(sender, MessageManager.prefix() + " &cUsage: /" + label
                    + " <survival|creative|adventure|spectator> [player]");
            return true;
        }
        if (!PermissionManager.has(sender, "necromanager.gamemode")) {
            MessageManager.send(sender, "messages.no-perm", "&cYou don't have enough permissions to issue this command!");
            return true;
        }

        GameMode mode = parseMode(args[0]);
        if (mode == null) {
            MessageManager.sendRaw(sender, MessageManager.prefix()
                    + " &cInvalid mode. Use survival, creative, adventure, or spectator.");
            return true;
        }

        Player target;
        if (args.length == 2) {
            if (!PermissionManager.has(sender, "necromanager.gamemode.others")) {
                MessageManager.send(sender, "messages.no-perm", "&cYou don't have enough permissions to issue this command!");
                return true;
            }
            target = Bukkit.getPlayerExact(args[1]);
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

        target.setGameMode(mode);
        String modeName = mode.name().toLowerCase(Locale.ROOT);
        if (target.equals(sender)) {
            MessageManager.send(sender, "messages.gamemode-self", "&aGamemode changed to %mode%.",
                    Map.of("mode", modeName));
        } else {
            MessageManager.send(target, "messages.gamemode-target", "&eAn administrator changed your gamemode to %mode%.",
                    Map.of("mode", modeName));
            MessageManager.send(sender, "messages.gamemode-other", "&aChanged %player%'s gamemode to %mode%.",
                    Map.of("player", target.getName(), "mode", modeName));
        }
        return true;
    }

    private GameMode parseMode(String input) {
        return switch (input.toLowerCase(Locale.ROOT)) {
            case "survival", "s", "0" -> GameMode.SURVIVAL;
            case "creative", "c", "1" -> GameMode.CREATIVE;
            case "adventure", "a", "2" -> GameMode.ADVENTURE;
            case "spectator", "sp", "3" -> GameMode.SPECTATOR;
            default -> null;
        };
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
                                     @NotNull String alias, @NotNull String @NotNull [] args) {
        if (!PermissionManager.has(sender, "necromanager.gamemode")) return List.of();
        if (args.length == 1) {
            String input = args[0].toLowerCase(Locale.ROOT);
            return List.of("survival", "creative", "adventure", "spectator").stream()
                    .filter(mode -> mode.startsWith(input)).toList();
        }
        if (args.length == 2 && PermissionManager.has(sender, "necromanager.gamemode.others")) {
            String input = args[1].toLowerCase(Locale.ROOT);
            List<String> names = new ArrayList<>();
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.getName().toLowerCase(Locale.ROOT).startsWith(input)) names.add(player.getName());
            }
            names.sort(String.CASE_INSENSITIVE_ORDER);
            return names;
        }
        return List.of();
    }
}
