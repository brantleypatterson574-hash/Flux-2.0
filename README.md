# Flux 2.0 (Fabric / Minecraft 1.21.11)

Yarn-mapped Fabric utility client with a switchable dropdown/ClickGUI menu, original themes, animated notifications, persistent settings, ESP tools, and a large set of combat, crystal-PvP, movement, utility, visual, HUD, and funny party modules. Every module and setting label is written in plain English so the menu is easy to understand.

> Intended for singleplayer, LAN, and private servers. Do not use cheats on public servers where they are not allowed. No anti-cheat bypass or evasion guidance is included.

## Website

A static marketing/download site lives in `website/`:

- `website/index.html` — hero, feature grid, searchable module browser, screenshot gallery and install guide.
- `website/preview.html` — interactive browser mockup of the in-game menu.
- `website/downloads/flux-2.0.jar` — downloadable jar. Every Gradle build copies the fresh jar here and rewrites the displayed size and SHA-256.
- `website/downloads/manifest.json` — machine-readable name, size and checksum for the current download.

The GitHub Pages URL for this repo is **https://brantleypatterson574-hash.github.io/Flux-2.0/**. The `.github/workflows/pages.yml` workflow publishes the `website/` folder on every push to `main` or `master`; enable **Settings → Pages → Source: GitHub Actions** once in the repo. Open `website/index.html` locally for a preview, or serve the folder with any static file server.

## Dropdown Menu

The default layout is a **top menu bar with flyouts**. The classic centered ClickGUI is kept as a selectable alternative.

- Press **Right Shift** to open the menu (rebind under **Options -> Controls -> Key Binds -> Flux 2.0 -> Open Flux 2.0 Menu**).
- The bar has two rows: the wordmark, search, **Credits**, and **x** on top; category tabs underneath.
- Category tabs use real Minecraft item sprites: the Crystal tab shows an End Crystal, Sword shows a diamond sword, Mace shows a mace, and so on. The images come from the game's item models, not custom PNGs.
- **Hover or click** a category tab to drop down its module list. Moving across tabs switches flyouts; clicking the active tab closes it.
- **Left click** a module row to toggle it and open a settings flyout beside the list; **right click** to only open/close settings.
- **Left click/drag** a number slider to change it; **double click** a number to type an exact value (decimals allowed); **right click** any setting to reset it to default.
- **Left click** an enum row to open a dropdown and choose a value directly. **Shift + left click** cycles backward, and **ESC** closes the dropdown.
- **Left click** a boolean to toggle it. Enable options use rounded pills with a soft glow when on.
- **Left click** a **keybind**, then press a key to rebind it. **Right click** clears it; **ESC** cancels capture.
- **Left click** a **text** setting, type freely, then press **Enter** or click away. **ESC** cancels.
- Color settings show a live **#RRGGBB** swatch. Click the swatch to apply it, or type `#RRGGBB` / `0xRRGGBB` directly.
- **Left click** a **checklist** setting (for example **Entity Types** or **Blocks**) to open a searchable checkbox screen. Use **All** / **Clear**, then **Done** or **ESC** to return.
- Scroll over the module or settings flyout to scroll it.
- Type in the search field to filter **all** modules by name, description, or category; results open in their own flyout. **Right click** the field clears it.
- Clicking outside a flyout dismisses the flyout. **ESC** backs out one level at a time, then closes the menu. **x** closes it directly.

### Menu Layout

Click the top-bar **THEME** chip to cycle palettes, or the **CLICK GUI / DROPDOWN** chip to switch shells instantly. You can also open **Menu Theme** and change **Menu Layout** without losing settings:

- **Dropdown** *(default)* — top bar, category tabs, module flyout, and settings flyout.
- **ClickGUI** — the classic centered window with a category sidebar and settings panel.

Both layouts share the same modules, themes, search, rounded enable pills, tooltips, and config files. **Menu Scale** ranges from **0.5 to 2.0** and affects both shells: lower values shrink the whole dropdown/ClickGUI, while values above 1.0 enlarge the dropdown shell until its ten panels are clamped to the available width. The dropdown layout caches tab measurements, flyout bounds, gradients, and category lists so opening or moving through menus stays smooth. Menu text uses Minecraft's default font, so resource-pack fonts apply without a separate Flux typeface. A **Profile** button in both menus opens `https://guns.lol/kitxxmod` through Minecraft's confirm-first link screen.

Settings are saved automatically to `config/utilmod-settings.txt` in a human-readable tab-separated format. Checklist choices remain in `config/utilmod-selections.txt`. Old settings files are ignored safely if labels change.

