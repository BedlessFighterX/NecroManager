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
        var cfg = Main.getPlugin().getConfig();

        boolean blockBreakStatus = cfg.getBoolean("disabler.blockbreak");

        String prefix = ChatColor.translateAlternateColorCodes('&', cfg.getString("messages.prefix", "&7[SpaceManager]&r"));
        String message = ChatColor.translateAlternateColorCodes('&', cfg.getString("messages.blockbreak", "&cYou can't break blocks here."));

        if (!blockBreakStatus) return;

        if (!player.hasPermission("spacemanager.blockbreak")
                || !player.hasPermission("spacemanager.admin")) {
            e.setCancelled(true);
            player.sendMessage("%prefix% %message%"
                    .replace("%prefix%", prefix)
                    .replace("%message%", message));
        }
    }
}
