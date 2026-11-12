# NecroManager — Complete Feature Roadmap

This document tracks the feature scope supplied for NecroManager. The implementation is native to NecroManager; this roadmap does not require companion plugins.

## Delivery principles

- Preserve the plugin identity as `NecroManager` and use the `necromanager.*` permission namespace.
- Implement shared services before command families: configuration, messages/placeholders, permissions, persistence, player state, teleport safety, inventory UI, scheduler, and audit logging.
- Keep dangerous actions permission-gated, validate targets/arguments, and make destructive operations explicit.
- Prefer Paper APIs; isolate optional integrations behind internal interfaces and keep the core functional without them.
- Track each feature as implemented, documented, source-reviewed, and verified by the automated build/release pipeline.

## Phase A — Foundation and hub

- [x] Central configuration, messages, permissions, lifecycle, safe plugin spawn, join/quit/death/respawn.
- [x] Global and UUID-keyed world restriction overrides.
- [x] Join effects, first-join message, configurable spawn-on-join and respawn.
- [x] Heal, feed, clear effects, fly, enderchest, invsee.
- [x] Hub scoreboard and tab header/footer with configurable refresh/placeholders.
- [x] Scheduled announcements with online-player threshold and placeholder formatting.
- [x] Global chat lock with staff/admin bypass.
- [x] Configurable player command blocklist with permission bypass.
- [x] Scheduled announcements with sound, title/actionbar delivery, minimum-online threshold, per-player opt-out permission, and disabled-world exclusions.
- [x] Persistent hologram create/delete/list commands and add/set/remove line editing with YAML storage and restart-safe TextDisplay rendering.
- [x] Configurable custom join items with persistent action metadata and slot placement.
- [x] Server-selector inventory with configurable entries and BungeeCord proxy connection requests.
- [x] Player-hider toggle command and join-item action, including visibility synchronization for newly joining players.
- [x] Protected selector inventory interactions (click/drag) and plugin-channel shutdown cleanup.
- [x] Configurable material-based launchpads with directional velocity, vertical boost, sound, and per-player cooldown.
- [x] Permission-controlled double jump with configurable boost, sound, cooldown, survival/adventure eligibility, and flight-state cleanup.
- [x] Staff vanish toggle, target management, hidden-player list, join synchronization, and silent quit.
- [x] Configurable block-interaction, hunger, item-drop, item-pickup and mob-spawning protections with staff bypass permissions for player-specific restrictions.
- [x] Void, fire and drowning damage prevention; offhand swap restriction; weather lock; fire spread/burn and leaf-decay protections.
- [x] Configurable anti-swear word filtering with replacement text, staff alerts and bypass permission.
- [x] Configurable player command blocklist with bypass, enable/disable setting, custom denial text, persistent add/remove/list administration and argument-aware tab completion.
- [x] Chat lock with explicit on/off/status controls, bypass permission, configurable denial text, and configurable global/self chat clearing.
- [x] Alert-only World Downloader handshake detection on explicit WDL plugin-message channels, staff alerts, per-player bypass, session deduplication, and no-auto-punishment safeguards.
- [x] Configurable join MOTD, title/subtitle, actionbar, sound and firework presentation; custom join/quit messages.
- [x] Shared, argument-aware tab completion for commands that accept player names, modes, weather/time values, or operation options; retain specialized command completers.
- [x] Configurable ordered join action engine with message, broadcast, title, actionbar, sound, player/console command, gamemode, potion effect, close-inventory, per-action permission and tick delay.
- [ ] Disabled-world feature exclusions.

## Phase B — Essentials-style core services

- [ ] Economy balances, starting balance, currency formatting, bounds, logs, command costs, item worth, pay/toggles, sell/worth/setworth, top balances.
- [ ] Kits, kit creation/deletion/reset/preview/cooldowns and permission-based delivery.
- [ ] Teleport request service, accept/deny/cancel/toggle, tpa/tphere/tpall, coordinate/random teleport, back history, offline teleport policy.
- [ ] Homes: set/list/visit/rename/delete, limits and persistence.
- [ ] Warps: set/list/visit/delete/info, persistence and permissions.
- [ ] Jail locations, jail/un-jail, duration and jailed-player tracking.
- [ ] Moderation: ban/tempban/IP ban, kick, mute, jail, unban and history.
- [ ] Private messages/reply/toggle, ignore, social spy, mail, action messages, nickname and nearby.
- [ ] Inventory/item operations: clear, item/give, item info, name/lore, enchant, repair, hat, stack, condense, disposal, unlimited, book, skull, firework, potion.
- [ ] Crafting interfaces and supported table utilities.
- [ ] Entity/spawner utilities with strict limits and permissions.
- [ ] World time/weather and personal time/weather.
- [ ] Server/player information, rules/MOTD/info, playtime/seen, ping, position, compass, depth, top/bottom, help and recipe.
- [ ] Configurable interactive signs.
- [ ] Command cooldowns and custom text commands.

## Phase C — Chat and AntiBuild

- [ ] Chat format, group formats, prefixes/suffixes, authorized colors and RGB, URL permissions.
- [ ] Local/global/question/shout channels and receive permissions.
- [ ] Team/world placeholders and group metadata.
- [ ] Per-item/block place/break/interact/craft/pickup/drop controls.
- [ ] Piston protections, blacklist, legacy blacklist compatibility, staff alerts and exemptions.
- [ ] Permission-based build bypass and per-item exceptions.

## Phase D — NecroManager anti-cheat and monitoring

- [ ] Reliable server-side AutoTotem behavior checks with configurable evidence thresholds and false-positive safeguards.
- [ ] Packet/protocol checks only where supported by the chosen Paper API or a documented internal packet abstraction.
- [ ] Violation records, profiles, staff tracking, top violations, manual checks, alert toggles and log maintenance.
- [ ] Configurable punishments with staged thresholds; manual punishment and review.
- [ ] Database abstraction with SQLite first; MySQL/MariaDB adapter after schema and migration design.
- [ ] Discord webhook alerts, cross-server alerts and Redis bridge as optional integrations.
- [ ] Bedrock player exception where player identity is reliably available.
- [ ] Mod/client detection only when evidence is trustworthy; do not punish based solely on spoofable client-brand strings.
- [ ] Folia scheduling support before claiming Folia compatibility.
- [ ] V3 GUI, live inventory monitor, metadata spoofing, proxy bridge and public API are separate advanced deliverables; no unsupported compatibility claims.

## Phase E — Reliability and release

- [ ] Permission matrix and command documentation.
- [ ] Config defaults, versioned migration, validation and safe reload.
- [ ] Persistent data migration and corruption recovery.
- [ ] Rate limits, bounded entity/item operations, scheduler ownership and shutdown cleanup.
- [ ] Audit log for staff/destructive actions.
- [ ] Automated tests, CI, supported-version matrix, release packaging and final source audit (when explicitly authorized).

## Status

The current main branch has the foundation, spawn/lifecycle/restriction systems, and initial player administration. Items checked above reflect code already present before this roadmap was added; unchecked items remain planned until implemented and documented.
