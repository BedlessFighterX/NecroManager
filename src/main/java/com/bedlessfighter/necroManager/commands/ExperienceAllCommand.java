package com.bedlessfighter.necroManager.commands;
import com.bedlessfighter.necroManager.PermissionManager; import com.bedlessfighter.necroManager.MessageManager;
import org.bukkit.Bukkit; import org.bukkit.command.*; import org.bukkit.entity.Player;
public final class ExperienceAllCommand implements CommandExecutor {
 public boolean onCommand(CommandSender s,Command c,String l,String[] a){
  if(!PermissionManager.has(s,"necromanager.experienceall")){MessageManager.send(s,"messages.no-permission","&cYou do not have permission.");return true;}
  if(a.length!=1){s.sendMessage("Usage: /experienceall <amount>");return true;}
  int amount;try{amount=Integer.parseInt(a[0]);}catch(NumberFormatException e){s.sendMessage("Amount must be an integer.");return true;}
  if(amount<0||amount>100000){s.sendMessage("Amount must be between 0 and 100000.");return true;}
  for(Player p:Bukkit.getOnlinePlayers())p.giveExp(amount);MessageManager.sendRaw(s,"&aGranted "+amount+" XP to all online players.");return true;
 }
}