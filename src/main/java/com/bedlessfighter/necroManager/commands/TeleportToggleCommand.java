package com.bedlessfighter.necroManager.commands;

import com.bedlessfighter.necroManager.MessageManager;
import com.bedlessfighter.necroManager.managers.TeleportRequestManager;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import java.util.List;

public final class TeleportToggleCommand implements CommandExecutor, TabCompleter {
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) { MessageManager.sendRaw(sender, "&cPlayers only."); return true; }
        if (!player.hasPermission("necromanager.tptoggle")) { MessageManager.sendRaw(sender, "&cYou do not have permission."); return true; }
        if (args.length != 0) { MessageManager.sendRaw(sender, "&eUsage: /tptoggle"); return true; }
        boolean enabled = TeleportRequestManager.toggle(player);
        if (enabled) MessageManager.sendRaw(sender, "&aTeleport requests are now enabled.");
        return true;
    }
    public List<String> onTabComplete(CommandSender s, Command c, String a, String[] args) { return List.of(); }
}
