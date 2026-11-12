package com.bedlessfighter.necroManager.commands;

import com.bedlessfighter.necroManager.managers.MessageManager;
import com.bedlessfighter.necroManager.managers.PlayerHiderManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class HideCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            MessageManager.sendRaw(sender, "&cOnly players can toggle player visibility.");
            return true;
        }
        if (!player.hasPermission("necromanager.hide")) {
            MessageManager.sendRaw(player, "&cYou do not have permission.");
            return true;
        }
        boolean hidden = PlayerHiderManager.toggle(player);
        MessageManager.sendRaw(player, hidden ? "&aPlayers hidden." : "&ePlayers visible.");
        return true;
    }
}
