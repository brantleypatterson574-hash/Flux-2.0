"""Generate Yarn-style mappings for the unobfuscated 26.x game jars.

Minecraft 26.x ships deobfuscated class/member names, and Fabric's intermediary
for those versions uses those same names. This script builds a Tiny v1 mapping
from the official 26.x names to the Yarn 1.21.11 names used by Flux's source.
That lets the exact same source compile for 1.21.11, 26.1, 26.2 and 26.3.
"""

from __future__ import annotations

import io
import json
import re
import urllib.request
import zipfile
from pathlib import Path

ROOT = Path(__file__).resolve().parents[2]
TOOLS = ROOT / "versions" / "tools"
CACHE = TOOLS / "cache"
TARGETS = ("26.1", "26.2", "26.3")

# A few classes were renamed after 1.21.11. The source namespace must match
# the real 26.x class names so Loom can rename them to the Yarn names Flux uses.
CLASS_SOURCE_OVERRIDES = {
    "net/minecraft/client/gui/GuiGraphics": "net/minecraft/client/gui/GuiGraphicsExtractor",
    "net/minecraft/client/renderer/state/SkyRenderState": "net/minecraft/client/renderer/state/level/SkyRenderState",
    "net/minecraft/world/inventory/ClickType": "net/minecraft/world/inventory/ContainerInput",
}

YARN_VERSION = "1.21.11+build.6"
YARN_URL = (
    "https://maven.fabricmc.net/net/fabricmc/yarn/"
    "1.21.11%2Bbuild.6/yarn-1.21.11%2Bbuild.6.jar"
)
MOJANG_1_21_11_CLIENT = (
    "https://piston-data.mojang.com/v1/objects/"
    "031a68bebf55d824f66d6573d8c752f0e1bf232a/client.txt"
)
MOJANG_1_21_11_SERVER = (
    "https://piston-data.mojang.com/v1/objects/"
    "5621e9253f05fd57872bbe7f8ddf5f9a7d525955/server.txt"
)

CLASS_LINE = re.compile(r"^(\S+)\s+->\s+(\S+):\s*$")
MEMBER_LINE = re.compile(r"^\s+(?:(?P<start>\d+):(?P<end>\d+):)?(?P<decl>.+?)\s+->\s+(?P<obf>\S+)\s*$")
METHOD_DECL = re.compile(r"^(?P<ret>\S+)\s+(?P<name>[A-Za-z_$][\w$]*)\((?P<params>.*)\)$")

PRIMITIVES = {
    "void": "V", "boolean": "Z", "byte": "B", "char": "C", "short": "S",
    "int": "I", "long": "J", "float": "F", "double": "D",
}


def download(url: str, path: Path) -> str:
    if path.suffix == ".jar":
        if not path.is_file() or path.stat().st_size == 0:
            CACHE.mkdir(parents=True, exist_ok=True)
            with urllib.request.urlopen(url, timeout=60) as response:
                path.write_bytes(response.read())
        import zipfile
        with zipfile.ZipFile(path) as archive:
            return archive.read("mappings/mappings.tiny").decode("utf-8")
    if path.is_file() and path.stat().st_size > 0:
        return path.read_text(encoding="utf-8", errors="replace")
    CACHE.mkdir(parents=True, exist_ok=True)
    with urllib.request.urlopen(url, timeout=60) as response:
        data = response.read()
    path.write_bytes(data)
    return data.decode("utf-8", errors="replace")


def download_binary(url: str, path: Path) -> bytes:
    if path.is_file() and path.stat().st_size > 0:
        return path.read_bytes()
    CACHE.mkdir(parents=True, exist_ok=True)
    with urllib.request.urlopen(url, timeout=120) as response:
        data = response.read()
    path.write_bytes(data)
    return data


def internal(type_name: str) -> str:
    return type_name.replace(".", "/")


def source_class(official_class: str) -> str:
    owner = internal(official_class)
    for old, new in CLASS_SOURCE_OVERRIDES.items():
        if owner == old:
            return new
        if owner.startswith(old + "$"):
            return new + owner[len(old):]
    return owner


