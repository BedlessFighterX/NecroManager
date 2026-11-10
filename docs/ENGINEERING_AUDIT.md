# Engineering Audit — Phase 1.1

Audit scope: current `main` source and resource files. This is a source inspection only; no compilation or runtime tests were performed.

## Current architecture

- One `JavaPlugin` entry point: `Main`.
- Commands are registered individually in `Main.onEnable()`.
- Event listeners cover block breaking/placing, player joins/leaves, entity damage, and player deaths.
- `SpawnManager` persists a Bukkit `Location` in the plugin configuration.
- `EffectManager` wraps potion-effect add/remove operations.
- Configuration reads and writes are routed through `ConfigManager`; the facade owns typed defaults, location cloning, reload, and save operations.
- Paper API dependency targets 1.21.10 snapshots; the run-paper task is configured to launch Minecraft 1.21.

## Findings

### High priority

1. **Damage restriction scope is broader than its name.** `DamageListener.onDamageByEntity` resolves a player attacker and cancels the event without checking whether the victim is a player. With `disabler.damage-others` enabled, player attacks against non-player entities can also be blocked.
2. **Fall-damage restriction affects non-player entities too.** The listener cancels fall damage for every entity; only players can use the configured permission bypass. Confirm whether the intended behavior is player-only before changing it.
3. **Spawn-on-join can report a missing spawn to every joining player.** This may be intentional, but it creates noisy feedback when spawn has not been configured.
4. **Potion effects use effectively permanent durations.** Join effects use `Integer.MAX_VALUE`; changing or disabling settings after a player has joined does not remove an already-applied effect.

### Medium priority

5. **Configuration concerns are duplicated.** Prefix lookup, color translation, default messages, and permission checks are repeated across multiple classes. This makes consistent behavior and future maintenance harder.
6. **Command validation is inconsistent.** Commands independently handle console senders, permissions, and feedback; some use `@NotNull` annotations while others do not.
7. **Spawn state and world spawn are coupled.** `SpawnManager.setSpawn` stores the plugin spawn and also changes the Bukkit world spawn. These should be an explicit, documented choice rather than an incidental side effect.
8. **Teleport feedback is inconsistent.** `TeleportCenterCommand` does not check the boolean result of `Player.teleport`, unlike `SpawnManager`.
9. **Death-message behavior is global.** The death listener decorates the server-wide death message and can suppress it entirely when disabled. There is no per-world or per-player control.
10. **Build target settings differ.** The compile-only Paper API is 1.21.10 snapshot while `runServer` requests Minecraft 1.21. The intended supported server version should be made consistent.

### Lower priority / cleanup

11. `EffectManager` is a non-final utility class with static methods and an implicit public constructor.
12. Several listener and command classes repeat identical color/message helper methods.
13. The plugin has no automated test suite or CI workflow in the inspected baseline.
14. The configuration has no schema/version migration mechanism; future setting changes need defaults and migration-safe handling.

## Ordered implementation sequence

1. **1.2 — Configuration access:** introduce a small, centralized configuration facade with safe typed getters and defaults; preserve existing config keys.
2. **1.3 — Message utility:** centralize color translation, prefix composition, and supported placeholders.
3. **1.4 — Permission utility:** centralize admin/op and feature permission checks without changing permission node names.
4. **1.5 — Lifecycle:** make initialization and shutdown responsibilities explicit and guard startup failures cleanly.
5. **Phase 2:** address spawn persistence, teleport semantics, command argument validation, and optional teleport-to-player behavior.
6. **Phase 3:** correct and clarify gameplay restriction scope, then add world-specific behavior only after global behavior is stable.

## Decisions to preserve during refactoring

- Keep existing command names and aliases: `/spawn`, `/lobby`, `/setspawn`, `/setlobby`, `/tpcenter`, and `/reloadmanager`.
- Keep existing permission node names.
- Keep current configuration keys unless a migration path is added.
- Do not silently change whether restrictions apply to mobs or whether setting the plugin spawn changes the Bukkit world spawn; make those behaviors explicit in the relevant implementation step.

## Phase 1.2 implementation notes

- Added `managers/ConfigManager.java` as the single configuration access facade.
- Migrated commands, gameplay listeners, join/quit/death listeners, and spawn persistence to the facade.
- Location reads return a clone so callers cannot accidentally mutate the configuration's stored location object.
- Missing string values use the caller-supplied fallback; typed getters accept explicit fallback values.

## Step 1.2 completion — Configuration access

