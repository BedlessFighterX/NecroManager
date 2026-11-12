package com.bedlessfighter.necroManager.commands;

import com.bedlessfighter.necroManager.MessageManager;
import com.bedlessfighter.necroManager.managers.WarpManager;
import org.bukkit.Location;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import java.util.List;

public final class WarpCommand implements CommandExecutor, TabCompleter {
    public boolean onCommand(CommandSender s, Command c, String l, String[] a) {
        if (!(s instanceof Player p)) { MessageManager.sendRaw(s, "&cPlayers only."); return true; }
        if (!p.hasPermission("necromanager.warp")) { MessageManager.sendRaw(p, "&cNo permission."); return true; }
        if (a.length != 1) { MessageManager.sendRaw(p, "&eUsage: /warp <name>"); return true; }
        String name = a[0].toLowerCase();
        if (!p.hasPermission("necromanager.warp." + name)) { MessageManager.sendRaw(p, "&cYou cannot use that warp."); return true; }
        Location loc = WarpManager.get(name);
        if (loc == null) { MessageManager.sendRaw(p, "&cWarp not found or its world is unavailable."); return true; }
        if (!loc.getBlock().isPassable() || !loc.clone().add(0, 1, 0).getBlock().isPassable()
                || !loc.clone().subtract(0, 1, 0).getBlock().getType().isSolid()) {
            MessageManager.sendRaw(p, "&cThat warp location is obstructed or unsafe."); return true;
        }
        if (!p.teleport(loc)) MessageManager.sendRaw(p, "&cTeleport was blocked.");
        else MessageManager.sendRaw(p, "&aTeleported to warp &e" + name + "&a.");
        return true;
    }
    public List<String> onTabComplete(CommandSender s, Command c, String l, String[] a) {
        if (!(s instanceof Player p) || a.length != 1) return List.of();
        return WarpManager.list().stream().filter(n -> n.startsWith(a[0].toLowerCase()))
                .filter(n -> p.hasPermission("necromanager.warp." + n)).toList();
    }
}
