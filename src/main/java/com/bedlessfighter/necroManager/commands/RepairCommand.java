package com.bedlessfighter.necroManager.commands;

import com.bedlessfighter.necroManager.managers.MessageManager;
import com.bedlessfighter.necroManager.managers.PermissionManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/** Repairs damageable items held by a player. */
public final class RepairCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                            @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            MessageManager.send(sender, "messages.repair-player-only", "&cOnly players can use this command.");
            return true;
        }
        if (!PermissionManager.has(sender, "necromanager.repair")) {
            MessageManager.send(sender, "messages.no-perm",
                    "&cYou don't have enough permissions to issue this command!");
            return true;
        }
        if (args.length > 1 || (args.length == 1
                && !args[0].equalsIgnoreCase("hand") && !args[0].equalsIgnoreCase("all"))) {
            MessageManager.sendRaw(sender, MessageManager.prefix() + " &cUsage: /" + command.getName() + " [hand|all]");
            return true;
        }

        int repaired;
        if (args.length == 0 || args[0].equalsIgnoreCase("hand")) {
            repaired = repair(player.getInventory().getItemInMainHand());
        } else {
            repaired = 0;
            for (ItemStack item : player.getInventory().getContents()) {
                repaired += repair(item);
            }
            repaired += repair(player.getInventory().getItemInOffHand());
            for (ItemStack armor : player.getInventory().getArmorContents()) {
                repaired += repair(armor);
            }
            player.updateInventory();
        }
        MessageManager.send(sender, "messages.repair-result",
                "&aRepaired &e%count%&a item(s).",
                java.util.Map.of("count", Integer.toString(repaired)));
        return true;
    }

    private int repair(ItemStack item) {
        if (item == null || item.getType().isAir()) {
            return 0;
        }
        ItemMeta meta = item.getItemMeta();
        if (!(meta instanceof Damageable damageable) || damageable.getDamage() <= 0) {
            return 0;
        }
        damageable.setDamage(0);
        item.setItemMeta(meta);
        return 1;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
                                     @NotNull String alias, @NotNull String @NotNull [] args) {
        if (args.length != 1 || !PermissionManager.has(sender, "necromanager.repair")) {
            return List.of();
        }
        String prefix = args[0].toLowerCase(java.util.Locale.ROOT);
        return List.of("hand", "all").stream().filter(option -> option.startsWith(prefix)).toList();
    }
}