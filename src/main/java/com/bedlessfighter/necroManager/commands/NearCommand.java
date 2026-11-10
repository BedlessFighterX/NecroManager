package com.bedlessfighter.necroManager.commands;
import com.bedlessfighter.necroManager.PermissionManager; import com.bedlessfighter.necroManager.MessageManager;
import org.bukkit.command.*; import org.bukkit.entity.Player;
public final class NearCommand implements CommandExecutor {
 public boolean onCommand(CommandSender s,Command c,String l,String[] a){
  if(!(s instanceof Player p)){MessageManager.sendRaw(s,"&cPlayers only.");return true;}
  if(!PermissionManager.has(s,"necromanager.near")){MessageManager.send(s,"messages.no-permission","&cYou do not have permission.");return true;}
  StringBuilder b=new StringBuilder("&6Nearby: "); for(Player q:p.getWorld().getPlayers())if(q!=p&&q.getLocation().distanceSquared(p.getLocation())<=10000)b.append("&e").append(q.getName()).append(" &7(").append((int)p.getLocation().distance(q.getLocation())).append("m) "); MessageManager.sendRaw(s,b.length()>11?b.toString():"&7No players within 100 blocks.");
  return true;
 }
}