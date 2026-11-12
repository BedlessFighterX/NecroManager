package com.bedlessfighter.necroManager.commands;

import com.bedlessfighter.necroManager.MessageManager;
import com.bedlessfighter.necroManager.managers.HomeManager;
import org.bukkit.Location;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import java.util.List;

public final class HomeCommand implements CommandExecutor, TabCompleter {
    public boolean onCommand(CommandSender s, Command c, String l, String[] a) {
        if (!(s instanceof Player p)) { MessageManager.sendRaw(s, "&cPlayers only."); return true; }
        if (!p.hasPermission("necromanager.home")) { MessageManager.sendRaw(s, "&cNo permission."); return true; }
        if (a.length > 1) { MessageManager.sendRaw(p, "&eUsage: /home [name]"); return true; }
        String name = a.length == 0 ? "home" : a[0];
        Location loc = HomeManager.get(p.getUniqueId(), name);
        if (loc == null) { MessageManager.sendRaw(p, "&cHome not found or its world is unavailable."); return true; }
        if (!loc.getBlock().isPassable() || !loc.clone().add(0, 1, 0).getBlock().isPassable()
                || !loc.clone().subtract(0, 1, 0).getBlock().getType().isSolid()) {
            MessageManager.sendRaw(p, "&cThat home location is obstructed or unsafe."); return true;
        }
        if (!p.teleport(loc)) MessageManager.sendRaw(p, "&cTeleport was blocked.");
        else MessageManager.sendRaw(p, "&aTeleported to home &e" + name.toLowerCase() + "&a.");
        return true;
    }
    public List<String> onTabComplete(CommandSender s, Command c, String l, String[] a) {
        if (!(s instanceof Player p) || a.length != 1) return List.of();
        return HomeManager.list(p.getUniqueId()).stream().filter(n -> n.startsWith(a[0].toLowerCase())).toList();
    }
}