## GUI Themes

The **Menu Theme** module (Util category) switches the menu and HUD palette. **Prestige** is the default; **Flux** and 34 other palettes remain available.

| Theme | Style |
| --- | --- |
| **Prestige** *(default)* | Soft lavender-tinted dark panels, light violet accent |
| **Flux** | Deep navy window, bright cyan accent, gradient header and shimmer |
| **Matrix** | Dark graphite with crimson accent |
| **UtilMod** | Dark slate with cyan accent |
| **Meteor** | GitHub-style dark panels, blue accent, green ON state |
| **Lumi** | Light lavender panels, dark text, soft purple accent |
| **Sakura** | Dark rose panels with blush-pink accent |
| **Ocean** | Deep blue-slate panels with cool cyan accent |
| **Slate** | Neutral grayish-dark panels with soft silver accent |

Fine-tuning: **Menu Layout**, **Theme**, **Menu Scale**, **Panel Opacity**, **Overlay Opacity**, **Rounded Corners**, **Gradient Header**, **Outlines**, **Panel Shadow**, and **Show Theme In Title**. All palettes are original recolourings inspired by general client styles; no proprietary assets or code are included.

## Notifications

The **Notifications** module shows animated toast cards when modules are toggled or client alerts fire. It has **Position**, **Use Theme Accent**, and **Module Toggles** settings. Toasts slide, fade, and show a progress bar. Notifications stay off until this module is enabled.

## Featured Modules

### Auto Totem 2.0

**Auto Totem** keeps a totem ready and is built for real PvP situations:

- **Modes:** Always, Health, First Hit, and Smart.
- **Smart protection:** health/absorption thresholds, first-damage latch, totem-pop detection, fall-distance threat, and elytra-speed threat.
- **Pop alert:** optional toast when a totem is consumed.
- **Equip delay:** 0-40 ticks, including immediate replacement after a pop.
- **Reserves:** keep spare totems in the inventory and optionally stage them into empty hotbar slots.
- **Double Hand:** support for keeping a second totem available.
- **Pause Movement Clicks:** avoids reshuffling while you are moving quickly.
- **Return Previous Item** restores the item originally in the offhand.
- **Show Status** draws a compact readiness overlay with configurable position and color.
- **Survival Only** avoids creative/spectator interference.

### Auto Mace

**Auto Mace** is the flagship combo module:

- **Triggers:** Smart (fall distance OR downward speed OR elytra), Fall, Speed, Elytra, Always, or a rebindable Key.
- **Mace selection:** Breach while grounded, Density while falling/elytra; explicit slots and enchantment-aware auto detection.
- **Shield stun combo:** swap to an axe, stun a shielding target, then return to the mace.
- **Silent aim:** smooth or instant server-facing rotation with configurable speed, view angle, target bone, and optional view restoration.
- **Targeting:** crosshair priority, nearest/lowest-health priority, players/mobs, through-walls control, and stop-on-death.
- **Timing:** attack delay, combo delay, cooldown gate, snap-back delay, and swap-back restoration.

Attacks send the raw attack packet after slot/rotation packets and replicate vanilla swing/cooldown side effects. Smash rules follow real mace behavior: airborne, no elytra, and falling at least 1.5 blocks; smashes ignore cooldown. Auto Mace takes priority over Mace AI so the two never fight over the hotbar.

### Mace AI

**Mace AI** is a deterministic rule-based decision engine, **not** a neural network. Each tick it scores visible targets and chooses: **Wind charge setup**, **Shield stun**, **Density smash**, **Breach hit**, or **Wait / plain hit**. When no smash is ready it can throw a real Wind Charge from your hotbar at a configurable pitch to launch you into fall height, then take the smash on the way down. Settings include target filters, range, view angle, smash fall distance, charge pitch and cooldown, shield hits, swap-back timing, attack/combo delays, silent aim, and an on-screen decision readout.

### Spawner Finder

**Spawner Finder** scans horizontally for mob spawners through walls and marks them with a configurable ESP box. Settings: **Range**, **Vertical Range**, **Scan Interval**, **Maximum Spawners**, **Style**, **Fill Box**, **Show Distance**, **Coordinates**, **Glow**, **Color**, and **Line Width**. Scanning is incremental and cached so large areas do not stall rendering.

### Clutch

**Clutch** automatically saves you from fall damage by placing water, powder snow, or a solid block beneath your feet. It measures fall distance, probes for ground, searches the hotbar and optionally the inventory, uses safe air placement, and can look down and restore your previous view. Settings: **Mode**, **Trigger Distance**, **Delay**, **Search Inventory**, **Look Down**, **Clutch Alerts**, **Swap Back Delay**, and **Only While Falling**.

