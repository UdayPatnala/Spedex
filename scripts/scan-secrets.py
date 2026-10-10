#!/usr/bin/env python3
"""
SpeDex Secret Scanner & Git Push Security Auditor
Usage:
    python scripts/scan-secrets.py               # Scans all tracked git files
    python scripts/scan-secrets.py --staged      # Scans only git staged files (pre-commit)
    python scripts/scan-secrets.py --install-hook # Installs automated git pre-commit hook
"""

import os
import sys
import re
import subprocess
from pathlib import Path

# High-risk secret patterns to detect and block
SECRET_PATTERNS = [
    (r"AIza[0-9A-Za-z\-_]{35}", "Google / Firebase API Key"),
    (r"AKIA[0-9A-Z]{16}", "AWS Access Key ID"),
    (r"-----BEGIN (RSA|EC|DSA|OPENSSH|PGP) PRIVATE KEY-----", "Private Cryptographic Key"),
    (r"gh[pousr]_[A-Za-z0-9_]{36,}", "GitHub Token"),
    (r"xox[baprs]-[0-9A-Za-z\-]{10,}", "Slack Token"),
    (r"sk_live_[0-9a-zA-Z]{24,}", "Stripe Live Secret Key"),
    (r"eyJ[A-Za-z0-9\-_]{20,}\.eyJ[A-Za-z0-9\-_]{20,}\.[A-Za-z0-9\-_]{20,}", "Hardcoded JWT Token"),
    (r"(?:postgres|mysql|mongodb)://[^:]+:[^@]+@[^/]+", "Database URI with Plaintext Password"),
]

# Patterns or filenames that are intentionally safe or expected to contain mock/dummy values
ALLOWLIST_FILE_PATTERNS = [
    r"\.env\.example$",
    r"scan-secrets\.py$",
    r"application.*\.properties$",  # Spring properties using ${VAR:default}
    r"test.*",
    r".*Test\.java$",
    r".*\.test\.(ts|tsx|js|py)$",
    r"mock_backend\.py$",
    r"test-trips-e2e\.py$",
]

ALLOWLIST_VALUE_PATTERNS = [
    r"AIzaSyDummy.*",
    r"your_.*_here",
    r"replace_with_.*",
    r"mock-.*",
]

def is_allowlisted_file(file_path: str) -> bool:
    normalized = file_path.replace("\\", "/")
    return any(re.search(pat, normalized) for pat in ALLOWLIST_FILE_PATTERNS)

def scan_text(text: str, filename: str):
    findings = []
    lines = text.splitlines()
    for line_idx, line in enumerate(lines, 1):
        for pattern, secret_type in SECRET_PATTERNS:
            matches = re.finditer(pattern, line)
            for m in matches:
                matched_val = m.group(0)
                if any(re.search(allowed, matched_val) for allowed in ALLOWLIST_VALUE_PATTERNS):
                    continue
                findings.append({
                    "file": filename,
                    "line": line_idx,
                    "type": secret_type,
                    "preview": matched_val[:8] + "..." + matched_val[-4:] if len(matched_val) > 12 else "[REDACTED]"
                })
    return findings

def get_tracked_files():
    try:
        output = subprocess.check_output(["git", "ls-files"], text=True, stderr=subprocess.DEVNULL)
        return [f.strip() for f in output.splitlines() if f.strip()]
    except Exception:
        return []

def get_staged_files():
    try:
        output = subprocess.check_output(["git", "diff", "--name-only", "--cached"], text=True, stderr=subprocess.DEVNULL)
        return [f.strip() for f in output.splitlines() if f.strip()]
    except Exception:
        return []

def install_hook():
    hooks_dir = Path(".git") / "hooks"
    if not hooks_dir.exists():
        print("[-] .git/hooks directory not found.")
        return False
    pre_commit_path = hooks_dir / "pre-commit"
    hook_content = (
        "#!/bin/sh\n"
        "# SpeDex Pre-Commit Secret Scanner\n"
        "python scripts/scan-secrets.py --staged\n"
        "if [ $? -ne 0 ]; then\n"
        "    echo \"[!] Commit rejected: Detected sensitive secrets in staged files.\"\n"
        "    exit 1\n"
        "fi\n"
    )
    with open(pre_commit_path, "w", encoding="utf-8", newline="\n") as f:
        f.write(hook_content)
    try:
        os.chmod(pre_commit_path, 0o755)
    except Exception:
        pass
    print(f"[+] Installed pre-commit secret hook to {pre_commit_path}")
    return True

def main():
    if "--install-hook" in sys.argv:
        install_hook()
        sys.exit(0)

    is_staged = "--staged" in sys.argv
    files = get_staged_files() if is_staged else get_tracked_files()

    print(f"[*] SpeDex Secret Scanner: Auditing {len(files)} {'staged' if is_staged else 'tracked'} files...")

    all_findings = []
    for file_path in files:
        if is_allowlisted_file(file_path):
            continue
        p = Path(file_path)
        if not p.is_file():
            continue
        try:
            content = p.read_text(encoding="utf-8", errors="ignore")
            findings = scan_text(content, file_path)
            all_findings.extend(findings)
        except Exception as e:
            pass

    if all_findings:
        print("\n" + "="*70)
        print(" [!] SECURITY ALERT: DETECTED UNMASKED SECRETS IN CODEBASE")
        print("="*70)
        for f in all_findings:
            print(f"  - {f['file']}:{f['line']} -> {f['type']} ({f['preview']})")
        print("="*70)
        print(" [!] Please externalize all credentials to .env / environment variables.")
        print("     Ensure .env is git-ignored and document variables in .env.example.\n")
        sys.exit(1)
    else:
        print("[OK] Zero secrets detected. Codebase passes security audit.")
        sys.exit(0)

if __name__ == "__main__":
    main()
