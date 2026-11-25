"""Build a 26.x source overlay from the shared Yarn-named 1.21.11 sources.

Minecraft 26.x renamed or removed a number of client APIs. Rather than litter
the recommended 1.21.11 source tree with version checks, this script copies it
into build/generated26/java and applies the small, mechanical API substitutions
needed by all 26.x profiles. The original src/main/java is never modified.
"""

from __future__ import annotations

import re
import shutil
import sys
import zipfile
from collections import defaultdict
from pathlib import Path

ROOT = Path(__file__).resolve().parents[2]
SOURCE = ROOT / "src" / "main" / "java"
OUTPUT = ROOT / "build" / "generated26" / "java"
TARGET = sys.argv[1] if len(sys.argv) > 1 else "26.3"
MAPPINGS = ROOT / "versions" / TARGET / "mappings.jar"
# 26.3 replaced the older input, animation and render-state APIs. Keep the
# mechanical rewrites scoped to that profile so 26.1/26.2 stay on their own
# signatures.
IS_263 = TARGET == "26.3"
# 26.1 still exposes the older client field/method names and keeps the shared
# source's uppercase registry constants. Only the later 26.x profiles need the
# intermediary-field and render-state rewrites.
IS_261 = TARGET == "26.1"

# Static registry constants are assigned Yarn/intermediary field names in the
# generated Tiny jar. Read the exact official -> named map from the profile so
# the overlay uses the symbol javac actually sees.
HOLDER_CLASSES = {"Blocks", "Items", "EntityType"}
HOLDER_FIELD = re.compile(r"\b(Blocks|Items|EntityType)\.([A-Z][A-Z0-9_]*)\b")

BLOCKED_ACCESSORS = {
    "com/yourname/utilmod/mixin/AttackRangeComponentMixin.java": {
        ".minRange()": ".minReach()",
        ".maxRange()": ".maxReach()",
        ".minCreativeRange()": ".minCreativeReach()",
        ".maxCreativeRange()": ".maxCreativeReach()",
        ".hitboxMargin()": ".comp_5262()",
        ".mobFactor()": ".comp_5263()",
    },
    "com/yourname/utilmod/mixin/ProjectileUtilHitboxMixin.java": {
        ".minRange()": ".minReach()",
        ".maxRange()": ".maxReach()",
        ".minCreativeRange()": ".minCreativeReach()",
        ".maxCreativeRange()": ".maxCreativeReach()",
        ".hitboxMargin()": ".comp_5262()",
        ".mobFactor()": ".comp_5263()",
    },
}

WORLD_RENDERER_MIXIN = """package com.yourname.utilmod.mixin;

import net.minecraft.client.render.WorldRenderer;
import org.spongepowered.asm.mixin.Mixin;

/**
 * 26.x moved weather and cloud drawing into WeatherRendering and CloudRenderer.
 * The old WorldRenderer hooks no longer exist, so this mixin is intentionally
 * inert on 26.x; the Weather module still applies its other client-side effects.
 */
@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
}
"""

ANTI_BED_LOOP = """        for (BlockEntity entity : client.world.getBlockEntities()) {
            if (!(entity instanceof BedBlockEntity)) continue;
            BlockPos pos = entity.getPos();
            if (pos == null || Vec3d.ofCenter(pos).distanceTo(client.player.getEyePos()) > range.getValue()) continue;
            // The two halves of a bed share one entity layout; keep only the foot.
            var state = client.world.getBlockState(pos);
            if (state.getBlock() instanceof BedBlock && state.get(BedBlock.PART) != BedPart.FOOT) continue;
            beds.add(pos);
        }
"""

ANTI_BED_LOOP_26 = """        // 26.x removed BedBlockEntity. Scan the block states around the player
        // instead; this keeps the warning overlay working without depending on
        // a block entity that no longer exists.
        BlockPos center = client.player.getBlockPos();
        int radius = (int) Math.ceil(range.getValue());
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    BlockPos pos = center.add(dx, dy, dz);
                    var state = client.world.getBlockState(pos);
                    if (!(state.getBlock() instanceof BedBlock)) continue;
                    if (state.get(BedBlock.PART) != BedPart.FOOT) continue;
                    if (Vec3d.ofCenter(pos).distanceTo(client.player.getEyePos()) > range.getValue()) continue;
                    beds.add(pos);
                }
            }
        }
"""

ICON_DRAW = """        c.drawTexture(RenderPipelines.GUI_TEXTURED, id, x, y,
            0.0f, 0.0f, size, size, 64, 64, 64, 64, color);"""

ICON_DRAW_26 = """        c.drawTexturedQuad(id, x, y, size, size, 0.0f, 0.0f, 1.0f, 1.0f);"""

