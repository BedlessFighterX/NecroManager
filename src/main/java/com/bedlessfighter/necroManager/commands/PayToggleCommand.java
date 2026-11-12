package com.bedlessfighter.necroManager.commands;

import com.bedlessfighter.necroManager.managers.EconomyManager;
import com.bedlessfighter.necroManager.managers.MessageManager;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public final class PayToggleCommand implements CommandExecutor, TabCompleter {
    public boolean onCommand(@NotNull CommandSender sender,@NotNull Command command,@NotNull String label,@NotNull String[] args) {
        if(!(sender instanceof Player player)){MessageManager.sendRaw(sender,"&cOnly players can use this command.");return true;}
        if(args.length!=0){MessageManager.sendRaw(sender,"&cUsage: /paytoggle");return true;}
        boolean disabled=EconomyManager.togglePayments(player.getUniqueId());
        MessageManager.sendRaw(player,disabled?"&cYou are no longer accepting payments.":"&aYou are now accepting payments.");
        return true;
    }
    public List<String> onTabComplete(@NotNull CommandSender s,@NotNull Command c,@NotNull String a,@NotNull String[] args){return List.of();}
}
