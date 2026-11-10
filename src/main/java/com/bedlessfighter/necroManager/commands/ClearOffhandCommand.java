package com.bedlessfighter.necroManager.commands;
import com.bedlessfighter.necroManager.PermissionManager;
import com.bedlessfighter.necroManager.MessageManager;
import org.bukkit.Bukkit; import org.bukkit.command.*; import org.bukkit.entity.Player;
public final class ClearOffhandCommand implements CommandExecutor {
 public boolean onCommand(CommandSender s, Command c, String l, String[] a){
  if(!PermissionManager.has(s,"necromanager.clearoffhand")){MessageManager.send(s,"messages.no-permission","&cYou do not have permission.");return true;}
  Player t=s instanceof Player p?p:null;
  if(a.length>0){if(!PermissionManager.has(s,"necromanager.clearoffhand.others")){MessageManager.send(s,"messages.no-permission","&cYou do not have permission.");return true;}t=Bukkit.getPlayerExact(a[0]);}
  if(t==null){MessageManager.send(s,"messages.player-not-found","&cPlayer not found.");return true;}
  t.getInventory().setItemInOffHand(null); MessageManager.sendRaw(s,"&aCleared offhand for &e"+t.getName()+"&a.");return true;
 }
}