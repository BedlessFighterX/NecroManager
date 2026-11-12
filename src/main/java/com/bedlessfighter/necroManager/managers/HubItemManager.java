package com.bedlessfighter.necroManager.managers;

import com.bedlessfighter.necroManager.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class HubItemManager {
    private static final NamespacedKey ACTION = new NamespacedKey(Main.getPlugin(), "hub_item_action");
    private static final NamespacedKey SERVER = new NamespacedKey(Main.getPlugin(), "hub_server");
    private static final NamespacedKey COMMAND = new NamespacedKey(Main.getPlugin(), "hub_item_command");
    private static final NamespacedKey JOIN_ITEM = new NamespacedKey(Main.getPlugin(), "hub_join_item");

    private HubItemManager() { throw new UnsupportedOperationException("Utility class"); }

    public static void giveJoinItems(Player player) {
        if (!ConfigManager.getBoolean("joins.items.enabled", false)) return;
        for (ItemStack item : player.getInventory().getContents()) {
            if (isJoinItem(item)) player.getInventory().remove(item);
        }
        for (Map<?, ?> entry : Main.getPlugin().getConfig().getMapList("joins.items.entries")) {
            ItemStack item = buildItem(entry, true);
            if (item == null) continue;
            int slot = number(entry.get("slot"), 4);
            if (slot < 0 || slot > 35) continue;
            ItemStack current = player.getInventory().getItem(slot);
            if (current == null || current.getType().isAir()) {
                player.getInventory().setItem(slot, item);
            } else {
                Map<Integer, ItemStack> overflow = player.getInventory().addItem(item);
                overflow.values().forEach(extra -> player.getWorld().dropItemNaturally(player.getLocation(), extra));
            }
        }
    }

    public static Inventory createServerSelector() {
        SelectorHolder holder = new SelectorHolder();
        String title = MessageManager.color(ConfigManager.getString(
                "menus.server-selector.title", "&8Select a server"));
        Inventory inventory = Bukkit.createInventory(holder, 27, title);
        holder.inventory = inventory;
        for (Map<?, ?> entry : Main.getPlugin().getConfig().getMapList("menus.server-selector.entries")) {
            ItemStack item = buildItem(entry, false);
            if (item == null) continue;
            int slot = number(entry.get("slot"), -1);
            if (slot < 0 || slot >= inventory.getSize()) continue;
            Object server = entry.get("server");
            if (server == null || server.toString().isBlank()) continue;
            ItemMeta meta = item.getItemMeta();
            meta.getPersistentDataContainer().set(SERVER, PersistentDataType.STRING, server.toString());
            item.setItemMeta(meta);
            inventory.setItem(slot, item);
        }
        return inventory;
    }

    public static boolean isSelector(InventoryHolder holder) {
        return holder instanceof SelectorHolder;
    }

    public static void connectFromItem(Player player, ItemStack item) {
        if (item == null || !item.hasItemMeta()) return;
        String server = item.getItemMeta().getPersistentDataContainer().get(SERVER, PersistentDataType.STRING);
        if (server == null || server.isBlank()) return;
        try (ByteArrayOutputStream bytes = new ByteArrayOutputStream();
             DataOutputStream output = new DataOutputStream(bytes)) {
            output.writeUTF("Connect");
            output.writeUTF(server);
            player.sendPluginMessage(Main.getPlugin(), "BungeeCord", bytes.toByteArray());
            player.closeInventory();
        } catch (IOException exception) {
            Main.getPlugin().getLogger().warning("Could not send server connection request: " + exception.getMessage());
        }
    }

    public static String command(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return null;
        return item.getItemMeta().getPersistentDataContainer().get(COMMAND, PersistentDataType.STRING);
    }

    public static String action(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return null;
        return item.getItemMeta().getPersistentDataContainer().get(ACTION, PersistentDataType.STRING);
    }

    public static boolean isJoinItem(ItemStack item) {
        return item != null && item.hasItemMeta()
                && item.getItemMeta().getPersistentDataContainer().has(JOIN_ITEM, PersistentDataType.BYTE);
    }

    private static ItemStack buildItem(Map<?, ?> entry, boolean joinItem) {
        Object materialValue = entry.get("material");
        Material material = materialValue == null ? null : Material.matchMaterial(materialValue.toString());
        if (material == null || material.isAir()) return null;
        ItemStack item = new ItemStack(material, Math.max(1, Math.min(64, number(entry.get("amount"), 1))));
        ItemMeta meta = item.getItemMeta();
        Object name = entry.get("name");
        if (name != null) meta.setDisplayName(MessageManager.color(name.toString()));
        Object lore = entry.get("lore");
        if (lore instanceof List<?> lines) {
            List<String> colored = new ArrayList<>();
            for (Object line : lines) colored.add(MessageManager.color(String.valueOf(line)));
            meta.setLore(colored);
        }
        Object action = entry.get("action");
        if (action != null) meta.getPersistentDataContainer().set(
                ACTION, PersistentDataType.STRING, action.toString().toUpperCase(java.util.Locale.ROOT));
        Object command = entry.get("command");
        if (command != null && !command.toString().isBlank()) {
            meta.getPersistentDataContainer().set(COMMAND, PersistentDataType.STRING, command.toString());
        }
        if (joinItem) meta.getPersistentDataContainer().set(JOIN_ITEM, PersistentDataType.BYTE, (byte) 1);
        item.setItemMeta(meta);
        return item;
    }

    private static int number(Object value, int fallback) {
        if (value instanceof Number number) return number.intValue();
        if (value == null) return fallback;
        try { return Integer.parseInt(value.toString()); }
        catch (NumberFormatException exception) { return fallback; }
    }

    private static final class SelectorHolder implements InventoryHolder {
        private Inventory inventory;
        @Override public Inventory getInventory() { return inventory; }
    }
}
