package com.bedlessfighter.necroManager.commands;

import com.bedlessfighter.necroManager.MessageManager;
import com.bedlessfighter.necroManager.managers.WarpManager;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import java.util.List;

public final class SetWarpCommand implements CommandExecutor, TabCompleter {
    public boolean onCommand(CommandSender s, Command c, String l, String[] a) {
        if (!(s instanceof Player p)) { MessageManager.sendRaw(s, "&cPlayers only."); return true; }
        if (!p.hasPermission("necromanager.warp.set")) { MessageManager.sendRaw(p, "&cNo permission."); return true; }
        if (a.length != 1) { MessageManager.sendRaw(p, "&eUsage: /setwarp <name>"); return true; }
        if (!WarpManager.set(a[0], p.getLocation())) { MessageManager.sendRaw(p, "&cInvalid warp name."); return true; }
        MessageManager.sendRaw(p, "&aWarp &e" + a[0].toLowerCase() + " &asaved.");
        return true;
    }
    public List<String> onTabComplete(CommandSender s, Command c, String l, String[] a) { return List.of(); }
}
