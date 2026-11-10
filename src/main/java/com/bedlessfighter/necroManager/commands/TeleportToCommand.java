package com.bedlessfighter.necroManager.commands;
import com.bedlessfighter.necroManager.PermissionManager; import com.bedlessfighter.necroManager.MessageManager;
import org.bukkit.Bukkit; import org.bukkit.command.*; import org.bukkit.entity.Player;
public final class TeleportToCommand implements CommandExecutor {
 public boolean onCommand(CommandSender s,Command c,String l,String[] a){
  if(!(s instanceof Player p)){MessageManager.sendRaw(s,"&cPlayers only.");return true;}
  if(!PermissionManager.has(s,"necromanager.teleportto")){MessageManager.send(s,"messages.no-permission","&cYou do not have permission.");return true;}
  if(a.length!=1){MessageManager.sendRaw(s,"&eUsage: /teleportto <player>");return true;}Player t=Bukkit.getPlayerExact(a[0]);if(t==null){MessageManager.send(s,"messages.player-not-found","&cPlayer not found.");return true;}p.teleport(t.getLocation());MessageManager.sendRaw(s,"&aTeleported to &e"+t.getName()+"&a.");
  return true;
 }
}