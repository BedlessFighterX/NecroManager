package com.bedlessfighter.necroManager.listeners;

import com.bedlessfighter.necroManager.Main;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public final class DeathListener implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        var config = Main.getPlugin().getConfig();
        if (!config.getBoolean("deaths.enabled", true)) {
            event.setDeathMessage(null);
            return;
        }

        String prefix = color(config.getString("messages.death-prefix", "&8[&c☠&8]"));
        String deathMessage = event.getDeathMessage();

        if (deathMessage != null && !deathMessage.isBlank()) {
            event.setDeathMessage(prefix + " " + deathMessage);
        }
    }

    private String color(String message) {
        return ChatColor.translateAlternateColorCodes('&', message == null ? "" : message);
    }
}
