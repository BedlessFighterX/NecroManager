package com.bedlessfighter.necroManager.commands;
import com.bedlessfighter.necroManager.managers.MessageManager; import com.bedlessfighter.necroManager.managers.PermissionManager;
import org.bukkit.command.*; import org.bukkit.entity.Player; import org.bukkit.block.Block; import org.jetbrains.annotations.NotNull;
public final class BottomCommand implements CommandExecutor {
 public boolean onCommand(@NotNull CommandSender s, @NotNull Command c, @NotNull String label, @NotNull String @NotNull [] a){
 if(a.length!=0){MessageManager.sendRaw(s,MessageManager.prefix()+" &cUsage: /bottom");return true;}
 if(!PermissionManager.has(s,"necromanager.bottom")){MessageManager.send(s,"messages.no-perm","&cYou don't have enough permissions to issue this command!");return true;}
 if(!(s instanceof Player p)){MessageManager.send(s,"messages.player-required","&cOnly players can use this command.");return true;}
 Block b=p.getLocation().getBlock(); int y=b.getWorld().getMinHeight()+1;
 while(y<b.getWorld().getMaxHeight()-1){Block feet=b.getWorld().getBlockAt(b.getX(),y,b.getZ());if(feet.isPassable()&&feet.getRelative(0,1,0).isPassable()&&feet.getRelative(0,-1,0).getType().isSolid()){p.teleport(feet.getLocation().add(.5,0,.5));MessageManager.send(s,"messages.bottom","&aTeleported to the bottom.");return true;}y++;}
 MessageManager.sendRaw(s,MessageManager.prefix()+" &cNo safe surface found.");return true;
 }
}