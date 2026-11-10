package com.bedlessfighter.necroManager.commands;

import com.bedlessfighter.necroManager.managers.ConfigManager;
import com.bedlessfighter.necroManager.managers.MessageManager;
import com.bedlessfighter.necroManager.managers.PermissionManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public final class ReloadCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length != 0) {
            MessageManager.sendRaw(sender, MessageManager.prefix()
                    + " &cUsage: /" + label);
            return true;
        }
        if (!PermissionManager.isAdmin(sender)) {
            MessageManager.send(sender, "messages.no-perm",
                    "&cYou don't have enough permissions to issue this command!");
            return true;
        }

        try {
            ConfigManager.reload();
            MessageManager.send(sender, "messages.reload",
                    "&aConfiguration reloaded successfully.");
        } catch (RuntimeException exception) {
            com.bedlessfighter.necroManager.Main.getPlugin().getLogger().severe(
                    "Configuration reload failed: " + exception.getMessage());
            MessageManager.send(sender, "messages.reload-failed",
                    "&cConfiguration reload failed. Check the server console.");
        }
        return true;
    }
}
