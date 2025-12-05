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
    public void onDamage(EntityDamageByEntityEvent e) {
        boolean isDamageByOthersEnabled = Main.getPlugin().getConfig().getBoolean("disabler.damage-others");
        boolean isDamageByFallEnabled = Main.getPlugin().getConfig().getBoolean("disabler.fall-damage");
        String prefix = ChatColor.translateAlternateColorCodes('&', Main.getPlugin().getConfig().getString("messages.prefix"));
        String message = ChatColor.translateAlternateColorCodes('&', Main.getPlugin().getConfig().getString("messages.damage-others"));
        boolean isPlayer = e.getDamager() instanceof Player;
        Player player = (Player) e.getDamager();
        boolean isStaff = player.hasPermission("spacemanager.admin");

        if (!isStaff) {
            if (isPlayer && isDamageByOthersEnabled) {
                e.setCancelled(true);
                player.sendMessage("%prefix% %message%".replace("%prefix%", prefix).replace("%message%", message));
            }
        }

        if (e.getCause() == EntityDamageEvent.DamageCause.FALL && isDamageByFallEnabled) e.setCancelled(true);
    }
}
