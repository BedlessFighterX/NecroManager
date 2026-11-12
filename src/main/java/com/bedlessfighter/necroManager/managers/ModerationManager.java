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

import java.io.File;
import java.io.IOException;
import java.util.*;

public final class ModerationManager implements Listener {
    private static File file;
    private static YamlConfiguration data;

    public static synchronized void load() {
        file = new File(Main.getPlugin().getDataFolder(), "moderation.yml");
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
        data = YamlConfiguration.loadConfiguration(file);
        boolean changed = false;
        ConfigurationSection section = data.getConfigurationSection("mutes");
        if (section != null) for (String id : section.getKeys(false)) {
            long until = data.getLong("mutes." + id + ".until", 0L);
            if (until > 0 && until <= System.currentTimeMillis()) {
                data.set("mutes." + id, null);
                changed = true;
            }
        }
        if (changed) save();
    }

    private static void ready() { if (data == null) load(); }
    private static void save() {
        try { data.save(file); }
        catch (IOException ex) { throw new IllegalStateException("Could not save moderation.yml", ex); }
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
        data.set("players." + target.getUniqueId(), target.getName());
        save();
    }

    public static synchronized UUID findPlayerId(String name) {
        ready();
        ConfigurationSection section = data.getConfigurationSection("players");
        if (section == null) return null;
        for (String key : section.getKeys(false)) {
            if (data.getString("players." + key, "").equalsIgnoreCase(name)) {
                try { return UUID.fromString(key); } catch (IllegalArgumentException ignored) { return null; }
            }
        }
        return null;
    }

    public static synchronized List<Map<?, ?>> warnings(UUID id) {
        ready();
        return new ArrayList<>(data.getMapList("warnings." + id));
    }

    public static synchronized int clearWarnings(UUID id) {
        ready();
        int count = data.getMapList("warnings." + id).size();
        data.set("warnings." + id, null);
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
        save();
        return true;
    }

    public static synchronized boolean unmute(UUID id) {
        ready();
        if (!data.contains("mutes." + id)) return false;
        data.set("mutes." + id, null);
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
        String duration = until == 0L ? "permanently" :
                "until " + new Date(until);
        player.sendMessage("§cYou are currently muted " + duration + ". Reason: §f" + muteReason(player.getUniqueId()));
    }
}
