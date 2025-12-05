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
        boolean blockPlaceStatus = Main.getPlugin().getConfig().getBoolean("disabler.blockplace");
        String prefix = ChatColor.translateAlternateColorCodes('&', Main.getPlugin().getConfig().getString("messages.prefix"));
        String message = ChatColor.translateAlternateColorCodes('&', Main.getPlugin().getConfig().getString("messages.blockplace"));

        if (blockPlaceStatus) {
            if (!player.hasPermission("spacemanager.admin") || !player.hasPermission("spacemanager.blockplace")) {
                e.setCancelled(true);
                player.sendMessage("%prefix% %message%".replace("%prefix%", prefix).replace("%message%", message));
            }
        } else {
            e.setCancelled(false);
        }
    }
}
