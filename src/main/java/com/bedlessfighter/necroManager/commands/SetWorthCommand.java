package com.bedlessfighter.necroManager.commands;

import com.bedlessfighter.necroManager.managers.*;
import org.bukkit.Material;
import org.bukkit.command.*;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.Locale;
import java.util.Arrays;

public final class SetWorthCommand implements CommandExecutor, TabCompleter {
    @Override public boolean onCommand(@NotNull CommandSender s,@NotNull Command c,@NotNull String l,@NotNull String[] a) {
        if(!PermissionManager.has(s,"necromanager.economy.worth.admin")){MessageManager.sendRaw(s,"&cYou do not have permission.");return true;}
        if(a.length!=2){MessageManager.sendRaw(s,"&cUsage: /setworth <material> <price> (0 removes value)");return true;}
        Material m=EconomyWorthManager.match(a[0]); double price;
        try{price=Double.parseDouble(a[1]);}catch(NumberFormatException ex){MessageManager.sendRaw(s,"&cEnter a valid price.");return true;}
        if(m==null||m.isAir()||!Double.isFinite(price)||price<0||price>1.0E12){MessageManager.sendRaw(s,"&cInvalid material or price.");return true;}
        EconomyWorthManager.set(m,price);
        MessageManager.sendRaw(s,price==0?"&aRemoved the sell value for &e"+m.name()+"&a.":"&aSet &e"+m.name()+" &ato &f"+EconomyFormat.format(price)+" &aper item.");
        return true;
    }
    @Override public List<String> onTabComplete(@NotNull CommandSender s,@NotNull Command c,@NotNull String l,@NotNull String[] a) {
        if(!PermissionManager.has(s,"necromanager.economy.worth.admin")||a.length!=1)return List.of();
        String p=a[0].toLowerCase(Locale.ROOT);
        return Arrays.stream(Material.values()).filter(m->!m.isAir()&&m.name().toLowerCase(Locale.ROOT).startsWith(p)).limit(80).map(Enum::name).toList();
    }
}