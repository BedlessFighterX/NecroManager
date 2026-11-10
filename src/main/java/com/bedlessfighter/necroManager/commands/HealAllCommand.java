package com.bedlessfighter.necroManager.commands;
import com.bedlessfighter.necroManager.PermissionManager; import com.bedlessfighter.necroManager.MessageManager;
import org.bukkit.Bukkit; import org.bukkit.command.*; import org.bukkit.entity.Player;
public final class HealAllCommand implements CommandExecutor {
 public boolean onCommand(CommandSender s,Command c,String l,String[] a){
  if(!(s instanceof Player p)){MessageManager.sendRaw(s,"&cPlayers only.");return true;}
  if(!PermissionManager.has(s,"necromanager.healall")){MessageManager.send(s,"messages.no-permission","&cYou do not have permission.");return true;}
  if(!PermissionManager.has(s,"necromanager.healall")){MessageManager.send(s,"messages.no-permission","&cYou do not have permission.");return true;}for(Player t:Bukkit.getOnlinePlayers()){t.setHealth(t.getAttribute(org.bukkit.attribute.Attribute.MAX_HEALTH).getValue());t.setFireTicks(0);}MessageManager.sendRaw(s,"&aHealed all online players.");
  return true;
 }
}