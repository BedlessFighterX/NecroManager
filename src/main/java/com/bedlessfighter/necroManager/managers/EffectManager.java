package com.bedlessfighter.necroManager.managers;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Shared potion-effect operations and join-effect configuration.
 */
public final class EffectManager {

    private EffectManager() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static void giveEffect(Player player, PotionEffectType type, int duration, int amplifier) {
        if (player == null || type == null || duration <= 0 || amplifier < 0) {
            return;
        }
        player.addPotionEffect(new PotionEffect(type, duration, amplifier, false, false, false));
    }

    public static void removeEffects(Player player, PotionEffectType type) {
        if (player != null && type != null) {
            player.removePotionEffect(type);
        }
    }

    public static void applyJoinEffects(Player player) {
        if (!ConfigManager.getBoolean("effects.enabled", true)) {
            return;
        }

        int durationSeconds = ConfigManager.getInt("effects.duration-seconds", 86400, 1, 604800);
        int durationTicks = durationSeconds * 20;

        if (ConfigManager.getBoolean("effects.speed", true)) {
            giveEffect(player, PotionEffectType.SPEED, durationTicks,
                    ConfigManager.getInt("effects.speed-amplifier", 2, 0, 255));
        }
        if (ConfigManager.getBoolean("effects.regeneration", true)) {
            giveEffect(player, PotionEffectType.REGENERATION, durationTicks,
                    ConfigManager.getInt("effects.regeneration-amplifier", 10, 0, 255));
        }
        if (ConfigManager.getBoolean("effects.feather-falling", true)) {
            giveEffect(player, PotionEffectType.SLOW_FALLING, durationTicks,
                    ConfigManager.getInt("effects.slow-falling-amplifier", 10, 0, 255));
        }
    }
}
