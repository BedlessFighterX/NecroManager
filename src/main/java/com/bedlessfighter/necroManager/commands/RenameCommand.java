package com.bedlessfighter.necroManager.commands;
import com.bedlessfighter.necroManager.PermissionManager; import com.bedlessfighter.necroManager.MessageManager;
import org.bukkit.command.*; import org.bukkit.entity.Player; import org.bukkit.inventory.ItemStack; import org.bukkit.inventory.meta.ItemMeta;
public final class RenameCommand implements CommandExecutor {
 public boolean onCommand(CommandSender s,Command c,String l,String[] a){
  if(!(s instanceof Player p)){MessageManager.sendRaw(s,"&cPlayers only.");return true;}if(!PermissionManager.has(s,"necromanager.rename")){MessageManager.sendRaw(s,"&cYou do not have permission.");return true;}
  if(a.length==0){MessageManager.sendRaw(s,"&cUsage: /rename <name>");return true;}ItemStack item=p.getInventory().getItemInMainHand();if(item.getType().isAir()){MessageManager.sendRaw(p,"&cHold an item first.");return true;}
  ItemMeta meta=item.getItemMeta();meta.setDisplayName(MessageManager.color(String.join(" ",a)));item.setItemMeta(meta);MessageManager.sendRaw(p,"&aRenamed held item.");return true;
 }
}