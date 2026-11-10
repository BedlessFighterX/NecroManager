package com.bedlessfighter.necroManager.listeners;

import com.bedlessfighter.necroManager.Main;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class DamageListener implements Listener {

    @EventHandler
    public void onDamageByEntity(EntityDamageByEntityEvent event) {
        var config = Main.getPlugin().getConfig();
        if (!config.getBoolean("disabler.damage-others", true)) {
            return;
        }

        if (!(event.getDamager() instanceof Player player)) {
            return;
        }

        if (player.hasPermission("necromanager.admin")
                || player.hasPermission("necromanager.damage-others")
                || player.isOp()) {
            return;
        }

        event.setCancelled(true);
        String prefix = color(config.getString("messages.prefix", "&7[NecroManager]&r"));
        String message = color(config.getString("messages.damage-others",
                "&cYou may not damage other players."));
        player.sendMessage(prefix + " " + message);
    }

    @EventHandler
    public void onFallDamage(EntityDamageEvent event) {
        var config = Main.getPlugin().getConfig();
        if (config.getBoolean("disabler.fall-damage", true)
                && event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            event.setCancelled(true);
        }
    }

    private String color(String message) {
        return ChatColor.translateAlternateColorCodes('&', message == null ? "" : message);
    }
}
