package com.bedlessfighter.necroManager.commands;

import com.bedlessfighter.necroManager.managers.*;
import org.bukkit.command.*;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public final class BaltopCommand implements CommandExecutor, TabCompleter {
    public boolean onCommand(@NotNull CommandSender sender,@NotNull Command command,@NotNull String label,@NotNull String[] args) {
        if(args.length!=0){MessageManager.sendRaw(sender,"&cUsage: /baltop");return true;}
        List<EconomyManager.Account> top=EconomyManager.top(ConfigManager.getInt("economy.top-size",10,1,50));
        MessageManager.sendRaw(sender,"&6&lTop Balances");
        if(top.isEmpty()){MessageManager.sendRaw(sender,"&7No economy accounts yet.");return true;}
        for(int i=0;i<top.size();i++){var account=top.get(i);MessageManager.sendRaw(sender,"&e"+(i+1)+". &f"+account.name()+" &7- &a"+EconomyFormat.format(account.balance()));}
        return true;
    }
    public List<String> onTabComplete(@NotNull CommandSender s,@NotNull Command c,@NotNull String a,@NotNull String[] args){return List.of();}
}
