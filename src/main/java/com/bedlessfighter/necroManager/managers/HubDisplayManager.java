package com.bedlessfighter.necroManager.managers;

import com.bedlessfighter.necroManager.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/** Maintains configurable sidebar and tab-list presentation for online players. */
public final class HubDisplayManager {
    private static final String OBJECTIVE = "nmhub";
    private static final Map<UUID, Scoreboard> BOARDS = new ConcurrentHashMap<>();

    private HubDisplayManager() { throw new UnsupportedOperationException("Utility class"); }

    public static void start() {
        if (!ConfigManager.getBoolean("hub.scoreboard.enabled", true)
                && !ConfigManager.getBoolean("hub.tablist.enabled", true)) return;
        Bukkit.getScheduler().runTaskTimer(Main.getPlugin(), () -> {
            for (Player player : Bukkit.getOnlinePlayers()) update(player);
        }, 1L, 40L);
    }

    public static void update(Player player) {
        if (player == null || !player.isOnline()) return;
        updateScoreboard(player);
        updateTabList(player);
    }

    private static void updateScoreboard(Player player) {
        if (!ConfigManager.getBoolean("hub.scoreboard.enabled", true)) return;
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        if (manager == null) return;
        Scoreboard board = BOARDS.computeIfAbsent(player.getUniqueId(), ignored -> manager.getNewScoreboard());
        Objective objective = board.getObjective(OBJECTIVE);
        if (objective == null) objective = board.registerNewObjective(OBJECTIVE, "dummy",
                color(apply(ConfigManager.getString("hub.scoreboard.title", "&a&lNecroManager"), player)));
        else objective.setDisplayName(color(apply(ConfigManager.getString("hub.scoreboard.title", "&a&lNecroManager"), player)));
        for (String entry : new ArrayList<>(board.getEntries())) board.resetScores(entry);
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        List<String> lines = ConfigManager.getStringList("hub.scoreboard.lines");
        int score = Math.min(lines.size(), 15);
        for (String line : lines) {
            if (score <= 0) break;
            String rendered = color(apply(line, player));
            // Scoreboard entries must be unique even when configured lines are identical.
            String entry = rendered;
            while (board.getEntries().contains(entry)) entry += ChatColor.RESET;
            objective.getScore(entry).setScore(score--);
        }
        player.setScoreboard(board);
    }

    public static void remove(Player player) {
        if (player != null) BOARDS.remove(player.getUniqueId());
    }

    private static void updateTabList(Player player) {
        if (!ConfigManager.getBoolean("hub.tablist.enabled", true)) return;
        String header = applyLines(ConfigManager.getStringList("hub.tablist.header"), player);
        String footer = applyLines(ConfigManager.getStringList("hub.tablist.footer"), player);
        player.setPlayerListHeaderFooter(header, footer);
    }

    private static String applyLines(List<String> lines, Player player) {
        List<String> rendered = new ArrayList<>();
        for (String line : lines) rendered.add(color(apply(line, player)));
        return String.join("\n", rendered);
    }

    private static String apply(String input, Player player) {
        return input.replace("%player%", player.getName())
                .replace("%displayname%", player.getDisplayName())
                .replace("%world%", player.getWorld().getName())
                .replace("%online%", Integer.toString(Bukkit.getOnlinePlayers().size()))
                .replace("%max%", Integer.toString(Bukkit.getMaxPlayers()));
    }

    private static String color(String input) {
        return ChatColor.translateAlternateColorCodes('&', input == null ? "" : input);
    }
}
