package com.bedlessfighter.necroManager.commands;
import com.bedlessfighter.necroManager.PermissionManager; import com.bedlessfighter.necroManager.MessageManager;
import org.bukkit.command.*; import org.bukkit.entity.Player; import org.bukkit.inventory.ItemStack;
public final class DropCommand implements CommandExecutor {
 public boolean onCommand(CommandSender s,Command c,String l,String[] a){
  if(!(s instanceof Player p)){MessageManager.sendRaw(s,"&cPlayers only.");return true;}
  if(!PermissionManager.has(s,"necromanager.drop")){MessageManager.send(s,"messages.no-permission","&cYou do not have permission.");return true;}
  ItemStack item=p.getInventory().getItemInMainHand(); if(item.getType().isAir()){MessageManager.sendRaw(s,"&cHold an item first.");return true;}
  p.getWorld().dropItemNaturally(p.getLocation(),item.clone());p.getInventory().setItemInMainHand(null);MessageManager.sendRaw(s,"&aDropped your held item.");return true;
 }
}