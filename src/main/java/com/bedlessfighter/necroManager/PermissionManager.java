package com.bedlessfighter.necroManager;

import org.bukkit.command.CommandSender;

/** Backwards-compatible facade for the managers package. */
public final class PermissionManager {
    private PermissionManager() {}
    public static boolean has(CommandSender sender, String permission) {
        return com.bedlessfighter.necroManager.managers.PermissionManager.has(sender, permission);
    }
    public static boolean hasDirect(CommandSender sender, String permission) {
        return com.bedlessfighter.necroManager.managers.PermissionManager.hasDirect(sender, permission);
    }
    public static boolean isAdmin(CommandSender sender) {
        return com.bedlessfighter.necroManager.managers.PermissionManager.isAdmin(sender);
    }
}
