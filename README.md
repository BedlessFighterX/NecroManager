# NecroManager

<p align="center">A Paper plugin prototype for lobby, spawn, and lightweight server-management behavior.</p>

<p align="center">
  <img src="https://img.shields.io/badge/Platform-Paper-2D2D2D?style=flat-square" alt="Paper" />
  <img src="https://img.shields.io/badge/Minecraft_API-1.21-2563EB?style=flat-square" alt="Minecraft API 1.21" />
  <img src="https://img.shields.io/badge/Java-21-ED8B00?style=flat-square&logo=openjdk&logoColor=white" alt="Java 21" />
  <img src="https://img.shields.io/badge/Build-Gradle-02303A?style=flat-square&logo=gradle&logoColor=white" alt="Gradle" />
</p>

> **Project status:** Early-stage. This README describes the behavior currently visible in the source; it is not a production-readiness or security guarantee. Test on a staging server and review permissions before deployment.

## Overview

NecroManager is a Paper server plugin built around basic lobby and spawn workflows. Its current source includes spawn management, join/quit message handling, configurable block and damage restrictions, and player effects applied when a player joins.

## Current features

- **Spawn management** — save a spawn location, teleport players to it, and optionally send players there when they join.
- **Join and quit messages** — configure message formats and suppress messages for staff.
- **Gameplay restrictions** — toggle block breaking, block placing, player-versus-player damage, and fall damage behavior.
- **Join effects** — independently toggle speed, regeneration, and slow-falling effects.
- **Administrative commands** — reload the configuration and center an administrator at the current block location.

Feature behavior is controlled primarily through `src/main/resources/config.yml`.

## Compatibility

The current build configuration uses:

| Component | Configured target |
|---|---|
| Server API | Paper API `1.21.10-R0.1-SNAPSHOT` |
| Plugin descriptor | `api-version: '1.21'` |
| Java source / target | Java 21 |
| Build system | Gradle Wrapper |

The project has not been documented or validated here against Spigot or older Minecraft versions. Use the Paper version compatible with the configured API, and verify startup and behavior on your own staging server.

## Commands

| Command | Purpose | Permission declared in `plugin.yml` |
|---|---|---|
| `/spawn` | Teleport to the configured spawn location | `necromanager.admin` |
| `/setspawn` | Save the current centered location as spawn | `necromanager.admin` |
| `/tpcenter` | Teleport yourself to the center of the current block | Checked by the command handler: `necromanager.admin` |
| `/reloadmanager` | Reload the plugin configuration | `necromanager.admin` |

The command handlers also perform permission checks in code. Review the command declarations and permission checks before granting access to non-operator groups.

## Configuration

The default configuration is stored in `src/main/resources/config.yml`.

| Section | Controls |
|---|---|
| `disabler` | Block placement, block breaking, player damage, and fall-damage toggles |
| `effects` | Global effects switch plus speed, regeneration, and slow-falling toggles |
| `joins` | Join announcements, spawn-on-join behavior, and silent staff |
| `quit` | Quit announcements and silent staff |
| `messages` | Plugin prefix and configurable feedback messages |

Back up `plugins/NecroManager/config.yml` before making changes. Review all permission-related behavior on a test server before using this plugin with a live player base.

## Build and run locally

**Requirements:** JDK 21.

Build the plugin:

```bash
./gradlew clean build
```

Run the configured Paper development server:

```bash
./gradlew runServer
```

The project configures the development server task for Minecraft 1.21. The resulting JAR is written to `build/libs/`.

## Repository notes

- No automated test source set is currently present in the repository.
- A `LICENSE` file is not currently present. Add an explicit license before inviting reuse or redistribution.
- Permission declarations and handler checks should be reviewed together; do not assume every non-operator permission combination behaves as intended.

---

<p align="center">
  <sub>Keep configuration explicit, permissions deliberate, and server changes tested before release.</sub>
</p>
