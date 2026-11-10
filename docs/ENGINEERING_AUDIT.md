# Engineering Audit — Phase 1.1

Audit scope: current `codex/initial-implementation` source and resource files. This is a source inspection only; no compilation or runtime tests were performed.

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

## Verification status

- Source files and primary resource/build files inspected.
- Configuration call sites migrated and reviewed for leftover local `config` references.
- No build, compilation, tests, or live-server validation performed, per project instruction.
