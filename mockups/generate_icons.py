"""
Generates Flux's original high-resolution menu icons.

Every icon is drawn as vector-ish geometry at 8x resolution and downsampled
with a Lanczos filter. The result is a 64x64 RGBA texture that stays smooth
when the ClickGUI draws it at 16-22 logical pixels. No vanilla item sprite is
nearest-neighbour upscaled, so the menu never shows blocky "pixel art" icons.

Outputs: src/main/resources/assets/flux/textures/gui/categories/*.png
"""
from pathlib import Path
import math

from PIL import Image, ImageDraw

ROOT = Path(__file__).resolve().parent.parent
OUT = ROOT / "src/main/resources/assets/flux/textures/gui/categories"
OUT.mkdir(parents=True, exist_ok=True)

SS = 8           # supersample factor; final size is 64x64
C = SS
SIZE = 64 * SS


def box(x1, y1, x2, y2):
    return (x1 * C, y1 * C, x2 * C, y2 * C)


def poly(points):
    return [(int(x * C), int(y * C)) for x, y in points]


def ellipse(d, x1, y1, x2, y2, fill, outline=None, width=1):
    d.ellipse(box(x1, y1, x2, y2), fill=fill, outline=outline, width=max(1, int(width * C)))


def line(d, points, fill, width=1, joint="curve"):
    d.line(poly(points), fill=fill, width=max(1, int(width * C)), joint=joint)


def rounded(d, x1, y1, x2, y2, r, fill, outline=None, width=1):
    d.rounded_rectangle(box(x1, y1, x2, y2), radius=int(r * C), fill=fill,
                        outline=outline, width=max(1, int(width * C)))


def finish(name, draw_fn):
    img = Image.new("RGBA", (SIZE, SIZE), (0, 0, 0, 0))
    d = ImageDraw.Draw(img)
    draw_fn(d)
    img = img.resize((64, 64), Image.LANCZOS)
    path = OUT / f"{name}.png"
    img.save(path)
    print("wrote", path.relative_to(ROOT))


def crystal(d):
    outline = (38, 18, 70, 255)
    body = (166, 92, 255, 255)
    light = (226, 190, 255, 255)
    dark = (98, 46, 178, 255)
    # Main gem.
    d.polygon(poly([(32, 3), (55, 27), (32, 61), (9, 27)]), fill=body, outline=outline, width=2)
    d.polygon(poly([(32, 3), (55, 27), (32, 31), (9, 27)]), fill=light)
    d.polygon(poly([(32, 31), (55, 27), (32, 61)]), fill=dark)
    d.polygon(poly([(9, 27), (32, 31), (32, 61)]), fill=(132, 68, 224, 255))
    # Facet glints.
    line(d, [(32, 5), (32, 29)], (255, 255, 255, 210), 1)
    line(d, [(13, 27), (31, 30)], (255, 255, 255, 150), 1)
    ellipse(d, 36, 12, 39, 15, (255, 255, 255, 235))
    # Small orbiting sparkle.
    d.polygon(poly([(54, 8), (56, 13), (61, 15), (56, 17), (54, 22), (52, 17), (47, 15), (52, 13)]),
              fill=(255, 246, 200, 255))


def sword(d):
    edge = (36, 40, 52, 255)
    steel = (222, 231, 242, 255)
    shade = (143, 158, 178, 255)
    gold = (244, 187, 70, 255)
    grip = (128, 76, 48, 255)
    # Blade from lower-left to upper-right.
    d.polygon(poly([(48, 4), (56, 12), (28, 40), (20, 32)]), fill=steel, outline=edge, width=2)
    d.polygon(poly([(48, 4), (52, 8), (24, 36), (20, 32)]), fill=(255, 255, 255, 235))
    d.polygon(poly([(28, 40), (20, 32), (16, 36), (24, 44)]), fill=shade, outline=edge, width=1)
    # Crossguard.
    d.polygon(poly([(15, 30), (22, 37), (34, 25), (27, 18)]), fill=gold, outline=edge, width=2)
    # Handle and pommel.
    d.polygon(poly([(12, 27), (18, 33), (10, 41), (4, 35)]), fill=grip, outline=edge, width=2)
    ellipse(d, 3, 38, 11, 46, gold, edge, 2)


