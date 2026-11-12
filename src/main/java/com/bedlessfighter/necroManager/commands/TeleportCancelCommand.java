package com.bedlessfighter.necroManager.commands;

import com.bedlessfighter.necroManager.MessageManager;
import com.bedlessfighter.necroManager.managers.TeleportRequestManager;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import java.util.List;

public final class TeleportCancelCommand implements CommandExecutor, TabCompleter {
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) { MessageManager.sendRaw(sender, "&cPlayers only."); return true; }
        if (!player.hasPermission("necromanager.tpcancel")) { MessageManager.sendRaw(sender, "&cYou do not have permission."); return true; }
        if (args.length != 0) { MessageManager.sendRaw(sender, "&eUsage: /tpcancel"); return true; }
        if (!TeleportRequestManager.cancel(player)) MessageManager.sendRaw(sender, "&cYou have no outgoing teleport request.");
        return true;
    }
    public List<String> onTabComplete(CommandSender s, Command c, String a, String[] args) { return List.of(); }
}
