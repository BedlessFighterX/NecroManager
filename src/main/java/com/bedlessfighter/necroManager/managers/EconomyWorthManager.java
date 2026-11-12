package com.bedlessfighter.necroManager.managers;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Locale;

public final class EconomyWorthManager {
    private EconomyWorthManager() {}
    private static String path(Material material) { return "economy.worth." + material.name(); }
    public static double get(Material material) {
        FileConfiguration config=com.bedlessfighter.necroManager.Main.getPlugin().getConfig();
        double value=config.getDouble(path(material),0.0);
        return Double.isFinite(value) && value>0 ? value : 0.0;
    }
    public static boolean set(Material material,double value) {
        if(material==null || material.isAir() || !Double.isFinite(value) || value<0 || value>1.0E12) return false;
        var plugin=com.bedlessfighter.necroManager.Main.getPlugin();
        plugin.getConfig().set(path(material),value==0?null:Math.round(value*100.0)/100.0);
        plugin.saveConfig();
        return true;
    }
    public static Material match(String input) {
        if(input==null) return null;
        return Material.matchMaterial(input.trim().toUpperCase(Locale.ROOT));
    }
}