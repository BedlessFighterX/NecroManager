package com.bedlessfighter.necroManager.listeners;

import com.bedlessfighter.necroManager.managers.MessageManager;
import com.bedlessfighter.necroManager.managers.PermissionManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;

/**
 * Configurable hub protections for interaction, hunger, item movement and mob spawning.
 */
public final class ProtectionListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null || event.getAction() == Action.PHYSICAL
                || !enabled("protections.block-interaction")
                || PermissionManager.has(event.getPlayer(), "necromanager.protection.interact")) {
            return;
        }
        event.setCancelled(true);
        MessageManager.send(event.getPlayer(), "messages.interaction-disabled",
                "&cBlock interaction is disabled.");
    }

    @EventHandler(ignoreCancelled = true)
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if (!(event.getEntity() instanceof Player player)
                || !enabled("protections.hunger")
                || PermissionManager.has(player, "necromanager.protection.hunger")) {
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onDrop(PlayerDropItemEvent event) {
        if (!enabled("protections.item-drop")
                || PermissionManager.has(event.getPlayer(), "necromanager.protection.drop")) {
            return;
        }
        event.setCancelled(true);
        MessageManager.send(event.getPlayer(), "messages.drop-disabled",
                "&cDropping items is disabled.");
    }

    @EventHandler(ignoreCancelled = true)
    public void onPickup(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player player)
                || !enabled("protections.item-pickup")
                || PermissionManager.has(player, "necromanager.protection.pickup")) {
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (enabled("protections.mob-spawning")) {
            event.setCancelled(true);
        }
    }

    private boolean enabled(String path) {
        return com.bedlessfighter.necroManager.Main.getPlugin()
                .getConfig().getBoolean(path, false);
    }
}
