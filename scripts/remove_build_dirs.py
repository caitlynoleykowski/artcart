#!/usr/bin/env python3
"""
remove_build_dirs.py

Recursively finds and deletes all subdirectories named exactly "build".

USAGE:
  python3 remove_build_dirs.py --dry-run
  python3 remove_build_dirs.py
  python3 remove_build_dirs.py --root /path/to/project
"""

import argparse
import shutil
from pathlib import Path

# directories we never want to traverse into
SKIP_DIRS = {
    ".git",
    ".idea",
    ".vscode",
    "node_modules",
}


def should_skip(path: Path) -> bool:
    return any(part in SKIP_DIRS for part in path.parts)


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--root", default=".", help="Project root (default: current directory)")
    parser.add_argument("--dry-run", action="store_true", help="Preview deletions without removing")
    args = parser.parse_args()

    root = Path(args.root).resolve()

    if not root.exists() or not root.is_dir():
        raise SystemExit(f"Invalid root directory: {root}")

    print(f"Scanning: {root}")
    print(f"Dry run : {args.dry_run}")
    print("-" * 50)

    found = []

    for path in root.rglob("*"):
        if path.is_dir() and path.name == "build":
            if should_skip(path):
                continue
            found.append(path)

    if not found:
        print("No build directories found.")
        return

    for d in found:
        print(f"[DELETE] {d}")
        if not args.dry_run:
            shutil.rmtree(d)

    print("-" * 50)
    print(
        f"Total directories removed: {len(found)}"
        if not args.dry_run
        else f"Total directories found: {len(found)}"
    )


if __name__ == "__main__":
    main()

