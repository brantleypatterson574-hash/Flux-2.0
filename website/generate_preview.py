"""Regenerate website/preview.html from the current 1.21.11 client sources.

The interactive preview is a hand-laid-out HTML shell with generated regions.
This script keeps those regions in sync with the client instead of leaving the
site with stale branding, themes or module descriptions:

  * theme palettes and theme buttons come from GuiTheme.java;
  * module names, categories and descriptions come from assets/js/app.js;
  * category colours and item-sprite icons come from the same module index;
  * branding uses the current Onyx Client menu names.

Representative module settings already present in preview.html are preserved.
Gradle runs this on every build via the `updateWebsitePreview` task.
"""
from __future__ import annotations

import json
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
GUI_THEME = ROOT / "src/main/java/com/yourname/utilmod/ui/GuiTheme.java"
APP_JS = ROOT / "website/assets/js/app.js"
PREVIEW = ROOT / "website/preview.html"

CSS_BEGIN = "/* BEGIN GENERATED: theme palettes */"
CSS_END = "/* END GENERATED: theme palettes */"
BUTTON_BEGIN = "<!-- BEGIN GENERATED: theme buttons -->"
BUTTON_END = "<!-- END GENERATED: theme buttons -->"
MODULE_BEGIN = "/* BEGIN GENERATED: module data */"
MODULE_END = "/* END GENERATED: module data */"
CATEGORY_BEGIN = "/* BEGIN GENERATED: categories */"
CATEGORY_END = "/* END GENERATED: categories */"

COLOR_FIELDS = [
    "overlay", "titleBar", "titleAccent", "accent", "panel", "panelBorder",
    "header", "headerGradientEnd", "moduleRow", "moduleRowEnabled",
    "moduleRowHover", "settingRow", "settingRowHover", "text", "textSecondary",
    "toggleOn", "toggleOff", "scrollTrack", "searchBg", "searchBorder",
    "outline", "shadow",
]

CSS_VARS = [
    ("--overlay", "overlay"),
    ("--accent", "titleAccent"),
    ("--accent-lo", "accent"),
    ("--panel", "panel"),
    ("--border", "panelBorder"),
    ("--header-a", "header"),
    ("--header-b", "headerGradientEnd"),
    ("--row", "moduleRow"),
    ("--row-on", "moduleRowEnabled"),
    ("--row-hover", "moduleRowHover"),
    ("--setting", "settingRow"),
    ("--text", "text"),
    ("--dim", "textSecondary"),
    ("--on", "toggleOn"),
    ("--off", "toggleOff"),
    ("--track", "scrollTrack"),
    ("--search", "searchBg"),
    ("--green", "toggleOn"),
]


def strip_java_comments(value: str) -> str:
    value = re.sub(r"/\*.*?\*/", "", value, flags=re.S)
    return re.sub(r"//[^\n]*", "", value)


def parse_themes() -> list[dict]:
    text = GUI_THEME.read_text(encoding="utf-8")
    enum_at = text.index("public enum GuiTheme")
    body_at = text.index("{", enum_at) + 1
    body_end = text.index("\n    ;", body_at)
    body = strip_java_comments(text[body_at:body_end])

    themes = []
    for name_match in re.finditer(r"([A-Z][A-Z0-9_]*)\s*\(", body):
        args = matching_delimiter(body, name_match.end() - 1, "(", ")")[1:-1]
        tokens = [part.strip() for part in args.split(",") if part.strip()]
        if len(tokens) != 25:
            raise RuntimeError(f"Unexpected GuiTheme entry {name_match.group(1)}: {len(tokens)} args")
        label = json.loads(tokens[0])
        colors = [int(token, 0) for token in tokens[1:23]]
        themes.append({
            "key": name_match.group(1).lower(),
            "label": label,
            **dict(zip(COLOR_FIELDS, colors)),
        })

    if len(themes) < 30:
        raise RuntimeError(f"Expected the full GuiTheme set, found {len(themes)}")
    return themes


def css_color(value: int) -> str:
    alpha = (value >> 24) & 0xFF
    red = (value >> 16) & 0xFF
    green = (value >> 8) & 0xFF
    blue = value & 0xFF
    if alpha == 0xFF:
        return f"#{red:02X}{green:02X}{blue:02X}"
    return f"rgba({red}, {green}, {blue}, {alpha / 255:.3f})".replace(".000", "0")


def style_block(theme: dict, indent: str = "  ") -> str:
    lines = []
    for css_var, field in CSS_VARS:
        value = css_color(theme[field])
        if css_var == "--overlay":
            alpha = (theme[field] >> 24) & 0xFF
            red = (theme[field] >> 16) & 0xFF
            green = (theme[field] >> 8) & 0xFF
            blue = theme[field] & 0xFF
            value = f"rgba({red}, {green}, {blue}, {alpha / 255:.3f})"
            value = value.replace(".000)", "0)")
        lines.append(f"{indent}{css_var}: {value};")
    return "\n".join(lines)


