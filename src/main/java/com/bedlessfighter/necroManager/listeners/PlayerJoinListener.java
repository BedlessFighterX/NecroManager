package com.bedlessfighter.necroManager.listeners;

import com.bedlessfighter.necroManager.managers.ConfigManager;
import com.bedlessfighter.necroManager.managers.HubDisplayManager;
import com.bedlessfighter.necroManager.managers.EffectManager;
import com.bedlessfighter.necroManager.managers.MessageManager;
import com.bedlessfighter.necroManager.managers.PermissionManager;
import com.bedlessfighter.necroManager.managers.SpawnManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;


public final class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        HubDisplayManager.update(player);

        if (!ConfigManager.getBoolean("joins.enabled", true)) {
            event.setJoinMessage(null);
        } else {
            boolean silentStaff = ConfigManager.getBoolean("joins.silent-staffs", true);
            boolean staff = PermissionManager.isAdmin(player)
                    || PermissionManager.hasDirect(player, "necromanager.silent-join");
            if (staff && silentStaff) {
                event.setJoinMessage(null);
            } else if (!player.hasPlayedBefore()
                    && ConfigManager.getBoolean("joins.first-join-message-enabled", false)) {
                event.setJoinMessage(MessageManager.format(
                        ConfigManager.getString("joins.first-join-message",
                                "&aWelcome %player% to the server!"),
                        MessageManager.playerPlaceholders(player)));
            } else {
                event.setJoinMessage(MessageManager.format(
                        ConfigManager.getString("joins.message", "&7%player% joined the server."),
                        MessageManager.playerPlaceholders(player)));
            }
        }

        EffectManager.applyJoinEffects(player);

        if (ConfigManager.getBoolean("joins.spawn-on-join", true)) {
            // Join handling is intentionally quiet; an unset spawn should not spam joiners.
            SpawnManager.teleportToSpawn(player, false);
        }
    }
}
