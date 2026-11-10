package com.bedlessfighter.necroManager.listeners;

import com.bedlessfighter.necroManager.managers.MessageManager;
import com.bedlessfighter.necroManager.managers.PermissionManager;
import com.bedlessfighter.necroManager.managers.RestrictionManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public final class BlockBreakListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (!RestrictionManager.isEnabled(player.getWorld(), "disabler.blockbreak", true)) return;
        if (PermissionManager.has(player, "necromanager.blockbreak")) return;

        event.setCancelled(true);
        MessageManager.send(player, "messages.blockbreak",
                "&cYou may not break blocks in this territory.");
    }
}
