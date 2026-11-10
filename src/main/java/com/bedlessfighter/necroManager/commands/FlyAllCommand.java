package com.bedlessfighter.necroManager.commands;
import com.bedlessfighter.necroManager.PermissionManager; import com.bedlessfighter.necroManager.MessageManager;
import org.bukkit.Bukkit; import org.bukkit.command.*; import org.bukkit.entity.Player;
public final class FlyAllCommand implements CommandExecutor {
 public boolean onCommand(CommandSender s,Command c,String l,String[] a){
  if(!PermissionManager.has(s,"necromanager.flyall")){MessageManager.send(s,"messages.no-permission","&cYou do not have permission.");return true;}
  boolean enable=Bukkit.getOnlinePlayers().stream().anyMatch(t->!t.getAllowFlight());for(Player t:Bukkit.getOnlinePlayers()){t.setAllowFlight(enable);if(!enable)t.setFlying(false);}MessageManager.sendRaw(s,"&aFlight "+(enable?"enabled":"disabled")+" for all online players.");
  return true;
 }
}