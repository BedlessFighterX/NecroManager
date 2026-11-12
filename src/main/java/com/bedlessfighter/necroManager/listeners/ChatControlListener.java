package com.bedlessfighter.necroManager.listeners;

import com.bedlessfighter.necroManager.managers.ConfigManager;
import com.bedlessfighter.necroManager.managers.MessageManager;
import com.bedlessfighter.necroManager.managers.PermissionManager;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

/** Enforces the global chat lock while allowing explicitly authorized bypasses. */
public final class ChatControlListener implements Listener {
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onChat(AsyncChatEvent event) {
        if (!ConfigManager.getBoolean("chat.locked", false)
                || PermissionManager.has(event.getPlayer(), "necromanager.chat.bypass")) return;
        event.setCancelled(true);
        event.getPlayer().sendMessage(MessageManager.prefixed("chat-locked",
                "&cChat is currently locked."));
    }
}
