package com.bedlessfighter.necroManager.commands;
import com.bedlessfighter.necroManager.PermissionManager; import com.bedlessfighter.necroManager.MessageManager;
import org.bukkit.Bukkit; import org.bukkit.command.*; import org.bukkit.entity.Player;
public final class SetWorldSpawnCommand implements CommandExecutor {
 public boolean onCommand(CommandSender s,Command c,String l,String[] a){
  if(!(s instanceof Player p)){MessageManager.sendRaw(s,"&cPlayers only.");return true;}
  if(!PermissionManager.has(s,"necromanager.setworldspawn")){MessageManager.send(s,"messages.no-permission","&cYou do not have permission.");return true;}
  p.getWorld().setSpawnLocation(p.getLocation());MessageManager.sendRaw(s,"&aWorld spawn set to your location.");
  return true;
 }
}