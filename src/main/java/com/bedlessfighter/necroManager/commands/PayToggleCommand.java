package com.bedlessfighter.necroManager.commands;

import com.bedlessfighter.necroManager.managers.MessageManager;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public final class PayToggleCommand implements CommandExecutor, TabCompleter {
    public boolean onCommand(@NotNull CommandSender sender,@NotNull Command command,@NotNull String label,@NotNull String[] args) {
        if(!(sender instanceof Player player)){MessageManager.sendRaw(sender,"&cOnly players can use this command.");return true;}
        if(args.length!=0){MessageManager.sendRaw(sender,"&cUsage: /paytoggle");return true;}
        boolean enabled=player.hasPermission("necromanager.economy.pay.disabled");
        if(enabled)player.removeAttachment(player.addAttachment(com.bedlessfighter.necroManager.Main.getPlugin(),"necromanager.economy.pay.disabled",true));
        else player.addAttachment(com.bedlessfighter.necroManager.Main.getPlugin(),"necromanager.economy.pay.disabled",true);
        MessageManager.sendRaw(player,enabled?"&aYou are now accepting payments.":"&cYou are no longer accepting payments.");
        return true;
    }
    public List<String> onTabComplete(@NotNull CommandSender s,@NotNull Command c,@NotNull String a,@NotNull String[] args){return List.of();}
}
