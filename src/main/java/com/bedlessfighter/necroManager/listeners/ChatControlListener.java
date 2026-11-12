package com.bedlessfighter.necroManager.listeners;

import com.bedlessfighter.necroManager.Main;
import com.bedlessfighter.necroManager.managers.ConfigManager;
import com.bedlessfighter.necroManager.managers.PermissionManager;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/** Applies configurable chat filtering and dispatches staff-only moderation alerts. */
public final class ChatControlListener implements Listener {
    private static final PlainTextComponentSerializer PLAIN = PlainTextComponentSerializer.plainText();

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onChat(AsyncChatEvent event) {
        Player player = event.getPlayer();
        if (ConfigManager.getBoolean("chat.locked", false)
                && !com.bedlessfighter.necroManager.managers.WorldFeatureManager.isDisabled(player, "chat-lock")
                && !PermissionManager.has(player, "necromanager.chat.bypass")) {
            event.setCancelled(true);
            player.sendMessage(net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacyAmpersand().deserialize(ConfigManager.getString("chat.locked-message", "&cChat is currently locked.")));
            return;
        }

        if (com.bedlessfighter.necroManager.managers.WorldFeatureManager.isDisabled(player, "anti-swear")) return;
        if (!ConfigManager.getBoolean("anti-swear.enabled", false)
                || PermissionManager.has(player, "necromanager.antiswear.bypass")) return;

        List<String> words = ConfigManager.getStringList("anti-swear.words").stream()
                .map(String::trim).filter(word -> !word.isEmpty()).distinct().toList();
        if (words.isEmpty()) return;

        String original = PLAIN.serialize(event.message());
        String alternatives = words.stream().map(Pattern::quote).collect(Collectors.joining("|"));
        Pattern filter = Pattern.compile("(?iu)(?<![\\p{L}\\p{N}_])(?:" + alternatives
                + ")(?![\\p{L}\\p{N}_])");
        if (!filter.matcher(original).find()) return;

        String replacement = ConfigManager.getString("anti-swear.replacement", "[filtered]");
        String filtered = filter.matcher(original).replaceAll(MatcherReplacement.quote(replacement));
        event.message(Component.text(filtered));

        if (ConfigManager.getBoolean("anti-swear.staff-alerts", true)) {
            String alert = ConfigManager.getString("anti-swear.alert-message",
                    "&c[ChatFilter] &f%player% used a filtered word: &7%message%")
                    .replace("%player%", player.getName())
                    .replace("%message%", original);
            Bukkit.getScheduler().runTask(Main.getPlugin(), () -> Bukkit.getOnlinePlayers().stream()
                    .filter(staff -> PermissionManager.has(staff, "necromanager.antiswear.alerts"))
                    .forEach(staff -> staff.sendMessage(colorize(alert))));
        }
    }

    private static Component colorize(String message) {
        return net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacyAmpersand()
                .deserialize(message);
    }

    private static final class MatcherReplacement {
        private MatcherReplacement() { }
        private static String quote(String value) {
            return java.util.regex.Matcher.quoteReplacement(value);
        }
    }
}
