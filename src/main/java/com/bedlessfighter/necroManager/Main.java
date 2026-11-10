package com.bedlessfighter.necroManager;

import com.bedlessfighter.necroManager.commands.ReloadCommand;
import com.bedlessfighter.necroManager.commands.SetSpawnCommand;
import com.bedlessfighter.necroManager.commands.SpawnCommand;
import com.bedlessfighter.necroManager.commands.TeleportCenterCommand;
import com.bedlessfighter.necroManager.listeners.*;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class Main extends JavaPlugin {

    private static Main plugin;

    @Override
    public void onEnable() {
        plugin = this;
        saveDefaultConfig();

        registerCommand("reloadmanager", new ReloadCommand());
        registerCommand("setspawn", new SetSpawnCommand());
        registerCommand("spawn", new SpawnCommand());
        registerCommand("tpcenter", new TeleportCenterCommand());

        getServer().getPluginManager().registerEvents(new BlockBreakListener(), this);
        getServer().getPluginManager().registerEvents(new BlockPlaceListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerLeaveListener(), this);
        getServer().getPluginManager().registerEvents(new DamageListener(), this);
        getServer().getPluginManager().registerEvents(new DeathListener(), this);

        getLogger().info("NecroManager enabled");
    }

    private void registerCommand(String name, org.bukkit.command.CommandExecutor executor) {
        PluginCommand command = getCommand(name);
        if (command == null) {
            throw new IllegalStateException("Command '" + name + "' is missing from plugin.yml");
        }
        command.setExecutor(executor);
    }

    @Override
    public void onDisable() {
        getServer().getScheduler().cancelTasks(this);
        plugin = null;
        getLogger().info("NecroManager disabled");
    }

    public static Main getPlugin() {
        return Objects.requireNonNull(plugin, "NecroManager is not enabled");
    }
}
