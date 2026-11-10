package com.bedlessfighter.necroManager.commands;
import com.bedlessfighter.necroManager.PermissionManager; import com.bedlessfighter.necroManager.MessageManager;
import org.bukkit.Bukkit; import org.bukkit.GameMode; import org.bukkit.command.*; import org.bukkit.entity.Player;
public final class GamemodeAllCommand implements CommandExecutor {
 public boolean onCommand(CommandSender s,Command c,String l,String[] a){
  if(!PermissionManager.has(s,"necromanager.gamemodeall")){MessageManager.send(s,"messages.no-permission","&cYou do not have permission.");return true;}
  if(a.length!=1){s.sendMessage("Usage: /gamemodeall <survival|creative|adventure|spectator>");return true;}
  GameMode mode;try{mode=GameMode.valueOf(a[0].toUpperCase(java.util.Locale.ROOT));}catch(IllegalArgumentException e){s.sendMessage("Invalid gamemode.");return true;}
  for(Player p:Bukkit.getOnlinePlayers())p.setGameMode(mode);MessageManager.sendRaw(s,"&aSet all online players to "+mode.name().toLowerCase(java.util.Locale.ROOT)+".");return true;
 }
}