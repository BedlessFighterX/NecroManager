package com.bedlessfighter.necroManager.commands;
import com.bedlessfighter.necroManager.PermissionManager; import com.bedlessfighter.necroManager.MessageManager;
import org.bukkit.Bukkit; import org.bukkit.command.*; import org.bukkit.entity.Player;
public final class PlayerTimeCommand implements CommandExecutor {
 public boolean onCommand(CommandSender s,Command c,String l,String[] a){
  if(!(s instanceof Player p)){MessageManager.sendRaw(s,"&cPlayers only.");return true;}
  if(!PermissionManager.has(s,"necromanager.ptime")){MessageManager.send(s,"messages.no-permission","&cYou do not have permission.");return true;}
  if(a.length!=1){MessageManager.sendRaw(s,"&eUsage: /ptime <day|night|reset>");return true;}switch(a[0].toLowerCase(java.util.Locale.ROOT)){case "day"->p.setPlayerTime(1000,true);case "night"->p.setPlayerTime(13000,true);case "reset"->p.resetPlayerTime();default->{MessageManager.sendRaw(s,"&cChoose day, night, or reset.");return true;}}MessageManager.sendRaw(s,"&aPersonal time updated.");
  return true;
 }
}