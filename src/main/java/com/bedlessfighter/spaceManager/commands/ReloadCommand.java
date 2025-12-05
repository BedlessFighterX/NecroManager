package com.bedlessfighter.spaceManager.commands;

import com.bedlessfighter.spaceManager.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ReloadCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        String prefix = ChatColor.translateAlternateColorCodes('&', Main.getPlugin().getConfig().getString("messages.prefix"));

        if (sender instanceof Player player) {
            if (player.hasPermission("spacemanager.admin")) {
                Main.getPlugin().reloadConfig();
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "%prefix% &rConfigs reloaded &asuccessfully!").replace("%prefix%", prefix));
            } else {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "%prefix% &rYou don't have permission to use this command!)").replace("%prefix%", prefix));
            }
        }

        return true;
    }
}
