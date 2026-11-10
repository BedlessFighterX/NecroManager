package com.bedlessfighter.necroManager.commands;
import com.bedlessfighter.necroManager.PermissionManager; import com.bedlessfighter.necroManager.MessageManager;
import org.bukkit.Bukkit; import org.bukkit.command.*; import org.bukkit.entity.Player;
public final class SetHealthCommand implements CommandExecutor {
 public boolean onCommand(CommandSender s,Command c,String l,String[] a){
  if(!(s instanceof Player p)){MessageManager.sendRaw(s,"&cPlayers only.");return true;}
  if(!PermissionManager.has(s,"necromanager.sethealth")){MessageManager.send(s,"messages.no-permission","&cYou do not have permission.");return true;}
  if(a.length==0){MessageManager.sendRaw(s,"&eUsage: /sethealth <0- max> [player]");return true;}double value;try{value=Double.parseDouble(a[0]);}catch(NumberFormatException ex){MessageManager.sendRaw(s,"&cHealth must be a number.");return true;}Player t=p;if(a.length>1){if(!PermissionManager.has(s,"necromanager.sethealth.others")){MessageManager.send(s,"messages.no-permission","&cYou do not have permission.");return true;}t=Bukkit.getPlayerExact(a[1]);}if(t==null){MessageManager.send(s,"messages.player-not-found","&cPlayer not found.");return true;}double max=t.getAttribute(org.bukkit.attribute.Attribute.MAX_HEALTH).getValue();if(!Double.isFinite(value)||value<0||value>max){MessageManager.sendRaw(s,"&cHealth must be between 0 and "+max+".");return true;}if(value==0)t.setHealth(0);else t.setHealth(value);MessageManager.sendRaw(s,"&aSet &e"+t.getName()+"&a health to &e"+value+"&a.");
  return true;
 }
}