### Other new visual/HUD modules

- **Visual Range** — alerts when players enter or leave a radius and shows a compact live player list.
- **Target HUD** — live health card and highlighted ESP box for the entity you are targeting.
- **Breadcrumbs** — fading trail behind your recent movement path.
- **Hit Effects** — short expanding confirmation effect whenever your attack connects.
- **Aim Assist** — now has separate **Horizontal Aim** and **Vertical Aim** toggles, so vertical-only assistance is possible. Smoothing remains enabled.
- **Stun Slam** — sends an axe hit to disable a raised shield, then a mace hit immediately behind it. With **Mace Delay** at zero both attacks go out in the same tick; the server still decides whether i-frames let both land.
- **Snap Tap** — records your recent movement keys and replays the last window when you press the **Replay Key**. It repeats only your own local input, so turn off Auto Walk to avoid two modules writing the same keys.

## Funny Extras

Ten small joke/party modules are included. They are client-side and visual unless noted, and they do not claim to bypass anything.

- **Auto GG** — sends a short chat message after a nearby player dies. It can restrict itself to players you attacked; chat is visible to the server, so use it on private servers.
- **Toast Remover** — wipes Minecraft's vanilla advancement and recipe pop-ups. Flux's own corner alerts still work.
- **Chat Filter** — hides incoming chat lines containing words from your own comma-separated list. Only the display on your client changes.
- **Loot Alerts** — shows a corner alert when valuable items drop nearby. It can watch every item or a custom name list; it never picks items up.
- **Honest HUD** — shows coordinates, health, air, dimension and reported ping with plain labels about what the client actually knows.
- **Dinnerbone Mode** — uses Minecraft's built-in upside-down living-entity render flag. Nametags, hitboxes and physics stay normal.
- **Moon Jump** — gives your own jumps a floaty boost and softer fall. The server still owns your position and may correct it.
- **Banana Tooltips** — appends harmless banana jokes to item tooltips. Items and their real stats are untouched.
- **Disco Overlay** — paints subtle, slowly changing colour bands over the world. Opacity is capped so the world stays readable.
- **Confetti Deaths** — throws a small screen-space confetti burst when a nearby player dies. Purely cosmetic.

## Modules

### Combat

- **Kill Aura** — auto-attacks nearby players with range, view angle, line-of-sight, priority, and delay settings.
- **Aim Assist** — smooth view assistance with smoothing, deadzone, per-tick step cap, horizontal/vertical toggles, and entity filters.
- **Bow Aimbot** — turns your view toward a target while a bow or crossbow is held. Fires nothing.
- **Auto Crit** — jumps before melee hits for critical strikes.
- **Auto Mace** — full fall-strike combo automation described above.
- **Mace AI** — rule-based mace-PvP decision engine; leave Auto Mace off to use it.
- **Shield Breaker** — swaps to an axe against a blocking target, then returns to your weapon.
- **Sword Combat** — sword PvP spacing and auto-shield settings.
- **Auto Armor** — moves stronger armor into your own armor slots as it becomes available.
- **Sprint Reset** — sprint-reset / w-tap helper for sprint crits.
- **Sword Swap** — swaps to an axe against shields and the best sword otherwise, then restores your item.
- **Silent Aim** — configurable silent target rotation with view angle/bone/visibility filters.
- **Trigger Bot** — attacks the entity under your crosshair.
- **Auto Clicker** — hold-key or always-on clicking up to **60 CPS** with wall-clock timing and optional randomization.
- **Critical Hits** — jump/fall-based critical hit timing.
- **Attack Reach** — increases attack range up to 12 blocks (10 by default) through 1.21.11's `AttackRangeComponent`. It **never locks onto or rotates toward a player**; you still aim and click yourself. Install on your private server too.
- **Shield Stun** — swaps to an axe when a target raises a shield.
- **Shield Disabler** — sustained shield-disabling combo while attacking.
- **Auto Potion** — drinks or throws healing potions at low health.
- **Potion Refill** — refills hotbar potion stacks from storage.

### Crystal PvP

Alongside the original crystal and macro helpers, thirteen clean-room modules follow public names and descriptions credited to **kitpvptot**. They use ordinary hotbar interactions and are validated by the server like any other click. No Meteor code or assets are included.

