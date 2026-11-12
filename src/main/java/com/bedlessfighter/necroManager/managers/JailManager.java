package com.bedlessfighter.necroManager.managers;

import com.bedlessfighter.necroManager.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.IOException;
import java.util.*;

public final class JailManager implements Listener {
    private static File file;
    private static YamlConfiguration data;
    private static final Set<UUID> internalTeleports = new HashSet<>();
    private static final Map<UUID, BukkitTask> expiryTasks = new HashMap<>();
    public JailManager() {}

    public static synchronized void load() {
        file = new File(Main.getPlugin().getDataFolder(), "jails.yml");
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
        data = YamlConfiguration.loadConfiguration(file);
        for (String key : data.getConfigurationSection("jailed") == null
                ? Collections.<String>emptySet() : data.getConfigurationSection("jailed").getKeys(false)) {
            UUID id;
            try { id = UUID.fromString(key); } catch (IllegalArgumentException ex) { continue; }
            long expiry = data.getLong("jailed." + key + ".expires-at", 0L);
            if (expiry > 0 && expiry <= System.currentTimeMillis()) release(id, false);
            else if (expiry > 0) scheduleExpiry(id, expiry);
        }
    }
    private static void ready() { if (data == null) load(); }
    private static void save() {
        try { data.save(file); } catch (IOException ex) { throw new IllegalStateException("Could not save jails.yml", ex); }
    }
    public static synchronized List<String> list() {
        ready(); ConfigurationSection s=data.getConfigurationSection("locations");
        return s == null ? List.of() : s.getKeys(false).stream().sorted(String.CASE_INSENSITIVE_ORDER).toList();
    }
    public static boolean validName(String n) { return n != null && n.matches("[a-zA-Z0-9_-]{1,32}"); }
    public static synchronized boolean setJail(String name, Location loc) {
        ready(); if (!validName(name) || loc.getWorld()==null) return false;
        String p="locations."+name.toLowerCase(Locale.ROOT);
        data.set(p+".world",loc.getWorld().getName()); data.set(p+".x",loc.getX());
        data.set(p+".y",loc.getY()); data.set(p+".z",loc.getZ());
        data.set(p+".yaw",loc.getYaw()); data.set(p+".pitch",loc.getPitch()); save(); return true;
    }
    public static synchronized boolean deleteJail(String name) {
        ready(); String p="locations."+name.toLowerCase(Locale.ROOT);
        if (!data.contains(p)) return false;
        for (String id : data.getConfigurationSection("jailed") == null
                ? Collections.<String>emptySet() : data.getConfigurationSection("jailed").getKeys(false)) {
            if (data.getString("jailed."+id+".jail","").equalsIgnoreCase(name)) return false;
        }
        data.set(p,null); save(); return true;
    }
    public static synchronized Location getJail(String name) {
        ready(); String p="locations."+name.toLowerCase(Locale.ROOT);
        if (!data.isConfigurationSection(p)) return null;
        World w=Bukkit.getWorld(data.getString(p+".world",""));
        return w==null ? null : new Location(w,data.getDouble(p+".x"),data.getDouble(p+".y"),
                data.getDouble(p+".z"),(float)data.getDouble(p+".yaw"),(float)data.getDouble(p+".pitch"));
    }
    public static synchronized boolean isJailed(UUID id) { ready(); return data.contains("jailed."+id); }
    public static synchronized String jailName(UUID id) { ready(); return data.getString("jailed."+id+".jail",""); }
    public static synchronized String reason(UUID id) { ready(); return data.getString("jailed."+id+".reason","No reason specified"); }
    public static synchronized long expiresAt(UUID id) { ready(); return data.getLong("jailed."+id+".expires-at",0L); }
    public static synchronized boolean jail(Player target, String jail, long durationMillis, String reason) {
        Location destination=getJail(jail); if (destination==null) return false;
        String p="jailed."+target.getUniqueId();
        long expiry=durationMillis<=0 ? 0L : System.currentTimeMillis()+durationMillis;
        data.set(p+".name",target.getName()); data.set(p+".jail",jail.toLowerCase(Locale.ROOT));
        data.set(p+".expires-at",expiry); data.set(p+".reason",reason); save();
        cancelExpiry(target.getUniqueId()); if(expiry>0) scheduleExpiry(target.getUniqueId(),expiry);
        teleportInternally(target,destination); return true;
    }
    public static synchronized boolean release(UUID id, boolean teleportOut) {
        ready(); if(!data.contains("jailed."+id)) return false;
        data.set("jailed."+id,null); save(); cancelExpiry(id);
        Player p=Bukkit.getPlayer(id);
        if(teleportOut && p!=null) {
            Location spawn=Main.getPlugin().getConfig().getLocation("spawn.location");
            if(spawn!=null && spawn.getWorld()!=null) teleportInternally(p,spawn);
            else { World w=Bukkit.getWorlds().get(0); teleportInternally(p,w.getSpawnLocation()); }
        }
        return true;
    }
    private static void teleportInternally(Player p, Location loc) {
        internalTeleports.add(p.getUniqueId());
        try { p.teleport(loc); } finally { Bukkit.getScheduler().runTask(Main.getPlugin(),()->internalTeleports.remove(p.getUniqueId())); }
    }
    private static void scheduleExpiry(UUID id,long expiry) {
        long ticks=Math.max(1L,(expiry-System.currentTimeMillis()+49L)/50L);
        BukkitTask task=Bukkit.getScheduler().runTaskLater(Main.getPlugin(),()->{
            synchronized(JailManager.class) {
                if(!isJailed(id)) return;
                if(expiresAt(id)>0 && expiresAt(id)<=System.currentTimeMillis()) {
                    release(id,true); Player p=Bukkit.getPlayer(id);
                    if(p!=null) p.sendMessage("§aYour jail sentence has ended.");
                } else if(expiresAt(id)>0) scheduleExpiry(id,expiresAt(id));
            }
        },ticks);
        expiryTasks.put(id,task);
    }
    private static void cancelExpiry(UUID id) { BukkitTask t=expiryTasks.remove(id); if(t!=null)t.cancel(); }
    @EventHandler public void onMove(PlayerMoveEvent e) {
        if(!isJailed(e.getPlayer().getUniqueId()) || internalTeleports.contains(e.getPlayer().getUniqueId()) || e.getTo()==null) return;
        Location from=e.getFrom(),to=e.getTo();
        if(from.getBlockX()!=to.getBlockX() || from.getBlockY()!=to.getBlockY() || from.getBlockZ()!=to.getBlockZ()) e.setTo(from);
    }
    @EventHandler public void onTeleport(PlayerTeleportEvent e) {
        UUID id=e.getPlayer().getUniqueId();
        if(isJailed(id) && !internalTeleports.contains(id)) e.setCancelled(true);
    }
    @EventHandler public void onJoin(PlayerJoinEvent e) {
        UUID id=e.getPlayer().getUniqueId(); if(!isJailed(id)) return;
        Location loc=getJail(jailName(id));
        if(loc!=null) Bukkit.getScheduler().runTask(Main.getPlugin(),()->teleportInternally(e.getPlayer(),loc));
    }
    @EventHandler public void onQuit(PlayerQuitEvent e) { internalTeleports.remove(e.getPlayer().getUniqueId()); }
    public static synchronized void shutdown() {
        expiryTasks.values().forEach(BukkitTask::cancel); expiryTasks.clear(); internalTeleports.clear();
        if(data!=null) save();
    }
}