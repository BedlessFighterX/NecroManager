package com.bedlessfighter.necroManager.commands;
import com.bedlessfighter.necroManager.PermissionManager; import com.bedlessfighter.necroManager.MessageManager;
import org.bukkit.Bukkit; import org.bukkit.command.*; import org.bukkit.entity.Player;
public final class RepairAllCommand implements CommandExecutor {
 public boolean onCommand(CommandSender s,Command c,String l,String[] a){
  if(!PermissionManager.has(s,"necromanager.repairall")){MessageManager.send(s,"messages.no-permission","&cYou do not have permission.");return true;}
  int repaired=0;for(Player t:Bukkit.getOnlinePlayers()){for(org.bukkit.inventory.ItemStack item:t.getInventory().getContents()){if(item!=null&&item.getType().getMaxDurability()>0&&item.getDurability()>0){item.setDurability((short)0);repaired++;}}}MessageManager.sendRaw(s,"&aRepaired &e"+repaired+" &aitems across online players.");
  return true;
 }
}