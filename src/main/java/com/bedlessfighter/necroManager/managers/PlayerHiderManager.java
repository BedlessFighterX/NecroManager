package com.bedlessfighter.necroManager.managers;

import com.bedlessfighter.necroManager.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public final class PlayerHiderManager {
    private static final Set<UUID> HIDING = new HashSet<>();

    private PlayerHiderManager() { throw new UnsupportedOperationException("Utility class"); }

    public static boolean toggle(Player player) {
        if (!HIDING.add(player.getUniqueId())) {
            HIDING.remove(player.getUniqueId());
            for (Player online : Bukkit.getOnlinePlayers()) player.showPlayer(Main.getPlugin(), online);
            return false;
        }
        for (Player online : Bukkit.getOnlinePlayers()) {
            if (!online.equals(player)) player.hidePlayer(Main.getPlugin(), online);
        }
        return true;
    }

    public static void onJoin(Player joining) {
        for (Player viewer : Bukkit.getOnlinePlayers()) {
            if (HIDING.contains(viewer.getUniqueId()) && !viewer.equals(joining)) {
                viewer.hidePlayer(Main.getPlugin(), joining);
            }
            if (HIDING.contains(joining.getUniqueId()) && !viewer.equals(joining)) {
                joining.hidePlayer(Main.getPlugin(), viewer);
            }
        }
    }

    public static void onQuit(Player player) {
        HIDING.remove(player.getUniqueId());
        for (Player viewer : Bukkit.getOnlinePlayers()) {
            viewer.showPlayer(Main.getPlugin(), player);
        }
    }

    public static void clear() { HIDING.clear(); }
}
