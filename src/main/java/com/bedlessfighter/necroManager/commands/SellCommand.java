package com.bedlessfighter.necroManager.commands;

import com.bedlessfighter.necroManager.managers.*;
import org.bukkit.Material;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.Locale;

public final class SellCommand implements CommandExecutor, TabCompleter {
    @Override public boolean onCommand(@NotNull CommandSender s,@NotNull Command c,@NotNull String label,@NotNull String[] a) {
        if(!(s instanceof Player p)){MessageManager.sendRaw(s,"&cOnly players can sell items.");return true;}
        if(!PermissionManager.has(s,"necromanager.economy.sell")){MessageManager.sendRaw(s,"&cYou do not have permission.");return true;}
        boolean all=label.equalsIgnoreCase("sellall") || c.getName().equalsIgnoreCase("sellall") || (a.length==1&&a[0].equalsIgnoreCase("all"));
        if(a.length>(all&&a.length==1?1:0) || (a.length==1&&!a[0].equalsIgnoreCase("all")&&!a[0].equalsIgnoreCase("hand"))){MessageManager.sendRaw(s,"&cUsage: /sell [hand|all]");return true;}
        if(!all && (a.length==1&&a[0].equalsIgnoreCase("all"))) all=true;
        ItemStack hand=p.getInventory().getItemInMainHand();
        double total=0; int count=0;
        if(all) {
            ItemStack[] contents=p.getInventory().getStorageContents();
            for(int i=0;i<contents.length;i++) {
                ItemStack item=contents[i]; if(item==null||item.getType().isAir())continue;
                double worth=EconomyWorthManager.get(item.getType()); if(worth<=0)continue;
                total+=worth*item.getAmount(); count+=item.getAmount(); contents[i]=null;
            }
            p.getInventory().setStorageContents(contents);
        } else {
            if(hand.getType().isAir()){MessageManager.sendRaw(s,"&cHold an item to sell.");return true;}
            double worth=EconomyWorthManager.get(hand.getType());
            if(worth<=0){MessageManager.sendRaw(s,"&cThat item has no sell value.");return true;}
            count=hand.getAmount(); total=worth*count; p.getInventory().setItemInMainHand(null);
        }
        if(count==0){MessageManager.sendRaw(s,"&cNo sellable items were found.");return true;}
        if(!Double.isFinite(total)||total<=0||!EconomyManager.deposit(p,total,"sell","sold "+count+" items")) {
            MessageManager.sendRaw(s,"&cSale rejected by your balance limit."); return true;
        }
        MessageManager.sendRaw(s,"&aSold &e"+count+" &aitem(s) for &f"+EconomyFormat.format(total)+"&a.");
        return true;
    }
    @Override public List<String> onTabComplete(@NotNull CommandSender s,@NotNull Command c,@NotNull String l,@NotNull String[] a) {
        if(a.length!=1)return List.of();
        return List.of("hand","all").stream().filter(v->v.startsWith(a[0].toLowerCase(Locale.ROOT))).toList();
    }
}