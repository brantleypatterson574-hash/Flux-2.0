"""
Renders a faithful mockup of Onyx Client's classic ClickGuiScreen.

All geometry, colours and text roles are taken directly from:
  src/main/java/com/yourname/utilmod/ui/ClickGuiScreen.java
  src/main/java/com/yourname/utilmod/ui/SlGuiPaint.java
  src/main/java/com/yourname/utilmod/module/Category.java

Every label uses SlGuiPaint's staged semibold font with drawText(shadow=false),
so the preview matches the in-game weight and crispness. Regenerated on every
Gradle build via the `generateMockups` task.
"""
from pathlib import Path

from PIL import Image, ImageDraw, ImageFont

ROOT = Path(__file__).resolve().parent.parent
FONT = ROOT / "src/main/resources/assets/scalablelux/font/semibold.ttf"

S = 2  # supersample factor for crisp text
VW, VH = 1280, 800          # virtual GUI resolution (what guiScale() produces)
W, H = VW * S, VH * S

# One font for all text: SlGuiPaint.styled() applies it to every label.
TITLE_FONT = str(FONT)
BODY_FONT = str(FONT)

# ClickGuiScreen fixed palette (neutral grey shell).
ACCENT = 0xFFB8B8BE
WIN_BG = 0xFF0C0C0C
SIDE_BG = 0xFF101010
CARD_BG = 0xFF161616
CARD_HOVER = 0xFF1D1D1D
CARD_ACTIVE = 0xFF242424
CARD_ENABLED = 0xFF2C2C2C
FIELD_BG = 0xFF161616
BORDER = 0xFF2B2B2B
TEXT = 0xFFF4F4F5
TEXT_DIM = 0xFF8E8E93
TOGGLE_OFF = 0xFF303030

TITLE_HEIGHT = 52
SIDEBAR_WIDTH = 196
SETTINGS_WIDTH = 500
CATEGORY_HEIGHT = 40
MODULE_ROW_HEIGHT = 58
ROW_GAP = 14
SETTING_ROW_HEIGHT = 46
IPAD = 16
WINDOW_RADIUS = 16
CARD_RADIUS = 12

CATEGORIES = [
    ("Crystal", 0x67E8F9), ("Sword", 0xFB7185), ("Mace", 0xFBBF24),
    ("Movement", 0xA3E635), ("Player", 0x4ADE80), ("Render", 0xC084FC),
    ("World", 0x38BDF8), ("Donut", 0xFFF472B6), ("Util", 0x9CA3AF),
    ("HUD", 0xF0ABFC),
]
CATEGORY_ICONS = [
    "crystal.png", "sword.png", "mace.png", "movement.png", "player.png",
    "render.png", "world.png", "donut.png", "util.png", "hud.png",
]
ICON_DIR = ROOT / "src/main/resources/assets/flux/textures/gui/categories"

img = Image.new("RGBA", (W, H), (0, 0, 0, 255))


def sc(v):
    return int(round(v * S))


def argb(c):
    return ((c >> 16) & 255, (c >> 8) & 255, c & 255, (c >> 24) & 255)


def with_alpha(c, a):
    return (a << 24) | (c & 0x00FFFFFF)


def alpha_mul(c, f):
    return with_alpha(c, int(round(((c >> 24) & 255) * max(0.0, min(1.0, f)))))


def title_color(c):
    """SlGuiPaint.titleColor(): mix 24% toward white, force opaque alpha."""
    r, g, b = (c >> 16) & 255, (c >> 8) & 255, c & 255
    r = int(r * 0.76 + 255 * 0.24)
    g = int(g * 0.76 + 255 * 0.24)
    b = int(b * 0.76 + 255 * 0.24)
    return 0xFF000000 | (r << 16) | (g << 8) | b


def rect(x1, y1, x2, y2, color, radius=0):
    r, g, b, a = argb(color)
    if a == 0 or x2 <= x1 or y2 <= y1:
        return
    x1, y1, x2, y2 = sc(x1), sc(y1), sc(x2), sc(y2)
    rad = sc(radius)
    pad = rad + 4
    bx1, by1 = max(0, x1 - pad), max(0, y1 - pad)
    bx2, by2 = min(W, x2 + pad), min(H, y2 + pad)
    layer = Image.new("RGBA", (bx2 - bx1, by2 - by1), (0, 0, 0, 0))
    d = ImageDraw.Draw(layer)
    box = (x1 - bx1, y1 - by1, x2 - bx1 - 1, y2 - by1 - 1)
    if rad > 0:
        d.rounded_rectangle(box, radius=rad, fill=(r, g, b, a))
    else:
        d.rectangle(box, fill=(r, g, b, a))
    img.alpha_composite(layer, (bx1, by1))


