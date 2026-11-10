package com.bedlessfighter.necroManager.managers;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.UUID;

public final class ChatSocialManager {
    private static final Map<UUID, UUID> replies = new ConcurrentHashMap<>();
    private static final Map<UUID, Set<UUID>> ignored = new ConcurrentHashMap<>();
    private static final Set<UUID> socialSpy = ConcurrentHashMap.newKeySet();
    private ChatSocialManager() {}
    public static void reply(UUID player, UUID target) { replies.put(player, target); }
    public static UUID replyTarget(UUID player) { return replies.get(player); }
    public static boolean ignores(UUID receiver, UUID sender) { return ignored.getOrDefault(receiver, Set.of()).contains(sender); }
    public static boolean toggleIgnore(UUID player, UUID target) {
        Set<UUID> set = ignored.computeIfAbsent(player, k -> ConcurrentHashMap.newKeySet());
        if (set.remove(target)) return false;
        set.add(target); return true;
    }
    public static boolean toggleSpy(UUID player) {
        if (socialSpy.remove(player)) return false;
        socialSpy.add(player); return true;
    }
    public static boolean spying(UUID player) { return socialSpy.contains(player); }
    public static void cleanup(UUID player) { replies.remove(player); socialSpy.remove(player); ignored.remove(player); ignored.values().forEach(s -> s.remove(player)); }
}