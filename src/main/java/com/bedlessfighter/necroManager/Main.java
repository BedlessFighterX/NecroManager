package com.bedlessfighter.necroManager;

import com.bedlessfighter.necroManager.commands.ClearEffectsCommand;
import com.bedlessfighter.necroManager.commands.ClearArmorCommand;
import com.bedlessfighter.necroManager.commands.ClearHotbarCommand;
import com.bedlessfighter.necroManager.commands.SkullCommand;
import com.bedlessfighter.necroManager.commands.RenameCommand;
import com.bedlessfighter.necroManager.commands.LoreCommand;
import com.bedlessfighter.necroManager.commands.ClearInventoryCommand;
import com.bedlessfighter.necroManager.commands.MoreCommand;
import com.bedlessfighter.necroManager.commands.HatCommand;
import com.bedlessfighter.necroManager.commands.CraftCommand;
import com.bedlessfighter.necroManager.commands.AnvilCommand;
import com.bedlessfighter.necroManager.commands.MessageCommand;
import com.bedlessfighter.necroManager.commands.ReplyCommand;
import com.bedlessfighter.necroManager.commands.IgnoreCommand;
import com.bedlessfighter.necroManager.commands.SocialSpyCommand;
import com.bedlessfighter.necroManager.commands.WhoisCommand;
import com.bedlessfighter.necroManager.commands.FeedCommand;
import com.bedlessfighter.necroManager.commands.ExtinguishCommand;
import com.bedlessfighter.necroManager.commands.EnderChestCommand;
import com.bedlessfighter.necroManager.commands.FlyCommand;
import com.bedlessfighter.necroManager.commands.HealCommand;
import com.bedlessfighter.necroManager.commands.GamemodeCommand;
import com.bedlessfighter.necroManager.commands.PingCommand;
import com.bedlessfighter.necroManager.commands.GetPosCommand;
import com.bedlessfighter.necroManager.commands.RepairCommand;
import com.bedlessfighter.necroManager.commands.SpeedCommand;
import com.bedlessfighter.necroManager.commands.ListCommand;
import com.bedlessfighter.necroManager.commands.MotdCommand;
import com.bedlessfighter.necroManager.commands.RulesCommand;
import com.bedlessfighter.necroManager.commands.ClearChatCommand;
import com.bedlessfighter.necroManager.commands.BroadcastCommand;
import com.bedlessfighter.necroManager.commands.WeatherCommand;
import com.bedlessfighter.necroManager.commands.NightCommand;
import com.bedlessfighter.necroManager.commands.DayCommand;
import com.bedlessfighter.necroManager.commands.BottomCommand;
import com.bedlessfighter.necroManager.commands.TopCommand;
import com.bedlessfighter.necroManager.commands.InventorySeeCommand;
import com.bedlessfighter.necroManager.commands.ReloadCommand;
import com.bedlessfighter.necroManager.commands.SetSpawnCommand;
import com.bedlessfighter.necroManager.commands.SpawnCommand;
import com.bedlessfighter.necroManager.commands.TeleportCenterCommand;
import com.bedlessfighter.necroManager.listeners.BlockBreakListener;
import com.bedlessfighter.necroManager.listeners.BlockPlaceListener;
import com.bedlessfighter.necroManager.listeners.DamageListener;
import com.bedlessfighter.necroManager.listeners.DeathListener;
import com.bedlessfighter.necroManager.listeners.PlayerJoinListener;
import com.bedlessfighter.necroManager.listeners.PlayerLeaveListener;
import com.bedlessfighter.necroManager.listeners.PlayerRespawnListener;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

/**
 * Main lifecycle and dependency-registration entry point for NecroManager.
 */
public final class Main extends JavaPlugin {

    private static Main plugin;

    @Override
    public void onEnable() {
        plugin = this;

        try {
            saveDefaultConfig();
            registerCommands();
            registerListeners();

            getLogger().info("NecroManager v" + getPluginMeta().getVersion() + " enabled.");
        } catch (RuntimeException exception) {
            getLogger().severe("NecroManager could not finish startup: " + exception.getMessage());
            exception.printStackTrace();
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    private void registerCommands() {
        registerCommand("reloadmanager", new ReloadCommand());
        registerCommand("heal", new HealCommand());
        registerCommand("gamemode", new GamemodeCommand());
        registerCommand("ping", new PingCommand());
        registerCommand("getpos", new GetPosCommand());
        registerCommand("repair", new RepairCommand());
        registerCommand("speed", new SpeedCommand());
        registerCommand("top", new TopCommand());
        registerCommand("bottom", new BottomCommand());
        registerCommand("day", new DayCommand());
        registerCommand("night", new NightCommand());
        registerCommand("weather", new WeatherCommand());
        registerCommand("broadcast", new BroadcastCommand());
        registerCommand("clearchat", new ClearChatCommand());
        registerCommand("rules", new RulesCommand());
        registerCommand("motd", new MotdCommand());
        registerCommand("list", new ListCommand());
        registerCommand("msg", new MessageCommand());
        registerCommand("reply", new ReplyCommand());
        registerCommand("ignore", new IgnoreCommand());
        registerCommand("socialspy", new SocialSpyCommand());
        registerCommand("whois", new WhoisCommand());
        registerCommand("clearinventory", new ClearInventoryCommand());
        registerCommand("cleararmor", new ClearArmorCommand());
        registerCommand("clearhotbar", new ClearHotbarCommand());
        registerCommand("skull", new SkullCommand());
        registerCommand("rename", new RenameCommand());
        registerCommand("lore", new LoreCommand());
        registerCommand("more", new MoreCommand());
        registerCommand("hat", new HatCommand());
        registerCommand("craft", new CraftCommand());
        registerCommand("anvil", new AnvilCommand());
        registerCommand("feed", new FeedCommand());
        registerCommand("extinguish", new ExtinguishCommand());
        registerCommand("enderchest", new EnderChestCommand());
        registerCommand("invsee", new InventorySeeCommand());
        registerCommand("fly", new FlyCommand());
        registerCommand("cleareffects", new ClearEffectsCommand());
        registerCommand("setspawn", new SetSpawnCommand());
        registerCommand("spawn", new SpawnCommand());
        registerCommand("tpcenter", new TeleportCenterCommand());
    }

    private void registerCommand(String name, CommandExecutor executor) {
        PluginCommand command = getCommand(name);
        if (command == null) {
            throw new IllegalStateException(
                    "Required command '" + name + "' is missing from plugin.yml");
        }
        command.setExecutor(executor);
        if (executor instanceof org.bukkit.command.TabCompleter tabCompleter) {
            command.setTabCompleter(tabCompleter);
        }
    }

    private void registerListeners() {
        PluginManager manager = getServer().getPluginManager();
        manager.registerEvents(new BlockBreakListener(), this);
        manager.registerEvents(new BlockPlaceListener(), this);
        manager.registerEvents(new PlayerJoinListener(), this);
        manager.registerEvents(new PlayerLeaveListener(), this);
        manager.registerEvents(new PlayerRespawnListener(), this);
        manager.registerEvents(new DamageListener(), this);
        manager.registerEvents(new DeathListener(), this);
    }

    @Override
    public void onDisable() {
        getServer().getScheduler().cancelTasks(this);
        if (plugin == this) {
            plugin = null;
        }
        getLogger().info("NecroManager disabled.");
    }

    public static Main getPlugin() {
        return Objects.requireNonNull(plugin, "NecroManager is not enabled");
    }
}
