package com.bedlessfighter.necroManager.listeners;

import com.bedlessfighter.necroManager.managers.ConfigManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public final class BlockBreakListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();

        if (!ConfigManager.getBoolean("disabler.blockbreak", true)) return;
        if (player.isOp() || player.hasPermission("necromanager.admin")
                || player.hasPermission("necromanager.blockbreak")) return;

        event.setCancelled(true);
        player.sendMessage(color(ConfigManager.getString("messages.prefix", "&7[NecroManager]&r"))
                + " " + color(config.getString("messages.blockbreak",
                "&cYou may not break blocks in this territory.")));
    }

    private String color(String message) {
        return ChatColor.translateAlternateColorCodes('&', message == null ? "" : message);
    }
}