def source_descriptor(desc: str) -> str:
    return re.sub(
        r"L([^;]+);",
        lambda match: "L" + source_class(match.group(1)) + ";",
        desc,
    )


class ClassFileReader:
    """Minimal class-file parser used to inspect the real 26.x member names."""

    def __init__(self, data: bytes):
        self.data = data
        self.pos = 0

    def u1(self) -> int:
        value = self.data[self.pos]
        self.pos += 1
        return value

    def u2(self) -> int:
        value = int.from_bytes(self.data[self.pos:self.pos + 2], "big")
        self.pos += 2
        return value

    def u4(self) -> int:
        value = int.from_bytes(self.data[self.pos:self.pos + 4], "big")
        self.pos += 4
        return value

    def skip(self, count: int) -> None:
        self.pos += count

    def constant_pool(self) -> list[str | None]:
        count = self.u2()
        pool: list[str | None] = [None] * count
        index = 1
        while index < count:
            tag = self.u1()
            if tag == 1:  # CONSTANT_Utf8
                length = self.u2()
                pool[index] = self.data[self.pos:self.pos + length].decode("utf-8", "replace")
                self.skip(length)
            elif tag in (3, 4):  # Integer, Float
                self.skip(4)
            elif tag in (5, 6):  # Long, Double (two slots)
                self.skip(8)
                index += 1
            elif tag in (7, 8, 16, 19, 20):  # Class, String, MethodType, Module, Package
                self.skip(2)
            elif tag in (9, 10, 11, 12, 17, 18):  # refs, NameAndType, Dynamic, InvokeDynamic
                self.skip(4)
            elif tag == 15:  # MethodHandle
                self.skip(3)
            else:
                raise ValueError(f"Unknown constant pool tag {tag}")
            index += 1
        return pool

    def members(self, pool: list[str | None]) -> tuple[set[str], set[tuple[str, str]]]:
        self.u2()  # access_flags
        self.u2()  # this_class
        self.u2()  # super_class
        for _ in range(self.u2()):  # interfaces
            self.u2()

        fields: set[str] = set()
        methods: set[tuple[str, str]] = set()
        for target in (fields, methods):
            for _ in range(self.u2()):
                self.u2()  # access_flags
                name = pool[self.u2()]
                desc = pool[self.u2()]
                if name is not None:
                    if target is fields:
                        target.add(name)
                    else:
                        target.add((name, desc or ""))
                for _ in range(self.u2()):  # attributes
                    self.u2()
                    self.skip(self.u4())
        return fields, methods


def class_members(data: bytes) -> tuple[set[str], set[tuple[str, str]]]:
    reader = ClassFileReader(data)
    if reader.u4() != 0xCAFEBABE:
        raise ValueError("Not a class file")
    reader.u2()  # minor_version
    reader.u2()  # major_version
    pool = reader.constant_pool()
    return reader.members(pool)


def read_game_members(meta: dict, target: str) -> dict[str, tuple[set[str], set[tuple[str, str]]]]:
    result: dict[str, tuple[set[str], set[tuple[str, str]]]] = {}
    for kind in ("client", "server"):
        download = meta.get("downloads", {}).get(kind)
        if not download:
            continue
        jar_path = CACHE / f"{target}-{kind}.jar"
        data = download_binary(download["url"], jar_path)
        with zipfile.ZipFile(io.BytesIO(data)) as archive:
            for entry in archive.namelist():
                if not entry.endswith(".class"):
                    continue
                try:
                    result[entry[:-6]] = class_members(archive.read(entry))
                except (ValueError, IndexError, KeyError, TypeError):
                    continue
    return result


def descriptor(source_type: str, obf_to_official: dict[str, str]) -> str:
    """Convert a Mojang mapping source type into a JVM descriptor."""
    base = source_type.strip()
    array_depth = 0
    while base.endswith("[]"):
        array_depth += 1
        base = base[:-2]
    if base in PRIMITIVES:
        result = PRIMITIVES[base]
    else:
        mapped = obf_to_official.get(base, base)
        result = f"L{internal(mapped)};"
    return "[" * array_depth + result


