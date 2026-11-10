package com.bedlessfighter.necroManager.commands;

import com.bedlessfighter.necroManager.managers.ConfigManager;
import com.bedlessfighter.necroManager.managers.MessageManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public final class ReloadCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String @NotNull [] args) {
        if (!sender.hasPermission("necromanager.admin") && !sender.isOp()) {
            MessageManager.send(sender, "messages.no-perm",
                    "&cYou don't have enough permissions to issue this command!");
            return true;
        }

        ConfigManager.reload();
        MessageManager.send(sender, "messages.reload",
                "&aConfiguration reloaded successfully.");
        return true;
    }
}