def item_icon(x, y, name, size=16):
    """Composite the same smooth 64x64 icon the in-game GUI draws."""
    icon = Image.open(ICON_DIR / name).convert("RGBA")
    icon = icon.resize((sc(size), sc(size)), Image.LANCZOS)
    img.alpha_composite(icon, (sc(x), sc(y)))


def chevron(x, y, color, direction="right", size=7):
    """Draw a solid arrow as geometry; the semibold font has no triangle glyph."""
    r, g, b, a = argb(color)
    if direction == "down":
        points = [(0, 0), (size, 0), (size / 2, size * 0.7)]
    elif direction == "up":
        points = [(size / 2, size * 0.3), (size, size), (0, size)]
    else:
        points = [(0, 0), (size * 0.7, size / 2), (0, size)]
    layer = Image.new("RGBA", (sc(size) + 4, sc(size) + 4), (0, 0, 0, 0))
    d = ImageDraw.Draw(layer)
    d.polygon([((px + 1) * S, (py + 1) * S) for px, py in points], fill=(r, g, b, a))
    img.alpha_composite(layer, (sc(x), sc(y)))


def label(x, y, value, size, color, font=BODY_FONT, shadow=False, fit_w=None):
    f = ImageFont.truetype(font, max(1, sc(size)))
    if fit_w is not None and f.getlength(value) > fit_w * S:
        limit = fit_w * S - f.getlength("...")
        while value and f.getlength(value) > limit:
            value = value[:-1]
        value = value.rstrip() + "..."
    r, g, b, a = argb(color)
    px, py = sc(x), sc(y)
    tw = int(f.getlength(value))
    pad = 3 * S
    bx1, by1 = max(0, px - pad), max(0, py - pad)
    bx2, by2 = min(W, px + tw + pad + 2 * S), min(H, py + sc(size) + pad + 2 * S)
    if bx2 <= bx1 or by2 <= by1:
        return
    layer = Image.new("RGBA", (bx2 - bx1, by2 - by1), (0, 0, 0, 0))
    d = ImageDraw.Draw(layer)
    if shadow:
        d.text((px - bx1 + S, py - by1 + S), value, font=f, fill=(0, 0, 0, 200))
    d.text((px - bx1, py - by1), value, font=f, fill=(r, g, b, a))
    img.alpha_composite(layer, (bx1, by1))


def text_width(value, size, font=BODY_FONT):
    f = ImageFont.truetype(font, max(1, sc(size)))
    return f.getlength(value) / S


def toggle(x, y, w=34, h=18, enabled=False):
    if enabled:
        rect(x - 2, y - 2, x + w + 2, y + h + 2, alpha_mul(ACCENT, 0.22), h / 2 + 2)
    rect(x, y, x + w, y + h, ACCENT if enabled else TOGGLE_OFF, h / 2)
    knob = h - 6
    kx = x + w - knob - 3 if enabled else x + 3
    rect(kx, y + 3, kx + knob, y + 3 + knob, 0xFFFFFFFF if enabled else 0xFFB5B5C3, knob / 2)


def ui_button(x, y, w, h, active=False, radius=12):
    if active:
        rect(x - 1, y - 1, x + w + 1, y + h + 1, alpha_mul(ACCENT, 0.6), radius + 1)
    rect(x, y, x + w, y + h, CARD_ENABLED if active else FIELD_BG, radius)


def ui_field(x, y, w, h, focused=False, radius=12):
    rect(x - 1, y - 1, x + w + 1, y + h + 1, alpha_mul(ACCENT, 0.8) if focused else BORDER, radius + 1)
    rect(x, y, x + w, y + h, FIELD_BG, radius)


