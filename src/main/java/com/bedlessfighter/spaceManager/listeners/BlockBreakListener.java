package com.bedlessfighter.spaceManager.listeners;

import com.bedlessfighter.spaceManager.Main;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListener implements Listener {
    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();
        boolean blockBreakStatus = Main.getPlugin().getConfig().getBoolean("disabler.blockbreak");
        String prefix = ChatColor.translateAlternateColorCodes('&', Main.getPlugin().getConfig().getString("messages.prefix"));
        String message = ChatColor.translateAlternateColorCodes('&', Main.getPlugin().getConfig().getString("messages.blockbreak"));

        if (blockBreakStatus) {
            if (!player.hasPermission("spacemanager.blockbreak") || !player.hasPermission("spacemanager.admin")) {
                e.setCancelled(true);
                player.sendMessage("%prefix% %message%".replace("%prefix%", prefix).replace("%message%", message));
            }
        } else {
            e.setCancelled(false);
        }
    }
}