DRAW_TEXTURE_BLOCK = """        context.drawTexture(RenderPipelines.GUI_TEXTURED, id, x, y,
            0.0f, 0.0f, size, size, 64, 64, 64, 64,
            (Math.max(0, Math.min(255, (int) (alpha * 255.0f))) << 24) | 0x00FFFFFF);"""

DRAW_TEXTURE_BLOCK_26 = """        context.drawTexturedQuad(id, x, y, size, size, 0.0f, 0.0f, 1.0f, 1.0f);"""

KEY_INPUT_ACCESSORS = {
    "key": "comp_4795",
    "scancode": "keycode" if IS_263 else "comp_4796",
    "modifiers": "comp_4797",
}
KEY_INPUT_RECEIVERS = ("value", "input", "click")


def replace_once(text: str, old: str, new: str, label: str) -> str:
    if old not in text:
        raise RuntimeError(f"26.x overlay: expected text not found for {label}")
    return text.replace(old, new, 1)


def load_static_field_renames() -> dict[str, dict[str, str]]:
    renames: dict[str, dict[str, str]] = defaultdict(dict)
    if not MAPPINGS.is_file():
        raise RuntimeError(f"26.x overlay: missing mapping jar {MAPPINGS}")
    with zipfile.ZipFile(MAPPINGS) as archive:
        text = archive.read("mappings/mappings.tiny").decode("utf-8")
    for line in text.splitlines():
        parts = line.split("\t")
        if len(parts) < 5 or parts[0] != "FIELD":
            continue
        owner = parts[1].rsplit("/", 1)[-1]
        if owner not in HOLDER_CLASSES:
            continue
        official, named = parts[3], parts[4]
        if official != named and re.fullmatch(r"[A-Z][A-Z0-9_]*", official):
            renames[owner][official] = named
    return dict(renames)


def rewrite_static_fields(text: str, renames: dict[str, dict[str, str]]) -> str:
    def replace(match: re.Match[str]) -> str:
        named = renames.get(match.group(1), {}).get(match.group(2))
        return f"{match.group(1)}.{named}" if named else match.group(0)

    return HOLDER_FIELD.sub(replace, text)


def rewrite_method(text: str, method: str, transform) -> str:
    """Rewrite every `.method(arguments)` call, balancing nested parentheses."""
    marker = "." + method + "("
    result: list[str] = []
    cursor = 0
    while True:
        start = text.find(marker, cursor)
        if start < 0:
            result.append(text[cursor:])
            break
        open_paren = start + len(marker) - 1
        depth = 0
        index = open_paren
        while index < len(text):
            if text[index] == "(":
                depth += 1
            elif text[index] == ")":
                depth -= 1
                if depth == 0:
                    break
            index += 1
        if depth:
            raise RuntimeError(f"26.x overlay: unbalanced .{method}( call")
        args = text[open_paren + 1:index]
        result.append(text[cursor:start])
        result.append(transform(args))
        cursor = index + 1
    return "".join(result)


def rewrite_is_calls(text: str) -> str:
    """Rewrite ItemStack.isOf/isIn while leaving BlockState.isOf untouched."""
    result: list[str] = []
    cursor = 0
    pattern = re.compile(r"\.is(Of|In)\(")
    while True:
        match = pattern.search(text, cursor)
        if match is None:
            result.append(text[cursor:])
            break
        dot = match.start()
        open_paren = match.end() - 1
        depth = 1
        index = open_paren + 1
        while index < len(text) and depth:
            if text[index] == "(":
                depth += 1
            elif text[index] == ")":
                depth -= 1
            index += 1
        if depth:
            raise RuntimeError("26.x overlay: unbalanced .isOf/.isIn call")

        # Recover the receiver by walking back over balanced brackets.
        start = dot
        brackets = 0
        while start > 0:
            char = text[start - 1]
            if char in ")]":
                brackets += 1
            elif char in "([":
                if brackets:
                    brackets -= 1
                else:
                    break
            elif not brackets and (char.isspace() or char in ",;{}:=!&|?"):
                break
            start -= 1
        receiver = text[start:dot].strip()
        arg = text[open_paren + 1:index - 1].strip()

        state_call = "getBlockState" in receiver or receiver in {"state", "blockState"}
        block_arg = arg.startswith("Blocks.") or arg.startswith("BlockTags.")
        if state_call or block_arg:
            result.append(text[cursor:index])
        else:
            helper = "is" if match.group(1) == "Of" else "isIn"
            result.append(text[cursor:start])
            result.append(f"com.yourname.utilmod.util.StackCompat.{helper}({receiver}, {arg})")
        cursor = index
    return "".join(result)


