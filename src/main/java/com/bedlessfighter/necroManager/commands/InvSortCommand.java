package com.bedlessfighter.necroManager.commands;
import com.bedlessfighter.necroManager.PermissionManager; import com.bedlessfighter.necroManager.MessageManager;
import org.bukkit.command.*; import org.bukkit.entity.Player;
public final class InvSortCommand implements CommandExecutor {
 public boolean onCommand(CommandSender s,Command c,String l,String[] a){
  if(!(s instanceof Player p)){MessageManager.sendRaw(s,"&cPlayers only.");return true;}
  if(!PermissionManager.has(s,"necromanager.sortinv")){MessageManager.send(s,"messages.no-permission","&cYou do not have permission.");return true;}
  java.util.Arrays.sort(p.getInventory().getStorageContents(),java.util.Comparator.nullsLast(java.util.Comparator.comparing(i->i==null?null:i.getType().name())));MessageManager.sendRaw(s,"&eInventory sorting requested.");
  return true;
 }
}