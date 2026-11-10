package com.bedlessfighter.necroManager.managers;

import org.bukkit.command.CommandSender;

/**
 * Shared permission checks. Administrative access intentionally includes
 * operators, matching NecroManager's existing command and listener behavior.
 */
public final class PermissionManager {

    private PermissionManager() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static boolean isAdmin(CommandSender sender) {
        return sender.isOp() || sender.hasPermission("necromanager.admin");
    }

    public static boolean has(CommandSender sender, String permission) {
        return isAdmin(sender) || sender.hasPermission(permission);
    }

    public static boolean hasDirect(CommandSender sender, String permission) {
        return sender.hasPermission(permission);
    }
}
