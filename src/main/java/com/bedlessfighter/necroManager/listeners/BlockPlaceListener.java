package com.bedlessfighter.necroManager.listeners;

import com.bedlessfighter.necroManager.managers.ConfigManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public final class BlockPlaceListener implements Listener {

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();

        if (!ConfigManager.getBoolean("disabler.blockplace", true)) return;
        if (player.isOp() || player.hasPermission("necromanager.admin")
                || player.hasPermission("necromanager.blockplace")) return;

        event.setCancelled(true);
        player.sendMessage(color(ConfigManager.getString("messages.prefix", "&7[NecroManager]&r"))
                + " " + color(ConfigManager.getString("messages.blockplace",
                "&cYou may not place blocks in this territory.")));
    }

    private String color(String message) {
        return ChatColor.translateAlternateColorCodes('&', message == null ? "" : message);
    }
}
