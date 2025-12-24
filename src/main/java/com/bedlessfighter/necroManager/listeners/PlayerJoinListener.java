package com.bedlessfighter.necroManager.listeners;

import com.bedlessfighter.necroManager.Main;
import com.bedlessfighter.necroManager.managers.EffectManager;
import com.bedlessfighter.necroManager.managers.SpawnManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffectType;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        var cfg = Main.getPlugin().getConfig();

        boolean joinEnabled = cfg.getBoolean("joins.enabled");
        boolean silentStaff = cfg.getBoolean("joins.silent-staffs");
        boolean staff = p.hasPermission("necromanager.admin")
                || p.hasPermission("necromanager.silent-join")
                || p.isOp();


        if (joinEnabled) {
            String msgRaw = cfg.getString("joins.message", "&7%player% joined the server.");
            String msg = ChatColor.translateAlternateColorCodes('&', msgRaw.replace("%player%", p.getName()));

            if (staff && silentStaff) e.setJoinMessage(null);
            else e.setJoinMessage(msg);
        } else {
            e.setJoinMessage(null);
        }

        if (cfg.getBoolean("effects.enabled")) {
            if (cfg.getBoolean("effects.speed"))
                EffectManager.giveEffect(p, PotionEffectType.SPEED, Integer.MAX_VALUE, 2);
            if (cfg.getBoolean("effects.regeneration"))
                EffectManager.giveEffect(p, PotionEffectType.REGENERATION, Integer.MAX_VALUE, 10);
            if (cfg.getBoolean("effects.feather-falling"))
                EffectManager.giveEffect(p, PotionEffectType.SLOW_FALLING, Integer.MAX_VALUE, 10);
        }

        if (cfg.getBoolean("joins.spawn-on-join")) {
            SpawnManager.sendToSpawn(p);
        }
    }
}
