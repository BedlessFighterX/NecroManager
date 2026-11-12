package com.bedlessfighter.necroManager.commands;
import com.bedlessfighter.necroManager.managers.*;import org.bukkit.command.*;import org.bukkit.entity.Player;import org.jetbrains.annotations.NotNull;
public final class AnvilCommand implements CommandExecutor {
 @Override public boolean onCommand(@NotNull CommandSender s,@NotNull Command c,@NotNull String label, @NotNull String[] a){if(!(s instanceof Player p)){MessageManager.send(s,"messages.player-required","&cOnly players can open an anvil.");return true;}if(!PermissionManager.has(s,"necromanager.anvil")){MessageManager.send(s,"messages.no-perm","&cNo permission.");return true;}if(a.length!=0){MessageManager.sendRaw(s,"&cUsage: /anvil");return true;}p.openAnvil(p.getLocation(),true);return true;}
}