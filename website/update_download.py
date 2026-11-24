from pathlib import Path
import hashlib
import json
import re
import shutil
import sys


def main() -> int:
    if len(sys.argv) != 3:
        print("usage: update_download.py <built-jar> <website-dir>", file=sys.stderr)
        return 2

    jar = Path(sys.argv[1]).resolve()
    site = Path(sys.argv[2]).resolve()
    if not jar.is_file():
        print(f"missing jar: {jar}", file=sys.stderr)
        return 2

    downloads = site / "downloads"
    downloads.mkdir(parents=True, exist_ok=True)
    published = downloads / jar.name
    shutil.copy2(jar, published)

    digest = hashlib.sha256(published.read_bytes()).hexdigest().upper()
    size_text = f"{published.stat().st_size / (1024 * 1024):.1f} MB"

    index = site / "index.html"
    if index.is_file():
        text = index.read_text(encoding="utf-8")
        text, size_hits = re.subn(
            r'(<p class="file-note">)Fabric [^<]*(</p>)',
            rf"\1Fabric 1.21.11 · {size_text} · client jar\2",
            text,
        )
        text, hash_hits = re.subn(
            r"(SHA-256: <code>)[0-9A-Fa-f]{64}(</code>)",
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

    manifest = {
        "name": published.name,
        "size": published.stat().st_size,
        "sizeText": size_text,
        "sha256": digest,
        "minecraft": "1.21.11",
        "loader": "Fabric",
    }
    (downloads / "manifest.json").write_text(
        json.dumps(manifest, indent=2) + "\n", encoding="utf-8"
    )

    print(f"WEBSITE_UPDATED {published} {size_text} {digest}")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
