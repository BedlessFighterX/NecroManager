package com.bedlessfighter.necroManager.listeners;

import com.bedlessfighter.necroManager.Main;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlaceListener implements Listener {

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        var cfg = Main.getPlugin().getConfig();

        boolean blockPlaceStatus = cfg.getBoolean("disabler.blockplace");

        String prefix = ChatColor.translateAlternateColorCodes('&', cfg.getString("messages.prefix", "&7[NecroManager]&r"));
        String message = ChatColor.translateAlternateColorCodes('&', cfg.getString("messages.blockplace", "&cYou can't place blocks here."));

        if (!blockPlaceStatus) return;

        if (!player.hasPermission("necromanager.blockplace")
                || !player.hasPermission("necromanager.admin")) {
            e.setCancelled(true);
            player.sendMessage("%prefix% %message%"
                    .replace("%prefix%", prefix)
                    .replace("%message%", message));
        }
    }
}
