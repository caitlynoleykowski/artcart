#!/usr/bin/env python3
"""
remove_google_dirs.py

Recursively finds and deletes all subdirectories named exactly "google".

USAGE:
  python3 remove_google_dirs.py --dry-run
  python3 remove_google_dirs.py
  python3 remove_google_dirs.py --root /path/to/project
"""

import argparse
import shutil
from pathlib import Path

SKIP_DIRS = {
    ".git", ".gradle", ".idea", ".vscode",
    "build", "out", "node_modules",
    ".cxx", ".externalNativeBuild",
}


def is_skipped(path: Path) -> bool:
    return any(part in SKIP_DIRS for part in path.parts)


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--root", default=".", help="Project root (default: current directory)")
    parser.add_argument("--dry-run", action="store_true", help="Show what would be deleted without deleting")
    args = parser.parse_args()

    root = Path(args.root).resolve()

    if not root.exists() or not root.is_dir():
        raise SystemExit(f"Invalid root directory: {root}")

    print(f"Scanning: {root}")
    print(f"Dry run : {args.dry_run}")
    print("-" * 50)

    found = []

    for path in root.rglob("*"):
        if path.is_dir() and path.name == "google":
            if is_skipped(path):
                continue
            found.append(path)

    if not found:
        print("No directories named 'google' found.")
        return

    for d in found:
        print(f"[DELETE] {d}")
        if not args.dry_run:
            shutil.rmtree(d)

    print("-" * 50)
    print(f"Total directories removed: {len(found)}" if not args.dry_run else f"Total directories found: {len(found)}")


if __name__ == "__main__":
    main()

