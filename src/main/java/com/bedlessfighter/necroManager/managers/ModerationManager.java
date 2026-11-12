package com.bedlessfighter.necroManager.managers;

import com.bedlessfighter.necroManager.Main;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

import java.io.File;
import java.io.IOException;
import java.util.*;

public final class ModerationManager implements Listener {
    private static File file;
    private static YamlConfiguration data;

    public ModerationManager() { }

    public static synchronized void load() {
        file = new File(Main.getPlugin().getDataFolder(), "moderation.yml");
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
        data = YamlConfiguration.loadConfiguration(file);
        boolean changed = expireSection("mutes") | expireSection("bans");
        if (changed) save();
    }

    private static boolean expireSection(String root) {
        boolean changed = false;
        ConfigurationSection section = data.getConfigurationSection(root);
        if (section == null) return false;
        for (String id : section.getKeys(false)) {
            long until = data.getLong(root + "." + id + ".until", 0L);
            if (until > 0 && until <= System.currentTimeMillis()) {
                data.set(root + "." + id, null);
                changed = true;
            }
        }
        return changed;
    }

    private static void ready() { if (data == null) load(); }
    private static void save() {
        try { data.save(file); }
        catch (IOException ex) { throw new IllegalStateException("Could not save moderation.yml", ex); }
    }

    private static void remember(UUID id, String name) {
        if (name != null && !name.isBlank()) data.set("players." + id, name);
    }

    private static void record(UUID id, String name, String actor, String action, String reason, long until) {
        String path = "history." + id;
        List<Map<String, Object>> entries = new ArrayList<>(data.getMapList(path));
        Map<String, Object> entry = new LinkedHashMap<>();
        entry.put("actor", actor);
        entry.put("action", action);
        entry.put("reason", reason == null ? "" : reason);
        entry.put("time", System.currentTimeMillis());
        entry.put("until", until);
        entries.add(entry);
        if (entries.size() > 200) entries.remove(0);
        data.set(path, entries);
        remember(id, name);
    }

    public static synchronized void warn(Player target, String actor, String reason) {
        ready();
        String path = "warnings." + target.getUniqueId();
        List<Map<String, Object>> entries = new ArrayList<>(data.getMapList(path));
        Map<String, Object> entry = new LinkedHashMap<>();
        entry.put("actor", actor);
        entry.put("reason", reason);
        entry.put("time", System.currentTimeMillis());
        entries.add(entry);
        if (entries.size() > 100) entries.remove(0);
        data.set(path, entries);
        remember(target.getUniqueId(), target.getName());
        record(target.getUniqueId(), target.getName(), actor, "WARN", reason, 0L);
        save();
    }

    public static synchronized List<Map<?, ?>> warnings(UUID id) {
        ready();
        return new ArrayList<>(data.getMapList("warnings." + id));
    }

    public static synchronized int clearWarnings(UUID id) {
        ready();
        int count = data.getMapList("warnings." + id).size();
        data.set("warnings." + id, null);
        record(id, data.getString("players." + id, id.toString()), "Console", "CLEAR_WARNINGS",
                "Cleared " + count + " warning(s)", 0L);
        save();
        return count;
    }

    public static synchronized boolean mute(UUID id, String name, String actor, String reason, long durationMillis) {
        ready();
        long until = durationMillis <= 0 ? 0L : System.currentTimeMillis() + durationMillis;
        data.set("mutes." + id + ".name", name);
        data.set("mutes." + id + ".actor", actor);
        data.set("mutes." + id + ".reason", reason);
        data.set("mutes." + id + ".until", until);
        record(id, name, actor, until == 0L ? "MUTE" : "TEMPMUTE", reason, until);
        save();
        return true;
    }

    public static synchronized boolean unmute(UUID id) {
        ready();
        if (!isMuted(id)) return false;
        String name = data.getString("mutes." + id + ".name", data.getString("players." + id, id.toString()));
        data.set("mutes." + id, null);
        record(id, name, "Console", "UNMUTE", "Mute removed", 0L);
        save();
        return true;
    }

    public static synchronized boolean isMuted(UUID id) {
        ready();
        if (!data.contains("mutes." + id)) return false;
        long until = data.getLong("mutes." + id + ".until", 0L);
        if (until > 0 && until <= System.currentTimeMillis()) {
            data.set("mutes." + id, null);
            save();
            return false;
        }
        return true;
    }

