package com.bedlessfighter.necroManager.commands;
import com.bedlessfighter.necroManager.PermissionManager; import com.bedlessfighter.necroManager.MessageManager;
import org.bukkit.command.*; import org.bukkit.entity.Player; import org.bukkit.inventory.ItemStack;
public final class ItemDurabilityCommand implements CommandExecutor {
 public boolean onCommand(CommandSender s,Command c,String l,String[] a){
  if(!(s instanceof Player p)){MessageManager.sendRaw(s,"&cPlayers only.");return true;}
  if(!PermissionManager.has(s,"necromanager.itemdurability")){MessageManager.send(s,"messages.no-permission","&cYou do not have permission.");return true;}
  ItemStack i=p.getInventory().getItemInMainHand();if(i.getType().isAir()||i.getType().getMaxDurability()<=0){MessageManager.sendRaw(s,"&cHold a damageable item.");return true;}
  if(a.length==0){MessageManager.sendRaw(s,"&eUsage: /itemdurability <0-"+i.getType().getMaxDurability()+">");return true;}
  int d;try{d=Integer.parseInt(a[0]);}catch(NumberFormatException ex){MessageManager.sendRaw(s,"&cDurability must be a number.");return true;}
  if(d<0||d>i.getType().getMaxDurability()){MessageManager.sendRaw(s,"&cDurability must be between 0 and "+i.getType().getMaxDurability()+".");return true;}
  i.setDurability((short)d);MessageManager.sendRaw(s,"&aSet item damage to &e"+d+"&a.");return true;
 }
}