package com.bedlessfighter.spaceManager.listeners;

import com.bedlessfighter.spaceManager.Main;
import com.bedlessfighter.spaceManager.managers.EffectManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        var cfg = Main.getPlugin().getConfig();

        String msg = cfg.getString("joins.message").replace("%player%", p.getName());
        boolean joinEnabled = cfg.getBoolean("joins.enabled");
        boolean silentStaff = cfg.getBoolean("joins.silent-staffs");
        boolean staff = p.hasPermission("spacemanager.admin") || p.hasPermission("spacemanager.silent-join") || p.isOp();

        if (joinEnabled) {
            if (staff && !silentStaff) e.setJoinMessage(ChatColor.translateAlternateColorCodes('&', msg));
            else if (!staff) e.setJoinMessage(ChatColor.translateAlternateColorCodes('&', msg));
            else if (staff && silentStaff) e.setJoinMessage(null);
        }

        // Effect handling
        if (cfg.getBoolean("effects.enabled")) {
            if (cfg.getBoolean("effects.speed"))
                EffectManager.giveEffect(p, PotionEffectType.SPEED, Integer.MAX_VALUE, 2);
            if (cfg.getBoolean("effects.regeneration"))
                EffectManager.giveEffect(p, PotionEffectType.REGENERATION, Integer.MAX_VALUE, 10);

            if (cfg.getBoolean("effects.feather-falling"))
                EffectManager.giveEffect(p, PotionEffectType.SLOW_FALLING, Integer.MAX_VALUE, 10);
        }

        // Spawn handling

    }
}
