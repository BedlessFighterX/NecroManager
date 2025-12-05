package com.bedlessfighter.spaceManager.commands;

import com.bedlessfighter.spaceManager.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ReloadCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender,
                             @NotNull Command command,
                             @NotNull String label,
                             @NotNull String @NotNull [] args) {

        var cfg = Main.getPlugin().getConfig();
        String prefixRaw = cfg.getString("messages.prefix", "&b[SpaceManager]&r");
        String prefix = ChatColor.translateAlternateColorCodes('&', prefixRaw);

        if (!sender.hasPermission("spacemanager.admin")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "%prefix% &cYou don't have permission to use this command!")
                    .replace("%prefix%", prefix));
            return true;
        }

        Main.getPlugin().reloadConfig();
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "%prefix% &aConfigs reloaded successfully!")
                .replace("%prefix%", prefix));
        return true;
    }
}
