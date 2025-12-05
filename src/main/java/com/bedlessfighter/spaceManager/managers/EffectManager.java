package com.bedlessfighter.spaceManager.managers;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class EffectManager {
    public static void giveEffect(Player player, PotionEffectType type, int duration, int amplifier) {
        player.addPotionEffect(new PotionEffect(type, duration, amplifier, false, false, false));
    }

    public static void removeEffects(Player player, PotionEffectType type) {
        player.removePotionEffect(type);
    }
}
