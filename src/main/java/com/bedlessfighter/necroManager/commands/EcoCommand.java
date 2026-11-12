package com.bedlessfighter.necroManager.commands;

import com.bedlessfighter.necroManager.managers.*;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.Locale;

public final class EcoCommand implements CommandExecutor, TabCompleter {
    public boolean onCommand(@NotNull CommandSender sender,@NotNull Command command,@NotNull String label,@NotNull String[] args) {
        if(!PermissionManager.has(sender,"necromanager.economy.admin")){MessageManager.sendRaw(sender,"&cYou do not have permission.");return true;}
        if(args.length!=3 || !List.of("give","take","set").contains(args[0].toLowerCase(Locale.ROOT))){MessageManager.sendRaw(sender,"&cUsage: /eco <give|take|set> <player> <amount>");return true;}
        Player target=Bukkit.getPlayerExact(args[1]);
        if(target==null){MessageManager.sendRaw(sender,"&cThat player must be online.");return true;}
        double amount;
        try{amount=Double.parseDouble(args[2]);}catch(NumberFormatException ex){MessageManager.sendRaw(sender,"&cEnter a valid amount.");return true;}
        if(!Double.isFinite(amount)||amount<0){MessageManager.sendRaw(sender,"&cAmount must be a non-negative number.");return true;}
        boolean success=switch(args[0].toLowerCase(Locale.ROOT)){
            case "give" -> amount>0 && EconomyManager.deposit(target,amount,sender.getName(),"admin give");
            case "take" -> amount>0 && EconomyManager.withdraw(target,amount,sender.getName(),"admin take");
            case "set" -> EconomyManager.set(target,amount,sender.getName(),"admin set");
            default -> false;
        };
        if(!success){MessageManager.sendRaw(sender,"&cTransaction rejected by balance limits or insufficient funds.");return true;}
        MessageManager.sendRaw(sender,"&aUpdated "+target.getName()+"'s balance to &f"+EconomyFormat.format(EconomyManager.balance(target))+"&a.");
        return true;
    }
    public List<String> onTabComplete(@NotNull CommandSender s,@NotNull Command c,@NotNull String a,@NotNull String[] args) {
        if(!PermissionManager.has(s,"necromanager.economy.admin"))return List.of();
        if(args.length==1)return List.of("give","take","set").stream().filter(v->v.startsWith(args[0].toLowerCase(Locale.ROOT))).toList();
        if(args.length==2)return Bukkit.getOnlinePlayers().stream().map(Player::getName).filter(v->v.toLowerCase(Locale.ROOT).startsWith(args[1].toLowerCase(Locale.ROOT))).toList();
        return List.of();
    }
}
