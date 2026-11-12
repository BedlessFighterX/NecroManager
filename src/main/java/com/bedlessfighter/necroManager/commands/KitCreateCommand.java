package com.bedlessfighter.necroManager.commands;

import com.bedlessfighter.necroManager.managers.KitManager;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import java.util.List;

public class KitCreateCommand implements CommandExecutor, TabCompleter {
    public boolean onCommand(CommandSender s, Command c, String l, String[] a) {
        if (!(s instanceof Player p)) { s.sendMessage("Players only."); return true; }
        if (!p.hasPermission("necromanager.kit.admin")) { p.sendMessage(ChatColor.RED + "No permission."); return true; }
        if (a.length < 1 || a.length > 2) { p.sendMessage(ChatColor.RED + "Usage: /kitcreate <name> [cooldown-seconds]"); return true; }
        long cooldown = 0;
        try { if (a.length == 2) cooldown = Long.parseLong(a[1]); } catch (NumberFormatException ex) { p.sendMessage(ChatColor.RED + "Cooldown must be a whole number."); return true; }
        if (cooldown < 0 || cooldown > 31536000) { p.sendMessage(ChatColor.RED + "Cooldown must be between 0 and 31536000 seconds."); return true; }
        if (!KitManager.create(a[0], p, cooldown)) { p.sendMessage(ChatColor.RED + "Invalid name or kit already exists."); return true; }
        p.sendMessage(ChatColor.GREEN + "Created kit " + a[0].toLowerCase() + " from your inventory and armor.");
        return true;
    }
    public List<String> onTabComplete(CommandSender s, Command c, String l, String[] a) { return List.of(); }
}