- Extended `ConfigManager` with bounded integer and finite bounded decimal getters.
- Added a long getter and defensive-copy string-list getter.
- Kept existing configuration paths and existing call sites compatible.
- Documented centralized access and safe getter behavior in the README.
- Existing listeners, commands, and managers use `ConfigManager` rather than reading the plugin configuration directly; `Main` retains lifecycle-level default-config initialization.

## Step 1.3 completion — Message utility

- Added `MessageManager` for ampersand color translation, the configured global prefix, configured message lookup, placeholder substitution, and consistent sender delivery.
- Migrated commands, gameplay listeners, join/quit/death announcements, and spawn feedback to the shared utility.
- Preserved existing message configuration paths and default text.
- Added consistent teleport-failure feedback to `/tpcenter` while migrating it.
- Updated README documentation.

## Step 1.3 completion — Message utility

- Added `MessageManager` for ampersand color translation, the configured prefix, configured message lookup, placeholder substitution, and consistent sender delivery.
- Migrated commands, gameplay listeners, join/quit/death announcements, and spawn feedback to the shared utility.
- Preserved existing message configuration paths and default text.
- Added consistent teleport-failure feedback to `/tpcenter` while migrating it.

## Step 1.4 completion — Permission utility

- Added `PermissionManager` with shared admin, inherited feature-permission, and direct-permission checks.
- Migrated commands, spawn management, gameplay restriction listeners, and silent join/quit checks.
- Preserved existing permission nodes and operator/admin bypass semantics.

## Step 1.5 completion — Plugin lifecycle

- Split command and listener registration into dedicated lifecycle methods.
- Added startup failure handling: missing required command metadata is logged and the plugin is disabled rather than leaving a partially initialized plugin active.
- Made the static plugin reference clear only when the owning plugin instance shuts down.
- Added version-aware startup logging and retained scheduler task cleanup on disable.

## Phase 2.1 completion — Persistent spawn storage

- Replaced new spawn writes with an explicit structured format containing world name, world UUID, coordinates, yaw, and pitch.
- Kept compatibility with existing serialized Bukkit `Location` values; they remain readable and are converted to the structured format when an administrator next runs `/setspawn`.
- Validate world resolution, finite coordinates, world height bounds, and Minecraft horizontal coordinate limits before returning or saving a spawn. An unloaded or missing world is treated as an unset spawn.
- Decoupled the plugin spawn from the vanilla world spawn. `spawn.update-world-spawn` defaults to `false` and must be explicitly enabled to change Bukkit's world spawn.
- Updated the README and default configuration with the new behavior.

## Phase 2.2–2.4 completion — Spawn command surface

- Confirmed `/spawn` and `/lobby` share the same executor and permission node.
- Confirmed `/setspawn` and `/setlobby` share the same executor and administrative permission. Updated command descriptions to distinguish the plugin spawn from the vanilla world spawn.
- Added `/spawn <player>` for online targets. The sender must be an administrator; target lookup is exact and online-only, teleport failure is reported, and both target and sender receive configurable feedback. Console senders may use the target form.
- Added a bounded usage check and documented the command syntax.

## Phase 2.5 completion — Spawn-on-join behavior

- Added a reusable spawn teleport operation with optional player-facing feedback and a success result.
- Join handling now uses the quiet path: a missing/unavailable spawn does not send repeated setup warnings to every joining player, while explicit `/spawn` requests retain clear feedback.
- Spawn-on-join remains controlled by `joins.spawn-on-join`; no implicit world-spawn fallback is introduced.

## Phase 2.6 completion — Teleport safety

- Added safe destination resolution for self-service, administrative, and join-triggered spawn teleports.
- The configured location is preferred when it has clear feet/head space, a solid non-hazardous floor, and lies inside the world border. Otherwise, a bounded nearby search checks the same conditions.
- Hazard checks reject liquids, fire, lava, cactus, magma, campfires, powder snow, berry bushes, wither roses, pointed dripstone, and big dripleaf stems.
- Added configurable safety toggle, horizontal search radius, and vertical search range. Safe teleportation is enabled by default; a missing safe destination is reported distinctly and no teleport is attempted.
- All three spawn teleport entry points use the shared safe destination resolver.

## Phase 3.1–3.2 completion — Restriction scope and world overrides

- Restricted `disabler.damage-others` to player victims, including direct attacks and player-fired projectiles. Player attacks against mobs are no longer unintentionally cancelled.
- Restricted `disabler.fall-damage` to player entities; mob fall damage is no longer affected by a player-facing setting.
- Added `RestrictionManager` to resolve global restriction defaults with optional per-world overrides.
- Block break, block place, player damage, and player fall-damage restrictions now support `world-overrides.<world-uuid>.<setting-path>`. UUID keys remain stable across world renames.
- Damage handlers now ignore already-cancelled events to avoid duplicate handling and messages.

