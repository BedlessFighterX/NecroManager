package com.bedlessfighter.necroManager.commands;
import com.bedlessfighter.necroManager.PermissionManager; import com.bedlessfighter.necroManager.MessageManager;
import org.bukkit.Bukkit; import org.bukkit.command.*; import org.bukkit.entity.Player;
public final class ClearEffectsAllCommand implements CommandExecutor {
 public boolean onCommand(CommandSender s,Command c,String l,String[] a){
  if(!PermissionManager.has(s,"necromanager.cleareffectsall")){MessageManager.send(s,"messages.no-permission","&cYou do not have permission.");return true;}
  for(Player t:Bukkit.getOnlinePlayers()){for(org.bukkit.potion.PotionEffect effect:new java.util.ArrayList<>(t.getActivePotionEffects()))t.removePotionEffect(effect.getType());}MessageManager.sendRaw(s,"&aCleared potion effects for all online players.");
  return true;
 }
}