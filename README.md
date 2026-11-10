# NecroManager

A Paper plugin for basic lobby and spawn management. The current code includes join and quit messages, spawn handling, configurable gameplay restrictions, and join effects.

> This project is still early in development. Check the permissions and test it on a staging server before using it on a live server.

## Current features

- Set a spawn location and teleport players to it.
- Optionally send players to spawn when they join.
- Configure join and quit messages, including silent staff.
- Toggle block breaking, block placing, player damage, and fall damage restrictions.
- Prevent direct player attacks and player-fired projectile damage when player damage is disabled.
- Apply speed, regeneration, and slow-falling effects when players join.
- Customize or disable the decorated player-death message.
- Show players a configurable notice when fall damage is disabled.
- Reload the plugin configuration.

Most settings are in `src/main/resources/config.yml`. Runtime configuration reads and writes are centralized through `ConfigManager`. It provides fallback-aware typed getters, bounded numeric getters, defensive copies for locations and string lists, and centralized save/reload operations. Message formatting, legacy color codes, configurable prefixes, and `%placeholder%`` substitution are centralized through `MessageManager`. Permission decisions are centralized through `PermissionManager`, preserving the existing operator, admin, and feature-specific permission behavior.

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
| `/spawn`, `/lobby` | Teleport to the configured spawn | `necromanager.spawn` |
| `/setspawn`, `/setlobby` | Set the spawn at your current location | `necromanager.admin` |
| `/tpcenter` | Move to the center of the current block | `necromanager.admin` |
| `/reloadmanager` | Reload the configuration | `necromanager.admin` |

The command declarations and permission checks are both in the source. The `necromanager.admin` permission defaults to operators; `necromanager.spawn` is available to everyone by default.

## Permission nodes

| Permission | Purpose | Default |
|---|---|---|
| `necromanager.admin` | Administrative commands and restriction bypass | Operators |
| `necromanager.spawn` | Use `/spawn` | Everyone |
| `necromanager.blockbreak` | Bypass block-break restriction | Operators |
| `necromanager.blockplace` | Bypass block-place restriction | Operators |
| `necromanager.damage-others` | Bypass player-damage restriction | Operators |
| `necromanager.falldamage` | Bypass fall-damage restriction | Operators |
| `necromanager.silent-join` | Hide from NecroManager join messages | Operators |
| `necromanager.silent-leave` | Hide from NecroManager quit messages | Operators |

## Configuration

The default configuration is in `src/main/resources/config.yml`.

| Section | Description |
|---|---|
| `disabler` | Toggles block and damage restrictions |
| `effects` | Controls join effects |
| `joins` | Join messages, silent staff, and spawn-on-join |
| `quit` | Quit messages and silent staff |
| `deaths` | Enables or disables decorated death messages |
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
