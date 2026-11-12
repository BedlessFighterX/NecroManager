package com.bedlessfighter.necroManager.listeners;

import com.bedlessfighter.necroManager.Main;
import com.bedlessfighter.necroManager.managers.MessageManager;
import com.bedlessfighter.necroManager.managers.PermissionManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

/**
 * Configurable hub protections for player actions and environmental changes.
 */
public final class ProtectionListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null || event.getAction() == Action.PHYSICAL
                || !enabled("protections.block-interaction")
                || PermissionManager.has(event.getPlayer(), "necromanager.protection.interact")) return;
        event.setCancelled(true);
        MessageManager.send(event.getPlayer(), "messages.interaction-disabled", "&cBlock interaction is disabled.");
    }

    @EventHandler(ignoreCancelled = true)
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if (!(event.getEntity() instanceof Player player) || !enabled("protections.hunger")
                || PermissionManager.has(player, "necromanager.protection.hunger")) return;
        event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onDrop(PlayerDropItemEvent event) {
        if (!enabled("protections.item-drop")
                || PermissionManager.has(event.getPlayer(), "necromanager.protection.drop")) return;
        event.setCancelled(true);
        MessageManager.send(event.getPlayer(), "messages.drop-disabled", "&cDropping items is disabled.");
    }

    @EventHandler(ignoreCancelled = true)
    public void onPickup(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player player) || !enabled("protections.item-pickup")
                || PermissionManager.has(player, "necromanager.protection.pickup")) return;
        event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (enabled("protections.mob-spawning")) event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)
                || PermissionManager.has(player, "necromanager.protection.environment")) return;

        EntityDamageEvent.DamageCause cause = event.getCause();
        if ((cause == EntityDamageEvent.DamageCause.VOID && enabled("protections.void-damage"))
                || ((cause == EntityDamageEvent.DamageCause.FIRE
                || cause == EntityDamageEvent.DamageCause.FIRE_TICK)
                && enabled("protections.fire-damage"))
                || (cause == EntityDamageEvent.DamageCause.DROWNING
                && enabled("protections.drowning-damage"))) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onSwapHands(PlayerSwapHandItemsEvent event) {
        if (!enabled("protections.offhand-swap")
                || PermissionManager.has(event.getPlayer(), "necromanager.protection.offhand")) return;
        event.setCancelled(true);
        MessageManager.send(event.getPlayer(), "messages.offhand-disabled", "&cOffhand swapping is disabled.");
    }

    @EventHandler(ignoreCancelled = true)
    public void onFireSpread(BlockSpreadEvent event) {
        if (enabled("protections.fire-spread") && event.getSource().getType() == Material.FIRE) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockBurn(BlockBurnEvent event) {
        if (enabled("protections.block-burn")) event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onLeavesDecay(LeavesDecayEvent event) {
        if (enabled("protections.leaf-decay")) event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onWeatherChange(WeatherChangeEvent event) {
        if (enabled("protections.weather-lock")) event.setCancelled(true);
    }

    private boolean enabled(String path) {
        return Main.getPlugin().getConfig().getBoolean(path, false);
    }
}
