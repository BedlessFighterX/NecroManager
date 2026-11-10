package com.bedlessfighter.necroManager.listeners;

import com.bedlessfighter.necroManager.managers.ConfigManager;
import com.bedlessfighter.necroManager.managers.MessageManager;
import com.bedlessfighter.necroManager.managers.PermissionManager;
import com.bedlessfighter.necroManager.managers.RestrictionManager;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public final class DamageListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        if (!RestrictionManager.isEnabled(event.getEntity().getWorld(), "disabler.damage-others", true)) {
            return;
        }

        Player attacker = resolvePlayerAttacker(event.getDamager());
        if (attacker == null || PermissionManager.has(attacker, "necromanager.damage-others")) {
            return;
        }

        event.setCancelled(true);
        MessageManager.send(attacker, "messages.damage-others",
                "&cYou may not damage other players.");
    }

    private Player resolvePlayerAttacker(Entity damager) {
        if (damager instanceof Player player) {
            return player;
        }
        if (damager instanceof Projectile projectile
                && projectile.getShooter() instanceof Player player) {
            return player;
        }
        return null;
    }

    @EventHandler(ignoreCancelled = true)
    public void onFallDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }
        if (!ConfigManager.getBoolean("disabler.fall-damage", true)
                || event.getCause() != EntityDamageEvent.DamageCause.FALL
                || PermissionManager.has(player, "necromanager.falldamage")) {
            return;
        }

        event.setCancelled(true);
        MessageManager.send(player, "messages.fall-damage",
                "&cFall damage is disabled in this area.");
    }
}
