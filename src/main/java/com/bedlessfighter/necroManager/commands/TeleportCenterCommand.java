package com.bedlessfighter.necroManager.commands;

import com.bedlessfighter.necroManager.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TeleportCenterCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        var cfg = Main.getPlugin().getConfig();
        String noperm = ChatColor.translateAlternateColorCodes('&', cfg.getString("messages.no-perm"));

        String prefix = ChatColor.translateAlternateColorCodes('&', cfg.getString("messages.prefix"));
        String message = ChatColor.translateAlternateColorCodes('&', "%prefix% &rYou have been centered &asuccessfully!".replace("%prefix%", prefix));

        if (sender instanceof Player player) {
            boolean staff = player.hasPermission("necromanager.admin") || player.isOp();

            if (staff) {
                player.teleport(player.getLocation().toCenterLocation());
                player.sendMessage(message);
            } else {
                player.sendMessage("%prefix% %messsage%".replace("%prefix%", prefix).replace("%message%", message));
            }
        }

        return true;
    }
}
