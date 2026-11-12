package com.bedlessfighter.necroManager.managers;

import com.bedlessfighter.necroManager.Main;
import com.bedlessfighter.necroManager.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public final class TeleportRequestManager implements Listener {
    public enum Direction { TO_TARGET, HERE }
    private record Request(UUID requester, UUID target, Direction direction, long expiresAt) { }
    private static final Map<UUID, Request> INCOMING = new HashMap<>();
    private static final Map<UUID, UUID> OUTGOING = new HashMap<>();
    private static final Set<UUID> DISABLED = new HashSet<>();

    public static synchronized boolean send(Player requester, Player target, Direction direction) {
        if (requester.equals(target) || DISABLED.contains(target.getUniqueId())) return false;
        if (OUTGOING.containsKey(requester.getUniqueId())) return false;
        UUID targetId = target.getUniqueId();
        if (INCOMING.containsKey(targetId)) return false;
        Request request = new Request(requester.getUniqueId(), targetId, direction,
                System.currentTimeMillis() + Math.max(5L, Math.min(300L, Main.getPlugin().getConfig().getLong("teleport-requests.expiry-seconds", 60L))) * 1000L);
        INCOMING.put(targetId, request);
        OUTGOING.put(requester.getUniqueId(), targetId);
        String action = direction == Direction.HERE ? "teleport to you" : "teleport to them";
        target.sendMessage(MessageManager.color("&e" + requester.getName()
                + " &7wants to " + action + ". Use &a/tpaccept &7or &c/tpdeny&7. &8(60s)"));
        requester.sendMessage(MessageManager.color("&aTeleport request sent to &e" + target.getName() + "&a."));
        Bukkit.getScheduler().runTaskLater(Main.getPlugin(), () -> expire(request), Math.max(100L, Math.min(6000L, Main.getPlugin().getConfig().getLong("teleport-requests.expiry-seconds", 60L) * 20L)));
        return true;
    }

    private static synchronized void expire(Request request) {
        if (INCOMING.get(request.target()) != request) return;
        INCOMING.remove(request.target());
        OUTGOING.remove(request.requester());
        Player requester = Bukkit.getPlayer(request.requester());
        Player target = Bukkit.getPlayer(request.target());
        if (requester != null) requester.sendMessage(MessageManager.color("&cYour teleport request expired."));
        if (target != null) target.sendMessage(MessageManager.color("&7The teleport request expired."));
    }

    public static synchronized boolean accept(Player target, String requesterName) {
        Request request = INCOMING.get(target.getUniqueId());
        if (request == null || request.expiresAt() < System.currentTimeMillis()) return false;
        Player requester = Bukkit.getPlayer(request.requester());
        if (requester == null) {
            clear(request);
            target.sendMessage(MessageManager.color("&cThat player is no longer online."));
            return true;
        }
        if (requesterName != null && !requester.getName().equalsIgnoreCase(requesterName)) return false;
        Player mover = request.direction() == Direction.HERE ? target : requester;
        Player destination = request.direction() == Direction.HERE ? requester : target;
        clear(request);
        if (mover.teleport(destination.getLocation())) {
            mover.sendMessage(MessageManager.color("&aTeleport request accepted."));
            destination.sendMessage(MessageManager.color("&a" + mover.getName() + " has teleported."));
        } else {
            target.sendMessage(MessageManager.color("&cTeleport was blocked."));
            requester.sendMessage(MessageManager.color("&cTeleport was blocked."));
        }
        return true;
    }

    public static synchronized boolean deny(Player target, String requesterName) {
        Request request = INCOMING.get(target.getUniqueId());
        if (request == null) return false;
        Player requester = Bukkit.getPlayer(request.requester());
        if (requesterName != null && (requester == null || !requester.getName().equalsIgnoreCase(requesterName))) return false;
        clear(request);
        target.sendMessage(MessageManager.color("&7Teleport request denied."));
        if (requester != null) requester.sendMessage(MessageManager.color("&c" + target.getName() + " denied your teleport request."));
        return true;
    }

    public static synchronized boolean cancel(Player requester) {
        UUID targetId = OUTGOING.get(requester.getUniqueId());
        if (targetId == null) return false;
        Request request = INCOMING.get(targetId);
        if (request == null) { OUTGOING.remove(requester.getUniqueId()); return false; }
        clear(request);
        requester.sendMessage(MessageManager.color("&7Your teleport request was cancelled."));
        Player target = Bukkit.getPlayer(targetId);
        if (target != null) target.sendMessage(MessageManager.color("&7" + requester.getName() + " cancelled their teleport request."));
        return true;
    }

    public static synchronized boolean toggle(Player player) {
        UUID id = player.getUniqueId();
        if (DISABLED.remove(id)) return true;
        DISABLED.add(id);
        Request request = INCOMING.get(id);
        if (request != null) clear(request);
        player.sendMessage(MessageManager.color("&eTeleport requests are now disabled."));
        return false;
    }

    public static synchronized boolean requestsEnabled(Player player) {
        return !DISABLED.contains(player.getUniqueId());
    }

    private static void clear(Request request) {
        INCOMING.remove(request.target());
        OUTGOING.remove(request.requester());
    }

    public static synchronized void clearAll() {
        INCOMING.clear();
        OUTGOING.clear();
        DISABLED.clear();
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        UUID id = event.getPlayer().getUniqueId();
        synchronized (TeleportRequestManager.class) {
            UUID target = OUTGOING.remove(id);
            if (target != null) INCOMING.remove(target);
            Request incoming = INCOMING.remove(id);
            if (incoming != null) OUTGOING.remove(incoming.requester());
            DISABLED.remove(id);
        }
    }
}
