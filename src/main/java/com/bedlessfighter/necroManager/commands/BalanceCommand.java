package com.bedlessfighter.necroManager.commands;

import com.bedlessfighter.necroManager.managers.ConfigManager;
import com.bedlessfighter.necroManager.managers.EconomyManager;
import com.bedlessfighter.necroManager.managers.EconomyFormat;
import com.bedlessfighter.necroManager.managers.MessageManager;
import com.bedlessfighter.necroManager.managers.PermissionManager;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.Locale;

public final class BalanceCommand implements CommandExecutor, TabCompleter {
    public boolean onCommand(@NotNull CommandSender sender,@NotNull Command command,@NotNull String label,@NotNull String[] args) {
        if (args.length > 1 || (args.length == 1 && !PermissionManager.has(sender,"necromanager.economy.balance.others"))) {
            MessageManager.sendRaw(sender,"&cUsage: /balance [player]"); return true;
        }
        if (args.length == 0 && !(sender instanceof Player)) { MessageManager.sendRaw(sender,"&cConsole must specify a player."); return true; }
        Player target = args.length == 0 ? (Player)sender : Bukkit.getPlayerExact(args[0]);
        if (target == null) { MessageManager.sendRaw(sender,"&cThat player is not online."); return true; }
        String amount = EconomyFormat.format(EconomyManager.balance(target));
        MessageManager.sendRaw(sender,"&a"+target.getName()+"'s balance: &f"+amount);
        return true;
    }
    public List<String> onTabComplete(@NotNull CommandSender s,@NotNull Command c,@NotNull String a,@NotNull String[] args) {
        return args.length==1 && PermissionManager.has(s,"necromanager.economy.balance.others") ? Bukkit.getOnlinePlayers().stream().map(Player::getName).filter(n->n.toLowerCase(Locale.ROOT).startsWith(args[0].toLowerCase(Locale.ROOT))).toList() : List.of();
    }
}
