package com.bedlessfighter.necroManager.commands;

import com.bedlessfighter.necroManager.managers.KitManager;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import java.util.List;

public class KitListCommand implements CommandExecutor, TabCompleter {
    public boolean onCommand(CommandSender s, Command c, String l, String[] a) {
        List<String> kits = KitManager.kits();
        s.sendMessage(ChatColor.GREEN + "Kits (" + kits.size() + "): " + (kits.isEmpty() ? "none" : String.join(", ", kits)));
        return true;
    }
    public List<String> onTabComplete(CommandSender s, Command c, String l, String[] a) { return List.of(); }
}