def mace(d):
    edge = (46, 34, 18, 255)
    gold = (239, 178, 62, 255)
    light = (255, 225, 148, 255)
    dark = (176, 112, 30, 255)
    grip = (122, 74, 42, 255)
    # Shaft.
    rounded(d, 28, 32, 36, 61, 3, grip, edge, 2)
    line(d, [(30, 34), (30, 58)], (176, 122, 80, 255), 1)
    # Heavy head with flanges.
    d.polygon(poly([(18, 8), (46, 8), (52, 16), (46, 28), (18, 28), (12, 16)]),
              fill=gold, outline=edge, width=2)
    d.polygon(poly([(18, 8), (46, 8), (40, 13), (24, 13)]), fill=light)
    d.polygon(poly([(18, 28), (46, 28), (40, 23), (24, 23)]), fill=dark)
    # Spikes.
    for p in [[(12, 12), (3, 8), (12, 18)], [(52, 12), (61, 8), (52, 18)],
              [(24, 8), (32, 1), (32, 8)], [(32, 8), (40, 1), (40, 8)]]:
        d.polygon(poly(p), fill=light, outline=edge, width=1)


def movement(d):
    edge = (24, 54, 32, 255)
    green = (132, 214, 92, 255)
    pale = (206, 248, 174, 255)
    white = (245, 255, 240, 255)
    # Wing behind the boot.
    d.polygon(poly([(12, 16), (30, 24), (28, 34), (8, 26)]), fill=pale, outline=edge, width=1)
    d.polygon(poly([(14, 19), (27, 25), (25, 30), (12, 24)]), fill=white)
    # Boot.
    d.polygon(poly([(30, 10), (46, 10), (48, 32), (58, 38), (58, 52), (22, 52), (20, 34)]),
              fill=green, outline=edge, width=2)
    d.polygon(poly([(30, 11), (45, 11), (46, 30), (26, 30)]), fill=pale)
    line(d, [(24, 40), (54, 40)], (255, 255, 255, 180), 2)
    line(d, [(58, 43), (58, 49)], edge, 2)


def player(d):
    edge = (20, 58, 42, 255)
    body = (72, 202, 138, 255)
    light = (168, 246, 206, 255)
    dark = (36, 138, 92, 255)
    # Head.
    ellipse(d, 22, 7, 42, 27, body, edge, 2)
    ellipse(d, 25, 10, 31, 16, light)
    # Shoulders.
    d.polygon(poly([(8, 60), (12, 43), (22, 32), (42, 32), (52, 43), (56, 60)]),
              fill=dark, outline=edge, width=2)
    d.polygon(poly([(22, 33), (42, 33), (36, 44), (28, 44)]), fill=body)
    line(d, [(32, 44), (32, 59)], (150, 235, 194, 200), 1)


def render(d):
    edge = (44, 24, 68, 255)
    body = (182, 108, 246, 255)
    light = (232, 194, 255, 255)
    dark = (104, 52, 166, 255)
    # Spyglass tube, lower-left to upper-right.
    d.polygon(poly([(12, 44), (18, 38), (44, 12), (52, 20), (26, 46), (20, 52)]),
              fill=dark, outline=edge, width=2)
    d.polygon(poly([(16, 42), (22, 48), (46, 24), (42, 20)]), fill=body)
    # Objective lens.
    ellipse(d, 40, 8, 56, 24, light, edge, 2)
    ellipse(d, 44, 12, 52, 20, (250, 248, 255, 255), body, 1)
    # Eyepiece.
    ellipse(d, 8, 40, 20, 52, body, edge, 2)
    ellipse(d, 11, 43, 17, 49, (36, 20, 56, 255))


def world(d):
    edge = (20, 62, 104, 255)
    ocean = (56, 152, 226, 255)
    land = (96, 196, 116, 255)
    land2 = (132, 218, 142, 255)
    # Globe.
    ellipse(d, 6, 6, 58, 58, ocean, edge, 3)
    # Continents (simple smooth blobs).
    d.polygon(poly([(16, 20), (26, 15), (32, 19), (29, 27), (21, 30), (16, 27)]), fill=land)
    d.polygon(poly([(34, 12), (46, 14), (50, 21), (43, 26), (35, 22)]), fill=land2)
    d.polygon(poly([(22, 34), (34, 31), (42, 38), (38, 50), (27, 52), (21, 44)]), fill=land)
    d.polygon(poly([(46, 34), (54, 38), (52, 48), (45, 46)]), fill=land2)
    # Latitude / longitude lines.
    for y in (24, 32, 40):
        line(d, [(8, y), (56, y)], (230, 248, 255, 110), 1)
    for x in (24, 32, 40):
        line(d, [(x, 8), (x, 56)], (230, 248, 255, 95), 1)
    ellipse(d, 6, 6, 58, 58, None, edge, 3)


