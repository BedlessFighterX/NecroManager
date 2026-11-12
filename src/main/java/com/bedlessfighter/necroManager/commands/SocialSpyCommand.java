package com.bedlessfighter.necroManager.commands;
import com.bedlessfighter.necroManager.managers.*;import org.bukkit.command.*;import org.jetbrains.annotations.NotNull;
public final class SocialSpyCommand implements CommandExecutor {
 @Override public boolean onCommand(@NotNull CommandSender s,@NotNull Command c,@NotNull String label, @NotNull String[] a){if(!PermissionManager.has(s,"necromanager.socialspy")){MessageManager.send(s,"messages.no-perm","&cNo permission.");return true;}if(!(s instanceof org.bukkit.entity.Player p)){MessageManager.sendRaw(s,"&cOnly players can toggle social spy.");return true;}boolean enabled=ChatSocialManager.toggleSpy(p.getUniqueId());MessageManager.sendRaw(s,enabled?"&aSocialSpy enabled.":"&eSocialSpy disabled.");return true;}
}