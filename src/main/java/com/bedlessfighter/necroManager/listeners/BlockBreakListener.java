package com.bedlessfighter.necroManager.listeners;

import com.bedlessfighter.necroManager.managers.ConfigManager;
import com.bedlessfighter.necroManager.managers.MessageManager;
import com.bedlessfighter.necroManager.managers.PermissionManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public final class BlockBreakListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (!ConfigManager.getBoolean("disabler.blockbreak", true)) return;
        if (PermissionManager.has(player, "necromanager.blockbreak")) return;

        event.setCancelled(true);
        MessageManager.send(player, "messages.blockbreak",
                "&cYou may not break blocks in this territory.");
    }
}
