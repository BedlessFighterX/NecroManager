package com.bedlessfighter.necroManager.listeners;

import com.bedlessfighter.necroManager.managers.ConfigManager;
import com.bedlessfighter.necroManager.managers.MessageManager;
import com.bedlessfighter.necroManager.managers.PermissionManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.Locale;

/** Blocks configured player commands without interfering with console execution. */
public final class CommandBlocklistListener implements Listener {
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onCommand(PlayerCommandPreprocessEvent event) {
        if (PermissionManager.has(event.getPlayer(), "necromanager.commandblocklist.bypass")) return;
        String raw = event.getMessage();
        String command = raw.startsWith("/") ? raw.substring(1) : raw;
        String root = command.split("\\s+", 2)[0].toLowerCase(Locale.ROOT);
        if (root.contains(":")) root = root.substring(root.indexOf(':') + 1);
        for (String blocked : ConfigManager.getStringList("command-blocklist.commands")) {
            String normalized = blocked.toLowerCase(Locale.ROOT).replaceFirst("^/", "");
            if (normalized.equals(root)) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(MessageManager.prefixed("command-blocked",
                        "&cThat command is disabled."));
                return;
            }
        }
    }
}
