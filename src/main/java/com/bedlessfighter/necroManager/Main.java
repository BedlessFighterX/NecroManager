package com.bedlessfighter.necroManager;

import com.bedlessfighter.necroManager.commands.ClearEffectsCommand;
import com.bedlessfighter.necroManager.commands.ChatLockCommand;
import com.bedlessfighter.necroManager.managers.HubDisplayManager;
import com.bedlessfighter.necroManager.managers.AnnouncementManager;
import com.bedlessfighter.necroManager.managers.HologramManager;
import com.bedlessfighter.necroManager.commands.HologramCommand;
import com.bedlessfighter.necroManager.commands.ServersCommand;
import com.bedlessfighter.necroManager.commands.HideCommand;
import com.bedlessfighter.necroManager.commands.VanishCommand;
import com.bedlessfighter.necroManager.listeners.HubItemListener;
import com.bedlessfighter.necroManager.listeners.VanishListener;
import com.bedlessfighter.necroManager.listeners.MovementAbilityListener;
import com.bedlessfighter.necroManager.managers.MovementAbilityManager;
import com.bedlessfighter.necroManager.managers.PlayerHiderManager;
import com.bedlessfighter.necroManager.listeners.ChatControlListener;
import com.bedlessfighter.necroManager.listeners.CommandBlocklistListener;
import com.bedlessfighter.necroManager.listeners.ProtectionListener;
import com.bedlessfighter.necroManager.commands.RepairAllCommand;
import com.bedlessfighter.necroManager.commands.GamemodeAllCommand;
import com.bedlessfighter.necroManager.commands.TeleportAllCommand;
import com.bedlessfighter.necroManager.commands.TimeAllCommand;
import com.bedlessfighter.necroManager.commands.WeatherAllCommand;
import com.bedlessfighter.necroManager.commands.ExperienceAllCommand;
import com.bedlessfighter.necroManager.commands.SetLevelCommand;
import com.bedlessfighter.necroManager.commands.SetLevelAllCommand;
import com.bedlessfighter.necroManager.commands.ClearXPCommand;
import com.bedlessfighter.necroManager.commands.ClearXPAllCommand;
import com.bedlessfighter.necroManager.commands.SetFoodCommand;
import com.bedlessfighter.necroManager.commands.SetFoodAllCommand;
import com.bedlessfighter.necroManager.commands.ClearEffectsAllCommand;
import com.bedlessfighter.necroManager.commands.ExtinguishAllCommand;
import com.bedlessfighter.necroManager.commands.PlayerTimeCommand;
import com.bedlessfighter.necroManager.commands.TeleportToCommand;
import com.bedlessfighter.necroManager.commands.TeleportHereCommand;
import com.bedlessfighter.necroManager.commands.SetWorldSpawnCommand;
import com.bedlessfighter.necroManager.commands.WorldSpawnCommand;
import com.bedlessfighter.necroManager.commands.SetHealthCommand;
import com.bedlessfighter.necroManager.commands.ExperienceCommand;
import com.bedlessfighter.necroManager.commands.FeedAllCommand;
import com.bedlessfighter.necroManager.commands.HealAllCommand;
import com.bedlessfighter.necroManager.commands.KillCommand;
import com.bedlessfighter.necroManager.commands.HealthCommand;
import com.bedlessfighter.necroManager.commands.WorldInfoCommand;
import com.bedlessfighter.necroManager.commands.InvSortCommand;
import com.bedlessfighter.necroManager.commands.EnderSeeCommand;
import com.bedlessfighter.necroManager.commands.WorkBenchCommand;
import com.bedlessfighter.necroManager.commands.HatRemoveCommand;
import com.bedlessfighter.necroManager.commands.ClearChatSelfCommand;
import com.bedlessfighter.necroManager.commands.CompassCommand;
import com.bedlessfighter.necroManager.commands.CoordsCommand;
import com.bedlessfighter.necroManager.commands.NearCommand;
import com.bedlessfighter.necroManager.commands.MaptableCommand;
import com.bedlessfighter.necroManager.commands.GrindCommand;
import com.bedlessfighter.necroManager.commands.StoneCommand;
import com.bedlessfighter.necroManager.commands.SmithingCommand;
import com.bedlessfighter.necroManager.commands.EnchantingCommand;
import com.bedlessfighter.necroManager.commands.LoomCommand;
import com.bedlessfighter.necroManager.commands.CartographyCommand;
import com.bedlessfighter.necroManager.commands.GrindstoneCommand;
import com.bedlessfighter.necroManager.commands.StonecutterCommand;
import com.bedlessfighter.necroManager.commands.SmithingTableCommand;
import com.bedlessfighter.necroManager.commands.ClearOffhandCommand;
import com.bedlessfighter.necroManager.commands.DropCommand;
import com.bedlessfighter.necroManager.commands.ItemInfoCommand;
import com.bedlessfighter.necroManager.commands.ClearCursorCommand;
import com.bedlessfighter.necroManager.commands.ItemDurabilityCommand;
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
            HubDisplayManager.start();
            AnnouncementManager.start();
            HologramManager.load();
            getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

            getLogger().info("NecroManager v" + getPluginMeta().getVersion() + " enabled.");
        } catch (RuntimeException exception) {
            getLogger().severe("NecroManager could not finish startup: " + exception.getMessage());
            exception.printStackTrace();
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    private void registerCommands() {
        registerCommand("smithingtable", new SmithingTableCommand());
        registerCommand("stonecutter", new StonecutterCommand());
        registerCommand("grindstone", new GrindstoneCommand());
        registerCommand("cartography", new CartographyCommand());
        registerCommand("loom", new LoomCommand());
        registerCommand("enchanting", new EnchantingCommand());
        registerCommand("smithing", new SmithingCommand());
        registerCommand("stone", new StoneCommand());
        registerCommand("grind", new GrindCommand());
        registerCommand("maptable", new MaptableCommand());
        registerCommand("near", new NearCommand());
        registerCommand("coords", new CoordsCommand());
        registerCommand("compass", new CompassCommand());
        registerCommand("clearmychat", new ClearChatSelfCommand());
        registerCommand("removehat", new HatRemoveCommand());
        registerCommand("workbench", new WorkBenchCommand());
        registerCommand("endersee", new EnderSeeCommand());
        registerCommand("sortinv", new InvSortCommand());
        registerCommand("worldinfo", new WorldInfoCommand());
        registerCommand("health", new HealthCommand());
        registerCommand("kill", new KillCommand());
        registerCommand("healall", new HealAllCommand());
        registerCommand("feedall", new FeedAllCommand());
        registerCommand("experience", new ExperienceCommand());
        registerCommand("sethealth", new SetHealthCommand());
        registerCommand("worldspawn", new WorldSpawnCommand());
        registerCommand("setworldspawn", new SetWorldSpawnCommand());
        registerCommand("tphere", new TeleportHereCommand());
        registerCommand("teleportto", new TeleportToCommand());
        registerCommand("ptime", new PlayerTimeCommand());
        registerCommand("extinguishall", new ExtinguishAllCommand());
        registerCommand("cleareffectsall", new ClearEffectsAllCommand());
        registerCommand("repairall", new RepairAllCommand());
        registerCommand("gamemodeall", new GamemodeAllCommand());
        registerCommand("teleportall", new TeleportAllCommand());
        registerCommand("timeall", new TimeAllCommand());
        registerCommand("weatherall", new WeatherAllCommand());
        registerCommand("experienceall", new ExperienceAllCommand());
        registerCommand("setlevel", new SetLevelCommand());
        registerCommand("setlevelall", new SetLevelAllCommand());
        registerCommand("clearxp", new ClearXPCommand());
        registerCommand("clearxpall", new ClearXPAllCommand());
        registerCommand("setfood", new SetFoodCommand());
        registerCommand("setfoodall", new SetFoodAllCommand());
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
        registerCommand("clearoffhand", new ClearOffhandCommand());
        registerCommand("drop", new DropCommand());
        registerCommand("iteminfo", new ItemInfoCommand());
        registerCommand("clearcursor", new ClearCursorCommand());
        registerCommand("itemdurability", new ItemDurabilityCommand());
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
        registerCommand("chatlock", new ChatLockCommand());
        registerCommand("hologram", new HologramCommand());
        registerCommand("servers", new ServersCommand());
        registerCommand("hide", new HideCommand());
        registerCommand("vanish", new VanishCommand());
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
        } else {
            command.setTabCompleter(new com.bedlessfighter.necroManager.commands.CommandTabCompleter());
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
        manager.registerEvents(new ChatControlListener(), this);
        manager.registerEvents(new CommandBlocklistListener(), this);
        manager.registerEvents(new ProtectionListener(), this);
        manager.registerEvents(new HubItemListener(), this);
        manager.registerEvents(new VanishListener(), this);
        manager.registerEvents(new MovementAbilityListener(), this);
    }

    @Override
    public void onDisable() {
        getServer().getScheduler().cancelTasks(this);
        HologramManager.unload();
        MovementAbilityManager.clearAll();
        PlayerHiderManager.clear();
        com.bedlessfighter.necroManager.managers.VanishManager.clear();
        getServer().getMessenger().unregisterOutgoingPluginChannel(this, "BungeeCord");
        if (plugin == this) {
            plugin = null;
        }
        getLogger().info("NecroManager disabled.");
    }

    public static Main getPlugin() {
        return Objects.requireNonNull(plugin, "NecroManager is not enabled");
    }
}
