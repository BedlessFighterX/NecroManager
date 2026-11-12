package com.bedlessfighter.necroManager.managers;

import com.bedlessfighter.necroManager.Main;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/** Cycles configured announcements with audience thresholds and optional presentation effects. */
public final class AnnouncementManager {
    private static final AtomicInteger NEXT = new AtomicInteger();

    private AnnouncementManager() { throw new UnsupportedOperationException("Utility class"); }

    public static void start() {
        if (!ConfigManager.getBoolean("announcements.enabled", false)) return;
        int interval = ConfigManager.getInt("announcements.interval-seconds", 300, 10, 86400);
        Bukkit.getScheduler().runTaskTimer(Main.getPlugin(), AnnouncementManager::announce,
                interval * 20L, interval * 20L);
    }

    private static void announce() {
        int online = Bukkit.getOnlinePlayers().size();
        if (online < ConfigManager.getInt("announcements.minimum-online", 0, 0, 1000)) return;
        List<String> messages = ConfigManager.getStringList("announcements.messages");
        if (messages.isEmpty()) return;

        String template = messages.get(Math.floorMod(NEXT.getAndIncrement(), messages.size()));
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (WorldFeatureManager.isDisabled(player, "announcements")) continue;
            if (player.hasPermission("necromanager.announcements.ignore")) continue;
            if (ConfigManager.getStringList("announcements.disabled-worlds")
                    .contains(player.getWorld().getName())) continue;

            Map<String, String> placeholders = Map.of(
                    "player", player.getName(), "displayname", player.getDisplayName(),
                    "world", player.getWorld().getName(), "online", Integer.toString(online),
                    "max", Integer.toString(Bukkit.getMaxPlayers()));
            String message = MessageManager.format(template, placeholders);

            if (ConfigManager.getBoolean("announcements.chat.enabled", true)) player.sendMessage(message);
            if (ConfigManager.getBoolean("announcements.title.enabled", false)) {
                String title = MessageManager.format(
                        ConfigManager.getString("announcements.title.text", "&aAnnouncement"), placeholders);
                String subtitleTemplate = ConfigManager.getString(
                        "announcements.title.subtitle", "%message%").replace("%message%", template);
                String subtitle = MessageManager.format(subtitleTemplate, placeholders);
                player.sendTitle(title, subtitle,
                        ConfigManager.getInt("announcements.title.fade-in", 10, 0, 200),
                        ConfigManager.getInt("announcements.title.stay", 50, 0, 1200),
                        ConfigManager.getInt("announcements.title.fade-out", 10, 0, 200));
            }
            if (ConfigManager.getBoolean("announcements.actionbar.enabled", false)) {
                player.sendActionBar(message);
            }
            playSound(player);
        }
    }

    private static void playSound(Player player) {
        if (!ConfigManager.getBoolean("announcements.sound.enabled", false)) return;
        String configured = ConfigManager.getString(
                "announcements.sound.name", "ENTITY_EXPERIENCE_ORB_PICKUP");
        try {
            Sound sound = Sound.valueOf(configured.toUpperCase(Locale.ROOT));
            float volume = (float) ConfigManager.getInt("announcements.sound.volume", 1, 0, 10);
            float pitch = (float) ConfigManager.getInt("announcements.sound.pitch", 1, 0, 2);
            player.playSound(player.getLocation(), sound, volume, pitch);
        } catch (IllegalArgumentException exception) {
            Main.getPlugin().getLogger().warning(
                    "Invalid announcements.sound.name '" + configured + "'. Sound playback skipped.");
        }
    }
}
