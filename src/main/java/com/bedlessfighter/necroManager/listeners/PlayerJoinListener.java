package com.bedlessfighter.necroManager.listeners;

import com.bedlessfighter.necroManager.Main;
import com.bedlessfighter.necroManager.managers.ConfigManager;
import com.bedlessfighter.necroManager.managers.HubDisplayManager;
import com.bedlessfighter.necroManager.managers.EffectManager;
import com.bedlessfighter.necroManager.managers.MessageManager;
import com.bedlessfighter.necroManager.managers.PermissionManager;
import com.bedlessfighter.necroManager.managers.SpawnManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Sound;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.List;
import java.util.Locale;
import java.util.Map;

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
            SpawnManager.teleportToSpawn(player, false);
        }

        // Defer presentation by one tick so the configured spawn teleport has completed.
        Bukkit.getScheduler().runTask(Main.getPlugin(), () -> {
            if (!player.isOnline()) return;
            sendJoinMotd(player);
            sendJoinTitle(player);
            sendJoinActionbar(player);
            playJoinSound(player);
            launchJoinFirework(player);
        });
    }

    private void sendJoinMotd(Player player) {
        if (!ConfigManager.getBoolean("joins.motd.enabled", false)) return;
        List<String> lines = ConfigManager.getStringList("joins.motd.lines");
        Map<String, String> placeholders = MessageManager.playerPlaceholders(player);
        for (String line : lines) {
            player.sendMessage(MessageManager.format(line, placeholders));
        }
    }

    private void sendJoinTitle(Player player) {
        if (!ConfigManager.getBoolean("joins.title.enabled", false)) return;
        Map<String, String> placeholders = MessageManager.playerPlaceholders(player);
        String title = MessageManager.format(
                ConfigManager.getString("joins.title.text", "&aWelcome!"), placeholders);
        String subtitle = MessageManager.format(
                ConfigManager.getString("joins.title.subtitle", "&7Enjoy your stay."), placeholders);
        int fadeIn = ConfigManager.getInt("joins.title.fade-in", 10, 0, 200);
        int stay = ConfigManager.getInt("joins.title.stay", 60, 0, 1200);
        int fadeOut = ConfigManager.getInt("joins.title.fade-out", 20, 0, 200);
        player.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
    }

    private void sendJoinActionbar(Player player) {
        if (!ConfigManager.getBoolean("joins.actionbar.enabled", false)) return;
        String message = MessageManager.format(
                ConfigManager.getString("joins.actionbar.text", "&aWelcome, %player%!"),
                MessageManager.playerPlaceholders(player));
        player.sendActionBar(Component.text(org.bukkit.ChatColor.stripColor(message)));
    }

    private void playJoinSound(Player player) {
        if (!ConfigManager.getBoolean("joins.sound.enabled", false)) return;
        String configured = ConfigManager.getString("joins.sound.name", "ENTITY_PLAYER_LEVELUP");
        try {
            Sound sound = Sound.valueOf(configured.toUpperCase(Locale.ROOT));
            float volume = (float) ConfigManager.getDouble("joins.sound.volume", 1.0, 0.0, 4.0);
            float pitch = (float) ConfigManager.getDouble("joins.sound.pitch", 1.0, 0.5, 2.0);
            player.playSound(player.getLocation(), sound, volume, pitch);
        } catch (IllegalArgumentException exception) {
            Main.getPlugin().getLogger().warning("Invalid joins.sound.name: " + configured);
        }
    }

    private void launchJoinFirework(Player player) {
        if (!ConfigManager.getBoolean("joins.firework.enabled", false)) return;
        Firework firework = player.getWorld().spawn(player.getLocation(), Firework.class);
        FireworkMeta meta = firework.getFireworkMeta();
        int power = ConfigManager.getInt("joins.firework.power", 1, 0, 2);
        meta.setPower(power);
        FireworkEffect.Type type;
        try {
            type = FireworkEffect.Type.valueOf(
                    ConfigManager.getString("joins.firework.type", "BALL").toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException exception) {
            type = FireworkEffect.Type.BALL;
        }
        meta.addEffect(FireworkEffect.builder().with(type)
                .withColor(Color.LIME).withFade(Color.WHITE).trail(true).build());
        firework.setFireworkMeta(meta);
        if (ConfigManager.getBoolean("joins.firework.instant", true)) {
            firework.detonate();
        }
    }
}
