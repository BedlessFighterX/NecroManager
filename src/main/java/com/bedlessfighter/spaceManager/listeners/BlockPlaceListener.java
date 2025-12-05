package com.bedlessfighter.spaceManager.listeners;

import com.bedlessfighter.spaceManager.Main;
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

        String prefix = ChatColor.translateAlternateColorCodes('&', cfg.getString("messages.prefix", "&7[SpaceManager]&r"));
        String message = ChatColor.translateAlternateColorCodes('&', cfg.getString("messages.blockplace", "&cYou can't place blocks here."));

        if (!blockPlaceStatus) return;

        if (!player.hasPermission("spacemanager.blockplace")
                || !player.hasPermission("spacemanager.admin")) {
            e.setCancelled(true);
            player.sendMessage("%prefix% %message%"
                    .replace("%prefix%", prefix)
                    .replace("%message%", message));
        }
    }
}
