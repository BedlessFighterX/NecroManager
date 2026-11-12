package com.bedlessfighter.necroManager.commands;

import com.bedlessfighter.necroManager.MessageManager;
import com.bedlessfighter.necroManager.managers.WarpManager;
import org.bukkit.Location;
import org.bukkit.command.*;
import java.util.List;

public final class WarpInfoCommand implements CommandExecutor, TabCompleter {
    public boolean onCommand(CommandSender s, Command c, String l, String[] a) {
        if (!s.hasPermission("necromanager.warp.info")) { MessageManager.sendRaw(s, "&cNo permission."); return true; }
        if (a.length != 1) { MessageManager.sendRaw(s, "&eUsage: /warpinfo <name>"); return true; }
        Location loc = WarpManager.get(a[0]);
        if (loc == null) { MessageManager.sendRaw(s, "&cWarp not found or its world is unavailable."); return true; }
        MessageManager.sendRaw(s, "&aWarp &e" + a[0].toLowerCase() + "&a: &f" + loc.getWorld().getName()
                + " &7(" + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ() + ")");
        return true;
    }
    public List<String> onTabComplete(CommandSender s, Command c, String l, String[] a) {
        if (a.length != 1) return List.of();
        return WarpManager.list().stream().filter(n -> n.startsWith(a[0].toLowerCase())).toList();
    }
}
