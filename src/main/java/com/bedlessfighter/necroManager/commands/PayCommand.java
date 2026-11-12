package com.bedlessfighter.necroManager.commands;

import com.bedlessfighter.necroManager.managers.*;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.Locale;

public final class PayCommand implements CommandExecutor, TabCompleter {
    public boolean onCommand(@NotNull CommandSender sender,@NotNull Command command,@NotNull String label,@NotNull String[] args) {
        if (!(sender instanceof Player player)) { MessageManager.sendRaw(sender,"&cOnly players can pay other players."); return true; }
        if (args.length != 2) { MessageManager.sendRaw(sender,"&cUsage: /pay <player> <amount>"); return true; }
        Player target=Bukkit.getPlayerExact(args[0]);
        if(target==null || target.equals(player)){MessageManager.sendRaw(sender,"&cChoose another online player.");return true;}
        if(target.hasPermission("necromanager.economy.pay.disabled")){MessageManager.sendRaw(sender,"&cThat player is not accepting payments.");return true;}
        double amount;
        try { amount=Double.parseDouble(args[1]); } catch(NumberFormatException ex){MessageManager.sendRaw(sender,"&cEnter a valid amount.");return true;}
        if(!Double.isFinite(amount)||amount<=0){MessageManager.sendRaw(sender,"&cAmount must be greater than zero.");return true;}
        double balance=EconomyManager.balance(player);
        if(balance<amount){MessageManager.sendRaw(sender,"&cYou cannot afford that payment.");return true;}
        if(EconomyManager.balance(target)+amount>ConfigManager.getDouble("economy.maximum-balance",1.0E9,0,1.0E15)){MessageManager.sendRaw(sender,"&cThat payment would exceed the recipient's balance limit.");return true;}
        if(!EconomyManager.withdraw(player,amount,player.getName(),"pay to "+target.getUniqueId())){MessageManager.sendRaw(sender,"&cPayment could not be processed.");return true;}
        if(!EconomyManager.deposit(target,amount,player.getName(),"pay from "+player.getUniqueId())){
            EconomyManager.deposit(player,amount,"system","payment rollback");MessageManager.sendRaw(sender,"&cPayment failed and was refunded.");return true;
        }
        MessageManager.sendRaw(player,"&aSent &f"+EconomyFormat.format(amount)+" &ato "+target.getName()+".");
        MessageManager.sendRaw(target,"&aReceived &f"+EconomyFormat.format(amount)+" &afrom "+player.getName()+".");
        return true;
    }
    public List<String> onTabComplete(@NotNull CommandSender s,@NotNull Command c,@NotNull String a,@NotNull String[] args) {
        return args.length==1?Bukkit.getOnlinePlayers().stream().map(Player::getName).filter(n->!n.equalsIgnoreCase(s.getName())&&n.toLowerCase(Locale.ROOT).startsWith(args[0].toLowerCase(Locale.ROOT))).toList():List.of();
    }
}
