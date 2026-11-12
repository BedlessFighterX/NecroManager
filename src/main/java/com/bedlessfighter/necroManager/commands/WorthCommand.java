package com.bedlessfighter.necroManager.commands;

import com.bedlessfighter.necroManager.managers.*;
import org.bukkit.Material;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.Locale;

public final class WorthCommand implements CommandExecutor, TabCompleter {
    @Override public boolean onCommand(@NotNull CommandSender sender,@NotNull Command command,@NotNull String label,@NotNull String[] args) {
        Material material;
        if(args.length==0) {
            if(!(sender instanceof Player p)){MessageManager.sendRaw(sender,"&cUsage: /worth <material>");return true;}
            material=p.getInventory().getItemInMainHand().getType();
        } else if(args.length==1) material=EconomyWorthManager.match(args[0]);
        else {MessageManager.sendRaw(sender,"&cUsage: /worth [material]");return true;}
        if(material==null || material.isAir()){MessageManager.sendRaw(sender,"&cSpecify a valid item material.");return true;}
        double value=EconomyWorthManager.get(material);
        MessageManager.sendRaw(sender,value<=0?"&e"+material.name()+" &7has no sell value.":"&e"+material.name()+" &7is worth &a"+EconomyFormat.format(value)+" &7per item.");
        return true;
    }
    @Override public List<String> onTabComplete(@NotNull CommandSender s,@NotNull Command c,@NotNull String a,@NotNull String[] args) {
        if(args.length!=1)return List.of();
        String prefix=args[0].toLowerCase(Locale.ROOT);
        return java.util.Arrays.stream(Material.values()).filter(m->!m.isAir()&&m.name().toLowerCase(Locale.ROOT).startsWith(prefix)).limit(80).map(Enum::name).toList();
    }
}