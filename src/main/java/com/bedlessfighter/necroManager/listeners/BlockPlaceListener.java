package com.bedlessfighter.necroManager.listeners;

import com.bedlessfighter.necroManager.Main;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public final class BlockPlaceListener implements Listener {

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        var cfg = Main.getPlugin().getConfig();

        if (!cfg.getBoolean("disabler.blockplace")) return;
        if (player.hasPermission("necromanager.admin")
                || player.hasPermission("necromanager.blockplace")) return;

        event.setCancelled(true);
        String prefix = ChatColor.translateAlternateColorCodes('&',
                cfg.getString("messages.prefix", "&7[NecroManager]&r"));
        String message = ChatColor.translateAlternateColorCodes('&',
                cfg.getString("messages.blockplace", "&cYou may not place blocks here."));
        player.sendMessage(prefix + " " + message);
    }
}