def draw_background():
    """Approximate an in-game scene plus the FLUX overlay wash behind the window."""
    d = ImageDraw.Draw(img)
    for y in range(H):
        t = y / H
        if t < 0.58:
            k = t / 0.58
            r = int(112 + 44 * k)
            g = int(150 + 46 * k)
            b = int(196 + 40 * k)
        elif t < 0.62:
            r, g, b = 96, 132, 86
        else:
            k = min(1.0, (t - 0.62) / 0.38)
            r = int(74 - 26 * k)
            g = int(116 - 40 * k)
            b = int(56 - 22 * k)
        d.line([(0, y), (W, y)], fill=(r, g, b, 255))
    # Blocky terrain silhouettes for a subtle "game behind the menu" feel.
    for bx, by, bw, bh, col in [
        (40, 470, 190, 330, (58, 92, 44, 255)),
        (250, 520, 150, 280, (66, 100, 48, 255)),
        (980, 500, 200, 300, (54, 84, 42, 255)),
        (760, 560, 110, 240, (70, 104, 50, 255)),
    ]:
        d.rectangle([sc(bx), sc(by), sc(bx + bw), sc(by + bh)], fill=col)
    # Grey FLUX overlay (0x90040404) + the extra black 0.25 wash from render().
    layer = Image.new("RGBA", (W, H), (4, 4, 4, int(255 * 0.565)))
    img.alpha_composite(layer)
    layer = Image.new("RGBA", (W, H), (0, 0, 0, int(255 * 0.25)))
    img.alpha_composite(layer)


def setting_row(y, sx, sw, setting):
    x2 = sx + sw
    rect(sx, y, x2, y + SETTING_ROW_HEIGHT, CARD_BG, 8)
    label(sx + 8, y + 14, setting["label"], 11.5, TEXT_DIM,
          fit_w=max(40, sw - 156))
    kind = setting["type"]
    if kind == "number":
        tx, tw = sx + 6 + 6, sw - 12 - 12
        ty = y + 36
        rect(tx, ty, tx + tw, ty + 5, TOGGLE_OFF, 2)
        pct = (setting["value"] - setting["min"]) / (setting["max"] - setting["min"])
        pct = max(0.0, min(1.0, pct))
        end = tx + int(tw * pct)
        rect(tx, ty, end, ty + 5, alpha_mul(ACCENT, 0.92), 2)
        rect(end - 5, ty - 1, end + 6, ty + 6, alpha_mul(ACCENT, 0.22), 6)
        rect(end - 4, ty, end + 5, ty + 6, 0xFFF7F8FA, 5)
        value = setting.get("display", str(setting["value"]))
        vw = max(52, text_width(value, 10.0) + 18)
        vx = x2 - 8 - vw
        rect(vx, y + 7, x2 - 8, y + 27, alpha_mul(ACCENT, 0.16), 10)
        label(vx + 9, y + 12, value, 10.0, TEXT)
        if setting.get("color"):
            c = setting["color"]
            cw = x2 - 27
            rect(cw, y + 12, cw + 18, y + 30, 0xFF000000 | c, 9)
    elif kind == "bool":
        toggle(x2 - 42, y + 14, 34, 18, setting["value"])
    elif kind == "enum":
        bx = x2 - 116
        ui_button(bx, y + 11, 108, 24, False, 12)
        label(bx + 9, y + 17, setting["value"], 10.0, TEXT, fit_w=76)
        chevron(bx + 92, y + 16, ACCENT, "down", 7)
    elif kind == "checklist":
        value = setting["value"]
        value_w = text_width(value, 10.0)
        pw = value_w + 34
        bx = x2 - 8 - pw
        rect(bx, y + 11, x2 - 8, y + 35, alpha_mul(ACCENT, 0.16), 12)
        label(bx + 10, y + 17, value, 10.0, ACCENT)
        chevron(bx + 12 + value_w, y + 18, ACCENT, "right", 7)


def advanced_header(y, sx, sw, expanded):
    """ClickGuiScreen.drawAdvancedHeader(): collapsible HUD placement row."""
    x2 = sx + sw
    rect(sx, y, x2, y + SETTING_ROW_HEIGHT, CARD_BG, 8)
    rect(sx + 1, y + 8, sx + 4, y + SETTING_ROW_HEIGHT - 8, alpha_mul(ACCENT, 0.88), 2)
    label(sx + 12, y + 15, "Advanced", 11.5, title_color(TEXT), TITLE_FONT, shadow=False)
    hint = "HIDE" if expanded else "PLACEMENT"
    hint_w = text_width(hint, 11.0)
    color = ACCENT if expanded else TEXT_DIM
    label(x2 - 10 - hint_w - 13, y + 17, hint, 11.0, color)
    chevron(x2 - 10 - 7, y + 18, color, "up" if expanded else "down", 7)


