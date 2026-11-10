package com.bedlessfighter.necroManager.listeners;

import com.bedlessfighter.necroManager.managers.ConfigManager;
import com.bedlessfighter.necroManager.managers.MessageManager;
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
        MessageManager.send(player, "messages.blockplace",
                "&cYou may not place blocks in this territory.");
    }
}
