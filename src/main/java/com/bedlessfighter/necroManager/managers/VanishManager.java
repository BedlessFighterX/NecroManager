package com.bedlessfighter.necroManager.managers;

import com.bedlessfighter.necroManager.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class VanishManager {
    private static final Set<UUID> VANISHED = ConcurrentHashMap.newKeySet();

    private VanishManager() { throw new UnsupportedOperationException("Utility class"); }

    public static boolean isVanished(Player player) {
        return VANISHED.contains(player.getUniqueId());
    }

    public static boolean toggle(Player player) {
        return setVanished(player, !isVanished(player));
    }

    public static boolean setVanished(Player player, boolean vanished) {
        if (vanished) VANISHED.add(player.getUniqueId());
        else VANISHED.remove(player.getUniqueId());
        for (Player viewer : Bukkit.getOnlinePlayers()) {
            if (viewer.equals(player)) continue;
            if (vanished) viewer.hidePlayer(Main.getPlugin(), player);
            else viewer.showPlayer(Main.getPlugin(), player);
        }
        return vanished;
    }

    public static Set<UUID> vanishedIds() {
        return Collections.unmodifiableSet(VANISHED);
    }

    public static void handleJoin(Player joining) {
        for (Player online : Bukkit.getOnlinePlayers()) {
            if (isVanished(online)) joining.hidePlayer(Main.getPlugin(), online);
        }
        if (isVanished(joining)) {
            for (Player viewer : Bukkit.getOnlinePlayers()) {
                if (!viewer.equals(joining)) viewer.hidePlayer(Main.getPlugin(), joining);
            }
        }
    }

    public static void handleQuit(Player player) {
        VANISHED.remove(player.getUniqueId());
    }

    public static void clear() {
        for (Player vanished : Bukkit.getOnlinePlayers()) {
            for (Player viewer : Bukkit.getOnlinePlayers()) {
                if (!viewer.equals(vanished)) viewer.showPlayer(Main.getPlugin(), vanished);
            }
        }
        VANISHED.clear();
    }
}
