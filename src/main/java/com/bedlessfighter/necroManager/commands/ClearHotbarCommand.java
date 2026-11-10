package com.bedlessfighter.necroManager.commands;
import com.bedlessfighter.necroManager.PermissionManager; import com.bedlessfighter.necroManager.MessageManager;
import org.bukkit.Bukkit; import org.bukkit.command.*; import org.bukkit.entity.Player;
public final class ClearHotbarCommand implements CommandExecutor {
 public boolean onCommand(CommandSender s,Command c,String l,String[] a){
  if(!PermissionManager.has(s,"necromanager.clearhotbar")){MessageManager.sendRaw(s,"&cYou do not have permission.");return true;}
  Player t=s instanceof Player p?p:null;if(a.length>0){if(!PermissionManager.has(s,"necromanager.clearhotbar.others")){MessageManager.sendRaw(s,"&cYou do not have permission.");return true;}t=Bukkit.getPlayerExact(a[0]);}
  if(t==null){MessageManager.sendRaw(s,"&cPlayer not found.");return true;}for(int i=0;i<9;i++)t.getInventory().setItem(i,null);MessageManager.sendRaw(s,"&aCleared hotbar for &e"+t.getName()+"&a.");return true;
 }
}