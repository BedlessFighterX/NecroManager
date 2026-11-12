package com.bedlessfighter.necroManager.commands;

import com.bedlessfighter.necroManager.managers.ModerationManager;
import com.bedlessfighter.necroManager.managers.PermissionManager;
import com.bedlessfighter.necroManager.managers.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.*;

public final class ModerationCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String cmd = command.getName().toLowerCase(Locale.ROOT);
        String permission = switch (cmd) {
            case "warn", "warnings", "clearwarnings" -> "necromanager.moderation.warn";
            case "mute", "tempmute", "unmute" -> "necromanager.moderation.mute";
            case "kick" -> "necromanager.moderation.kick";
            default -> "necromanager.admin";
        };
        if (!PermissionManager.has(sender, permission)) {
            MessageManager.sendRaw(sender, "&cYou do not have permission.");
            return true;
        }
        if (cmd.equals("warn")) {
            if (args.length < 2) return usage(sender, "/warn <player> <reason>");
            Player target = Bukkit.getPlayerExact(args[0]);
            if (target == null) return fail(sender, "Player must be online.");
            String reason = join(args, 1);
            ModerationManager.warn(target, actor(sender), reason);
            target.sendMessage("§cYou have received a warning: §f" + reason);
            MessageManager.sendRaw(sender, "&aWarned &e" + target.getName() + "&a.");
            return true;
        }
        if (cmd.equals("warnings")) {
            if (args.length != 1) return usage(sender, "/warnings <player>");
            Player target = Bukkit.getPlayerExact(args[0]);
            UUID id = target == null ? findKnownPlayer(args[0]) : target.getUniqueId();
            if (id == null) return fail(sender, "No warning record found for that player.");
            List<Map<?, ?>> entries = ModerationManager.warnings(id);
            MessageManager.sendRaw(sender, "&eWarnings (" + entries.size() + "):");
            int start = Math.max(0, entries.size() - 10);
            for (int i = start; i < entries.size(); i++) {
                Map<?, ?> entry = entries.get(i);
                sender.sendMessage("§7" + (i + 1) + ". §f" + entry.getOrDefault("reason", "No reason") +
                        " §8— " + entry.getOrDefault("actor", "Console"));
            }
            return true;
        }
        if (cmd.equals("clearwarnings")) {
            if (args.length != 1) return usage(sender, "/clearwarnings <player>");
            Player target = Bukkit.getPlayerExact(args[0]);
            UUID id = target == null ? findKnownPlayer(args[0]) : target.getUniqueId();
            if (id == null) return fail(sender, "No warning record found for that player.");
            int count = ModerationManager.clearWarnings(id);
            MessageManager.sendRaw(sender, "&aCleared &e" + count + "&a warning(s).");
            return true;
        }
        if (cmd.equals("mute") || cmd.equals("tempmute")) {
            int reasonAt = cmd.equals("tempmute") ? 2 : 1;
            if (args.length <= reasonAt) return usage(sender, cmd.equals("mute") ?
                    "/mute <player> <reason>" : "/tempmute <player> <duration> <reason>");
            Player target = Bukkit.getPlayerExact(args[0]);
            if (target == null) return fail(sender, "Player must be online.");
            long duration = 0L;
            if (cmd.equals("tempmute")) {
                Long parsed = parseDuration(args[1]);
                if (parsed == null) return fail(sender, "Invalid duration. Use 10m, 2h, or 1d.");
                duration = parsed;
            }
            String reason = join(args, reasonAt);
            ModerationManager.mute(target.getUniqueId(), target.getName(), actor(sender), reason, duration);
            target.sendMessage("§cYou have been muted" + (duration == 0 ? " permanently" : " for " + args[1]) +
                    ". Reason: §f" + reason);
            MessageManager.sendRaw(sender, "&aMuted &e" + target.getName() + "&a.");
            return true;
        }
        if (cmd.equals("unmute")) {
            if (args.length != 1) return usage(sender, "/unmute <player>");
            Player target = Bukkit.getPlayerExact(args[0]);
            UUID id = target == null ? findKnownPlayer(args[0]) : target.getUniqueId();
            if (id == null || !ModerationManager.unmute(id)) return fail(sender, "That player is not muted.");
            if (target != null) target.sendMessage("§aYour mute has been removed.");
            MessageManager.sendRaw(sender, "&aMute removed.");
            return true;
        }
        if (cmd.equals("kick")) {
            if (args.length < 2) return usage(sender, "/kick <player> <reason>");
            Player target = Bukkit.getPlayerExact(args[0]);
            if (target == null) return fail(sender, "Player must be online.");
            String reason = join(args, 1);
            target.kickPlayer("§cYou have been kicked.\n§7Reason: §f" + reason);
            MessageManager.sendRaw(sender, "&aKicked &e" + target.getName() + "&a.");
            return true;
        }
        return true;
    }

    private static UUID findKnownPlayer(String name) {
        for (Player online : Bukkit.getOnlinePlayers())
            if (online.getName().equalsIgnoreCase(name)) return online.getUniqueId();
        return null;
    }
    private static String actor(CommandSender sender) { return sender instanceof Player p ? p.getName() : "Console"; }
    private static String join(String[] args, int start) { return String.join(" ", Arrays.copyOfRange(args, start, args.length)); }
    private static Long parseDuration(String value) {
        if (value == null || !value.matches("(?i)[1-9][0-9]*[smhd]")) return null;
        try {
            long amount = Long.parseLong(value.substring(0, value.length() - 1));
            long factor = switch (Character.toLowerCase(value.charAt(value.length() - 1))) {
                case 's' -> 1000L; case 'm' -> 60000L; case 'h' -> 3600000L; default -> 86400000L;
            };
            return Math.multiplyExact(amount, factor);
        } catch (ArithmeticException | NumberFormatException ex) { return null; }
    }
    private boolean usage(CommandSender sender, String text) { MessageManager.sendRaw(sender, "&eUsage: " + text); return true; }
    private boolean fail(CommandSender sender, String text) { MessageManager.sendRaw(sender, "&c" + text); return true; }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        String cmd = command.getName().toLowerCase(Locale.ROOT);
        if (args.length == 1 && Set.of("warn", "warnings", "clearwarnings", "mute", "tempmute", "unmute", "kick").contains(cmd)) {
            String prefix = args[0].toLowerCase(Locale.ROOT);
            return Bukkit.getOnlinePlayers().stream().map(Player::getName)
                    .filter(n -> n.toLowerCase(Locale.ROOT).startsWith(prefix))
                    .sorted(String.CASE_INSENSITIVE_ORDER).toList();
        }
        if (cmd.equals("tempmute") && args.length == 2) return List.of("10m", "30m", "1h", "6h", "1d", "7d");
        return List.of();
    }
}
