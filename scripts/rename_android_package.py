#!/usr/bin/env python3
"""
rename_android_package.py

Renames an Android app id / package across a multi-module project:
- Replaces old package string with new package string in common text files
- Moves source directories under src/**/java and src/**/kotlin (including test/androidTest)
- Updates build.gradle / build.gradle.kts occurrences (namespace/applicationId, etc.)
- Updates package declarations/imports and XML/manifest references that contain the old package

USAGE:
  python3 rename_android_package.py --old com.google.samples.apps --new com.coco0o0o.artcart
  python3 rename_android_package.py --old ... --new ... --dry-run
"""

from __future__ import annotations

import argparse
import os
import re
import shutil
from dataclasses import dataclass
from pathlib import Path
from typing import Iterable, List, Tuple


TEXT_EXTS = {
    ".kt", ".java", ".kts",
    ".gradle", ".xml",
    ".properties", ".pro",
    ".txt", ".md",
    ".json", ".yaml", ".yml",
    ".toml",
}

TEXT_FILENAMES = {
    "gradle.properties",
    "settings.gradle",
    "settings.gradle.kts",
    "build.gradle",
    "build.gradle.kts",
    "AndroidManifest.xml",
}

# directories to skip
SKIP_DIR_NAMES = {
    ".git", ".gradle", ".idea", ".vscode",
    "build", "out", "node_modules",
    ".cxx", ".externalNativeBuild",
}

# For moving directories we care about typical Android source roots:
SOURCE_ROOT_NAMES = {"java", "kotlin"}


@dataclass
class Change:
    kind: str
    src: Path
    dst: Path | None = None
    detail: str | None = None


def is_skipped_dir(path: Path) -> bool:
    return any(part in SKIP_DIR_NAMES for part in path.parts)


def looks_like_text_file(path: Path) -> bool:
    if path.name in TEXT_FILENAMES:
        return True
    return path.suffix.lower() in TEXT_EXTS


def iter_project_files(root: Path) -> Iterable[Path]:
    for p in root.rglob("*"):
        if p.is_dir():
            continue
        if is_skipped_dir(p):
            continue
        yield p


def read_text(path: Path) -> str | None:
    try:
        # Try utf-8 first; fall back to latin-1 to avoid crashing on odd encodings.
        return path.read_text(encoding="utf-8")
    except UnicodeDecodeError:
        try:
            return path.read_text(encoding="latin-1")
        except Exception:
            return None
    except Exception:
        return None


def write_text(path: Path, content: str) -> None:
    path.write_text(content, encoding="utf-8")


def replace_in_text(content: str, old_pkg: str, new_pkg: str) -> Tuple[str, int]:
    """
    Replace exact occurrences of old package with new package.
    We also replace common "oldPkg/" style occurrences in case it appears in XML tools:node or resources.
    """
    if old_pkg not in content:
        return content, 0
    new_content = content.replace(old_pkg, new_pkg)
    count = content.count(old_pkg)
    return new_content, count


def find_source_roots(root: Path) -> List[Path]:
    """
    Find directories like:
      <module>/src/main/java
      <module>/src/main/kotlin
      <module>/src/test/java
      <module>/src/androidTest/java
      <module>/src/*/kotlin
    """
    roots: List[Path] = []
    for src_dir in root.rglob("src"):
        if not src_dir.is_dir():
            continue
        if is_skipped_dir(src_dir):
            continue

        # Look one level deeper for source sets (main/test/androidTest/debug/release/etc)
        for source_set in src_dir.iterdir():
            if not source_set.is_dir():
                continue
            if is_skipped_dir(source_set):
                continue

            for lang in SOURCE_ROOT_NAMES:
                candidate = source_set / lang
                if candidate.is_dir():
                    roots.append(candidate)
    return roots


def move_package_dirs(source_root: Path, old_path: Path, new_path: Path) -> List[Change]:
    """
    If source_root/<old_path> exists, move it to source_root/<new_path>.
    Handles merging if destination exists.
    """
    changes: List[Change] = []
    src_pkg_dir = source_root / old_path
    if not src_pkg_dir.exists() or not src_pkg_dir.is_dir():
        return changes

    dst_pkg_dir = source_root / new_path
    changes.append(Change(kind="mkdir", src=dst_pkg_dir, dst=None, detail="ensure destination package dir"))
    # We'll move contents rather than the directory if dst exists (to avoid errors)
    changes.append(Change(kind="move_tree", src=src_pkg_dir, dst=dst_pkg_dir, detail="move package directory tree"))
    return changes


def apply_move_tree(src_dir: Path, dst_dir: Path, dry_run: bool) -> None:
    dst_dir.mkdir(parents=True, exist_ok=True)

    # Move children to dst (merging)
    for item in src_dir.iterdir():
        target = dst_dir / item.name
        if dry_run:
            continue
        if target.exists():
            # If both are dirs, recursively merge
            if item.is_dir() and target.is_dir():
                apply_move_tree(item, target, dry_run=False)
                # remove emptied dir
                try:
                    item.rmdir()
                except OSError:
                    pass
            else:
                # If file exists, overwrite by moving with rename (delete target first)
                if target.is_file():
                    target.unlink()
                shutil.move(str(item), str(target))
        else:
            shutil.move(str(item), str(target))

    # Remove now-empty src_dir and any empty parent dirs up to source_root
    if not dry_run:
        try:
            src_dir.rmdir()
        except OSError:
            pass