def method_descriptor(ret: str, params: str, obf_to_official: dict[str, str]) -> str:
    parts = [p for p in params.split(",") if p.strip()]
    return "(" + "".join(descriptor(p, obf_to_official) for p in parts) + ")" + descriptor(ret, obf_to_official)


def convert_descriptor(desc: str, mapping: dict[str, str]) -> str:
    """Replace Lclass; names in a JVM descriptor using a name mapping."""
    def repl(match: re.Match[str]) -> str:
        name = match.group(1).replace("/", ".")
        return "L" + internal(mapping.get(name, name)) + ";"
    return re.sub(r"L([^;]+);", repl, desc)


official_to_obf: dict[str, str] = {}
obf_to_official: dict[str, str] = {}
official_members: dict[str, dict] = {}

for filename, url in (
    ("1.21.11-client.txt", MOJANG_1_21_11_CLIENT),
    ("1.21.11-server.txt", MOJANG_1_21_11_SERVER),
):
    text = download(url, CACHE / filename)
    current: str | None = None
    for line in text.splitlines():
        class_match = CLASS_LINE.match(line)
        if class_match:
            current = class_match.group(1)
            obf = class_match.group(2)
            official_to_obf[current] = obf
            obf_to_official[obf] = current
            official_members.setdefault(current, {"fields": {}, "methods": {}})
            continue
        if not current:
            continue
        member = MEMBER_LINE.match(line)
        if not member:
            continue
        decl = member.group("decl").strip()
        obf_name = member.group("obf")
        method = METHOD_DECL.match(decl)
        if method:
            desc = method_descriptor(method.group("ret"), method.group("params"), obf_to_official)
            official_members[current]["methods"][(method.group("name"), desc)] = obf_name
        elif " " in decl:
            type_name, name = decl.rsplit(" ", 1)
            desc = descriptor(type_name, obf_to_official)
            official_members[current]["fields"][name] = (obf_name, desc)

# --- Yarn tiny mapping ------------------------------------------------------
yarn_text = download(YARN_URL, CACHE / "yarn-1.21.11.jar")
yarn_class: dict[str, str] = {}
yarn_method: dict[tuple[str, str, str], str] = {}
yarn_field: dict[tuple[str, str], str] = {}
for line in yarn_text.splitlines():
    parts = line.split("\t")
    if not parts:
        continue
    if parts[0] == "CLASS":
        yarn_class[parts[1]] = parts[3]
    elif parts[0] == "METHOD":
        yarn_method[(parts[1], parts[3], parts[2])] = parts[5]
    elif parts[0] == "FIELD":
        yarn_field[(parts[1], parts[3])] = parts[5]

# --- Build official -> Yarn lookup -----------------------------------------
yarn_class_by_official: dict[str, str] = {}
yarn_method_by_official: dict[tuple[str, str, str], str] = {}
yarn_method_by_official_name: dict[tuple[str, str], str] = {}
yarn_field_by_official: dict[tuple[str, str], str] = {}

for official_class, obf_class in official_to_obf.items():
    named = yarn_class.get(obf_class)
    if named:
        yarn_class_by_official[official_class] = named

    members = official_members.get(official_class, {"fields": {}, "methods": {}})
    for name, (obf_name, official_desc) in members["fields"].items():
        named_field = yarn_field.get((obf_class, obf_name))
        if named_field:
            yarn_field_by_official[(official_class, name)] = named_field

    for (name, official_desc), obf_name in members["methods"].items():
        obf_desc = convert_descriptor(official_desc, {v: k for k, v in obf_to_official.items()})
        named_method = yarn_method.get((obf_class, obf_name, obf_desc))
        if not named_method:
            # Some Yarn entries use a slightly older descriptor; try the
            # official descriptor directly before giving up.
            named_method = yarn_method.get((obf_class, obf_name, official_desc))
        if named_method:
            yarn_method_by_official[(official_class, name, official_desc)] = named_method
            yarn_method_by_official_name.setdefault((official_class, name), named_method)

