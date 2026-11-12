package com.bedlessfighter.necroManager.commands;

import com.bedlessfighter.necroManager.MessageManager;
import com.bedlessfighter.necroManager.managers.HomeManager;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import java.util.List;

public final class RenameHomeCommand implements CommandExecutor, TabCompleter {
    public boolean onCommand(CommandSender s, Command c, String l, String[] a) {
        if (!(s instanceof Player p)) { MessageManager.sendRaw(s, "&cPlayers only."); return true; }
        if (!p.hasPermission("necromanager.renamehome")) { MessageManager.sendRaw(p, "&cNo permission."); return true; }
        if (a.length != 2) { MessageManager.sendRaw(p, "&eUsage: /renamehome <old> <new>"); return true; }
        MessageManager.sendRaw(p, HomeManager.rename(p.getUniqueId(), a[0], a[1]) ? "&aHome renamed." : "&cCould not rename home (check names).");
        return true;
    }
    public List<String> onTabComplete(CommandSender s, Command c, String l, String[] a) {
        if (!(s instanceof Player p) || a.length != 1) return List.of();
        return HomeManager.list(p.getUniqueId()).stream().filter(n -> n.startsWith(a[0].toLowerCase())).toList();
    }
}
