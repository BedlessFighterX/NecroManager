package com.bedlessfighter.necroManager.listeners;

import com.bedlessfighter.necroManager.Main;
import com.bedlessfighter.necroManager.managers.ConfigManager;
import com.bedlessfighter.necroManager.managers.PermissionManager;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

/**
 * Detects explicit World Downloader plugin-message handshakes.
 * This is an alert-only signal: it is not proof of a successful download and never triggers punishment.
 */
public final class AntiWorldDownloaderListener implements Listener, PluginMessageListener {
    private static final String[] CHANNELS = {"WDL|INIT", "WDL|CONTROL", "wdl:init", "wdl:control"};
    private final Set<UUID> alerted = new HashSet<>();

    public void registerChannels(Plugin plugin) {
        for (String channel : CHANNELS) {
            plugin.getServer().getMessenger().registerIncomingPluginChannel(plugin, channel, this);
        }
    }

    public void unregisterChannels(Plugin plugin) {
        plugin.getServer().getMessenger().unregisterIncomingPluginChannel(plugin, this);
        alerted.clear();
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!ConfigManager.getBoolean("anti-world-downloader.enabled", false)
                || PermissionManager.has(player, "necromanager.antiworlddownloader.bypass")) return;
        String normalized = channel.toLowerCase(Locale.ROOT);
        if (!normalized.equals("wdl|init") && !normalized.equals("wdl|control")
                && !normalized.equals("wdl:init") && !normalized.equals("wdl:control")) return;
        if (!alerted.add(player.getUniqueId())) return;

        Main.getPlugin().getLogger().warning("World Downloader handshake received from "
                + player.getName() + " on channel " + channel + ". Alert only; no punishment applied.");
        if (!ConfigManager.getBoolean("anti-world-downloader.staff-alerts", true)) return;\n        String alert = ConfigManager.getString("anti-world-downloader.alert-message",
                "&c[Anti-WDL] &f%player% sent a World Downloader handshake (&7%channel%&f).")
                .replace("%player%", player.getName()).replace("%channel%", channel);
        Bukkit.getScheduler().runTask(Main.getPlugin(), () -> Bukkit.getOnlinePlayers().stream()
                .filter(staff -> PermissionManager.has(staff, "necromanager.antiworlddownloader.alerts"))
                .forEach(staff -> staff.sendMessage(
                        LegacyComponentSerializer.legacyAmpersand().deserialize(alert))));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        alerted.remove(event.getPlayer().getUniqueId());
    }
}
