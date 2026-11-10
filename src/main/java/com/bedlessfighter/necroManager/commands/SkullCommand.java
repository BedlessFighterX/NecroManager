package com.bedlessfighter.necroManager.commands;
import com.bedlessfighter.necroManager.PermissionManager; import com.bedlessfighter.necroManager.MessageManager;
import org.bukkit.Material; import org.bukkit.command.*; import org.bukkit.entity.Player; import org.bukkit.inventory.ItemStack; import org.bukkit.inventory.meta.SkullMeta;
public final class SkullCommand implements CommandExecutor {
 public boolean onCommand(CommandSender s,Command c,String l,String[] a){
  if(!(s instanceof Player p)){MessageManager.sendRaw(s,"&cPlayers only.");return true;}if(!PermissionManager.has(s,"necromanager.skull")){MessageManager.sendRaw(s,"&cYou do not have permission.");return true;}
  if(a.length!=1){MessageManager.sendRaw(s,"&cUsage: /skull <online-player>");return true;}Player target=p.getServer().getPlayerExact(a[0]);if(target==null){MessageManager.sendRaw(s,"&cPlayer must be online.");return true;}
  ItemStack head=new ItemStack(Material.PLAYER_HEAD);SkullMeta meta=(SkullMeta)head.getItemMeta();meta.setOwningPlayer(target);head.setItemMeta(meta);p.getInventory().addItem(head).values().forEach(item->p.getWorld().dropItemNaturally(p.getLocation(),item));MessageManager.sendRaw(p,"&aCreated a head for &e"+target.getName()+"&a.");return true;
 }
}