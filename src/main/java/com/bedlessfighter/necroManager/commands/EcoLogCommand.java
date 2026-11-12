package com.bedlessfighter.necroManager.commands;

import com.bedlessfighter.necroManager.managers.*;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.Locale;

public final class EcoLogCommand implements CommandExecutor, TabCompleter {
    @Override public boolean onCommand(@NotNull CommandSender s,@NotNull Command c,@NotNull String l,@NotNull String[] a) {
        if(!PermissionManager.has(s,"necromanager.economy.admin")){MessageManager.sendRaw(s,"&cYou do not have permission.");return true;}
        if(a.length!=1){MessageManager.sendRaw(s,"&cUsage: /ecolog <online-player>");return true;}
        Player p=Bukkit.getPlayerExact(a[0]);
        if(p==null){MessageManager.sendRaw(s,"&cPlayer must be online to inspect their account history.");return true;}
        List<String> entries=EconomyManager.history(p.getUniqueId(),10);
        MessageManager.sendRaw(s,"&6&lRecent economy transactions: &e"+p.getName());
        if(entries.isEmpty())MessageManager.sendRaw(s,"&7No recorded transactions.");
        else for(String entry:entries)MessageManager.sendRaw(s,"&7"+entry.replace("|"," &8» &f"));
        return true;
    }
    @Override public List<String> onTabComplete(@NotNull CommandSender s,@NotNull Command c,@NotNull String l,@NotNull String[] a) {
        if(!PermissionManager.has(s,"necromanager.economy.admin")||a.length!=1)return List.of();
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).filter(n->n.toLowerCase(Locale.ROOT).startsWith(a[0].toLowerCase(Locale.ROOT))).toList();
    }
}