- **Auto Crystal** — places and detonates end crystals around a target.
- **Anchor Aura** — charges and lights respawn anchors near a target from the hotbar.
- **Anti Anchor** — covers your head space when a nearby respawn anchor could hit you.
- **Anti Anvil** — covers your head space when a falling anvil is overhead.
- **Auto Anvil** — places anvils above a nearby target from the hotbar.
- **Auto City** — attacks mineable blocks around a target's feet.
- **Auto Trap** — builds an obsidian or cobblestone box around a nearby target.
- **Bed Aura** — places and breaks beds near a target.
- **Burrow** — places a block under your feet while falling.
- **Hole Filler** — fills empty holes around a nearby target with hotbar blocks.
- **Offhand** — keeps a chosen utility item in your offhand; turn off Auto Totem if both want the slot.
- **Self Anvil** — places an anvil above your head as quick cover.
- **Self Trap** — places blocks above and around your own head.
- **Self Web** — places cobweb around your own feet when an enemy is close.
- **Anchor Macro** — keybind macro that charges a respawn anchor then detonates a crystal.
- **Safe Anchor** — warns and equips a totem when a respawn anchor can hurt you.
- **Crystal Hit** — obsidian + crystal double-tap helper.
- **Trap Alert** — on-screen warning when enclosed by obsidian.
- **Auto Obsidian** — auto-equips obsidian at low health or near anchors/crystals.
- **Surround** — places obsidian or cobweb around you. Block changes are validated by the server.
- **Anti Bed** — warns about nearby beds and can cover them with a block. Placement changes are validated by the server.
- **Auto Web** — places cobweb around a nearby target. Block changes are validated by the server.

### Movement

- **Fly** — free flight with WASD/Space/Shift.
- **Speed** — multiplied horizontal movement with mode and ground/air gates.
- **No Knockback** — suppresses melee, projectile, and explosion knockback. Requires this jar on your private server.
- **No Fall** — clears fall distance locally and rewrites the server's fall-damage distance. Requires this jar on your private server.
- **No Web** — walks through cobwebs, powder snow, honey and soul sand at full speed by cancelling the client-side block slow. The private-server jar is still needed if the server enforces its own slowdown.
- **Auto Jump** — automatic jumping with delay and height controls.
- **Clutch** — automatic fall-saving placement described above.

### Player / Macros

- **Auto Breach** — same-tick Breach/Density attribute swap on manual attack, with smart switching and swap-back.
- **Spear Swap** — cosmetic-only fun macro that rapidly flickers a spear with your held item. No attacks, targeting, or combat behavior.
- **Pearl Key** — rebindable ender-pearl throw macro.
- **Wind Charge Key** — rebindable wind-charge macro with configurable pitch. **Perfect Jump** jumps on the exact local burst tick, while you are still rising, for the tallest launch.
- **Pearl Catch** — pearl + delayed wind-charge catch state machine.
- **Aggro Pearl** — responds to nearby hostile pearls with your own throw.

### Utility

- **Auto Eat** — eats when health or hunger is low.
- **Auto Totem** — advanced totem protection described above.
- **Anti Hunger** — clears food exhaustion locally and authoritatively on your private server.
- **Inventory Totems** — stages spare totems into hotbar slots.
- **Hotbar Refill** — refills depleted hotbar stacks from matching inventory stacks.
- **Auto Experience** — throws experience bottles at your feet on a delay.
- **Fast Place** — delayed repeat-use helper while a key/use button is held.
- **Auto Mine** — breaks the block under your crosshair; combines with Fast Mine on a private server.
- **Fast Mine** — multiplies server-authoritative break progress (1-32x). Requires this jar on your private server.
- **Base Finder** — incremental slice scanning plus clustering/likelihood scoring for bases, with storage, utility, ore, farm, portal, and light-source categories.
- **Auto Reconnect** — reconnects to the last server or a direct host/port after disconnect.
- **Config Profiles** — saves/restores module enabled states in memory.
- **Friends** — not a module; use `/utilmod friend add|remove|list|clear <name>`. Trigger Bot and Aggro Pearl honor the list.

### Visuals