## Phase 2.6 completion — Teleport safety

- Added safe spawn destination resolution for self-service, administrative, and join-triggered teleports.
- The configured point is preferred when the feet/head blocks are clear, the floor is solid and non-hazardous, and the point is inside the world border. A bounded nearby search is used when needed.
- Added configurable safety toggle, horizontal radius, and vertical range. Unsafe destinations are refused with distinct feedback.

## Phase 3.1–3.2 completion — Gameplay restriction scope and world overrides

- Player-damage restriction now applies only to player victims, including direct and player-fired projectile attacks. Player-versus-mob combat remains available.
- Fall-damage restriction now applies only to players rather than all entities.
- Added `RestrictionManager` and UUID-keyed per-world overrides for block breaking, block placing, player damage, and player fall damage.
- Block and damage handlers ignore events already cancelled by another plugin.

## Phase 4.1–4.2 completion — Player lifecycle configuration

- Replaced effectively permanent join potion effects with a configurable duration (default 24 hours, bounded to 1 second–7 days) and per-effect amplifier settings.
- Centralized join effect application in `EffectManager`; the join listener no longer duplicates potion-effect configuration logic.
- Made decorated death-message enablement world-overridable through the existing UUID-keyed override resolver.

## Phase 4.3–4.4 completion — First join and respawn handling

- Added an optional first-join broadcast with configurable text; it is disabled by default and continues to respect the global and silent-staff join-message settings.
- Added a dedicated respawn listener. When `respawn.use-plugin-spawn` is enabled, players respawn at the shared safe plugin spawn; if no safe destination exists, vanilla respawn behavior is left untouched.
- Registered the respawn listener in the plugin lifecycle.

## Phase 4.5–4.6 completion — Player messages and command ergonomics

- Added shared player placeholders for `%player%`, `%displayname%`, `%world%`, and `%online%`; join, quit, and death messages now use the same placeholder source.
- Added permission-aware online-player tab completion to `/spawn <player>`.
- Moved self-spawn permission enforcement into the command executor so administrative `/spawn <player>` remains available to admins who hold `necromanager.admin` without separately holding `necromanager.spawn`.
- Corrected the administrative teleport success placeholder key to match the message formatter.

## Phase 5.1 — Administrative command review

- Reviewed reload and teleport-center command permission handling and feedback.
- Registered tab completers through the shared command registration path when a command executor also implements `TabCompleter`.

## Phase 5.2 — Administrative command hardening

- Added strict no-argument validation and usage feedback to `/setspawn`, `/reloadmanager`, and `/tpcenter`.
- Added guarded configuration reload feedback: runtime reload failures are logged and reported with a configurable player-facing message.
- Kept administrative permission checks in command executors so denial behavior remains consistent with plugin-level declarations.

## Phase 5.3 — Player administration commands\n\n- Added `/heal [player]` with self-targeting, exact online-player targeting, console support when a target is supplied, and permission-aware tab completion. Healing uses the target's `MAX_HEALTH` attribute rather than assuming a fixed health cap.\n- Added `/feed [player]` with the same targeting, console, permission, and tab-completion behavior; restores food, saturation, and exhaustion.\n- Added dedicated `necromanager.heal` and `necromanager.feed` permission nodes (operator default); administrators inherit access through `PermissionManager`.\n- Added configurable success, target-notification, invalid-target, and health-attribute failure messages.\n\n## Phase 5.4 — Potion-effect administration\n\n- Added `/cleareffects [player]` to remove every active potion effect from the sender or an exact online-player target.\n- Added console targeting, permission-aware online-player tab completion, and a dedicated `necromanager.cleareffects` permission node (operator default; admins inherit access).\n- Added configurable feedback for self and administrative target use, including the number of effects removed.\n\n## Phase 5.5 — Flight administration\n\n- Added `/fly [player]` to toggle allow-flight for the sender or an exact online-player target. Disabling flight also safely clears active flying state.\n- Added console target support, permission-aware tab completion, and a dedicated `necromanager.fly` permission node (operator default; admins inherit access).\n- Added configurable feedback for self-service and administrative target use.\n\n## Phase 5.6 — Ender chest administration

- Added `/enderchest [player]` with `/ec` alias to open the sender's or an exact online target's ender chest.
- Added console targeting, permission-aware online-player tab completion, and dedicated `necromanager.enderchest` permission (operator default; admins inherit access).
- Added configurable feedback to both the target and command sender when an administrator opens another player's ender chest.

## Verification status

- Source files and primary resource/build files inspected.
- Configuration call sites migrated and reviewed for leftover local `config` references.
- No build, compilation, tests, or live-server validation performed, per project instruction.
