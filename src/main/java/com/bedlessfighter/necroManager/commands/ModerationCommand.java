package com.bedlessfighter.necroManager.commands;

import com.bedlessfighter.necroManager.managers.ModerationManager;
import com.bedlessfighter.necroManager.managers.PermissionManager;
import com.bedlessfighter.necroManager.managers.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.*;

public final class ModerationCommand implements CommandExecutor, TabCompleter {
    private static final Set<String> PLAYER_TARGET_COMMANDS = Set.of(
            "warn", "warnings", "clearwarnings", "mute", "tempmute", "unmute", "kick",
            "ban", "tempban", "unban", "baninfo", "modhistory");

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String cmd = command.getName().toLowerCase(Locale.ROOT);
        String permission = switch (cmd) {
            case "warn", "warnings", "clearwarnings" -> "necromanager.moderation.warn";
            case "mute", "tempmute", "unmute" -> "necromanager.moderation.mute";
            case "kick" -> "necromanager.moderation.kick";
            case "ban", "tempban", "unban", "baninfo", "modhistory" -> "necromanager.moderation.ban";
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
            UUID id = resolve(args[0]);
            if (id == null) return fail(sender, "No warning record found for that player.");
            List<Map<?, ?>> entries = ModerationManager.warnings(id);
            MessageManager.sendRaw(sender, "&eWarnings for &f" + ModerationManager.knownName(id) + " &e(" + entries.size() + "):");
            int start = Math.max(0, entries.size() - 10);
            for (int i = start; i < entries.size(); i++) {
                Map<?, ?> entry = entries.get(i);
                sender.sendMessage("§7" + (i + 1) + ". §f" + value(entry, "reason", "No reason") +
                        " §8— " + value(entry, "actor", "Console"));
            }
            return true;
        }
        if (cmd.equals("clearwarnings")) {
            if (args.length != 1) return usage(sender, "/clearwarnings <player>");
            UUID id = resolve(args[0]);
            if (id == null) return fail(sender, "No player record found.");
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
            UUID id = resolve(args[0]);
            if (id == null || !ModerationManager.unmute(id)) return fail(sender, "That player is not muted.");
            Player target = Bukkit.getPlayer(id);
            if (target != null) target.sendMessage("§aYour mute has been removed.");
            MessageManager.sendRaw(sender, "&aMute removed.");
            return true;
        }
        if (cmd.equals("kick")) {
            if (args.length < 2) return usage(sender, "/kick <player> <reason>");
            Player target = Bukkit.getPlayerExact(args[0]);
            if (target == null) return fail(sender, "Player must be online.");
            String reason = join(args, 1);
            ModerationManager.logAction(target.getUniqueId(), target.getName(), actor(sender), "KICK", reason);
            target.kickPlayer("§cYou have been kicked.\n§7Reason: §f" + reason);
            MessageManager.sendRaw(sender, "&aKicked &e" + target.getName() + "&a.");
            return true;
        }
        if (cmd.equals("ban") || cmd.equals("tempban")) {
            int reasonAt = cmd.equals("tempban") ? 2 : 1;
            if (args.length <= reasonAt) return usage(sender, cmd.equals("ban") ?
                    "/ban <player> <reason>" : "/tempban <player> <duration> <reason>");
            Player target = Bukkit.getPlayerExact(args[0]);
            if (target == null) return fail(sender, "Player must be online.");
            long duration = 0L;
            if (cmd.equals("tempban")) {
                Long parsed = parseDuration(args[1]);
                if (parsed == null) return fail(sender, "Invalid duration. Use 10m, 2h, or 1d.");
                duration = parsed;
            }
            String reason = join(args, reasonAt);
            ModerationManager.ban(target.getUniqueId(), target.getName(), actor(sender), reason, duration);
            target.kickPlayer("§cYou are banned from this server.\n§7Reason: §f" + reason +
                    (duration == 0L ? "\n§7Duration: Permanent" : "\n§7Duration: " + args[1]));
            MessageManager.sendRaw(sender, "&aBanned &e" + target.getName() + "&a.");
            return true;
        }
        if (cmd.equals("unban")) {
            if (args.length != 1) return usage(sender, "/unban <player>");
            UUID id = ModerationManager.findKnownPlayer(args[0]);
            if (id == null || !ModerationManager.unban(id, actor(sender))) return fail(sender, "That player is not currently banned.");
            MessageManager.sendRaw(sender, "&aBan removed for &e" + ModerationManager.knownName(id) + "&a.");
            return true;
        }
        if (cmd.equals("baninfo")) {
            if (args.length != 1) return usage(sender, "/baninfo <player>");
            UUID id = resolve(args[0]);
            if (id == null || !ModerationManager.isBanned(id)) return fail(sender, "That player is not currently banned.");
            long until = ModerationManager.banUntil(id);
            MessageManager.sendRaw(sender, "&eBan information for &f" + ModerationManager.knownName(id));
            sender.sendMessage("§7Reason: §f" + ModerationManager.banReason(id));
            sender.sendMessage("§7By: §f" + ModerationManager.banActor(id));
            sender.sendMessage("§7Expiry: §f" + (until == 0L ? "Permanent" : new Date(until)));
            return true;
        }
        if (cmd.equals("modhistory")) {
            if (args.length != 1) return usage(sender, "/modhistory <player>");
            UUID id = resolve(args[0]);
            if (id == null) return fail(sender, "No moderation history found for that player.");
            List<Map<?, ?>> entries = ModerationManager.history(id);
            MessageManager.sendRaw(sender, "&eModeration history for &f" + ModerationManager.knownName(id) +
                    " &e(" + entries.size() + "):");
            int start = Math.max(0, entries.size() - 15);
            for (int i = start; i < entries.size(); i++) {
                Map<?, ?> entry = entries.get(i);
                long time = entry.get("time") instanceof Number n ? n.longValue() : 0L;
                sender.sendMessage("§7" + new Date(time) + " §c" + value(entry, "action", "ACTION") +
                        " §f" + value(entry, "reason", "") + " §8by " + value(entry, "actor", "Console"));
            }
            return true;
        }
        return true;
    }

    private static UUID resolve(String name) {
        Player online = Bukkit.getPlayerExact(name);
        if (online != null) return online.getUniqueId();
        return ModerationManager.findKnownPlayer(name);
    }
    private static String value(Map<?, ?> map, String key, String fallback) {
        Object value = map.get(key);
        return value == null ? fallback : String.valueOf(value);
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
            long duration = Math.multiplyExact(amount, factor);
            Math.addExact(System.currentTimeMillis(), duration);
            return duration;
        } catch (ArithmeticException | NumberFormatException ex) { return null; }
    }
    private boolean usage(CommandSender sender, String text) { MessageManager.sendRaw(sender, "&eUsage: " + text); return true; }
    private boolean fail(CommandSender sender, String text) { MessageManager.sendRaw(sender, "&c" + text); return true; }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        String cmd = command.getName().toLowerCase(Locale.ROOT);
        if (args.length == 1 && PLAYER_TARGET_COMMANDS.contains(cmd)) {
            String prefix = args[0].toLowerCase(Locale.ROOT);
            Set<String> names = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
            Bukkit.getOnlinePlayers().stream().map(Player::getName).forEach(names::add);
            if (Set.of("warnings", "clearwarnings", "unmute", "unban", "baninfo", "modhistory").contains(cmd))
                names.addAll(ModerationManager.knownNames());
            return names.stream().filter(n -> n.toLowerCase(Locale.ROOT).startsWith(prefix)).toList();
        }
        if ((cmd.equals("tempmute") || cmd.equals("tempban")) && args.length == 2)
            return List.of("10m", "30m", "1h", "6h", "1d", "7d");
        return List.of();
    }
}
