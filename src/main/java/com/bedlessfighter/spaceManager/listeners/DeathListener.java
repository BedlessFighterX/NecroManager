package com.bedlessfighter.spaceManager.listeners;

import com.bedlessfighter.spaceManager.Main;
import com.bedlessfighter.spaceManager.managers.EffectManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.PotionEffectType;

public class DeathListener implements Listener {
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player player = e.getPlayer();
        var cfg = Main.getPlugin().getConfig();

        String prefix = ChatColor.translateAlternateColorCodes('&', cfg.getString("messages.prefix"));

        e.setDeathMessage("%prefix% %message%".replace("%prefix%", prefix).replace("%message%", e.getDeathMessage()));
    }
}
