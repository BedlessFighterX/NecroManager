package com.bedlessfighter.necroManager.listeners;

import com.bedlessfighter.necroManager.managers.ConfigManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public final class DamageListener implements Listener {

    @EventHandler
    public void onDamageByEntity(EntityDamageByEntityEvent event) {
        if (!ConfigManager.getBoolean("disabler.damage-others", true)) return;

        Player attacker = resolvePlayerAttacker(event.getDamager());
        if (attacker == null) return;
        if (attacker.isOp() || attacker.hasPermission("necromanager.admin")
                || attacker.hasPermission("necromanager.damage-others")) return;

        event.setCancelled(true);
        String prefix = color(ConfigManager.getString("messages.prefix", "&7[NecroManager]&r"));
        String message = color(ConfigManager.getString("messages.damage-others",
                "&cYou may not damage other players."));
        attacker.sendMessage(prefix + " " + message);
    }

    private Player resolvePlayerAttacker(Entity damager) {
        if (damager instanceof Player player) return player;
        if (damager instanceof Projectile projectile
                && projectile.getShooter() instanceof Player player) return player;
        return null;
    }

    @EventHandler
    public void onFallDamage(EntityDamageEvent event) {
        if (!ConfigManager.getBoolean("disabler.fall-damage", true)
                || event.getCause() != EntityDamageEvent.DamageCause.FALL) return;

        if (event.getEntity() instanceof Player player
                && (player.isOp() || player.hasPermission("necromanager.admin")
                || player.hasPermission("necromanager.falldamage"))) return;

        event.setCancelled(true);
        if (event.getEntity() instanceof Player player) {
            String prefix = color(ConfigManager.getString("messages.prefix", "&7[NecroManager]&r"));
            String message = color(ConfigManager.getString("messages.fall-damage",
                    "&cFall damage is disabled in this area."));
            player.sendMessage(prefix + " " + message);
        }
    }

    private String color(String message) {
        return ChatColor.translateAlternateColorCodes('&', message == null ? "" : message);
    }
}
