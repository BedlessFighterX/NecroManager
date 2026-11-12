package com.bedlessfighter.necroManager.commands;

import com.bedlessfighter.necroManager.MessageManager;
import com.bedlessfighter.necroManager.managers.HomeManager;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import java.util.List;

public final class HomesCommand implements CommandExecutor, TabCompleter {
    public boolean onCommand(CommandSender s, Command c, String l, String[] a) {
        if (!(s instanceof Player p)) { MessageManager.sendRaw(s, "&cPlayers only."); return true; }
        List<String> homes = HomeManager.list(p.getUniqueId());
        MessageManager.sendRaw(p, "&aHomes (&e" + homes.size() + "/" + (HomeManager.limit(p) == Integer.MAX_VALUE ? "∞" : HomeManager.limit(p)) + "&a): &f" + (homes.isEmpty() ? "none" : String.join(", ", homes)));
        return true;
    }
    public List<String> onTabComplete(CommandSender s, Command c, String l, String[] a) { return List.of(); }
}
