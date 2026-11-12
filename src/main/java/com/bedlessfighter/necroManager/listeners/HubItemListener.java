package com.bedlessfighter.necroManager.listeners;

import com.bedlessfighter.necroManager.managers.ConfigManager;
import com.bedlessfighter.necroManager.managers.HubItemManager;
import com.bedlessfighter.necroManager.managers.MessageManager;
import com.bedlessfighter.necroManager.managers.PlayerHiderManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public final class HubItemListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerHiderManager.onJoin(player);
        org.bukkit.Bukkit.getScheduler().runTask(com.bedlessfighter.necroManager.Main.getPlugin(),
                () -> { if (player.isOnline()) HubItemManager.giveJoinItems(player); });
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        PlayerHiderManager.onQuit(event.getPlayer());
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Action action = event.getAction();
        if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) return;
        ItemStack item = event.getItem();
        if (item == null) return;
        String itemAction = HubItemManager.action(item);
        if (itemAction == null) return;
        Player player = event.getPlayer();
        event.setCancelled(true);
        switch (itemAction) {
            case "SERVER_SELECTOR" -> player.openInventory(HubItemManager.createServerSelector());
            case "PLAYER_HIDER" -> {
                boolean hidden = PlayerHiderManager.toggle(player);
                MessageManager.sendRaw(player, hidden ? "&aPlayers hidden." : "&ePlayers visible.");
            }
            default -> { }
        }
    }

    @EventHandler
    public void onSelectorClick(InventoryClickEvent event) {
        if (!HubItemManager.isSelector(event.getView().getTopInventory().getHolder())) return;
        event.setCancelled(true);
        if (event.getClickedInventory() != event.getView().getTopInventory()
                || !(event.getWhoClicked() instanceof Player player)) return;
        HubItemManager.connectFromItem(player, event.getCurrentItem());
    }

    @EventHandler
    public void onSelectorDrag(InventoryDragEvent event) {
        if (HubItemManager.isSelector(event.getView().getTopInventory().getHolder())) event.setCancelled(true);
    }
}
