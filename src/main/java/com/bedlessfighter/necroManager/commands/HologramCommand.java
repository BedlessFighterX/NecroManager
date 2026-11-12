package com.bedlessfighter.necroManager.commands;

import com.bedlessfighter.necroManager.managers.HologramManager;
import com.bedlessfighter.necroManager.managers.MessageManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/** Administrative CRUD interface for persistent text holograms. */
public final class HologramCommand implements CommandExecutor, TabCompleter {
    private static final List<String> ACTIONS = List.of(
            "create", "delete", "list", "addline", "setline", "removeline");

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("necromanager.hologram")) {
            MessageManager.send(sender, "messages.no-permission", "&cYou do not have permission.");
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage(MessageManager.color("&e/hologram create <id>"));
            sender.sendMessage(MessageManager.color("&e/hologram delete <id>"));
            sender.sendMessage(MessageManager.color("&e/hologram list"));
            sender.sendMessage(MessageManager.color("&e/hologram addline <id> <text>"));
            sender.sendMessage(MessageManager.color("&e/hologram setline <id> <line> <text>"));
            sender.sendMessage(MessageManager.color("&e/hologram removeline <id> <line>"));
            return true;
        }

        String action = args[0].toLowerCase(Locale.ROOT);
        if (action.equals("list") && args.length == 1) {
            sender.sendMessage(MessageManager.color("&aHolograms: &f"
                    + (HologramManager.ids().isEmpty() ? "none" : String.join(", ", HologramManager.ids()))));
            return true;
        }
        if (action.equals("create") && args.length == 2) {
            if (!(sender instanceof Player player)) {
                sender.sendMessage(MessageManager.color("&cOnly players can create holograms."));
            } else if (HologramManager.create(args[1], player.getLocation())) {
                sender.sendMessage(MessageManager.color("&aCreated hologram &f" + args[1] + "&a."));
            } else sender.sendMessage(MessageManager.color("&cInvalid or existing hologram ID."));
            return true;
        }
        if (action.equals("delete") && args.length == 2) {
            sender.sendMessage(MessageManager.color(HologramManager.delete(args[1])
                    ? "&aDeleted hologram &f" + args[1] + "&a." : "&cHologram not found."));
            return true;
        }
        if (action.equals("addline") && args.length >= 3) {
            boolean changed = HologramManager.addLine(args[1], join(args, 2));
            sender.sendMessage(MessageManager.color(changed ? "&aHologram line added." : "&cCould not add line (check ID and 20-line limit)."));
            return true;
        }
        if ((action.equals("setline") || action.equals("removeline")) && args.length >= (action.equals("setline") ? 4 : 3)) {
            int line;
            try { line = Integer.parseInt(args[2]); }
            catch (NumberFormatException exception) {
                sender.sendMessage(MessageManager.color("&cLine must be a number."));
                return true;
            }
            boolean changed = action.equals("setline")
                    ? HologramManager.setLine(args[1], line, join(args, 3))
                    : HologramManager.removeLine(args[1], line);
            sender.sendMessage(MessageManager.color(changed ? "&aHologram updated." : "&cInvalid hologram or line number."));
            return true;
        }
        sender.sendMessage(MessageManager.color("&cInvalid syntax. Use /hologram for help."));
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
                                     @NotNull String alias, @NotNull String @NotNull [] args) {
        if (!sender.hasPermission("necromanager.hologram")) return List.of();
        String prefix = args[args.length - 1].toLowerCase(Locale.ROOT);
        if (args.length == 1) return filter(ACTIONS, prefix);
        if (args.length == 2 && List.of("delete", "addline", "setline", "removeline").contains(args[0].toLowerCase(Locale.ROOT))) {
            return filter(HologramManager.ids(), prefix);
        }
        if (args.length == 3 && List.of("setline", "removeline").contains(args[0].toLowerCase(Locale.ROOT))) {
            int count = HologramManager.lineCount(args[1]);
            return java.util.stream.IntStream.rangeClosed(1, count).mapToObj(Integer::toString)
                    .filter(value -> value.startsWith(prefix)).collect(Collectors.toList());
        }
        return List.of();
    }

    private static List<String> filter(Iterable<String> values, String prefix) {
        java.util.ArrayList<String> matches = new java.util.ArrayList<>();
        for (String value : values) if (value.toLowerCase(Locale.ROOT).startsWith(prefix)) matches.add(value);
        return matches;
    }

    private static String join(String[] args, int start) {
        return String.join(" ", java.util.Arrays.copyOfRange(args, start, args.length));
    }
}
