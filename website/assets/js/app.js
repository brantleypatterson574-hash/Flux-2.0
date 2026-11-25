const CATEGORIES = [
  { id: "ALL", label: "All", icon: "end_crystal.png", color: "#c084fc" },
  { id: "CRYSTAL", label: "Crystal", icon: "end_crystal.png", color: "#67E8F9" },
  { id: "SWORD", label: "Sword", icon: "sword.png", color: "#FB7185" },
  { id: "MACE", label: "Mace", icon: "mace.png", color: "#FBBF24" },
  { id: "MOVEMENT", label: "Movement", icon: "movement.png", color: "#A3E635" },
  { id: "PLAYER", label: "Player", icon: "player.png", color: "#4ADE80" },
  { id: "RENDER", label: "Render", icon: "spyglass.png", color: "#C084FC" },
  { id: "WORLD", label: "World", icon: "world.png", color: "#38BDF8" },
  { id: "DONUT", label: "Donut", icon: "donut.png", color: "#F472B6" },
  { id: "UTIL", label: "Util", icon: "steak.png", color: "#9CA3AF" },
  { id: "HUD", label: "HUD", icon: "hud.png", color: "#F0ABFC" }
];

const MODULES = [
  { n: "Auto Totem", c: "CRYSTAL", t: "Client", d: "Keeps a totem in your offhand, detects pops and damage, and holds reserves." },

  { n: "Crystal Aura", c: "CRYSTAL", t: "Client", d: "Attacks nearby players during crystal PvP, with optional end-crystal cleanup." },

  { n: "Auto Crystal", c: "CRYSTAL", t: "Server validated", d: "Places and detonates end crystals around the nearest target. Server still validates every action." },

  { n: "Kill Aura", c: "SWORD", t: "Client", d: "Attacks nearby players and mobs with any held item, with range and target filtering." },

  { n: "Sword Aura", c: "SWORD", t: "Client", d: "Attacks nearby targets only while a sword is held. Optional cooldown and critical-hit gates." },

  { n: "Sword Swap", c: "SWORD", t: "Client", d: "Equips the best hotbar sword when an enemy is nearby, then optionally returns to your old slot." },

  { n: "Crit Assist", c: "SWORD", t: "Client", d: "Only swings when the vanilla critical-hit conditions are met: airborne, falling and cooldown ready." },

  { n: "Trigger Bot", c: "SWORD", t: "Client", d: "Attacks the entity under your crosshair after a configurable human-like delay." },

  { n: "Auto Clicker", c: "SWORD", t: "Client", d: "Repeats real attack/use presses with configurable CPS and timing jitter." },

  { n: "Combo Assister", c: "SWORD", t: "Client", d: "Locks onto a target and spaces attacks into a steady combo instead of rapid target flipping." },

  { n: "Reach", c: "PLAYER", t: "Server synced", d: "Gives you super long arms. Extra block reach affects client-side interactions; extra entity reach is mirrored to the private server." },

  { n: "Mace Swap", c: "MACE", t: "Client", d: "Swaps to the mace for fall-strike hits, with Breach/Density swapping, shield stun and snapback." },

  { n: "Density Assist", c: "MACE", t: "Visual", d: "Shows your fall distance and the mace smash bonus damage it would deal." },

  { n: "Breach Swap", c: "MACE", t: "Client", d: "Same-tick Breach mace swap on attack (attribute swapping)." },

  { n: "Mace Aura", c: "MACE", t: "Client", d: "Attacks nearby targets while holding a mace. Optional smash-only mode waits for a fall strike." },

  { n: "Smash Assist", c: "MACE", t: "Client", d: "Automatically holds a Density mace when a fall strike is ready, then returns to your previous slot." },

  { n: "Sprint", c: "MOVEMENT", t: "Client", d: "Keeps you sprinting while you move, so you never have to double-tap." },

  { n: "Auto Walk", c: "MOVEMENT", t: "Client", d: "Automatically holds a movement key." },

  { n: "Snap Tap", c: "MOVEMENT", t: "Client", d: "Press a key to replay your last movement exactly. Local input only; disable Auto Walk so the two modules do not fight over the keys." },

  { n: "Wind Charge Jump", c: "MOVEMENT", t: "Client", d: "Keybind macro that throws a wind charge at a configured pitch for a quick boost." },

  { n: "Auto Jump", c: "MOVEMENT", t: "Client", d: "Jumps automatically while walking, for smooth obstacle traversal." },

  { n: "Velocity", c: "MOVEMENT", t: "Server synced", d: "Controls knockback and liquid push. Requires this jar on your private server." },

  { n: "Fast Place", c: "PLAYER", t: "Client", d: "Repeats item use on a delay for faster block placement." },

  { n: "Auto Eat", c: "PLAYER", t: "Client", d: "Automatically eats food when your health gets low." },

  { n: "Auto Respawn", c: "PLAYER", t: "Client", d: "Respawns automatically after you die." },

  { n: "Auto Mend", c: "PLAYER", t: "Client", d: "Automatically repairs Mending gear by keeping damaged items offhand." },

  { n: "Auto Log", c: "PLAYER", t: "Client", d: "Disconnects automatically at low health. Client-side only." },

  { n: "Fullbright", c: "RENDER", t: "Visual", d: "Maximizes brightness in dark areas." },

  { n: "Hit Color", c: "RENDER", t: "Visual", d: "Shows a short colored confirmation effect whenever your attack connects." },

  { n: "Zoom", c: "RENDER", t: "Visual", d: "Hold a key to zoom in and release to return to your normal FOV." },

  { n: "Crosshair", c: "RENDER", t: "Visual", d: "Draws a custom client-side crosshair style." },

  { n: "Xray", c: "HUD", t: "Visual", d: "Highlights selected blocks through walls. Personal, client-side overlay." },

  { n: "Entity ESP", c: "HUD", t: "Visual", d: "Prestige-style entity ESP with a selectable list of every entity type." },

  { n: "Target HUD", c: "HUD", t: "Visual", d: "Shows a live health card and ESP box for the entity you are targeting." },

  { n: "Nametags", c: "HUD", t: "Visual", d: "Draws readable name tags with health numbers, armor and distance over chosen entity types." },

  { n: "Keybinds", c: "HUD", t: "GUI", d: "Lists module keybinds on the HUD." },

  { n: "Stats HUD", c: "HUD", t: "Visual", d: "Configurable FPS, coordinates, biome, speed, ping and memory overlay." },

  { n: "Weather", c: "WORLD", t: "Visual", d: "Hides rain, snow and clouds on your screen. Nothing changes on the server." },

  { n: "Base Finder", c: "DONUT", t: "Visual", d: "Scans nearby blocks for player-built structures and reports dense clusters." },

  { n: "Reaction Monitor", c: "UTIL", t: "Self-check", d: "Warns when your own click speed, swapping or Breach automation looks inhuman. Purely client-side self-check." },

  { n: "Anti AFK", c: "UTIL", t: "Client", d: "Keeps the local session active with small idle animations." },

  { n: "Menu Theme", c: "UTIL", t: "GUI", d: "Change the ClickGUI palette, layout, scale and typography." },

  { n: "Config", c: "UTIL", t: "GUI", d: "Saves and restores module enabled states in five persistent slots." },

  { n: "Hitbox Adjuster", c: "UTIL", t: "Server synced", d: "Widens your attack hitbox so targets are easier to hit. Server-side checks need this jar on your private server." },

  { n: "Auto Obsidian", c: "UTIL", t: "Server validated", d: "Places obsidian traps, pillars or floors around the nearest player. Server validates every placement." },

  { n: "Auto Anchor", c: "UTIL", t: "Server validated", d: "Charges nearby respawn anchors with glowstone and ignites them with flint and steel." },

  { n: "Sky Mood", c: "WORLD", t: "Visual", d: "Client-only sky presets: daylight, golden hour, sunset, blue hour and night." },

  { n: "Threat Radar", c: "RENDER", t: "Visual", d: "Highlights combat dangers around you. Only draws on your screen." },

  { n: "Projectile Trails", c: "RENDER", t: "Visual", d: "Shows fading flight paths for nearby projectiles. Visual only." },

  { n: "Totem Pop FX", c: "RENDER", t: "Visual", d: "Purely cosmetic rings and ghost outlines when a totem saves a player." },

  { n: "Safe Spot ESP", c: "RENDER", t: "Visual", d: "Marks nearby bedrock and obsidian holes. Does not change any blocks." },

  { n: "Tracers", c: "RENDER", t: "Visual", d: "Draws client-side lines from your crosshair toward chosen entities nearby. Lines of mystery, mostly." },

  { n: "Item Highlight", c: "RENDER", t: "Visual", d: "Outlines dropped items nearby so they are easier to spot. Ground loot, but louder." },

  { n: "Storage ESP", c: "RENDER", t: "Visual", d: "Finds nearby chests, barrels and similar blocks. Draws on your screen only. Loot radar; opening the chest is still your job." },

  { n: "City ESP", c: "RENDER", t: "Visual", d: "Outlines mineable blocks next to nearby players. Never places or breaks blocks. No blocks were harmed in the making of this overlay." },

  { n: "Void ESP", c: "RENDER", t: "Visual", d: "Marks nearby holes and open void edges. Only draws an overlay. Holes are just floors with confidence issues." },

  { n: "Breadcrumbs", c: "RENDER", t: "Visual", d: "Draws a fading trail of your recent footsteps. Visual only. Hansel and Gretel, but local." },

  { n: "Trajectories", c: "RENDER", t: "Visual", d: "Predicts throwable flight paths and marks nearby projectiles. Visual only. Math, but with confetti." },

  { n: "Logout Spots", c: "RENDER", t: "Visual", d: "Marks the last known position of players who vanish nearby. Visual only. The client remembers the good times." },

  { n: "Block Selection", c: "RENDER", t: "Visual", d: "Highlights the block you are looking at. Visual only. Even blocks deserve a spotlight." },

  { n: "Auto Armor", c: "SWORD", t: "Client", d: "Moves better armor into your own armor slots. Only touches your inventory. Diamond pants are a lifestyle upgrade." },

  { n: "Weather Changer", c: "WORLD", t: "Client", d: "Sets your own rain and sky shade for visuals only. Server weather is untouched. Personal weather, zero meteorological consequences." },

  { n: "Light Overlay", c: "RENDER", t: "Visual", d: "Shows safe and unsafe light levels as crosses on nearby block tops. Visual only. Tiny crosses with strong opinions." },

  { n: "Break Indicators", c: "RENDER", t: "Visual", d: "Outlines the block you are mining and colours it by break progress. A progress bar for your commitment issues." },

  { n: "Auto Web", c: "SWORD", t: "Server validated", d: "Places cobweb around a nearby target. Changes blocks - use only on a private server with the jar installed." },

  { n: "Surround", c: "CRYSTAL", t: "Server validated", d: "Places obsidian or cobweb around you. Changes blocks - private server with the jar only." },

  { n: "Anti Bed", c: "CRYSTAL", t: "Server validated", d: "Warns about nearby beds and can cover them with a block. Placement changes blocks - private server only." },

  { n: "Bow Aimbot", c: "SWORD", t: "Client", d: "Turns your view towards a target while holding a bow or crossbow. Fires nothing. Your arrows still need your help." },

  { n: "No Web", c: "MOVEMENT", t: "Client", d: "Walk through cobwebs and other slowing blocks at full speed. Needs the private-server jar if the server enforces the slow. Cobwebs are now a suggestion." },

  { n: "Mace AI", c: "MACE", t: "Client", d: "Rule-based mace decisions: wind-charge setup, shield stun, Density smash, Breach hit or a plain follow-up. Stands down while Mace Swap is on. Server rules still decide what lands." },

  { n: "Stun Slam", c: "MACE", t: "Client", d: "Axe shield disable followed by a mace hit. At zero delay both attacks are sent in one tick; servers still decide if they land. Private-server/client-only." },

  { n: "Shield Breaker", c: "SWORD", t: "Server validated", d: "Faces a shielding opponent, swaps briefly to an axe to stun the shield, then swaps back. Block/inventory changes require this jar on your private server if applicable. Shields are not voted off the island." },

  { n: "Auto GG", c: "UTIL", t: "Server validated", d: "Sends a friendly chat message after a nearby player dies. Chat is visible to the server, so keep this to private servers." },

  { n: "Toast Remover", c: "UTIL", t: "Client", d: "Clears Minecraft's vanilla pop-up toasts so advancement and recipe cards never block the view." },

  { n: "Chat Filter", c: "UTIL", t: "Client", d: "Hides chat lines containing your own list of words. Only the text shown to you is affected." },

  { n: "Loot Alerts", c: "HUD", t: "Visual", d: "Shows a corner alert when valuable items drop nearby. Reads local entities only; it never picks items up." },

  { n: "Honest HUD", c: "HUD", t: "Visual", d: "A tiny HUD that reports only what your client actually knows, with a few reminders about the rest." },

  { n: "Dinnerbone Mode", c: "RENDER", t: "Visual", d: "Turns nearby living models upside down using Minecraft's own rendering flag. Hitboxes and names stay normal." },

  { n: "Moon Jump", c: "MOVEMENT", t: "Client", d: "Makes your own jumps springy and floaty, like the moon has poor customer service. Servers may correct the motion." },

  { n: "Banana Tooltips", c: "RENDER", t: "Visual", d: "Adds harmless banana jokes to item tooltips in your own menu. Items and their real stats are untouched." },

  { n: "Disco Overlay", c: "RENDER", t: "Visual", d: "Paints soft, slowly changing colour bands over the world. It is deliberately subtle and draws nothing else." },

  { n: "Confetti Deaths", c: "RENDER", t: "Visual", d: "Throws a small screen-space confetti burst when a nearby player dies. Purely cosmetic." },
];

