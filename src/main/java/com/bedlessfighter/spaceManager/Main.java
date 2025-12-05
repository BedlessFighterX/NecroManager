package com.bedlessfighter.spaceManager;

import com.bedlessfighter.spaceManager.commands.ReloadCommand;
import com.bedlessfighter.spaceManager.commands.SetSpawnCommand;
import com.bedlessfighter.spaceManager.commands.SpawnCommand;
import com.bedlessfighter.spaceManager.commands.TeleportCenterCommand;
import com.bedlessfighter.spaceManager.listeners.*;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    private static Main plugin;

    @Override
    public void onEnable() {
        // Handling inner main file configs
        saveDefaultConfig();
        plugin = this;

        // Handling commands
        getCommand("reloadmanager").setExecutor(new ReloadCommand());
        getCommand("setspawn").setExecutor(new SetSpawnCommand());
        getCommand("spawn").setExecutor(new SpawnCommand());
        getCommand("tpcenter").setExecutor(new TeleportCenterCommand());

        // Handling events
        getServer().getPluginManager().registerEvents(new BlockBreakListener(), this);
        getServer().getPluginManager().registerEvents(new BlockPlaceListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerLeaveListener(), this);
        getServer().getPluginManager().registerEvents(new DamageListener(), this);
        getServer().getPluginManager().registerEvents(new DeathListener(), this);

        getLogger().info("Plugin has been enabled");
    }

    @Override
    public void onDisable() {
        getServer().getScheduler().cancelTasks(this);
        getLogger().info("Plugin has been disabled");
    }

    public static Main getPlugin() {
        return plugin;
    }
}