def theme_css(themes: list[dict]) -> str:
    default = next((theme for theme in themes if theme["key"] == "prestige"), themes[0])
    lines = [":root {", "  --scale: 1;", style_block(default), "}"]
    for theme in themes:
        lines.append(f'html[data-theme="{theme["key"]}"] {{')
        lines.append(style_block(theme))
        lines.append("}")
    return "\n".join(lines)


def theme_buttons(themes: list[dict]) -> str:
    buttons = []
    for theme in themes:
        active = ' class="active"' if theme["key"] == "prestige" else ""
        buttons.append(f'  <button data-theme="{theme["key"]}"{active}>{theme["label"]}</button>')
    return "\n".join(buttons)


def js_string(value: str) -> str:
    return json.dumps(value, ensure_ascii=False)


def parse_app_data() -> tuple[list[dict], list[dict]]:
    app = APP_JS.read_text(encoding="utf-8")

    category_pattern = re.compile(
        r'\{\s*id:\s*"([^"]+)",\s*label:\s*"([^"]+)",\s*'
        r'icon:\s*"([^"]+)",\s*color:\s*"([^"]+)"\s*\}'
    )
    categories = [
        {"id": match.group(1), "label": match.group(2),
         "icon": match.group(3), "color": match.group(4)}
        for match in category_pattern.finditer(app)
        if match.group(1) != "ALL"
    ]

    module_pattern = re.compile(
        r'\{\s*n:\s*("(?:[^"\\]|\\.)*")\s*,\s*c:\s*"([A-Z]+)"\s*,\s*'
        r't:\s*("(?:[^"\\]|\\.)*")\s*,\s*d:\s*("(?:[^"\\]|\\.)*")\s*\}'
    )
    modules = []
    for match in module_pattern.finditer(app):
        modules.append({
            "n": json.loads(match.group(1)),
            "c": match.group(2),
            "d": json.loads(match.group(4)),
        })

    if len(categories) < 5 or len(modules) < 50:
        raise RuntimeError(f"Module index looks incomplete: {len(categories)} categories, {len(modules)} modules")
    return categories, modules


def matching_delimiter(text: str, start: int, opening: str, closing: str) -> str:
    depth = 0
    in_string = False
    escaped = False
    for index in range(start, len(text)):
        char = text[index]
        if in_string:
            if escaped:
                escaped = False
            elif char == "\\":
                escaped = True
            elif char == '"':
                in_string = False
            continue
        if char == '"':
            in_string = True
        elif char == opening:
            depth += 1
        elif char == closing:
            depth -= 1
            if depth == 0:
                return text[start:index + 1]
    raise RuntimeError(f"Unclosed {opening}{closing} block in source")


def matching_bracket(text: str, start: int) -> str:
    return matching_delimiter(text, start, "[", "]")


def split_objects(array_text: str) -> list[str]:
    objects = []
    depth = 0
    start = -1
    in_string = False
    escaped = False
    for index, char in enumerate(array_text):
        if in_string:
            if escaped:
                escaped = False
            elif char == "\\":
                escaped = True
            elif char == '"':
                in_string = False
            continue
        if char == '"':
            in_string = True
        elif char == "{":
            if depth == 0:
                start = index
            depth += 1
        elif char == "}" and depth:
            depth -= 1
            if depth == 0 and start >= 0:
                objects.append(array_text[start:index + 1])
                start = -1
    return objects


def parse_preview_modules(array_text: str) -> tuple[dict[str, str], dict[str, bool]]:
    settings: dict[str, str] = {}
    enabled: dict[str, bool] = {}
    for obj in split_objects(array_text):
        name_match = re.search(r'\bn\s*:\s*("(?:[^"\\]|\\.)*")', obj)
        setting_match = re.search(r'\bs\s*:', obj)
        if not name_match or not setting_match:
            continue
        name = json.loads(name_match.group(1))
        bracket_at = obj.index("[", setting_match.end())
        settings[name] = matching_bracket(obj, bracket_at)
        on_match = re.search(r'\bon\s*:\s*(true|false)', obj)
        enabled[name] = bool(on_match and on_match.group(1) == "true")
    return settings, enabled


def module_js(modules: list[dict], settings: dict[str, str], enabled: dict[str, bool]) -> str:
    lines = ["const MODULES = ["]
    for index, module in enumerate(modules):
        comma = "," if index + 1 < len(modules) else ""
        lines.append(
            f'  {{ n: {js_string(module["n"])}, c: "{module["c"]}", '
            f'on: {"true" if enabled.get(module["n"], False) else "false"}, '
            f'd: {js_string(module["d"])}, s:{settings.get(module["n"], "[]")} }}{comma}'
        )
    lines.append("];")
    return "\n".join(lines)