    public static synchronized String muteReason(UUID id) {
        ready();
        return data.getString("mutes." + id + ".reason", "No reason specified");
    }

    public static synchronized long muteUntil(UUID id) {
        ready();
        return data.getLong("mutes." + id + ".until", 0L);
    }

    public static synchronized String muteActor(UUID id) {
        ready();
        return data.getString("mutes." + id + ".actor", "Console");
    }

    public static synchronized boolean ban(UUID id, String name, String actor, String reason, long durationMillis) {
        ready();
        long until = durationMillis <= 0 ? 0L : System.currentTimeMillis() + durationMillis;
        data.set("bans." + id + ".name", name);
        data.set("bans." + id + ".actor", actor);
        data.set("bans." + id + ".reason", reason);
        data.set("bans." + id + ".until", until);
        record(id, name, actor, until == 0L ? "BAN" : "TEMPBAN", reason, until);
        save();
        return true;
    }

    public static synchronized boolean unban(UUID id, String actor) {
        ready();
        if (!isBanned(id)) return false;
        String name = data.getString("bans." + id + ".name", data.getString("players." + id, id.toString()));
        data.set("bans." + id, null);
        record(id, name, actor, "UNBAN", "Ban removed", 0L);
        save();
        return true;
    }

    public static synchronized boolean isBanned(UUID id) {
        ready();
        if (!data.contains("bans." + id)) return false;
        long until = data.getLong("bans." + id + ".until", 0L);
        if (until > 0 && until <= System.currentTimeMillis()) {
            data.set("bans." + id, null);
            save();
            return false;
        }
        return true;
    }

    public static synchronized String banReason(UUID id) {
        ready();
        return data.getString("bans." + id + ".reason", "No reason specified");
    }

    public static synchronized String banActor(UUID id) {
        ready();
        return data.getString("bans." + id + ".actor", "Console");
    }

    public static synchronized long banUntil(UUID id) {
        ready();
        return data.getLong("bans." + id + ".until", 0L);
    }

    public static synchronized String knownName(UUID id) {
        ready();
        return data.getString("players." + id, id.toString());
    }

    public static synchronized UUID findKnownPlayer(String name) {
        ready();
        for (String raw : Objects.requireNonNullElse(data.getConfigurationSection("players"),
                data.createSection("players")).getKeys(false)) {
            if (data.getString("players." + raw, "").equalsIgnoreCase(name)) {
                try { return UUID.fromString(raw); } catch (IllegalArgumentException ignored) { }
            }
        }
        return null;
    }

    public static synchronized List<String> knownNames() {
        ready();
        ConfigurationSection section = data.getConfigurationSection("players");
        if (section == null) return List.of();
        return section.getKeys(false).stream().map(id -> data.getString("players." + id, ""))
                .filter(s -> !s.isBlank()).sorted(String.CASE_INSENSITIVE_ORDER).toList();
    }

    public static synchronized List<Map<?, ?>> history(UUID id) {
        ready();
        return new ArrayList<>(data.getMapList("history." + id));
    }

    public static synchronized void logAction(UUID id, String name, String actor, String action, String reason) {
        ready();
        record(id, name, actor, action, reason, 0L);
        save();
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        UUID id = event.getPlayer().getUniqueId();
        if (!isMuted(id)) return;
        event.setCancelled(true);
        String reason = muteReason(id);
        long until = muteUntil(id);
        String duration = until == 0L ? "permanently" : "for " +
                Math.max(1L, (until - System.currentTimeMillis()) / 1000L) + "s";
        event.getPlayer().sendMessage("§cYou are muted " + duration + ". Reason: §f" + reason);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!isMuted(player.getUniqueId())) return;
        long until = muteUntil(player.getUniqueId());
        String duration = until == 0L ? "permanently" : "until " + new Date(until);
        player.sendMessage("§cYou are currently muted " + duration + ". Reason: §f" + muteReason(player.getUniqueId()));
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        UUID id = event.getPlayer().getUniqueId();
        if (!isBanned(id)) return;
        long until = banUntil(id);
        String expiry = until == 0L ? "Permanent" : "Expires: " + new Date(until);
        event.disallow(PlayerLoginEvent.Result.KICK_BANNED,
                "§cYou are banned from this server.\n§7Reason: §f" + banReason(id) +
                        "\n§7By: §f" + banActor(id) + "\n§7" + expiry);
    }
}