def cleanup_empty_parents(start_dir: Path, stop_dir: Path, dry_run: bool) -> None:
    """
    Remove empty dirs from start_dir up to stop_dir (exclusive).
    """
    cur = start_dir
    while True:
        if cur == stop_dir:
            return
        if not cur.exists() or not cur.is_dir():
            return
        try:
            # only remove if empty
            next(cur.iterdir())
            return
        except StopIteration:
            if dry_run:
                return
            try:
                cur.rmdir()
            except OSError:
                return
            cur = cur.parent


def do_replacements(root: Path, old_pkg: str, new_pkg: str, dry_run: bool) -> List[Change]:
    changes: List[Change] = []
    for p in iter_project_files(root):
        if not looks_like_text_file(p):
            continue
        txt = read_text(p)
        if txt is None:
            continue
        new_txt, count = replace_in_text(txt, old_pkg, new_pkg)
        if count > 0:
            changes.append(Change(kind="edit", src=p, dst=None, detail=f"replaced {count} occurrence(s)"))
            if not dry_run:
                write_text(p, new_txt)
    return changes


def do_moves(root: Path, old_pkg: str, new_pkg: str, dry_run: bool) -> List[Change]:
    changes: List[Change] = []
    old_path = Path(*old_pkg.split("."))
    new_path = Path(*new_pkg.split("."))

    for source_root in find_source_roots(root):
        # Generate planned moves
        planned = move_package_dirs(source_root, old_path, new_path)
        if not planned:
            continue
        changes.extend(planned)

        # Apply move
        src_pkg_dir = source_root / old_path
        dst_pkg_dir = source_root / new_path
        if not dry_run:
            apply_move_tree(src_pkg_dir, dst_pkg_dir, dry_run=False)
            # attempt to clean up old empty parents within source_root
            cleanup_empty_parents(start_dir=src_pkg_dir.parent, stop_dir=source_root, dry_run=False)

    return changes


def validate_package(pkg: str) -> None:
    # A simple sanity check; not perfect but catches obvious mistakes.
    # Android package segments: start with letter, then letters/digits/underscore.
    seg_re = re.compile(r"^[a-zA-Z][a-zA-Z0-9_]*$")
    segments = pkg.split(".")
    if len(segments) < 2:
        raise ValueError(f"Package '{pkg}' doesn't look valid (needs at least two segments).")
    bad = [s for s in segments if not seg_re.match(s)]
    if bad:
        raise ValueError(f"Package '{pkg}' has invalid segment(s): {bad}")


def main() -> None:
    ap = argparse.ArgumentParser()
    ap.add_argument("--old", required=True, help="Old applicationId/package (e.g. com.google.samples.apps)")
    ap.add_argument("--new", required=True, help="New applicationId/package (e.g. com.coco0o0o.artcart)")
    ap.add_argument("--root", default=".", help="Project root (default: .)")
    ap.add_argument("--dry-run", action="store_true", help="Print what would change, but do not modify files")
    args = ap.parse_args()

    root = Path(args.root).resolve()
    old_pkg = args.old.strip()
    new_pkg = args.new.strip()

    validate_package(old_pkg)
    validate_package(new_pkg)

    if not root.exists() or not root.is_dir():
        raise SystemExit(f"Root directory not found: {root}")

    # Recommendation: run from a clean git state
    print(f"Project root: {root}")
    print(f"Old package : {old_pkg}")
    print(f"New package : {new_pkg}")
    print(f"Dry run     : {args.dry_run}")
    print("-" * 60)

    # 1) Move directories first (so files end up in the new package path)
    move_changes = do_moves(root, old_pkg, new_pkg, args.dry_run)

    # 2) Replace text occurrences across project (gradle, manifests, code, xml, etc.)
    edit_changes = do_replacements(root, old_pkg, new_pkg, args.dry_run)

    # Summarize
    total_moves = sum(1 for c in move_changes if c.kind == "move_tree")
    total_edits = sum(1 for c in edit_changes if c.kind == "edit")

    print(f"Planned moves: {total_moves}")
    print(f"Edited files : {total_edits}")
    print("-" * 60)

    # Print details (keep it readable)
    for c in move_changes:
        if c.kind == "move_tree":
            print(f"[MOVE] {c.src}  ->  {c.dst}")
        elif c.kind == "mkdir":
            # only print mkdir if dry-run (otherwise it's noise)
            if args.dry_run:
                print(f"[MKDIR] {c.src}")

    for c in edit_changes:
        print(f"[EDIT] {c.src} ({c.detail})")

    if args.dry_run:
        print("\nDry-run complete. Re-run without --dry-run to apply.")
    else:
        print("\nDone. Next steps:")
        print("  - Sync Gradle")
        print("  - Rebuild project")
        print("  - Search for any remaining old package references (just in case)")
        print("  - If using Room/Hilt/Navigation SafeArgs etc., run a clean build if needed")


if __name__ == "__main__":
    main()

