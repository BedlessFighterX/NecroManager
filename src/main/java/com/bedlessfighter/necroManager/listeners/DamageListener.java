package com.bedlessfighter.necroManager.listeners;

import com.bedlessfighter.necroManager.Main;
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
        var config = Main.getPlugin().getConfig();
        if (!config.getBoolean("disabler.damage-others", true)) return;

        Player attacker = resolvePlayerAttacker(event.getDamager());
        if (attacker == null) return;
        if (attacker.isOp() || attacker.hasPermission("necromanager.admin")
                || attacker.hasPermission("necromanager.damage-others")) return;

        event.setCancelled(true);
        String prefix = color(config.getString("messages.prefix", "&7[NecroManager]&r"));
        String message = color(config.getString("messages.damage-others",
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
