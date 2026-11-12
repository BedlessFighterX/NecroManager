package com.bedlessfighter.necroManager.commands;

import com.bedlessfighter.necroManager.managers.KitManager;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import java.util.List;

public class KitCommand implements CommandExecutor, TabCompleter {
    public boolean onCommand(CommandSender s, Command c, String l, String[] a) {
        if (!(s instanceof Player p)) { s.sendMessage("Players only."); return true; }
        if (!p.hasPermission("necromanager.kit.use")) { s.sendMessage(ChatColor.RED + "No permission."); return true; }
        if (a.length == 0) { p.sendMessage(ChatColor.GREEN + "Available kits: " + String.join(", ", KitManager.kits())); return true; }
        String name = a[0].toLowerCase();
        if (!KitManager.exists(name)) { p.sendMessage(ChatColor.RED + "That kit does not exist."); return true; }
        long remaining = KitManager.remaining(p.getUniqueId(), name);
        if (remaining > 0) { p.sendMessage(ChatColor.RED + "That kit is on cooldown for " + remaining + "s."); return true; }
        if (!KitManager.claim(p, name)) { p.sendMessage(ChatColor.RED + "Could not claim that kit."); return true; }
        p.sendMessage(ChatColor.GREEN + "Kit " + name + " claimed.");
        return true;
    }
    public List<String> onTabComplete(CommandSender s, Command c, String a, String[] args) {
        return args.length == 1 ? KitManager.kits().stream().filter(k -> k.startsWith(args[0].toLowerCase())).toList() : List.of();
    }
}
