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

public final class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        var config = Main.getPlugin().getConfig();

        if (!config.getBoolean("joins.enabled", true)) {
            event.setJoinMessage(null);
        } else {
            boolean silentStaff = config.getBoolean("joins.silent-staffs", true);
            boolean staff = player.isOp()
                    || player.hasPermission("necromanager.admin")
                    || player.hasPermission("necromanager.silent-join");

            if (staff && silentStaff) {
 {
                event.setJoinMessage(null);
            } else {
                String raw = config.getString("joins.message", "&7%player% joined the server.");
                String message = raw == null ? "" : raw.replace("%player%", player.getName());
                event.setJoinMessage(color(message));
            }
        }

        if (config.getBoolean("effects.enabled", true)) {
            if (config.getBoolean("effects.speed", true)) {
                EffectManager.giveEffect(player, PotionEffectType.SPEED, Integer.MAX_VALUE, 2);
            }
            if (config.getBoolean("effects.regeneration", true)) {
                EffectManager.giveEffect(player, PotionEffectType.REGENERATION, Integer.MAX_VALUE, 10);
            }
            if (config.getBoolean("effects.feather-falling", true)) {
                EffectManager.giveEffect(player, PotionEffectType.SLOW_FALLING, Integer.MAX_VALUE, 10);
            }
        }

        if (config.getBoolean("joins.spawn-on-join", true)) {
            SpawnManager.sendToSpawn(player);
        }
    }

    private String color(String message) {
        return ChatColor.translateAlternateColorCodes('&', message == null ? "" : message);
    }
}
