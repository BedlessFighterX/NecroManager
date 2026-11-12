package com.bedlessfighter.necroManager.listeners;

import com.bedlessfighter.necroManager.managers.VanishManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public final class VanishListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        VanishManager.handleJoin(event.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        if (VanishManager.isVanished(event.getPlayer())) event.setQuitMessage(null);
        VanishManager.handleQuit(event.getPlayer());
    }
}
