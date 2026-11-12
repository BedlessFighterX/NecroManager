package com.bedlessfighter.necroManager.commands;

import com.bedlessfighter.necroManager.managers.JailManager;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import java.util.*;
import java.util.stream.Collectors;

public final class JailCommand implements CommandExecutor, TabCompleter {
    @Override public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String cmd=command.getName().toLowerCase(Locale.ROOT);
        if(cmd.equals("setjail")) {
            if(!(sender instanceof Player p)) { sender.sendMessage("§cPlayers only."); return true; }
            if(!sender.hasPermission("necromanager.jail.set")) { sender.sendMessage("§cNo permission."); return true; }
            if(args.length!=1 || !JailManager.setJail(args[0],p.getLocation())) { sender.sendMessage("§eUsage: /setjail <name>"); return true; }
            sender.sendMessage("§aJail location §e"+args[0]+" §asaved."); return true;
        }
        if(cmd.equals("deljail")) {
            if(!sender.hasPermission("necromanager.jail.admin")) { sender.sendMessage("§cNo permission."); return true; }
            if(args.length!=1) { sender.sendMessage("§eUsage: /deljail <name>"); return true; }
            if(!JailManager.deleteJail(args[0])) sender.sendMessage("§cJail not found or currently occupied.");
            else sender.sendMessage("§aDeleted jail §e"+args[0]+"§a."); return true;
        }
        if(cmd.equals("jails")) {
            sender.sendMessage("§aJails: §f"+(JailManager.list().isEmpty()?"none":String.join(", ",JailManager.list()))); return true;
        }
        if(cmd.equals("jailinfo")) {
            if(!sender.hasPermission("necromanager.jail.admin")) { sender.sendMessage("§cNo permission."); return true; }
            if(args.length!=1) { sender.sendMessage("§eUsage: /jailinfo <player>"); return true; }
            Player target=Bukkit.getPlayerExact(args[0]);
            if(target==null || !JailManager.isJailed(target.getUniqueId())) { sender.sendMessage("§cThat online player is not jailed."); return true; }
            long expiry=JailManager.expiresAt(target.getUniqueId());
            sender.sendMessage("§e"+target.getName()+" §7is jailed at §e"+JailManager.jailName(target.getUniqueId())
                    +"§7; reason: §f"+JailManager.reason(target.getUniqueId())
                    +(expiry==0?"§7; duration: §cpermanent":"§7; remaining: §f"+Math.max(0,(expiry-System.currentTimeMillis())/1000)+"s"));
            return true;
        }
        if(cmd.equals("unjail")) {
            if(!sender.hasPermission("necromanager.jail.release")) { sender.sendMessage("§cNo permission."); return true; }
            if(args.length!=1) { sender.sendMessage("§eUsage: /unjail <player>"); return true; }
            Player target=Bukkit.getPlayerExact(args[0]);
            if(target==null || !JailManager.release(target.getUniqueId(),true)) { sender.sendMessage("§cThat online player is not jailed."); return true; }
            target.sendMessage("§aYou have been released from jail."); sender.sendMessage("§aReleased §e"+target.getName()+"§a."); return true;
        }
        if(!sender.hasPermission("necromanager.jail")) { sender.sendMessage("§cNo permission."); return true; }
        if(args.length<2) { sender.sendMessage("§eUsage: /jail <player> <jail> [duration] [reason]"); return true; }
        Player target=Bukkit.getPlayerExact(args[0]);
        if(target==null) { sender.sendMessage("§cPlayer must be online."); return true; }
        if(!JailManager.list().stream().anyMatch(n->n.equalsIgnoreCase(args[1]))) { sender.sendMessage("§cUnknown jail. Use /jails."); return true; }
        int reasonAt=2; long duration=0;
        if(args.length>2) {
            Long parsed=parseDuration(args[2]);
            if(parsed!=null) { duration=parsed; reasonAt=3; }
        }
        String reason=String.join(" ",Arrays.copyOfRange(args,reasonAt,args.length));
        if(!JailManager.jail(target,args[1],duration,reason.isBlank()?"No reason specified":reason)) {
            sender.sendMessage("§cCould not jail player; check the jail world."); return true;
        }
        target.sendMessage("§cYou have been jailed at §e"+args[1]+"§c. Reason: §f"+(reason.isBlank()?"No reason specified":reason));
        sender.sendMessage("§aJailed §e"+target.getName()+"§a at §e"+args[1]+(duration>0?" §7for "+args[2]:" §7(permanent)")+"§a."); return true;
    }
    private Long parseDuration(String value) {
        if(value==null || !value.matches("(?i)[1-9][0-9]*[smhd]")) return null;
        try {
            long n=Long.parseLong(value.substring(0,value.length()-1));
            long factor=switch(Character.toLowerCase(value.charAt(value.length()-1))) { case 's'->1000L; case 'm'->60000L; case 'h'->3600000L; default->86400000L; };
            return Math.multiplyExact(n,factor);
        } catch(ArithmeticException|NumberFormatException ex) { return null; }
    }
    @Override public List<String> onTabComplete(CommandSender sender,Command command,String alias,String[] args) {
        String cmd=command.getName().toLowerCase(Locale.ROOT);
        if(args.length==1 && Set.of("jail","unjail","jailinfo").contains(cmd))
            return Bukkit.getOnlinePlayers().stream().map(Player::getName).filter(n->n.toLowerCase(Locale.ROOT).startsWith(args[0].toLowerCase(Locale.ROOT))).sorted().toList();
        if(cmd.equals("jail") && args.length==2)
            return JailManager.list().stream().filter(n->n.toLowerCase(Locale.ROOT).startsWith(args[1].toLowerCase(Locale.ROOT))).toList();
        if(cmd.equals("jail") && args.length==3) return List.of("10m","30m","1h","6h","1d","7d");
        if(cmd.equals("deljail") && args.length==1)
            return JailManager.list().stream().filter(n->n.toLowerCase(Locale.ROOT).startsWith(args[0].toLowerCase(Locale.ROOT))).toList();
        return List.of();
    }
}