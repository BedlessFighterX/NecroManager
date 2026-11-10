package com.bedlessfighter.necroManager.commands;
import com.bedlessfighter.necroManager.PermissionManager; import com.bedlessfighter.necroManager.MessageManager;
import org.bukkit.Bukkit; import org.bukkit.World; import org.bukkit.command.*;
public final class WeatherAllCommand implements CommandExecutor {
 public boolean onCommand(CommandSender s,Command c,String l,String[] a){
  if(!PermissionManager.has(s,"necromanager.weatherall")){MessageManager.send(s,"messages.no-permission","&cYou do not have permission.");return true;}
  if(a.length!=1||!(a[0].equalsIgnoreCase("clear")||a[0].equalsIgnoreCase("rain")||a[0].equalsIgnoreCase("thunder"))){s.sendMessage("Usage: /weatherall <clear|rain|thunder>");return true;}
  for(World w:Bukkit.getWorlds()){boolean storm=!a[0].equalsIgnoreCase("clear");w.setStorm(storm);w.setThundering(a[0].equalsIgnoreCase("thunder"));w.setWeatherDuration(12000);w.setThunderDuration(12000);}
  MessageManager.sendRaw(s,"&aSet weather in all worlds to "+a[0].toLowerCase()+".");return true;
 }
}