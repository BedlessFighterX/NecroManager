package com.bedlessfighter.necroManager.managers;

import com.bedlessfighter.necroManager.Main;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

/** File-backed, bounded economy ledger. All mutations are serialized and persisted immediately. */
public final class EconomyManager {
    private static final File FILE = new File(Main.getPlugin().getDataFolder(), "economy.yml");
    private static YamlConfiguration data;
    private EconomyManager() { throw new UnsupportedOperationException("Utility class"); }

    public static synchronized void load() {
        if (!Main.getPlugin().getDataFolder().exists()) Main.getPlugin().getDataFolder().mkdirs();
        data = YamlConfiguration.loadConfiguration(FILE);
    }

    private static void ensureLoaded() { if (data == null) load(); }
    private static String key(UUID uuid) { return "balances." + uuid; }
    private static double min() { return ConfigManager.getDouble("economy.minimum-balance", 0, -1.0E12, 1.0E12); }
    private static double max() { return ConfigManager.getDouble("economy.maximum-balance", 1.0E9, 0, 1.0E15); }
    private static double starting() { return clamp(ConfigManager.getDouble("economy.starting-balance", 100, 0, 1.0E12)); }
    private static double clamp(double value) { return Math.max(min(), Math.min(max(), value)); }
    private static double round(double value) { return BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP).doubleValue(); }

    public static synchronized double balance(OfflinePlayer player) {
        ensureLoaded();
        String path = key(player.getUniqueId());
        if (!data.contains(path)) {
            data.set(path, starting());
            data.set("names." + player.getUniqueId(), player.getName() == null ? player.getUniqueId().toString() : player.getName());
            save();
        } else if (player.getName() != null) data.set("names." + player.getUniqueId(), player.getName());
        return round(data.getDouble(path, starting()));
    }

    public static synchronized boolean set(OfflinePlayer player, double amount, String actor, String reason) {
        ensureLoaded();
        if (!Double.isFinite(amount) || amount < min() || amount > max()) return false;
        data.set(key(player.getUniqueId()), round(amount));
        if (player.getName() != null) data.set("names." + player.getUniqueId(), player.getName());
        log(actor, player.getUniqueId(), amount, reason);
        save();
        return true;
    }

    public static synchronized boolean deposit(OfflinePlayer player, double amount, String actor, String reason) {
        double current = balance(player);
        if (!Double.isFinite(amount) || amount <= 0 || current + amount > max()) return false;
        return set(player, current + amount, actor, reason);
    }

    public static synchronized boolean withdraw(OfflinePlayer player, double amount, String actor, String reason) {
        double current = balance(player);
        if (!Double.isFinite(amount) || amount <= 0 || current - amount < min()) return false;
        return set(player, current - amount, actor, reason);
    }

    private static void log(String actor, UUID target, double amount, String reason) {
        List<String> entries = new ArrayList<>(data.getStringList("transactions"));
        entries.add(Instant.now() + "|" + actor + "|" + target + "|" + round(amount) + "|" + reason);
        int limit = ConfigManager.getInt("economy.transaction-log-limit", 1000, 10, 10000);
        if (entries.size() > limit) entries = new ArrayList<>(entries.subList(entries.size() - limit, entries.size()));
        data.set("transactions", entries);
    }

    public static synchronized List<Account> top(int limit) {
        ensureLoaded();
        List<Account> accounts = new ArrayList<>();
        if (data.getConfigurationSection("balances") == null) return accounts;
        for (String id : data.getConfigurationSection("balances").getKeys(false)) {
            try {
                UUID uuid = UUID.fromString(id);
                accounts.add(new Account(uuid, data.getString("names." + id, id), data.getDouble("balances." + id)));
            } catch (IllegalArgumentException ignored) { }
        }
        accounts.sort(Comparator.comparingDouble(Account::balance).reversed());
        return accounts.stream().limit(limit).toList();
    }

    private static void save() {
        try { data.save(FILE); }
        catch (IOException exception) { throw new IllegalStateException("Could not save economy.yml", exception); }
    }

    public record Account(UUID uuid, String name, double balance) { }
}
