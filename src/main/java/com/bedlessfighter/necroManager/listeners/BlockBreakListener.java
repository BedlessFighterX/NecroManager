package com.bedlessfighter.necroManager.listeners;

import com.bedlessfighter.necroManager.Main;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public final class BlockBreakListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        var cfg = Main.getPlugin().getConfig();

        if (!cfg.getBoolean("disabler.blockbreak")) return;
        if (player.hasPermission("necromanager.admin")
                || player.hasPermission("necromanager.blockbreak")) return;

        event.setCancelled(true);
        String prefix = ChatColor.translateAlternateColorCodes('&',
                cfg.getString("messages.prefix", "&7[NecroManager]&r"));
        String message = ChatColor.translateAlternateColorCodes('&',
                cfg.getString("messages.blockbreak", "&cYou may not break blocks here."));
        player.sendMessage(prefix + " " + message);
    }
}