print(f"MAPPED_CLASSES={len(yarn_class_by_official)}")
print(f"MAPPED_METHODS={len(yarn_method_by_official)}")
print(f"MAPPED_FIELDS={len(yarn_field_by_official)}")

# --- Local version manifests ------------------------------------------------
# Loom checks the Gradle JVM against the game's declared Java version. Gradle
# itself runs on Java 21 here; the compile toolchain is switched to Java 25 for
# 26.x below. The local manifest keeps Loom happy without changing game data.
manifest = json.loads(
    urllib.request.urlopen(
        "https://launchermeta.mojang.com/mc/game/version_manifest_v2.json", timeout=60
    ).read().decode("utf-8")
)
version_urls = {item["id"]: item["url"] for item in manifest["versions"]}
actual_by_target: dict[str, dict[str, tuple[set[str], set[tuple[str, str]]]]] = {}
for target in TARGETS:
    meta = json.loads(urllib.request.urlopen(version_urls[target], timeout=60).read().decode("utf-8"))
    if "javaVersion" in meta:
        meta["javaVersion"]["majorVersion"] = 21
    version_dir = ROOT / "versions" / target
    version_dir.mkdir(parents=True, exist_ok=True)
    (version_dir / "version.json").write_text(
        json.dumps(meta, indent=2) + "\n", encoding="utf-8"
    )
    actual_by_target[target] = read_game_members(meta, target)
    print(f"{target}: inspected {len(actual_by_target[target])} game classes")

# --- Emit one Tiny v1 mapping per 26.x release ------------------------------
for target in TARGETS:
    # Loom reads Tiny v1 namespaces 0 and 1 as intermediary -> named. For 26.x
    # the intermediary names are the official game names, so namespace 0 is the
    # real 26.x name and namespace 1 is the Yarn name the source compiles with.
    out: list[str] = ["v1\tofficial\tnamed"]
    actual_members = actual_by_target[target]
    used_classes: set[str] = set()
    used_fields: set[tuple[str, str, str]] = set()
    used_methods: set[tuple[str, str, str, str]] = set()

    for official_class, named_class in sorted(yarn_class_by_official.items()):
        owner = source_class(official_class)
        named = internal(named_class)
        if named in used_classes:
            continue
        used_classes.add(named)
        out.append(f"CLASS\t{owner}\t{named}")

    for (official_class, name), named_field in sorted(yarn_field_by_official.items()):
        if official_class not in yarn_class_by_official:
            continue
        desc = source_descriptor(official_members[official_class]["fields"][name][1])
        owner = source_class(official_class)
        real_fields, _ = actual_members.get(owner, (set(), set()))
        # If a real 26.x field already has the target name, leave it alone;
        # remapping another field onto it would create a duplicate named field.
        if named_field != name and named_field in real_fields:
            continue
        key = (owner, desc, named_field)
        if key in used_fields:
            continue
        used_fields.add(key)
        out.append(f"FIELD\t{owner}\t{desc}\t{name}\t{named_field}")

    for (official_class, name, official_desc), named_method in sorted(yarn_method_by_official.items()):
        if official_class not in yarn_class_by_official:
            continue
        owner = source_class(official_class)
        desc = source_descriptor(official_desc)
        _, real_methods = actual_members.get(owner, (set(), set()))
        # Overloads are fine, but never rename onto a method that really exists.
        if named_method != name and any(method_name == named_method for method_name, _ in real_methods):
            continue
        key = (owner, name, desc, named_method)
        if key in used_methods:
            continue
        used_methods.add(key)
        out.append(f"METHOD\t{owner}\t{desc}\t{name}\t{named_method}")

    version_dir = ROOT / "versions" / target
    version_dir.mkdir(parents=True, exist_ok=True)
    mapping = version_dir / "mappings.tiny"
    mapping.write_text("\n".join(out) + "\n", encoding="utf-8")
    jar = version_dir / "mappings.jar"
    with zipfile.ZipFile(jar, "w", zipfile.ZIP_DEFLATED) as archive:
        archive.writestr("mappings/mappings.tiny", mapping.read_text(encoding="utf-8"))
    print(f"{target}: {len(out) - 1} entries -> {jar.relative_to(ROOT)}")