const iconFor = id => {
  const cat = CATEGORIES.find(c => c.id === id);
  return cat ? cat.icon : "end_crystal.png";
};
const colorFor = id => {
  const cat = CATEGORIES.find(c => c.id === id);
  return cat ? cat.color : "#c084fc";
};
const escapeHtml = value => String(value).replace(/[&<>"']/g, ch => ({
  "&": "&amp;", "<": "&lt;", ">": "&gt;", '"': "&quot;", "'": "&#39;"
}[ch]));

let activeCategory = "ALL";
let query = "";

function renderFilters() {
  const root = document.getElementById("categoryFilters");
  root.innerHTML = CATEGORIES.map(cat => `
    <button class="filter ${cat.id === activeCategory ? "active" : ""}"
      data-cat="${cat.id}" style="--filter-color:${cat.color}">
      <img src="assets/img/icons/${cat.icon}" alt=""> ${cat.label}
    </button>`).join("");
  root.querySelectorAll(".filter").forEach(btn => {
    btn.addEventListener("click", () => {
      activeCategory = btn.dataset.cat;
      renderFilters();
      renderModules();
    });
  });
}

function renderModules() {
  const q = query.trim().toLowerCase();
  const list = MODULES.filter(m => {
    const inCat = activeCategory === "ALL" || m.c === activeCategory;
    const inSearch = !q || m.n.toLowerCase().includes(q) || m.d.toLowerCase().includes(q) || m.t.toLowerCase().includes(q);
    return inCat && inSearch;
  });

  document.getElementById("resultCount").textContent =
    list.length + (list.length === 1 ? " module" : " modules");
  document.getElementById("emptyState").hidden = list.length !== 0;

  document.getElementById("moduleGrid").innerHTML = list.map(m => `
    <article class="module-card" style="--cat-color:${colorFor(m.c)}">
      <div class="module-top">
        <img src="assets/img/icons/${iconFor(m.c)}" alt="">
        <span class="module-cat">${m.c}</span>
      </div>
      <h3>${escapeHtml(m.n)}</h3>
      <p>${escapeHtml(m.d)}</p>
      <span class="tag">${escapeHtml(m.t)}</span>
    </article>`).join("");
}

document.getElementById("moduleSearch").addEventListener("input", event => {
  query = event.target.value;
  renderModules();
});

renderFilters();
renderModules();
