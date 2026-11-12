package com.bedlessfighter.necroManager.commands;

import com.bedlessfighter.necroManager.managers.KitManager;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.inventory.ItemStack;
import java.util.List;
import java.util.stream.Collectors;

public class KitPreviewCommand implements CommandExecutor, TabCompleter {
    public boolean onCommand(CommandSender s, Command c, String l, String[] a) {
        if (a.length != 1) { s.sendMessage(ChatColor.RED + "Usage: /kitpreview <name>"); return true; }
        List<ItemStack> items = KitManager.preview(a[0]);
        if (items.isEmpty()) { s.sendMessage(ChatColor.RED + "Kit not found or empty."); return true; }
        String contents = items.stream().map(i -> i.getAmount() + "x " + i.getType().name()).collect(Collectors.joining(", "));
        s.sendMessage(ChatColor.GREEN + "Kit " + a[0] + ": " + contents);
        s.sendMessage(ChatColor.GRAY + "Cooldown: " + KitManager.cooldown(a[0]) + " seconds.");
        return true;
    }
    public List<String> onTabComplete(CommandSender s, Command c, String l, String[] a) {
        return a.length == 1 ? KitManager.kits().stream().filter(k -> k.startsWith(a[0].toLowerCase())).toList() : List.of();
    }
}
