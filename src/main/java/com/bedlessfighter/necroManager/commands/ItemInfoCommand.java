package com.bedlessfighter.necroManager.commands;
import com.bedlessfighter.necroManager.PermissionManager; import com.bedlessfighter.necroManager.MessageManager;
import org.bukkit.command.*; import org.bukkit.entity.Player; import org.bukkit.inventory.ItemStack; import org.bukkit.inventory.meta.ItemMeta;
public final class ItemInfoCommand implements CommandExecutor {
 public boolean onCommand(CommandSender s,Command c,String l,String[] a){
  if(!(s instanceof Player p)){MessageManager.sendRaw(s,"&cPlayers only.");return true;}
  if(!PermissionManager.has(s,"necromanager.iteminfo")){MessageManager.send(s,"messages.no-permission","&cYou do not have permission.");return true;}
  ItemStack i=p.getInventory().getItemInMainHand();if(i.getType().isAir()){MessageManager.sendRaw(s,"&cHold an item first.");return true;}
  ItemMeta m=i.getItemMeta();MessageManager.sendRaw(s,"&6Item: &e"+i.getType()+" &7x"+i.getAmount());
  MessageManager.sendRaw(s,"&6Durability: &e"+(i.getType().getMaxDurability()>0?(i.getType().getMaxDurability()-i.getDurability())+"/"+i.getType().getMaxDurability():"N/A"));
  if(m!=null&&m.hasDisplayName())MessageManager.sendRaw(s,"&6Name: &f"+m.getDisplayName());if(m!=null&&m.hasLore())for(String lore:m.getLore())MessageManager.sendRaw(s,"&6Lore: &f"+lore);return true;
 }
}