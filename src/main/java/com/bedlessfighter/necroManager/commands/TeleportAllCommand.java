package com.bedlessfighter.necroManager.commands;
import com.bedlessfighter.necroManager.PermissionManager; import com.bedlessfighter.necroManager.MessageManager;
import org.bukkit.Bukkit; import org.bukkit.command.*; import org.bukkit.entity.Player;
public final class TeleportAllCommand implements CommandExecutor {
 public boolean onCommand(CommandSender s,Command c,String l,String[] a){
  if(!PermissionManager.has(s,"necromanager.teleportall")){MessageManager.send(s,"messages.no-permission","&cYou do not have permission.");return true;}
  if(a.length!=1){s.sendMessage("Usage: /teleportall <online-player>");return true;}
  Player target=Bukkit.getPlayerExact(a[0]);if(target==null){s.sendMessage("That player is not online.");return true;}
  for(Player p:Bukkit.getOnlinePlayers())if(p!=target)p.teleport(target.getLocation());MessageManager.sendRaw(s,"&aTeleported all online players to "+target.getName()+".");return true;
 }
}