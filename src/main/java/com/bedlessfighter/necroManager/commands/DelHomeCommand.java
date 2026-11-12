package com.bedlessfighter.necroManager.commands;

import com.bedlessfighter.necroManager.MessageManager;
import com.bedlessfighter.necroManager.managers.HomeManager;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import java.util.List;

public final class DelHomeCommand implements CommandExecutor, TabCompleter {
    public boolean onCommand(CommandSender s, Command c, String l, String[] a) {
        if (!(s instanceof Player p)) { MessageManager.sendRaw(s, "&cPlayers only."); return true; }
        if (!p.hasPermission("necromanager.delhome")) { MessageManager.sendRaw(p, "&cNo permission."); return true; }
        if (a.length != 1) { MessageManager.sendRaw(p, "&eUsage: /delhome <name>"); return true; }
        MessageManager.sendRaw(p, HomeManager.delete(p.getUniqueId(), a[0]) ? "&aHome deleted." : "&cHome not found.");
        return true;
    }
    public List<String> onTabComplete(CommandSender s, Command c, String l, String[] a) {
        if (!(s instanceof Player p) || a.length != 1) return List.of();
        return HomeManager.list(p.getUniqueId()).stream().filter(n -> n.startsWith(a[0].toLowerCase())).toList();
    }
}
