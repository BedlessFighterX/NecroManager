package com.bedlessfighter.necroManager.commands;
import com.bedlessfighter.necroManager.PermissionManager; import com.bedlessfighter.necroManager.MessageManager;
import org.bukkit.Bukkit; import org.bukkit.command.*; import org.bukkit.entity.Player;
public final class ExperienceCommand implements CommandExecutor {
 public boolean onCommand(CommandSender s,Command c,String l,String[] a){
  if(!(s instanceof Player p)){MessageManager.sendRaw(s,"&cPlayers only.");return true;}
  if(!PermissionManager.has(s,"necromanager.experience")){MessageManager.send(s,"messages.no-permission","&cYou do not have permission.");return true;}
  if(a.length==0){MessageManager.sendRaw(s,"&eUsage: /experience <amount> [player]");return true;}int amount;try{amount=Integer.parseInt(a[0]);}catch(NumberFormatException ex){MessageManager.sendRaw(s,"&cAmount must be an integer.");return true;}if(amount<0||amount>100000){MessageManager.sendRaw(s,"&cAmount must be between 0 and 100000.");return true;}Player t=p;if(a.length>1){if(!PermissionManager.has(s,"necromanager.experience.others")){MessageManager.send(s,"messages.no-permission","&cYou do not have permission.");return true;}t=Bukkit.getPlayerExact(a[1]);}if(t==null){MessageManager.send(s,"messages.player-not-found","&cPlayer not found.");return true;}t.giveExp(amount);MessageManager.sendRaw(s,"&aGranted &e"+amount+"&a XP to &e"+t.getName()+"&a.");
  return true;
 }
}