package com.bedlessfighter.necroManager.commands;
import com.bedlessfighter.necroManager.PermissionManager; import com.bedlessfighter.necroManager.MessageManager;
import org.bukkit.command.*; import org.bukkit.entity.Player; import org.bukkit.inventory.ItemStack; import org.bukkit.inventory.meta.ItemMeta; import java.util.*;
public final class LoreCommand implements CommandExecutor {
 public boolean onCommand(CommandSender s,Command c,String l,String[] a){
  if(!(s instanceof Player p)){MessageManager.sendRaw(s,"&cPlayers only.");return true;}if(!PermissionManager.has(s,"necromanager.lore")){MessageManager.sendRaw(s,"&cYou do not have permission.");return true;}
  if(a.length<1||(!a[0].equalsIgnoreCase("add")&&!a[0].equalsIgnoreCase("clear"))){MessageManager.sendRaw(s,"&cUsage: /lore <add <text>|clear>");return true;}ItemStack item=p.getInventory().getItemInMainHand();if(item.getType().isAir()){MessageManager.sendRaw(p,"&cHold an item first.");return true;}ItemMeta meta=item.getItemMeta();
  if(a[0].equalsIgnoreCase("clear"))meta.setLore(null);else{if(a.length<2){MessageManager.sendRaw(p,"&cUsage: /lore add <text>");return true;}List<String> lore=meta.hasLore()?new ArrayList<>(meta.getLore()):new ArrayList<>();lore.add(MessageManager.color(String.join(" ",Arrays.copyOfRange(a,1,a.length))));meta.setLore(lore);}item.setItemMeta(meta);MessageManager.sendRaw(p,"&aUpdated item lore.");return true;
 }
}