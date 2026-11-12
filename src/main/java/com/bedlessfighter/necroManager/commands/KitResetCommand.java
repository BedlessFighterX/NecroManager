package com.bedlessfighter.necroManager.commands;

import com.bedlessfighter.necroManager.managers.KitManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import java.util.ArrayList;
import java.util.List;

public class KitResetCommand implements CommandExecutor, TabCompleter {
    public boolean onCommand(CommandSender s, Command c, String l, String[] a) {
        if (!s.hasPermission("necromanager.kit.admin")) { s.sendMessage(ChatColor.RED + "No permission."); return true; }
        if (a.length != 2) { s.sendMessage(ChatColor.RED + "Usage: /kitreset <player> <kit>"); return true; }
        var target = Bukkit.getOfflinePlayer(a[0]);
        if (!KitManager.reset(target.getUniqueId(), a[1])) { s.sendMessage(ChatColor.RED + "Kit not found."); return true; }
        s.sendMessage(ChatColor.GREEN + "Reset " + a[1] + " cooldown for " + a[0] + ".");
        return true;
    }
    public List<String> onTabComplete(CommandSender s, Command c, String l, String[] a) {
        if (a.length == 1) return Bukkit.getOnlinePlayers().stream().map(p -> p.getName()).filter(n -> n.toLowerCase().startsWith(a[0].toLowerCase())).toList();
        if (a.length == 2) return KitManager.kits().stream().filter(k -> k.startsWith(a[1].toLowerCase())).toList();
        return new ArrayList<>();
    }
}
