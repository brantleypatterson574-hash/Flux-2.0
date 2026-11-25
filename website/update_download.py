"""Publish a built Flux/Onyx jar to the website download registry.

Default builds keep the legacy root jar path. Version profiles are published
under downloads/<version>/ so the site can offer several Minecraft targets.
"""

from __future__ import annotations

import argparse
import hashlib
import json
import re
import shutil
import sys
from pathlib import Path

DEFAULT_VERSION = "1.21.11"


def main() -> int:
    parser = argparse.ArgumentParser(description="Publish a built jar to the website.")
    parser.add_argument("jar", type=Path)
    parser.add_argument("site", type=Path)
    parser.add_argument("version", nargs="?", default=DEFAULT_VERSION)
    args = parser.parse_args()

    jar = args.jar.resolve()
    site = args.site.resolve()
    version = args.version
    is_default = version == DEFAULT_VERSION

    if not jar.is_file():
        print(f"missing jar: {jar}", file=sys.stderr)
        return 2

    downloads = site / "downloads"
    downloads.mkdir(parents=True, exist_ok=True)

    if is_default:
        published = downloads / "flux-2.0.jar"
    else:
        version_dir = downloads / version
        version_dir.mkdir(parents=True, exist_ok=True)
        published = version_dir / f"flux-2.0-{version}.jar"

    shutil.copy2(jar, published)

    size = published.stat().st_size
    size_text = f"{size / (1024 * 1024):.1f} MB"
    digest = hashlib.sha256(published.read_bytes()).hexdigest().upper()
    version_download = (
        "downloads/flux-2.0.jar"
        if is_default
        else f"downloads/{version}/{published.name}"
    )

    version_manifest = {
        "name": published.name,
        "size": size,
        "sizeText": size_text,
        "sha256": digest,
        "minecraft": version,
        "loader": "Fabric",
        "download": version_download,
        "recommended": is_default,
    }
    manifest_path = (
        downloads / "manifest.json"
        if is_default
        else downloads / version / "manifest.json"
    )
    manifest_path.write_text(
        json.dumps(version_manifest, indent=2) + "\n", encoding="utf-8"
    )

    index = site / "index.html"

    if is_default:
        if index.is_file():
            text = index.read_text(encoding="utf-8")
            text, size_hits = re.subn(
                r'(<p class="file-note"(?: [^>]*)?>)Fabric [^<]*(</p>)',
                rf"\1Fabric {version} · {size_text} · client jar\2",
                text,
            )
            text, hash_hits = re.subn(
                r"(SHA-256: <code(?: [^>]*)?>)[0-9A-Fa-f]{64}(</code>)",
                rf"\g<1>{digest}\g<2>",
                text,
            )
            if size_hits != 1 or hash_hits != 1:
                print(
                    f"index.html template mismatch: size_hits={size_hits} hash_hits={hash_hits}",
                    file=sys.stderr,
                )
                return 1
            index.write_text(text, encoding="utf-8")

    registry_path = downloads / "versions.json"
    registry = {"default": DEFAULT_VERSION, "versions": {}}
    if registry_path.is_file():
        try:
            loaded = json.loads(registry_path.read_text(encoding="utf-8"))
            if isinstance(loaded.get("versions"), dict):
                registry.update(loaded)
        except json.JSONDecodeError:
            pass

    registry["default"] = DEFAULT_VERSION
    registry.setdefault("versions", {})[version] = {
        "label": f"{version} (Recommended)" if is_default else version,
        "minecraft": version,
        "loader": "Fabric",
        "download": "downloads/flux-2.0.jar" if is_default else version_download,
        "manifest": (
            "downloads/manifest.json" if is_default else f"downloads/{version}/manifest.json"
        ),
        "name": "flux-2.0.jar" if is_default else published.name,
        "size": size,
        "sizeText": size_text,
        "sha256": digest,
        "recommended": is_default,
    }

    ordered_versions = {}
    if DEFAULT_VERSION in registry["versions"]:
        ordered_versions[DEFAULT_VERSION] = registry["versions"][DEFAULT_VERSION]
    for name in sorted(
        (name for name in registry["versions"] if name != DEFAULT_VERSION),
        key=lambda value: tuple(int(part) if part.isdigit() else part for part in value.split(".")),
    ):
        ordered_versions[name] = registry["versions"][name]
    registry["versions"] = ordered_versions

    registry_path.write_text(json.dumps(registry, indent=2) + "\n", encoding="utf-8")

    print(f"WEBSITE_UPDATED {published} {size_text} {digest}")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