- **Full Bright** — adjustable brightness with restore-on-disable.
- **Menu Theme** — Flux/Prestige/etc. palettes and menu fine-tuning.
- **Entity ESP** — defaults to **Model Glow**, a real silhouette outline using Minecraft's built-in outline pass. Other box styles, color-by-type, names, health, distance, and tracers are available. The **Entity Types** checklist starts with the player and hostile mobs but can select any registered entity type.
- **Hitbox ESP** — clean 3D wireframe hitboxes with optional classic fill and type colors.
- **Tracers** — cached tracer lines from your crosshair or eyes.
- **Name Tags** — readable name tags with health, armor, and distance.
- **Item ESP** — highlights dropped items with name, count, and distance.
- **Item Highlight** — outlines nearby dropped items so they are easier to spot.
- **Storage ESP** — highlights chests, shulkers, barrels, and other containers with a live fill-level readout.
- **City ESP** — outlines mineable blocks next to nearby players. Never places or breaks blocks.
- **Void ESP** — marks nearby holes and open void edges. Only draws an overlay.
- **Block ESP** — outlines any block in the game from a searchable checklist.
- **Spawner Finder** — through-wall spawner markers described above.
- **Hitboxes** — projected entity bounding boxes with a fully typeable **Hitbox Multiplier**. The multiplier expands attack/visual boxes only; real physics boxes are untouched. Install this jar on your private server too.
- **Breadcrumbs** — fading movement trail.
- **Logout Spots** — marks the last known position of players who vanish nearby. Visual only.
- **Block Selection** — highlights the block you are looking at. Visual only.
- **Break Indicators** — outlines the block you are mining and colours it by break progress.
- **Light Overlay** — shows safe and unsafe light levels as crosses on nearby block tops. Visual only.
- **Weather Changer** — sets your own rain and sky shade for visuals only. Server weather is untouched.
- **Hit Effects** — attack confirmation effect.
- **Crosshair** — dot/plus/circle/cross overlays.
- **Trajectories** — predicted pearl/wind-charge arc.

### Performance

- **Performance** — client-only render cost reducers: no particles, weather, clouds, view bobbing, or hurt shake.
- **Lag Meter** — FPS, frame time, RAM usage, and ping overlay.

### Anti-Cheat

**Anti-Cheat** only records, logs, and flags suspicious behavior. It never auto-kicks or auto-bans. Client checks cover impossible speed/flight/reach, very fast reactions, instant rotation snaps, and repeated Breach/Density swap patterns. Server flags are sent back to the client HUD. Thresholds and shown checks are configurable.

Server heuristics: a reaction sample needs an attack within 400 ms of taking damage; a flag needs at least 5 samples with mean <=120 ms and deviation <=25 ms. Rotation flags require at least 4 snaps in 5 seconds (yaw >35° or pitch >25°). Auto Breach flags require at least 3 pre-attack swaps plus 3 swap-backs within 250 ms of each attack.

### HUD

- **HUD** — watermark, optional FPS/coordinates/ping/speed/health/totem/server readouts, and a configurable module list.
- **Credits** — on-screen credits list.
- **Keybinds** — lists modules with rebindable keys.
- **Combat HUD** — health, absorption, armor, totem/crystal counts, and nearby enemies.
- **PvP Info** — attack-cooldown bar, combat items, potion timers, and armor durability.
- **Target HUD** — live target health card.
- **Visual Range** — live player radius list and alerts.
- **Notifications** — animated toast cards.

## Development

- Yarn mappings: `1.21.11+build.6`
- Fabric Loader: `0.20.1`
- Fabric API: `0.141.6+1.21.11`
- Java 21+ required; build with `.\gradlew.bat build` on Windows.
- Loom is pinned to `1.14.10` for Java 21 compatibility.
- Output jar: `build/libs/flux-2.0.jar`
- The mod is **client + server** (`environment: "*"`). Install the same jar on your private/dedicated server for the hitbox multiplier, Reach extension, Anti-Cheat checks, No Knockback, Fast Mine, and other server-authoritative features; singleplayer/LAN works without a separate install.

## Performance notes

- Overlay helpers cache the camera basis per frame, clip lines to the viewport, and cap diagonal work.
- Hitbox sizing expands only attack/visual boxes, never physics boxes; typed multipliers remain fully editable while expansion is bounded to 6 blocks for safe lookups.
- ESP, Tracers, Hitbox ESP, Spawner Finder, Base Finder, Block ESP, and Storage ESP use cached/throttled scans and caps so idle modules do no per-frame work.
- Finder caches revalidate mined or deselected blocks, Block ESP scans nearest-first, and the shared entity cache rebuilds its union only when a module's configuration changes.
- Both menu layouts cache tab measurements, flyout bounds, header gradients, category lists, and filtered results. Entity ESP skips 2D projection entirely in glow-only modes unless labels are enabled.
- Rounded menu edges and gradients reuse cached span tables, so repeated widgets no longer recompute square roots or colour blends each frame.
- HUD, menu filtering, and notifications are cached/throttled where possible.