def render(path, category_index, modules, selected_index, settings, enabled_rows,
           advanced=None, advanced_expanded=False):
    global img
    img = Image.new("RGBA", (W, H), (0, 0, 0, 255))
    draw_background()

    win_w = min(1400, max(SIDEBAR_WIDTH + SETTINGS_WIDTH + 8, VW - 28))
    win_h = min(1000, max(400, VH - 28))
    wx = (VW - win_w) // 2
    wy = (VH - win_h) // 2
    x2, y2 = wx + win_w, wy + win_h

    # Outer glow, rim and opaque window body.
    for i in range(7, 0, -1):
        rect(wx - i, wy - i, x2 + i, y2 + i,
             alpha_mul(ACCENT, 0.03 + 0.012 * (7 - i)), WINDOW_RADIUS + i)
    rect(wx - 1, wy - 1, x2 + 1, y2 + 1, alpha_mul(ACCENT, 0.75), WINDOW_RADIUS + 1)
    rect(wx, wy, x2, y2, WIN_BG, WINDOW_RADIUS)

    # ---- Title bar ----
    rect(wx + 14, wy + 12, wx + 44, wy + 42, ACCENT, 9)
    item_icon(wx + 17, wy + 15, "flux_logo.png", 22)
    label(wx + 54, wy + 18, "ONYX", 15.0, title_color(TEXT), TITLE_FONT, shadow=False)

    # Top-bar chips match the in-game theme/layout controls.
    rect(wx + 164, wy + 15, wx + 276, wy + 37, alpha_mul(ACCENT, 0.16), 10)
    label(wx + 172, wy + 21, "CLICK GUI", 8.5, ACCENT)
    chevron(wx + 172 + text_width("CLICK GUI", 8.5) + 7, wy + 22, ACCENT, "right", 6)
    rect(wx + 284, wy + 15, wx + 386, wy + 37, alpha_mul(ACCENT, 0.12), 10)
    label(wx + 292, wy + 21, "PRESTIGE", 8.5, ACCENT)
    chevron(wx + 292 + text_width("PRESTIGE", 8.5) + 7, wy + 22, ACCENT, "right", 6)
    for i, icon_name in enumerate(["crystal.png", "sword.png", "render.png", "util.png"]):
        item_icon(wx + 398 + i * 20, wy + 18, icon_name, 16)

    rect(wx + 1, wy + TITLE_HEIGHT - 1, x2 - 1, wy + TITLE_HEIGHT, BORDER)
    for bx, glyph in [(wx + win_w - 14 - 30 - 8 - 30, "i"), (wx + win_w - 14 - 30, "x")]:
        ui_button(bx, wy + 8, 30, 30, False, 15)
        label(bx + (30 - text_width(glyph, 9.0)) / 2, wy + 19, glyph, 9.0, TEXT_DIM)

    # ---- Sidebar ----
    sx, sy, sw, sh = wx, wy + TITLE_HEIGHT, SIDEBAR_WIDTH, win_h - TITLE_HEIGHT
    rect(sx + sw - 1, sy, sx + sw, sy + sh - WINDOW_RADIUS, BORDER)
    label(sx + 18, sy + 12, "MODULES", 8.0, TEXT_DIM)
    sidebar_top = wy + TITLE_HEIGHT + 30
    hy = sidebar_top + category_index * CATEGORY_HEIGHT
    rect(sx + 10, hy, sx + sw - 14, hy + CATEGORY_HEIGHT - 4, CARD_ACTIVE, 10)
    for i, (name, ccolor) in enumerate(CATEGORIES):
        cy = sidebar_top + i * CATEGORY_HEIGHT
        active = i == category_index
        item_icon(sx + 18, cy + 10, CATEGORY_ICONS[i], 16)
        label(sx + 42, cy + 10, name, 10.0, title_color(TEXT if active else TEXT_DIM),
              TITLE_FONT, shadow=False, fit_w=90)
        if active:
            rect(sx + 1, cy + 3, sx + 4, cy + CATEGORY_HEIGHT - 6, alpha_mul(ccolor, 0.9), 2)
            rect(sx + sw - 28, cy + 17, sx + sw - 22, cy + 23, alpha_mul(ccolor, 0.9), 3)

    # ---- Module catalog ----
    content_left = wx + SIDEBAR_WIDTH
    content_width = win_w - SIDEBAR_WIDTH - SETTINGS_WIDTH
    field_x, field_y, field_w = content_left + 16, wy + TITLE_HEIGHT + 5 + 10, content_width - 32
    ui_field(field_x, field_y, field_w, 34, False, 17)
    label(field_x + 30, field_y + 12, "Search modules...", 10.5, TEXT_DIM)
    mx, my = field_x + 14, field_y + (34 - 13) / 2
    rect(mx, my, mx + 7, my + 7, TEXT_DIM, 4)
    rect(mx + 7, my + 7, mx + 10, my + 10, TEXT_DIM, 1)

    heading = CATEGORIES[category_index][0] + " Modules"
    label(field_x + 4, field_y + 34 + 14, heading, 14.0,
          title_color(TEXT), TITLE_FONT, shadow=False)
    count = f"{len(modules)} modules"
    label(field_x + field_w - 4 - text_width(count, 9.0), field_y + 34 + 6,
          count, 9.0, TEXT_DIM)

    lx = content_left + 16
    ly = wy + TITLE_HEIGHT + 5 + 86
    lw = content_width - 32
    lh = (win_h - TITLE_HEIGHT - 10) - 86 - 12
    step = MODULE_ROW_HEIGHT + ROW_GAP
    for i, (name, desc) in enumerate(modules[:int(lh // step) + 1]):
        ry = ly + i * step
        selected = i == selected_index
        rect(lx - 1, ry - 1, lx + lw + 1, ry + MODULE_ROW_HEIGHT + 1,
             alpha_mul(ACCENT, 0.55) if selected else BORDER, 11)
        rect(lx, ry, lx + lw, ry + MODULE_ROW_HEIGHT, CARD_BG, 10)
        label(lx + 16, ry + 11, name, 12.5, title_color(TEXT), TITLE_FONT,
              shadow=False, fit_w=max(40, lw - 110))
        label(lx + 16, ry + 32, desc, 9.0, TEXT_DIM, fit_w=max(40, lw - 110))
        chevron(lx + lw - 72, ry + 23, ACCENT if selected else TEXT_DIM,
                "up" if selected else "down", 8)
        toggle(lx + lw - 50, ry + 19, 34, 18, i in enabled_rows)

    # Module-list scrollbar.
    max_scroll = len(modules) * step - lh
    if max_scroll > 0:
        thumb = max(16, int(lh * lh / (lh + max_scroll)))
        rect(lx + lw + 6, ly, lx + lw + 9, ly + thumb, alpha_mul(ACCENT, 0.45), 1)

    # ---- Settings panel ----
    px, py = wx + win_w - SETTINGS_WIDTH, wy + TITLE_HEIGHT
    pw, ph = SETTINGS_WIDTH, win_h - TITLE_HEIGHT
    rect(px + 2, py + 4, px + pw - 2, py + ph - 4, BORDER, CARD_RADIUS + 1)
    rect(px + 3, py + 5, px + pw - 3, py + ph - 5, SIDE_BG, CARD_RADIUS)
    rect(px + 9, py + 11, px + pw - 9, py + 39, CARD_ACTIVE, 10)
    rect(px + 9, py + 11, px + 12, py + 39, alpha_mul(ACCENT, 0.88))
    label(px + 20, py + 13, modules[selected_index][0], 16.0,
          title_color(TEXT), TITLE_FONT, shadow=False, fit_w=pw - 86)
    ui_button(px + pw - 34, py + 5, 28, 28, False, 14)
    label(px + pw - 34 + (28 - text_width("x", 9.0)) / 2, py + 12,
          "x", 9.0, TEXT_DIM)

    srx, srw = px + IPAD, SETTINGS_WIDTH - IPAD * 2
    row_top = py + 44

    # Normal settings, then the collapsible Advanced row, then (if expanded)
    # the HUD placement settings that were moved behind it.
    rows = list(settings)
    if advanced:
        rows.append({"__advanced__": True})
        if advanced_expanded:
            rows.extend(advanced)

    for i, setting in enumerate(rows):
        ry = row_top + i * SETTING_ROW_HEIGHT
        if ry + SETTING_ROW_HEIGHT > py + ph - 8:
            break
        if setting.get("__advanced__") if isinstance(setting, dict) else False:
            advanced_header(ry, srx, srw, advanced_expanded)
        else:
            setting_row(ry, srx, srw, setting)

    out = ROOT / path
    out.parent.mkdir(parents=True, exist_ok=True)
    img.convert("RGB").save(out)
    print("wrote", out.relative_to(ROOT))


SWORD_MODULES = [
    ("Sword Aura", "Attacks nearby targets only while a sword is held, with cooldown and critical-hit gates."),
    ("Sword Swap", "Equips the best hotbar sword when an enemy is nearby, then optionally returns to your old slot."),
    ("Crit Assist", "Only swings when the vanilla critical-hit conditions are met: airborne, falling and cooldown ready."),
    ("Trigger Bot", "Attacks the entity under your crosshair after a configurable human-like delay."),
    ("Auto Clicker", "Repeats real attack/use presses with configurable CPS and timing jitter."),
    ("Combo Assister", "Locks onto a target and spaces attacks into a steady combo instead of rapid target flipping."),
    ("Reach", "Hits targets from farther away. Client raycast and private-server check use the same value."),
]

TRIGGER_BOT_SETTINGS = [
    {"type": "number", "label": "Reaction Delay (Ms)", "value": 80.0, "min": 0.0, "max": 600.0, "display": "80"},
    {"type": "number", "label": "Range", "value": 5.0, "min": 1.0, "max": 16.0, "display": "5"},
    {"type": "bool", "label": "Players", "value": True},
    {"type": "bool", "label": "Mobs", "value": True},
    {"type": "bool", "label": "Require Sword", "value": False},
    {"type": "bool", "label": "Swing Hand", "value": True},
]

RENDER_MODULES = [
    ("Fullbright", "Maximizes brightness in dark areas and restores your old gamma when disabled."),
    ("Entity ESP", "Entity ESP with a selectable list of every entity type, glow, fill, tracers and distance fading."),
    ("Target HUD", "Shows a live health card and ESP box for the entity you are targeting."),
    ("Nametags", "Draws readable name tags with health, armor and distance over chosen entity types."),
    ("Hit Color", "Shows a short colored confirmation effect whenever your attack connects."),
    ("Zoom", "Hold a key to zoom in and release to return to your normal FOV."),
]

ENTITY_ESP_SETTINGS = [
    {"type": "checklist", "label": "Entity Types", "value": "Players + Mobs"},
    {"type": "number", "label": "Range", "value": 64.0, "min": 4.0, "max": 256.0, "display": "64"},
    {"type": "number", "label": "Update Interval", "value": 2.0, "min": 1.0, "max": 20.0, "display": "2"},
    {"type": "number", "label": "Maximum Entities", "value": 96.0, "min": 8.0, "max": 256.0, "display": "96"},
    {"type": "enum", "label": "Box Style", "value": "Model Glow"},
    {"type": "bool", "label": "Fill", "value": True},
    {"type": "bool", "label": "Glow", "value": True},
    {"type": "bool", "label": "Tracers", "value": False},
    {"type": "bool", "label": "Show Names", "value": True},
    {"type": "bool", "label": "Color By Type", "value": True},
    {"type": "number", "label": "Line Width", "value": 2.0, "min": 1.0, "max": 5.0, "display": "2"},
    {"type": "number", "label": "Visible Color", "value": 0, "min": 0, "max": 1,
     "display": "", "color": 0xB5FFD9},
    {"type": "number", "label": "Occluded Color", "value": 0, "min": 0, "max": 1,
     "display": "", "color": 0x4C6FA8},
]

DONUT_MODULES = [
    ("Base Finder", "Scans nearby blocks for player-built structures and reports dense clusters."),
    ("Light Detector", "Finds dark, mob-spawnable pockets around you with a configurable light threshold."),
]

BASE_FINDER_SETTINGS = [
    {"type": "number", "label": "Radius", "value": 16.0, "min": 6.0, "max": 48.0, "display": "16"},
    {"type": "number", "label": "Scan Interval", "value": 40.0, "min": 10.0, "max": 200.0, "display": "40"},
    {"type": "number", "label": "Minimum Signals", "value": 4.0, "min": 2.0, "max": 20.0, "display": "4"},
    {"type": "bool", "label": "Show HUD", "value": True},
]

BASE_FINDER_ADVANCED = [
    {"type": "number", "label": "X", "value": 6.0, "min": 0.0, "max": 1000.0, "display": "6"},
    {"type": "number", "label": "Y", "value": 96.0, "min": 0.0, "max": 1000.0, "display": "96"},
]

render("mockups/clickgui-flux-combat.png", 1, SWORD_MODULES, 3,
       TRIGGER_BOT_SETTINGS, {3})
render("mockups/clickgui-flux-visuals-esp.png", 5, RENDER_MODULES, 1,
       ENTITY_ESP_SETTINGS, {0, 1, 3})
render("mockups/clickgui-flux-hud-advanced.png", 7, DONUT_MODULES, 0,
       BASE_FINDER_SETTINGS, {0}, advanced=BASE_FINDER_ADVANCED,
       advanced_expanded=True)
