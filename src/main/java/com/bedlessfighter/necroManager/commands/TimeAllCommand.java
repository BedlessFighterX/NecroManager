package com.bedlessfighter.necroManager.commands;
import com.bedlessfighter.necroManager.PermissionManager; import com.bedlessfighter.necroManager.MessageManager;
import org.bukkit.Bukkit; import org.bukkit.command.*; import org.bukkit.entity.Player;
public final class TimeAllCommand implements CommandExecutor {
 public boolean onCommand(CommandSender s,Command c,String l,String[] a){
  if(!PermissionManager.has(s,"necromanager.timeall")){MessageManager.send(s,"messages.no-permission","&cYou do not have permission.");return true;}
  if(a.length!=1||!(a[0].equalsIgnoreCase("day")||a[0].equalsIgnoreCase("night")||a[0].equalsIgnoreCase("reset"))){s.sendMessage("Usage: /timeall <day|night|reset>");return true;}
  for(Player p:Bukkit.getOnlinePlayers()){if(a[0].equalsIgnoreCase("reset"))p.resetPlayerTime();else p.setPlayerTime(a[0].equalsIgnoreCase("day")?1000:13000,false);}
  MessageManager.sendRaw(s,"&aUpdated personal time for all online players.");return true;
 }
}