def category_js(categories: list[dict]) -> str:
    names = ", ".join(js_string(category["id"]) for category in categories)
    lines = [f"const CATEGORIES = [{names}];", "const CATEGORY_COLORS = {"]
    lines.extend(f'  {category["id"]}: {js_string(category["color"])},' for category in categories)
    lines.append("};")
    lines.append("const CATEGORY_ICONS = {")
    lines.extend(f'  {category["id"]}: {js_string(category["icon"])},' for category in categories)
    lines.append("};")
    return "\n".join(lines)


def replace_section(text: str, begin: str, end: str, content: str, legacy: re.Pattern[str]) -> str:
    block = f"{begin}\n{content}\n{end}"
    marker_pattern = re.compile(re.escape(begin) + r".*?" + re.escape(end), re.S)
    if marker_pattern.search(text):
        return marker_pattern.sub(lambda _: block, text, count=1)
    if legacy.search(text):
        return legacy.sub(lambda _: block, text, count=1)
    raise RuntimeError(f"Could not find preview section {begin}")


def sync_static_text(text: str, themes: list[dict], categories: list[dict], modules: list[dict]) -> str:
    replacements = [
        # Older shells were saved once through a cp1252 console, which left
        # mojibake in the two Advanced-arrow glyphs. Normalise them so the
        # preview never shows code-like fragments instead of triangles.
        ('\u00e2\u2013\u00b8', '\u25b8'),
        ('\u00e2\u2013\u00be', '\u25be'),
        ('<div class="logo">U</div>', '<div class="logo">O</div>'),
        ('<div class="brand">UTILMOD</div>', '<div class="brand">ONYX CLIENT</div>'),
        ('<div class="name">UTILMOD</div>', '<div class="name">ONYX CLIENT</div>'),
        ("UtilMod's shared three-column menu shell.", "Onyx Client's shared three-column menu shell."),
    ]
    for old, new in replacements:
        text = text.replace(old, new)

    default = next((theme for theme in themes if theme["key"] == "prestige"), themes[0])
    text = re.sub(
        r'(id="themeChip"[^>]*>)[^<]*(</div>)',
        lambda match: f'{match.group(1)}{default["label"].upper()}  &gt;{match.group(2)}',
        text,
        count=1,
    )

    footer = (
        f"Preview of the <b>Onyx Client ClickGUI</b> — {len(modules)} registered modules "
        f"across {len(categories)} categories, {len(themes)} themes, original Onyx styling. "
        "The top chips cycle the theme and switch layouts; toggles, search and settings "
        "are interactive. This is not the live game renderer."
    )
    text = re.sub(
        r"Preview of the <b>.*?</b> — .*?This is not the live game renderer\.",
        lambda _: footer,
        text,
        count=1,
        flags=re.S,
    )

    info = (
        f'<div class="setting"><span class="label">Modules registered</span>'
        f'<span class="value">{len(modules)}</span></div>\n'
        f'    <div class="setting"><span class="label">Themes available</span>'
        f'<span class="value">{len(themes)}</span></div>'
    )
    text = re.sub(
        r'<div class="setting"><span class="label">Modules registered</span>.*?</div>'
        r'(?:\n\s*<div class="setting"><span class="label">Themes available</span>.*?</div>)*',
        lambda _: info,
        text,
        count=1,
    )
    return text


def main() -> int:
    text = PREVIEW.read_text(encoding="utf-8")
    themes = parse_themes()
    categories, modules = parse_app_data()

    legacy_modules = re.search(r"const MODULES = \[.*?\n\];", text, re.S)
    if not legacy_modules and MODULE_BEGIN not in text:
        raise RuntimeError("Could not locate preview module data")
    settings, enabled = parse_preview_modules(
        legacy_modules.group(0) if legacy_modules else
        re.search(re.escape(MODULE_BEGIN) + r".*?" + re.escape(MODULE_END), text, re.S).group(0)
    )

    text = replace_section(
        text, CSS_BEGIN, CSS_END, theme_css(themes),
        re.compile(r"/\* ---------- Theme palettes.*?(?=\n\n\* \{ box-sizing)", re.S),
    )
    text = replace_section(
        text, BUTTON_BEGIN, BUTTON_END, theme_buttons(themes),
        re.compile(r"<strong>Theme</strong>\n.*?<span class=\"sep\"></span>", re.S),
    )
    text = replace_section(
        text, MODULE_BEGIN, MODULE_END, module_js(modules, settings, enabled),
        re.compile(r"const MODULES = \[.*?\n\];", re.S),
    )
    text = replace_section(
        text, CATEGORY_BEGIN, CATEGORY_END, category_js(categories),
        re.compile(r"const CATEGORIES = \[.*?const CATEGORY_ICONS = \{.*?\n\};", re.S),
    )
    text = sync_static_text(text, themes, categories, modules)

    if text != PREVIEW.read_text(encoding="utf-8"):
        PREVIEW.write_text(text, encoding="utf-8")
        print(f"PREVIEW_UPDATED themes={len(themes)} modules={len(modules)} categories={len(categories)}")
    else:
        print(f"PREVIEW_CURRENT themes={len(themes)} modules={len(modules)} categories={len(categories)}")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
