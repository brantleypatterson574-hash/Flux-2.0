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
  { id: "UTIL", label: "Util", icon: "steak.png", color: "#9CA3AF" }
];

const MODULES = [
  { n: "Auto Totem", c: "CRYSTAL", t: "Client", d: "Keeps a totem in your offhand, detects pops and damage, and holds reserves." },
  { n: "Crystal Aura", c: "CRYSTAL", t: "Client", d: "Attacks nearby players during crystal PvP, with optional end-crystal cleanup." },
  { n: "Auto Crystal", c: "CRYSTAL", t: "Server validated", d: "Places and detonates end crystals around the nearest target. The server still validates every action." },

  { n: "Sword Aura", c: "SWORD", t: "Client", d: "Attacks nearby targets only while a sword is held, with cooldown and critical-hit gates." },
  { n: "Sword Swap", c: "SWORD", t: "Client", d: "Equips the best hotbar sword when an enemy is nearby, then optionally returns to your old slot." },
  { n: "Crit Assist", c: "SWORD", t: "Client", d: "Only swings when the vanilla critical-hit conditions are met: airborne, falling and cooldown ready." },
  { n: "Trigger Bot", c: "SWORD", t: "Client", d: "Attacks the entity under your crosshair after a configurable human-like delay." },
  { n: "Auto Clicker", c: "SWORD", t: "Client", d: "Repeats real attack/use presses with configurable CPS and timing jitter." },
  { n: "Combo Assister", c: "SWORD", t: "Client", d: "Locks onto a target and spaces attacks into a steady combo instead of rapid target flipping." },
  { n: "Reach", c: "SWORD", t: "Server synced", d: "Hits targets from farther away. Client raycast and private-server check use the same value." },

  { n: "Mace Swap", c: "MACE", t: "Client", d: "Swaps to the mace for fall-strike hits, with Breach/Density swapping, shield stun and snapback." },
  { n: "Density Assist", c: "MACE", t: "Visual", d: "Shows your fall distance and the mace smash bonus damage it would deal." },
  { n: "Breach Swap", c: "MACE", t: "Client", d: "Same-tick Breach mace swap on attack with smart switching and swap-back control." },
  { n: "Mace Aura", c: "MACE", t: "Client", d: "Attacks nearby targets while holding a mace, with optional smash-only mode." },
  { n: "Smash Assist", c: "MACE", t: "Client", d: "Holds a Density mace when a fall strike is ready, then returns to your previous slot." },

  { n: "Sprint", c: "MOVEMENT", t: "Client", d: "Keeps you sprinting while you move, so you never have to double-tap." },
  { n: "Wind Charge Jump", c: "MOVEMENT", t: "Client", d: "Keybind macro that throws a wind charge at a configured pitch for a quick boost." },
  { n: "Velocity", c: "MOVEMENT", t: "Server synced", d: "Disables melee, projectile and explosion knockback. Requires this jar on your private server." },

  { n: "Fast Place", c: "PLAYER", t: "Client", d: "Repeats item use on a delay for faster block placement." },
  { n: "Inventory Sort", c: "PLAYER", t: "Client", d: "Sorts your inventory by item type with one key press." },
  { n: "Auto Eat", c: "PLAYER", t: "Client", d: "Automatically eats food when your health gets low." },

  { n: "Entity ESP", c: "RENDER", t: "Visual", d: "Entity ESP with a selectable list of every entity type, glow, fill, tracers and distance fading." },
  { n: "Target HUD", c: "RENDER", t: "Visual", d: "Shows a live health card and ESP box for the entity you are targeting." },
  { n: "Fullbright", c: "RENDER", t: "Visual", d: "Maximizes brightness in dark areas and restores your old gamma when disabled." },
  { n: "Nametags", c: "RENDER", t: "Visual", d: "Draws readable name tags with health, armor and distance over chosen entity types." },
  { n: "Hit Color", c: "RENDER", t: "Visual", d: "Shows a short colored confirmation effect whenever your attack connects." },
  { n: "Zoom", c: "RENDER", t: "Visual", d: "Hold a key to zoom in and release to return to your normal FOV." },

  { n: "Weather", c: "WORLD", t: "Visual", d: "Hides rain, snow and clouds on your screen. Nothing changes on the server." },

  { n: "Base Finder", c: "DONUT", t: "Visual", d: "Scans nearby blocks for player-built structures and reports dense clusters." },
  { n: "Light Detector", c: "DONUT", t: "Visual", d: "Finds dark, mob-spawnable pockets around you with a configurable light threshold." },

  { n: "Reaction Monitor", c: "UTIL", t: "Self-check", d: "Warns when your own click speed, swapping or Breach automation looks inhuman. Purely client-side." },
  { n: "Menu Theme", c: "UTIL", t: "GUI", d: "Change the ClickGUI/Dropdown palette, layout and scale from the top bar or settings." },
  { n: "Config", c: "UTIL", t: "GUI", d: "Saves and restores module enabled states across profiles." },
  { n: "Keybinds", c: "UTIL", t: "GUI", d: "Lists module keybinds on the HUD with configurable spacing and position." },
  { n: "Hitbox Adjuster", c: "UTIL", t: "Server synced", d: "Widens the attack raycast so targets are easier to hit. Server-side checks need this jar on your private server." },
  { n: "Auto Obsidian", c: "UTIL", t: "Server validated", d: "Places obsidian traps, pillars or floors around the nearest player. The server validates every placement." },
  { n: "Auto Anchor", c: "UTIL", t: "Server validated", d: "Charges nearby respawn anchors with glowstone and ignites them with flint and steel." }
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
