package com.bedlessfighter.necroManager.managers;

import com.bedlessfighter.necroManager.Main;
import org.bukkit.Bukkit;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/** Cycles configured server announcements with a configurable online threshold. */
public final class AnnouncementManager {
    private static final AtomicInteger NEXT = new AtomicInteger();

    private AnnouncementManager() { throw new UnsupportedOperationException("Utility class"); }

    public static void start() {
        if (!ConfigManager.getBoolean("announcements.enabled", false)) return;
        int interval = ConfigManager.getInt("announcements.interval-seconds", 300, 10, 86400);
        Bukkit.getScheduler().runTaskTimer(Main.getPlugin(), AnnouncementManager::announce,
                interval * 20L, interval * 20L);
    }

    private static void announce() {
        if (Bukkit.getOnlinePlayers().size()
                < ConfigManager.getInt("announcements.minimum-online", 0, 0, 1000)) return;
        List<String> messages = ConfigManager.getStringList("announcements.messages");
        if (messages.isEmpty()) return;
        int index = Math.floorMod(NEXT.getAndIncrement(), messages.size());
        String message = MessageManager.format(messages.get(index), Map.of(
                "online", Integer.toString(Bukkit.getOnlinePlayers().size()),
                "max", Integer.toString(Bukkit.getMaxPlayers())
        ));
        Bukkit.broadcastMessage(message);
    }
}
