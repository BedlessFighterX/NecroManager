package com.bedlessfighter.necroManager.commands;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Permission-aware fallback completions shared by commands that do not need
 * a bespoke completion implementation.
 */
public final class CommandTabCompleter implements TabCompleter {
    private static final List<String> GAME_MODES =
            List.of("survival", "creative", "adventure", "spectator");
    private static final List<String> WEATHER = List.of("clear", "rain", "thunder");
    private static final List<String> TIME = List.of("day", "night", "reset");
    private static final Set<String> PLAYER_FIRST = Set.of(
            "clearinventory", "cleararmor", "clearhotbar", "skull", "clearoffhand",
            "teleportto", "tphere", "teleportall", "worldspawn", "kill", "heal",
            "feed", "fly", "extinguish", "ping", "getpos", "cleareffects",
            "enderchest", "invsee", "spawn", "whois", "ignore", "ptime",
            "whois", "ignore", "msg"
    );
    private static final Set<String> PLAYER_OPTIONAL = Set.of(
            "setlevel", "clearxp", "setfood", "gamemode", "speed"
    );

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
                                     @NotNull String alias, @NotNull String @NotNull [] args) {
        if (command.getPermission() != null && !sender.hasPermission(command.getPermission())) {
            return List.of();
        }
        String name = command.getName().toLowerCase(Locale.ROOT);
        int position = args.length - 1;
        String input = args[position].toLowerCase(Locale.ROOT);

        if (PLAYER_FIRST.contains(name) && position == 0) {
            return players(input);
        }
        if (PLAYER_OPTIONAL.contains(name)) {
            if ((name.equals("gamemode") && position == 0)
                    || (name.equals("speed") && position == 1)) {
                if (name.equals("gamemode")) return filter(GAME_MODES, input);
                return filter(List.of("walk", "fly"), input);
            }
            if (name.equals("clearxp") && position == 0) return players(input);
            if ((name.equals("setlevel") || name.equals("setfood")
                    || name.equals("experience") || name.equals("sethealth")) && position == 1) {
                return players(input);
            }
            if (name.equals("gamemode") && position == 1
                    && sender.hasPermission("necromanager.gamemode.others")) return players(input);
            if (name.equals("speed") && position == 2
                    && sender.hasPermission("necromanager.speed.others")) return players(input);
        }

        return switch (name) {
            case "gamemodeall" -> position == 0 ? filter(GAME_MODES, input) : List.of();
            case "weather", "weatherall" -> position == 0 ? filter(WEATHER, input) : List.of();
            case "timeall" -> position == 0 ? filter(TIME, input) : List.of();
            case "repair" -> position == 0 ? filter(List.of("hand", "all"), input) : List.of();
            case "lore" -> position == 0 ? filter(List.of("add", "clear"), input) : List.of();
            case "ptime" -> position == 0 ? filter(TIME, input) : List.of();
            case "experienceall", "setlevelall", "setfoodall", "experience",
                 "sethealth", "itemdurability" -> position == 0 ? filter(
                    List.of("1", "5", "10", "20", "50", "100"), input) : List.of();
            default -> List.of();
        };
    }

    private static List<String> players(String input) {
        List<String> names = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getName().toLowerCase(Locale.ROOT).startsWith(input)) {
                names.add(player.getName());
            }
        }
        names.sort(String.CASE_INSENSITIVE_ORDER);
        return names;
    }

    private static List<String> worlds(String input) {
        List<String> names = new ArrayList<>();
        for (World world : Bukkit.getWorlds()) {
            if (world.getName().toLowerCase(Locale.ROOT).startsWith(input)) names.add(world.getName());
        }
        names.sort(String.CASE_INSENSITIVE_ORDER);
        return names;
    }

    private static List<String> filter(Collection<String> values, String input) {
        return values.stream().filter(value -> value.toLowerCase(Locale.ROOT).startsWith(input)).toList();
    }
}
