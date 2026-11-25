"""Post a build update to a Discord webhook after a successful jar build.

The webhook URL is read from the DISCORD_WEBHOOK_URL environment variable or
from config/discord_webhook.enc, which is encrypted with Windows DPAPI and can
only be decrypted by the current Windows user. Plaintext config/discord_webhook.txt
is still accepted for older setups, but new installs should use the encrypted file.
Missing configuration is not an error: builds still succeed and skip the post.
"""

from __future__ import annotations

import argparse
import datetime as dt
import hashlib
import json
import os
import subprocess
import urllib.request
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
WEBHOOK_FILE = ROOT / "config" / "discord_webhook.txt"
ENCRYPTED_WEBHOOK_FILE = ROOT / "config" / "discord_webhook.enc"
DECRYPT_SCRIPT = ROOT / "scripts" / "decrypt_webhook.ps1"
STATE_FILE = ROOT / "config" / "discord_build_state.json"
SITE_BASE = "https://brantleypatterson574-hash.github.io/Flux-2.0"


def git(*args: str) -> str:
    result = subprocess.run(
        ["git", *args], cwd=ROOT, capture_output=True, text=True, check=False
    )
    return result.stdout.strip() if result.returncode == 0 else ""


def read_encrypted_webhook() -> str | None:
    if not ENCRYPTED_WEBHOOK_FILE.is_file() or not DECRYPT_SCRIPT.is_file():
        return None
    result = subprocess.run(
        [
            "powershell", "-NoProfile", "-ExecutionPolicy", "Bypass",
            "-File", str(DECRYPT_SCRIPT), "-Path", str(ENCRYPTED_WEBHOOK_FILE),
        ],
        capture_output=True,
        text=True,
        check=False,
    )
    if result.returncode != 0:
        print(f"DISCORD_WEBHOOK_DECRYPT_FAILED {result.stderr.strip()[:200]}")
        return None
    lines = result.stdout.strip().splitlines()
    return lines[0].strip() if lines and lines[0].strip() else None


def read_webhook() -> str | None:
    env = os.environ.get("DISCORD_WEBHOOK_URL", "").strip()
    if env:
        return env
    encrypted = read_encrypted_webhook()
    if encrypted:
        return encrypted
    if WEBHOOK_FILE.is_file():
        value = WEBHOOK_FILE.read_text(encoding="utf-8").strip().splitlines()
        if value and value[0].strip():
            return value[0].strip()
    return None


def sha256(path: Path) -> str:
    digest = hashlib.sha256()
    with path.open("rb") as handle:
        for chunk in iter(lambda: handle.read(1024 * 1024), b""):
            digest.update(chunk)
    return digest.hexdigest().upper()


def human_size(size: int) -> str:
    return f"{size / (1024 * 1024):.1f} MB"


def download_url(version: str) -> str:
    if version == "1.21.11":
        return f"{SITE_BASE}/downloads/flux-2.0.jar"
    return f"{SITE_BASE}/downloads/{version}/flux-2.0-{version}.jar"


def whats_new(last_commit: str, current_commit: str, label: str = "Rebuilt jar") -> str:
    if not current_commit:
        return f"- {label}; no git history available."

    if last_commit and last_commit != current_commit:
        log = git("log", "--pretty=format:- %s (%h)", f"{last_commit}..{current_commit}")
        if log:
            lines = [line for line in log.splitlines() if line.strip()][:8]
            return "\n".join(lines)

    log = git("log", "--pretty=format:- %s (%h)", "-5", current_commit)
    if not log:
        return f"- {label}; no new commits since the last announcement."

    lines = [line for line in log.splitlines() if line.strip()][:5]
    if last_commit == current_commit:
        return "\n".join([f"- {label}; no new commits since the last update.", *lines])
    return "\n".join(lines)


