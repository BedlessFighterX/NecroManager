package com.bedlessfighter.necroManager.commands;
import com.bedlessfighter.necroManager.PermissionManager; import com.bedlessfighter.necroManager.MessageManager;
import org.bukkit.Bukkit; import org.bukkit.command.*; import org.bukkit.entity.Player;
public final class WorldSpawnCommand implements CommandExecutor {
 public boolean onCommand(CommandSender s,Command c,String l,String[] a){
  if(!(s instanceof Player p)){MessageManager.sendRaw(s,"&cPlayers only.");return true;}
  if(!PermissionManager.has(s,"necromanager.worldspawn")){MessageManager.send(s,"messages.no-permission","&cYou do not have permission.");return true;}
  p.teleport(p.getWorld().getSpawnLocation());MessageManager.sendRaw(s,"&aTeleported to this world's spawn.");
  return true;
 }
}