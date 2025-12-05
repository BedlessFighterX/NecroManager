package com.bedlessfighter.spaceManager;

import com.bedlessfighter.spaceManager.commands.ReloadCommand;
import com.bedlessfighter.spaceManager.commands.SetSpawnCommand;
import com.bedlessfighter.spaceManager.commands.SpawnCommand;
import com.bedlessfighter.spaceManager.listeners.*;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    private static Main plugin;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        plugin = this;

        if (getCommand("reloadmanager") != null)
            getCommand("reloadmanager").setExecutor(new ReloadCommand());
        if (getCommand("setspawn") != null)
            getCommand("setspawn").setExecutor(new SetSpawnCommand());
        if (getCommand("spawn") != null)
            getCommand("spawn").setExecutor(new SpawnCommand());

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
