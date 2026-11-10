package com.bedlessfighter.necroManager.commands;
import com.bedlessfighter.necroManager.PermissionManager; import com.bedlessfighter.necroManager.MessageManager;
import org.bukkit.Bukkit; import org.bukkit.command.*; import org.bukkit.entity.Player;
public final class ExtinguishAllCommand implements CommandExecutor {
 public boolean onCommand(CommandSender s,Command c,String l,String[] a){
  if(!PermissionManager.has(s,"necromanager.extinguishall")){MessageManager.send(s,"messages.no-permission","&cYou do not have permission.");return true;}
  for(Player t:Bukkit.getOnlinePlayers())t.setFireTicks(0);MessageManager.sendRaw(s,"&aExtinguished all online players.");
  return true;
 }
}