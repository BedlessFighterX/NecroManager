package com.bedlessfighter.necroManager.listeners;

import com.bedlessfighter.necroManager.managers.ConfigManager;
import com.bedlessfighter.necroManager.managers.MessageManager;
import com.bedlessfighter.necroManager.managers.PermissionManager;
import com.bedlessfighter.necroManager.managers.RestrictionManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public final class BlockPlaceListener implements Listener {

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (!RestrictionManager.isEnabled(player.getWorld(), "disabler.blockplace", true)) return;
        if (PermissionManager.has(player, "necromanager.blockplace")) return;

        event.setCancelled(true);
        MessageManager.send(player, "messages.blockplace",
                "&cYou may not place blocks in this territory.");
    }
}
