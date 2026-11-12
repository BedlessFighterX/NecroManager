package com.bedlessfighter.necroManager.commands;

import com.bedlessfighter.necroManager.MessageManager;
import com.bedlessfighter.necroManager.managers.HomeManager;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import java.util.List;

public final class SetHomeCommand implements CommandExecutor, TabCompleter {
    public boolean onCommand(CommandSender s, Command c, String l, String[] a) {
        if (!(s instanceof Player p)) { MessageManager.sendRaw(s, "&cPlayers only."); return true; }
        if (!p.hasPermission("necromanager.sethome")) { MessageManager.sendRaw(s, "&cNo permission."); return true; }
        if (a.length > 1) { MessageManager.sendRaw(s, "&eUsage: /sethome [name]"); return true; }
        String name = a.length == 0 ? "home" : a[0];
        if (!HomeManager.set(p, name)) { MessageManager.sendRaw(p, "&cInvalid name or home limit reached."); return true; }
        MessageManager.sendRaw(p, "&aHome &e" + name.toLowerCase() + " &asaved.");
        return true;
    }
    public List<String> onTabComplete(CommandSender s, Command c, String l, String[] a) { return List.of(); }
}
