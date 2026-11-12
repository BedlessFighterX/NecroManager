package com.bedlessfighter.necroManager.commands;

import com.bedlessfighter.necroManager.managers.*;
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
        boolean all=label.equalsIgnoreCase("sellall") || (a.length==1&&a[0].equalsIgnoreCase("all"));
        if(a.length>1 || (a.length==1&&!a[0].equalsIgnoreCase("all")&&!a[0].equalsIgnoreCase("hand"))){MessageManager.sendRaw(s,"&cUsage: /sell [hand|all]");return true;}
        if(a.length==1&&a[0].equalsIgnoreCase("hand"))all=false;
        ItemStack[] storage=p.getInventory().getStorageContents();
        double total=0; int count=0;
        if(all) {
            for(ItemStack item:storage) {
                if(item==null||item.getType().isAir())continue;
                double worth=EconomyWorthManager.get(item.getType());
                if(worth>0){total+=worth*item.getAmount();count+=item.getAmount();}
            }
        } else {
            ItemStack hand=p.getInventory().getItemInMainHand();
            if(hand.getType().isAir()){MessageManager.sendRaw(s,"&cHold an item to sell.");return true;}
            double worth=EconomyWorthManager.get(hand.getType());
            if(worth<=0){MessageManager.sendRaw(s,"&cThat item has no sell value.");return true;}
            count=hand.getAmount();total=worth*count;
        }
        if(count==0||!Double.isFinite(total)||total<=0){MessageManager.sendRaw(s,"&cNo sellable items were found.");return true;}
        if(!EconomyManager.deposit(p,total,"sell","sold "+count+" items")){MessageManager.sendRaw(s,"&cSale rejected by your balance limit; no items were removed.");return true;}
        if(all) {
            for(int i=0;i<storage.length;i++) {
                ItemStack item=storage[i];
                if(item!=null&&!item.getType().isAir()&&EconomyWorthManager.get(item.getType())>0)storage[i]=null;
            }
            p.getInventory().setStorageContents(storage);
        } else p.getInventory().setItemInMainHand(null);
        MessageManager.sendRaw(s,"&aSold &e"+count+" &aitem(s) for &f"+EconomyFormat.format(total)+"&a.");
        return true;
    }
    @Override public List<String> onTabComplete(@NotNull CommandSender s,@NotNull Command c,@NotNull String l,@NotNull String[] a) {
        if(a.length!=1)return List.of();
        return List.of("hand","all").stream().filter(v->v.startsWith(a[0].toLowerCase(Locale.ROOT))).toList();
    }
}