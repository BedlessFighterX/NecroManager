package com.bedlessfighter.spaceManager.listeners;

import com.bedlessfighter.spaceManager.Main;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class DamageListener implements Listener {

    @EventHandler
    public void onDamageByEntity(EntityDamageByEntityEvent e) {
        var cfg = Main.getPlugin().getConfig();

        boolean isDamageByOthersEnabled = cfg.getBoolean("disabler.damage-others");

        if (e.getDamager() instanceof Player player) {
            boolean isStaff = player.hasPermission("spacemanager.admin");
            if (!isStaff && isDamageByOthersEnabled) {
                String prefix = ChatColor.translateAlternateColorCodes('&', cfg.getString("messages.prefix", "&7[SpaceManager]&r"));
                String message = ChatColor.translateAlternateColorCodes('&', cfg.getString("messages.damage-others", "&cYou can't damage others here."));
                e.setCancelled(true);
                player.sendMessage("%prefix% %message%"
                        .replace("%prefix%", prefix)
                        .replace("%message%", message));
            }
        }
    }

    @EventHandler
    public void onFallDamage(EntityDamageEvent e) {
        var cfg = Main.getPlugin().getConfig();
        boolean isDamageByFallEnabled = cfg.getBoolean("disabler.fall-damage");

        if (isDamageByFallEnabled && e.getCause() == EntityDamageEvent.DamageCause.FALL) {
            e.setCancelled(true);
        }
    }
}