def changed_site_files(last_commit: str, current_commit: str) -> str:
    if last_commit and current_commit:
        output = git("diff", "--name-only", f"{last_commit}..{current_commit}", "--", "website")
    elif current_commit:
        output = git("show", "--pretty=format:", "--name-only", current_commit, "--", "website")
    else:
        output = ""
    files = [line for line in output.splitlines() if line.strip()][:12]
    return "\n".join(f"- `{name}`" for name in files) or "- Website files updated."


def truncate(value: str, limit: int = 1000) -> str:
    return value if len(value) <= limit else value[: limit - 3] + "..."


def main() -> int:
    parser = argparse.ArgumentParser()
    parser.add_argument("--version", default="1.21.11")
    parser.add_argument("--jar")
    parser.add_argument("--manifest")
    parser.add_argument("--site-update", action="store_true")
    args = parser.parse_args()

    webhook = read_webhook()
    if not webhook:
        print("DISCORD_UPDATE_SKIPPED no webhook configured")
        return 0

    state = {}
    if STATE_FILE.is_file():
        try:
            state = json.loads(STATE_FILE.read_text(encoding="utf-8"))
        except json.JSONDecodeError:
            state = {}

    current_commit = git("rev-parse", "HEAD")
    last_commit = state.get("last_commit", "")
    checksum = "site"
    now = dt.datetime.now(dt.timezone.utc).isoformat()

    if args.site_update:
        changes = whats_new(last_commit, current_commit, "Site refresh")
        files = changed_site_files(last_commit, current_commit)
        embed = {
            "title": "Onyx Client website updated",
            "description": "The download page and previews were refreshed.",
            "color": 0x4ADE80,
            "fields": [
                {"name": "Site", "value": SITE_BASE, "inline": False},
                {"name": "What's new", "value": truncate(changes), "inline": False},
                {"name": "Changed files", "value": truncate(files), "inline": False},
            ],
            "footer": {"text": "Built by kitpvptot"},
            "timestamp": now,
        }
    else:
        if not args.jar:
            print("DISCORD_UPDATE_SKIPPED no jar supplied")
            return 0
        jar_path = Path(args.jar)
        if not jar_path.is_file():
            print(f"DISCORD_UPDATE_SKIPPED jar not found: {jar_path}")
            return 0

        size = jar_path.stat().st_size
        checksum = sha256(jar_path)
        changes = whats_new(last_commit, current_commit)
        embed = {
            "title": f"Onyx Client {args.version} build ready",
            "description": "A fresh jar was built and the website download was refreshed.",
            "color": 0x9F7AEA,
            "fields": [
                {"name": "Version", "value": args.version, "inline": True},
                {"name": "Size", "value": human_size(size), "inline": True},
                {"name": "SHA-256", "value": f"```{checksum}```", "inline": False},
                {"name": "What's new", "value": truncate(changes), "inline": False},
                {"name": "Download", "value": download_url(args.version), "inline": False},
            ],
            "footer": {"text": "Built by kitpvptot"},
            "timestamp": now,
        }

    payload = json.dumps({"embeds": [embed]}).encode("utf-8")
    request = urllib.request.Request(
        webhook,
        data=payload,
        headers={"Content-Type": "application/json", "User-Agent": "Onyx-Build-Bot/1.0"},
        method="POST",
    )

    try:
        with urllib.request.urlopen(request, timeout=20) as response:
            response.read()
    except Exception as error:  # Do not fail a successful build over a webhook.
        print(f"DISCORD_UPDATE_FAILED {type(error).__name__}: {error}")
        return 0

    STATE_FILE.parent.mkdir(parents=True, exist_ok=True)
    STATE_FILE.write_text(
        json.dumps(
            {
                "last_commit": current_commit,
                "last_hash": checksum,
                "version": args.version,
                "mode": "site" if args.site_update else "jar",
                "updated_at": now,
            },
            indent=2,
        )
        + "\n",
        encoding="utf-8",
    )
    print(f"DISCORD_UPDATE_POSTED {'site' if args.site_update else args.version} {checksum}")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
