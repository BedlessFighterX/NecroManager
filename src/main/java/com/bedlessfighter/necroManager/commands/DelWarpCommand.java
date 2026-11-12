package com.bedlessfighter.necroManager.commands;

import com.bedlessfighter.necroManager.MessageManager;
import com.bedlessfighter.necroManager.managers.WarpManager;
import org.bukkit.command.*;
import java.util.List;

public final class DelWarpCommand implements CommandExecutor, TabCompleter {
    public boolean onCommand(CommandSender s, Command c, String l, String[] a) {
        if (!s.hasPermission("necromanager.warp.delete")) { MessageManager.sendRaw(s, "&cNo permission."); return true; }
        if (a.length != 1) { MessageManager.sendRaw(s, "&eUsage: /delwarp <name>"); return true; }
        MessageManager.sendRaw(s, WarpManager.delete(a[0]) ? "&aWarp deleted." : "&cWarp not found.");
        return true;
    }
    public List<String> onTabComplete(CommandSender s, Command c, String l, String[] a) {
        if (a.length != 1) return List.of();
        return WarpManager.list().stream().filter(n -> n.startsWith(a[0].toLowerCase())).toList();
    }
}
