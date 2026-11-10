package com.bedlessfighter.necroManager.commands;
import com.bedlessfighter.necroManager.PermissionManager; import com.bedlessfighter.necroManager.MessageManager;
import org.bukkit.Bukkit; import org.bukkit.command.*; import org.bukkit.entity.Player;
public final class KillCommand implements CommandExecutor {
 public boolean onCommand(CommandSender s,Command c,String l,String[] a){
  if(!(s instanceof Player p)){MessageManager.sendRaw(s,"&cPlayers only.");return true;}
  if(!PermissionManager.has(s,"necromanager.kill")){MessageManager.send(s,"messages.no-permission","&cYou do not have permission.");return true;}
  Player t=p;if(a.length>0){if(!PermissionManager.has(s,"necromanager.kill.others")){MessageManager.send(s,"messages.no-permission","&cYou do not have permission.");return true;}t=Bukkit.getPlayerExact(a[0]);}if(t==null){MessageManager.send(s,"messages.player-not-found","&cPlayer not found.");return true;}t.setHealth(0);MessageManager.sendRaw(s,"&aKilled &e"+t.getName()+"&a.");
  return true;
 }
}