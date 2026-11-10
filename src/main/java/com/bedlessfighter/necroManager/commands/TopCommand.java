package com.bedlessfighter.necroManager.commands;
import com.bedlessfighter.necroManager.managers.MessageManager;
import com.bedlessfighter.necroManager.managers.PermissionManager;
import org.bukkit.Material; import org.bukkit.block.Block; import org.bukkit.command.*; import org.bukkit.entity.Player; import org.jetbrains.annotations.NotNull;
public final class TopCommand implements CommandExecutor {
 public boolean onCommand(@NotNull CommandSender s,@NotNull Command c,@NotNull String @NotNull [] a){
 if(a.length!=0){MessageManager.sendRaw(s,MessageManager.prefix()+" &cUsage: /top");return true;}
 if(!PermissionManager.has(s,"necromanager.top")){MessageManager.send(s,"messages.no-perm","&cYou don't have enough permissions to issue this command!");return true;}
 if(!(s instanceof Player p)){MessageManager.send(s,"messages.player-required","&cOnly players can use this command.");return true;}
 Block b=p.getLocation().getBlock(); int y=b.getWorld().getHighestBlockYAt(b.getX(),b.getZ()); Block feet=b.getWorld().getBlockAt(b.getX(),y+1,b.getZ());
 if(!feet.isPassable()||!feet.getRelative(0,1,0).isPassable()||!feet.getRelative(0,-1,0).getType().isSolid()){MessageManager.sendRaw(s,MessageManager.prefix()+" &cNo safe surface found.");return true;}
 p.teleport(feet.getLocation().add(.5,0,.5).setDirection(p.getLocation().getDirection())); MessageManager.send(s,"messages.top","&aTeleported to the top.");return true;
 }
}