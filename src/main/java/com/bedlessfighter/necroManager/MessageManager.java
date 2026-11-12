package com.bedlessfighter.necroManager;

import org.bukkit.command.CommandSender;

/** Backwards-compatible facade for the managers package. */
public final class MessageManager {
    private MessageManager() {}
    public static void sendRaw(CommandSender sender, String message) {
        com.bedlessfighter.necroManager.managers.MessageManager.sendRaw(sender, message);
    }
    public static void send(CommandSender sender, String path, String fallback) {
        com.bedlessfighter.necroManager.managers.MessageManager.send(sender, path, fallback);
    }
}
