package com.bedlessfighter.necroManager.commands;

import com.bedlessfighter.necroManager.managers.*;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import java.util.*;

public final class MessageCommand implements CommandExecutor, TabCompleter {
 @Override public boolean onCommand(@NotNull CommandSender sender,@NotNull Command command,@NotNull String[] args) {
  if(args.length<2){MessageManager.sendRaw(sender,"&cUsage: /msg <player> <message>");return true;}
  if(!(sender instanceof Player from)){MessageManager.send(sender,"messages.player-required","&cOnly players can send private messages.");return true;}
  if(!PermissionManager.has(sender,"necromanager.msg")){MessageManager.send(sender,"messages.no-perm","&cNo permission.");return true;}
  Player to=Bukkit.getPlayerExact(args[0]); if(to==null){MessageManager.send(sender,"messages.player-not-found","&cPlayer not online.");return true;}
  if(to.equals(from)){MessageManager.sendRaw(sender,"&cYou cannot message yourself.");return true;}
  if(ChatSocialManager.ignores(to.getUniqueId(),from.getUniqueId())){MessageManager.sendRaw(sender,"&cThat player is ignoring you.");return true;}
  String body=String.join(" ",Arrays.copyOfRange(args,1,args.length));
  ChatSocialManager.reply(from.getUniqueId(),to.getUniqueId()); ChatSocialManager.reply(to.getUniqueId(),from.getUniqueId());
  MessageManager.sendRaw(from,"&d[You -> "+to.getName()+"] &f"+body); MessageManager.sendRaw(to,"&d["+from.getName()+" -> You] &f"+body);
  for(Player p:Bukkit.getOnlinePlayers())if(!p.equals(from)&&!p.equals(to)&&ChatSocialManager.spying(p.getUniqueId())&&PermissionManager.has(p,"necromanager.socialspy"))MessageManager.sendRaw(p,"&8[Spy] "+from.getName()+" -> "+to.getName()+": "+body);
  return true;
 }
 @Override public List<String> onTabComplete(@NotNull CommandSender s,@NotNull Command c,@NotNull String a,@NotNull String[] args){if(args.length!=1)return List.of();String q=args[0].toLowerCase(Locale.ROOT);return Bukkit.getOnlinePlayers().stream().map(Player::getName).filter(n->n.toLowerCase(Locale.ROOT).startsWith(q)).sorted(String.CASE_INSENSITIVE_ORDER).toList();}
}