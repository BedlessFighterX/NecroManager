package com.bedlessfighter.necroManager.listeners;

import com.bedlessfighter.necroManager.managers.ConfigManager;
import com.bedlessfighter.necroManager.managers.MessageManager;
import com.bedlessfighter.necroManager.managers.RestrictionManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;


public final class DeathListener implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (!RestrictionManager.isEnabled(event.getEntity().getWorld(), "deaths.enabled", true)) {
            event.setDeathMessage(null);
            return;
        }

        String deathMessage = event.getDeathMessage();
        if (deathMessage == null || deathMessage.isBlank()) return;

        String decorated = MessageManager.format(
                ConfigManager.getString("messages.death-prefix", "&8[&c☠&8]") + " " + deathMessage,
                MessageManager.playerPlaceholders(event.getEntity()));
        event.setDeathMessage(decorated);
    }
}
