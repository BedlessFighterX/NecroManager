# NecroManager

A Paper plugin for basic lobby and spawn management. The current code includes join and quit messages, spawn handling, configurable gameplay restrictions, and join effects.

> This project is still early in development. Check the permissions and test it on a staging server before using it on a live server.

## Current features

- Set a spawn location and teleport players to it.
- Optionally send players to spawn quietly when they join; missing spawn configuration does not generate a warning for every joining player.
- Configure join and quit messages, including silent staff and an optional first-join announcement.
- Optionally respawn players at the safe configured plugin spawn.
- Toggle block breaking, block placing, player damage, and fall damage restrictions.
- Prevent direct player attacks and player-fired projectile damage when player damage is disabled.
- Apply configurable-duration speed, regeneration, and slow-falling effects when players join.
- Customize or disable the decorated player-death message.
- Show players a configurable notice when fall damage is disabled.
- Heal yourself or an online player with `/heal [player]`.
- Restore hunger and saturation with `/feed [player]`.
- Extinguish yourself or an online player with `/extinguish [player]`.
- View your own or an online player's server-reported latency with `/ping [player]`.
- Clear active potion effects with `/cleareffects [player]`.
- Toggle flight for yourself or an online player with `/fly [player]`.
- Open your ender chest or inspect an online player's ender chest with `/enderchest [player]` (`/ec` alias).
- Inspect an online player's editable inventory with `/invsee <player>`; the target is notified.
- Reload the plugin configuration.

Spawn teleports are safety-checked by default: NecroManager prefers the configured point, then searches nearby for two clear blocks above a solid, non-hazardous floor within the world border. The search radius and vertical range are configurable; set `spawn.safe-teleport: false` to disable relocation checks and use the exact configured coordinates. If no safe destination is found, the teleport is refused.

The plugin spawn is stored independently from Minecraft's vanilla world spawn. Its structured configuration records the world name and UUID, coordinates, and rotation; older serialized Bukkit locations remain readable and are upgraded the next time `/setspawn` is used. `spawn.update-world-spawn` defaults to `false`; enable it only when `/setspawn` should also move the vanilla world spawn. An unavailable or invalid configured world is treated as an unset spawn rather than teleporting to an unintended location.

Most settings are in `src/main/resources/config.yml`. Runtime configuration reads and writes are centralized through `ConfigManager`. It provides fallback-aware typed getters, bounded numeric getters, defensive copies for locations and string lists, and centralized save/reload operations. Message formatting, legacy color codes, configurable prefixes, and `%placeholder%` substitution are centralized through `MessageManager`. Player lifecycle messages support `%player%`, `%displayname%`, `%world%`, and `%online%`. Permission decisions are centralized through `PermissionManager`, preserving the existing operator, admin, and feature-specific permission behavior. Startup now registers commands and listeners in dedicated lifecycle methods and disables the plugin with a logged error if required command metadata is missing.

## Requirements

The current Gradle build is configured for:

| Component | Version |
|---|---|
| Paper API | 1.21.10 snapshot |
| Plugin API version | 1.21 |
| Java | 21 |
| Build | Gradle Wrapper |

The project has not been checked against older Minecraft versions or Spigot. Use a compatible Paper server and test the plugin before deploying it.

## Commands

| Command | Description | Permission |
|---|---|---|
| `/spawn`, `/lobby` | Teleport to the configured spawn; admins may use `/spawn <player>` with online-player tab completion | `necromanager.spawn` (self), `necromanager.admin` (other players) |
| `/setspawn`, `/setlobby` | Set the spawn at your current location | `necromanager.admin` |
| `/tpcenter` | Move to the center of the current block | `necromanager.admin` |
| `/heal [player]` | Restore your health or heal an online player | `necromanager.heal` or admin |
| `/feed [player]` | Restore your hunger or feed an online player | `necromanager.feed` or admin |
| `/extinguish [player]` | Extinguish yourself or an online player | `necromanager.extinguish` or admin |
| `/ping [player]` | Show your ping or an online player's ping | `necromanager.ping` or admin |
| `/cleareffects [player]` | Remove active potion effects from yourself or an online player | `necromanager.cleareffects` or admin |
| `/fly [player]` | Toggle flight for yourself or an online player | `necromanager.fly` or admin |
| `/enderchest [player]`, `/ec` | Open your ender chest or an online player's ender chest | `necromanager.enderchest` or admin |
| `/invsee <player>` | Open an online player's inventory for inspection | `necromanager.invsee` or admin |
| `/reloadmanager` | Reload the configuration | `necromanager.admin` |

The command declarations and permission checks are both in the source. The `necromanager.admin` permission defaults to operators; `necromanager.spawn` is available to everyone by default.

## Permission nodes

| Permission | Purpose | Default |
|---|---|---|
| `necromanager.admin` | Administrative commands and restriction bypass | Operators |
| `necromanager.heal` | Use `/heal` | Operators |
| `necromanager.feed` | Use `/feed` | Operators |
| `necromanager.extinguish` | Use `/extinguish` | Operators |
| `necromanager.ping` | Use `/ping` | Everyone |
| `necromanager.cleareffects` | Use `/cleareffects` | Operators |
| `necromanager.fly` | Use `/fly` | Operators |
| `necromanager.enderchest` | Use `/enderchest` | Operators |
| `necromanager.invsee` | Use `/invsee` | Operators |
| `necromanager.spawn` | Use `/spawn` | Everyone |
| `necromanager.blockbreak` | Bypass block-break restriction | Operators |
| `necromanager.blockplace` | Bypass block-place restriction | Operators |
| `necromanager.damage-others` | Bypass player-damage restriction | Operators |
| `necromanager.falldamage` | Bypass fall-damage restriction | Operators |
| `necromanager.silent-join` | Hide from NecroManager join messages | Operators |
| `necromanager.silent-leave` | Hide from NecroManager quit messages | Operators |

## Configuration

The default configuration is in `src/main/resources/config.yml`. Global gameplay restrictions can be overridden per world under `world-overrides`, using the world's UUID as the key. For example, setting `world-overrides.<world-uuid>.disabler.blockbreak: false` allows block breaking in that world while retaining the global setting elsewhere. World UUIDs keep overrides stable when world folders are renamed. Damage and fall restrictions now apply only to players; mob-versus-mob and player-versus-mob combat are not blocked by the player-damage setting. The same UUID override structure can control `deaths.enabled` per world. Join-effect duration and amplifier values are configurable under `effects`.

| Section | Description |
|---|---|
| `disabler` | Global block and damage restriction defaults |
| `world-overrides` | Optional per-world restriction overrides keyed by world UUID |
| `effects` | Controls join effects |
| `joins` | Join messages, silent staff, first-join announcements, and spawn-on-join |
| `respawn` | Optional respawn at the configured safe plugin spawn |
| `quit` | Quit messages and silent staff |
| `deaths` | Enables or disables decorated death messages |
| `spawn` | Controls persistent plugin spawn storage and optional vanilla world-spawn updates |
| `messages` | Message prefix and plugin messages |

Back up `plugins/NecroManager/config.yml` before editing it.

## Build and run

Requires JDK 21.

Build:

```bash
./gradlew clean build
```

Start the configured local Paper server:

```bash
./gradlew runServer
```

The JAR is created in `build/libs/`.

## Notes

There are currently no automated tests or license file in the repository. Test permission behavior and gameplay restrictions on a staging server before deploying. Add a license before allowing others to reuse or redistribute the project.
