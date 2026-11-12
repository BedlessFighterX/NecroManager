package com.bedlessfighter.necroManager.commands;

import com.bedlessfighter.necroManager.managers.HubItemManager;
import com.bedlessfighter.necroManager.managers.MessageManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class ServersCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            MessageManager.sendRaw(sender, "&cOnly players can open the server selector.");
            return true;
        }
        if (!player.hasPermission("necromanager.servers")) {
            MessageManager.sendRaw(player, "&cYou do not have permission.");
            return true;
        }
        player.openInventory(HubItemManager.createServerSelector());
        return true;
    }
}