def donut(d):
    edge = (96, 44, 78, 255)
    dough = (226, 166, 104, 255)
    frost = (250, 132, 190, 255)
    light = (255, 196, 226, 255)
    # Dough ring.
    ellipse(d, 5, 12, 59, 56, dough, edge, 3)
    ellipse(d, 22, 27, 42, 45, (0, 0, 0, 0))
    # Frosting drips.
    d.polygon(poly([(7, 26), (14, 18), (24, 15), (34, 15), (46, 17), (56, 25),
                    (54, 34), (49, 31), (47, 40), (41, 35), (36, 43), (30, 36),
                    (25, 43), (20, 34), (14, 39), (11, 30)]), fill=frost)
    ellipse(d, 20, 25, 44, 47, (0, 0, 0, 0))
    line(d, [(12, 24), (20, 19), (30, 17)], light, 2)
    # Sprinkles.
    for x, y, col in [(16, 29, (255, 240, 120)), (24, 21, (118, 226, 255)),
                      (34, 22, (255, 255, 255)), (45, 27, (138, 240, 170)),
                      (51, 35, (255, 214, 120)), (19, 43, (255, 255, 255)),
                      (40, 46, (120, 220, 255))]:
        line(d, [(x - 2, y + 1), (x + 2, y - 1)], col, 2)


def util(d):
    edge = (74, 38, 30, 255)
    meat = (166, 78, 68, 255)
    light = (232, 142, 132, 255)
    fat = (250, 222, 210, 255)
    # Steak silhouette.
    d.polygon(poly([(8, 34), (12, 18), (24, 10), (40, 12), (54, 22), (57, 38),
                    (50, 52), (32, 58), (14, 52)]), fill=meat, outline=edge, width=3)
    d.polygon(poly([(14, 34), (18, 22), (28, 16), (40, 18), (49, 26), (50, 38),
                    (42, 48), (28, 51), (17, 45)]), fill=light)
    # Marbling.
    line(d, [(17, 30), (26, 26), (35, 29), (45, 27)], fat, 3)
    line(d, [(20, 40), (29, 36), (39, 40), (47, 37)], fat, 3)
    line(d, [(30, 18), (28, 26), (32, 34), (29, 44)], fat, 2)
    ellipse(d, 12, 20, 18, 26, fat)


def hud(d):
    edge = (74, 42, 96, 255)
    panel = (34, 28, 52, 255)
    panel_light = (62, 48, 84, 255)
    # Compact dashboard.
    rounded(d, 4, 12, 60, 52, 8, panel, edge, 3)
    rounded(d, 8, 16, 56, 25, 4, panel_light)
    # Bars.
    for x, w, color in [(10, 10, (255, 122, 180)), (23, 8, (255, 210, 110)), (34, 14, (130, 224, 255))]:
        rounded(d, x, 19, x + w, 23, 2, color)
    # Graph line.
    line(d, [(10, 44), (17, 37), (24, 41), (31, 30), (38, 35), (45, 28), (54, 32)],
         (150, 240, 190, 255), 3)
    ellipse(d, 43, 26, 47, 30, (255, 255, 255, 255))


def logo(d):
    edge = (52, 42, 70, 255)
    accent = (206, 176, 255, 255)
    white = (255, 255, 255, 255)
    tile = (36, 30, 54, 255)
    # Rounded tile with a smooth, geometric O.
    rounded(d, 4, 4, 60, 60, 14, tile, edge, 2)
    ellipse(d, 16, 12, 48, 52, tile, white, 7)
    ellipse(d, 48, 44, 54, 50, accent)


for name, fn in [
    ("crystal", crystal), ("sword", sword), ("mace", mace), ("movement", movement),
    ("player", player), ("render", render), ("world", world), ("donut", donut),
    ("util", util), ("hud", hud), ("flux_logo", logo),
]:
    finish(name, fn)
