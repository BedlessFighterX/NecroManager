package com.bedlessfighter.necroManager.commands;
import com.bedlessfighter.necroManager.PermissionManager; import com.bedlessfighter.necroManager.MessageManager;
import org.bukkit.command.*; import org.bukkit.entity.Player;
public final class MaptableCommand implements CommandExecutor {
 public boolean onCommand(CommandSender s,Command c,String l,String[] a){
  if(!(s instanceof Player p)){MessageManager.sendRaw(s,"&cPlayers only.");return true;}
  if(!PermissionManager.has(s,"necromanager.maptable")){MessageManager.send(s,"messages.no-permission","&cYou do not have permission.");return true;}
  p.openCartographyTable(p.getLocation(),true);
  return true;
 }
}