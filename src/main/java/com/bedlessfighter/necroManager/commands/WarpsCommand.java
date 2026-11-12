package com.bedlessfighter.necroManager.commands;

import com.bedlessfighter.necroManager.MessageManager;
import com.bedlessfighter.necroManager.managers.WarpManager;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import java.util.List;
import java.util.stream.Collectors;

public final class WarpsCommand implements CommandExecutor, TabCompleter {
    public boolean onCommand(CommandSender s, Command c, String l, String[] a) {
        if (!s.hasPermission("necromanager.warps")) { MessageManager.sendRaw(s, "&cNo permission."); return true; }
        List<String> names = WarpManager.list().stream()
                .filter(n -> !(s instanceof Player p) || p.hasPermission("necromanager.warp." + n)).toList();
        MessageManager.sendRaw(s, "&aWarps (&e" + names.size() + "&a): &f" + (names.isEmpty() ? "none" : String.join(", ", names)));
        return true;
    }
    public List<String> onTabComplete(CommandSender s, Command c, String l, String[] a) { return List.of(); }
}
