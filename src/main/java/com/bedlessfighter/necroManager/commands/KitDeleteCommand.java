package com.bedlessfighter.necroManager.commands;

import com.bedlessfighter.necroManager.managers.KitManager;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import java.util.List;

public class KitDeleteCommand implements CommandExecutor, TabCompleter {
    public boolean onCommand(CommandSender s, Command c, String l, String[] a) {
        if (!s.hasPermission("necromanager.kit.admin")) { s.sendMessage(ChatColor.RED + "No permission."); return true; }
        if (a.length != 1) { s.sendMessage(ChatColor.RED + "Usage: /kitdelete <name>"); return true; }
        s.sendMessage(KitManager.delete(a[0]) ? ChatColor.GREEN + "Deleted kit " + a[0] + "." : ChatColor.RED + "Kit not found.");
        return true;
    }
    public List<String> onTabComplete(CommandSender s, Command c, String l, String[] a) {
        return a.length == 1 ? KitManager.kits().stream().filter(k -> k.startsWith(a[0].toLowerCase())).toList() : List.of();
    }
}