def rewrite_key_input_accessors(text: str) -> str:
    """Rename KeyInput/Click accessors without touching KeybindSetting methods."""
    receivers = "|".join(KEY_INPUT_RECEIVERS)
    pattern = re.compile(rf"\b({receivers})\.(key|scancode|modifiers)\(\)")

    def replace(match: re.Match[str]) -> str:
        return f"{match.group(1)}.{KEY_INPUT_ACCESSORS[match.group(2)]}()"

    return pattern.sub(replace, text)


def rewrite(text: str, relative: str, renames: dict[str, dict[str, str]]) -> str:
    text = rewrite_is_calls(text)
    text = text.replace(".isOf(", ".is(")
    if not IS_261:
        text = text.replace("Items.RED_BED", "Items.BED.red()")
        # Later 26.x moved the static block-entity constants to BlockEntityTypes.
        text = text.replace("BlockEntityType.", "BlockEntityTypes.")
        text = rewrite_static_fields(text, renames)

        if "BlockEntityTypes." in text and "import net.minecraft.world.level.block.entity.BlockEntityTypes;" not in text:
            if "import net.minecraft.block.entity.BlockEntityType;" in text:
                text = text.replace(
                    "import net.minecraft.block.entity.BlockEntityType;",
                    "import net.minecraft.block.entity.BlockEntityType;\n"
                    "import net.minecraft.world.level.block.entity.BlockEntityTypes;",
                )
            else:
                text = re.sub(
                    r"(package [^\n]+\n)",
                    r"\1\nimport net.minecraft.world.level.block.entity.BlockEntityTypes;\n",
                    text,
                    count=1,
                )

        text = re.sub(r"(?<=[A-Za-z0-9_\)\]])\.currentScreen\b", ".inGameHud.screen()", text)
        text = re.sub(r"\.options\.hudHidden\b", ".inGameHud.hud.isHidden()", text)
    text = text.replace(".drawText(", ".text(")
    text = rewrite_method(
        text,
        "drawTextWithShadow",
        lambda args: ".text(" + args.rstrip() + ", true)",
    )
    if IS_263:
        text = rewrite_method(
            text,
            "swingHand",
            lambda args: (
                ".swing(" + args.rstrip()
                + ", net.minecraft.component.type.SwingAnimationComponent.DEFAULT, true)"
            ),
        )
    text = text.replace(".offset(", ".relative(")
    text = text.replace(".readString(", ".readUtf(")
    text = text.replace(".clickSlot(", ".handleContainerInput(")
    text = re.sub(r"(Registries\.[A-Z_]+\.)getId\(", r"\1getKey(", text)
    text = text.replace(".nutrition()", ".comp_2491()")
    text = text.replace(".saturation()", ".comp_2492()")
    if not IS_261:
        text = text.replace(".getCamera()", ".mainCamera()")
    text = text.replace("mc.getWindow().getWidth()", "mc.getWindow().getScreenWidth()")
    text = text.replace("mc.getWindow().getHeight()", "mc.getWindow().getScreenHeight()")
    text = text.replace("PayloadTypeRegistry.playC2S()", "PayloadTypeRegistry.serverboundPlay()")
    text = text.replace("PayloadTypeRegistry.playS2C()", "PayloadTypeRegistry.clientboundPlay()")
    if not IS_261:
        text = text.replace("client.getToastManager()", "client.inGameHud.toastManager()")
    text = text.replace(".getActiveItem()", ".getActiveOrMainHandStack()")
    text = text.replace(".stopUsingItem()", ".releaseUsingItem()")
    if not IS_261:
        text = text.replace("client.setScreen(", "client.inGameHud.setScreen(")
    text = text.replace("click.x()", "click.comp_4798()")
    text = text.replace("click.y()", "click.comp_4799()")
    text = text.replace(
        "public void render(DrawContext context, int mouseX, int mouseY, float delta)",
        "public void extractRenderState(DrawContext context, int mouseX, int mouseY, float delta)",
    )
    if not IS_261:
        text = text.replace(ICON_DRAW, ICON_DRAW_26)
        if relative == "com/yourname/utilmod/ui/DropdownMenuScreen.java":
            text = replace_once(text, DRAW_TEXTURE_BLOCK, DRAW_TEXTURE_BLOCK_26, "DropdownMenuScreen texture draw")
    text = text.replace("context.drawItemWithoutEntity(stack, 0, 0);", "context.item(stack, 0, 0);")
    text = re.sub(
        r"PlayerInteractEntityC2SPacket\.attack\(\s*(\w+)\s*,\s*([^)]*?)\s*\)",
        r"new PlayerInteractEntityC2SPacket(\1.getId(), net.minecraft.util.Hand.MAIN_HAND, \1.getEntityPos(), \2)",
        text,
    )
    if IS_263:
        text = re.sub(r"InputUtil\.isKeyPressed\([^,]+,\s*([^)]+)\)", r"InputUtil.isKeyDown(\1)", text)
        text = text.replace("InputUtil.Type.KEYSYM", "InputUtil.Type.KEYBOARD")
    text = rewrite_key_input_accessors(text)

    if relative == "com/yourname/utilmod/ui/MenuLinks.java":
        text = replace_once(
            text,
            "ConfirmLinkScreen.open(parent, PROFILE_URL, true);",
            "ConfirmLinkScreen.open(parent, URI.create(PROFILE_URL), true);",
            "MenuLinks URI",
        )
        if "import java.net.URI;" not in text:
            text = re.sub(r"(package [^\n]+\n)", r"\1\nimport java.net.URI;\n", text, count=1)

    if relative == "com/yourname/utilmod/mixin/ChatFilterMixin.java":
        if IS_263:
            text = text.replace("packet.unsignedContent()", "packet.unsignedContent().orElse(null)")
        else:
            text = text.replace("packet.unsignedContent()", "packet.comp_1103()")
        text = text.replace("packet.body().content()", "packet.comp_1102().comp_1090()")
        text = text.replace("packet.body()", "packet.comp_1102()")
        text = text.replace("packet.message()", "packet.comp_1097()")

    if IS_263:
        if relative == "com/yourname/utilmod/module/impl/SkyPaletteModule.java":
            text = text.replace(
                "state.skyColor = 0xFF000000 | module.skyColor.getColorValue();",
                "com.yourname.utilmod.util.SkyColorCompat.setSky(state, 0xFF000000 | module.skyColor.getColorValue());",
            )
            text = text.replace(
                "state.sunriseAndSunsetColor = 0xFF000000 | module.horizonColor.getColorValue();",
                "com.yourname.utilmod.util.SkyColorCompat.setHorizon(state, 0xFF000000 | module.horizonColor.getColorValue());",
            )
            text = text.replace(
                "state.skyColor = palette.sky();",
                "com.yourname.utilmod.util.SkyColorCompat.setSky(state, palette.sky());",
            )
            text = text.replace(
                "state.sunriseAndSunsetColor = palette.horizon();",
                "com.yourname.utilmod.util.SkyColorCompat.setHorizon(state, palette.horizon());",
            )

        if relative == "com/yourname/utilmod/module/impl/WeatherChangerModule.java":
            text = text.replace(
                "state.skyColor = ColorUtil.mix(state.skyColor, stormTint, shade * 0.70f);",
                "state.skyColor = com.yourname.utilmod.util.SkyColorCompat.mixSky(state.skyColor, stormTint, shade * 0.70f);",
            )
            text = text.replace(
                "state.sunriseAndSunsetColor = ColorUtil.mix(state.sunriseAndSunsetColor, stormTint, shade * 0.45f);",
                "state.sunriseAndSunsetColor = com.yourname.utilmod.util.SkyColorCompat.mixHorizon(state.sunriseAndSunsetColor, stormTint, shade * 0.45f);",
            )

    for old, new in BLOCKED_ACCESSORS.get(relative, {}).items():
        text = text.replace(old, new)

    if relative == "com/yourname/utilmod/mixin/WorldRendererMixin.java":
        return WORLD_RENDERER_MIXIN
    if relative == "com/yourname/utilmod/module/impl/AntiBedModule.java":
        text = text.replace("import net.minecraft.block.entity.BedBlockEntity;\n", "")
        text = text.replace("import net.minecraft.block.entity.BlockEntity;\n", "")
        text = replace_once(text, ANTI_BED_LOOP, ANTI_BED_LOOP_26, "AntiBedModule bed scan")
    return text


def main() -> None:
    renames = load_static_field_renames()
    if OUTPUT.exists():
        shutil.rmtree(OUTPUT)
    OUTPUT.mkdir(parents=True)
    count = 0
    for source in sorted(SOURCE.rglob("*.java")):
        relative = source.relative_to(SOURCE).as_posix()
        target = OUTPUT / relative
        target.parent.mkdir(parents=True, exist_ok=True)
        target.write_text(
            rewrite(source.read_text(encoding="utf-8"), relative, renames),
            encoding="utf-8",
        )
        count += 1
    print(f"prepared {count} {TARGET} sources -> {OUTPUT.relative_to(ROOT)}")


if __name__ == "__main__":
    main()
