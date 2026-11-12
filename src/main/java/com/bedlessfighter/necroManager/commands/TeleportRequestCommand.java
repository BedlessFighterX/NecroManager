package com.bedlessfighter.necroManager.commands;

import com.bedlessfighter.necroManager.MessageManager;
import com.bedlessfighter.necroManager.managers.TeleportRequestManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.List;

public final class TeleportRequestCommand implements CommandExecutor, TabCompleter {
    private final TeleportRequestManager.Direction direction;
    public TeleportRequestCommand(TeleportRequestManager.Direction direction) { this.direction = direction; }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) { MessageManager.sendRaw(sender, "&cPlayers only."); return true; }
        if (!player.hasPermission(direction == TeleportRequestManager.Direction.HERE ? "necromanager.tpahere" : "necromanager.tpa")) {
            MessageManager.sendRaw(sender, "&cYou do not have permission."); return true;
        }
        if (args.length != 1) {
            MessageManager.sendRaw(sender, direction == TeleportRequestManager.Direction.HERE
                    ? "&eUsage: /tpahere <player>" : "&eUsage: /tpa <player>");
            return true;
        }
        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null) { MessageManager.sendRaw(sender, "&cPlayer not found."); return true; }
        if (target.equals(player)) { MessageManager.sendRaw(sender, "&cYou cannot send a request to yourself."); return true; }
        if (!TeleportRequestManager.requestsEnabled(target)) { MessageManager.sendRaw(sender, "&cThat player is not accepting teleport requests."); return true; }
        if (!TeleportRequestManager.send(player, target, direction)) {
            MessageManager.sendRaw(sender, "&cYou or that player already has a pending request.");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length != 1) return List.of();
        return Bukkit.getOnlinePlayers().stream().map(Player::getName)
                .filter(name -> !sender.getName().equalsIgnoreCase(name))
                .filter(name -> name.toLowerCase().startsWith(args[0].toLowerCase()))
                .sorted(String.CASE_INSENSITIVE_ORDER).toList();
    }
}
