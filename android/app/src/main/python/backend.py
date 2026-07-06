"""
BLOCK: Backend Router (Python Core)
PURPOSE: Unified backend entry point for GodMode → routes messages to providers.
SAFE: comment only
"""

import datetime
import os

# BLOCK: Provider Logic Imports
# PURPOSE: Detect provider + send message to correct backend
# SAFE: comment only
from providers import (
    detect_provider_from_key,
    send_message_to_provider
)

# BLOCK: Storage Imports
# PURPOSE: Load config + selected provider
# SAFE: comment only
from storage import load_config, get_selected_provider

# BLOCK: Crypto Import
# PURPOSE: Decrypt encrypted config.enc using seed phrase
# SAFE: comment only
from config_crypto import decrypt_config


def load_encrypted_config(seed_phrase: str):
    """
    BLOCK: Encrypted Config Loader
    PURPOSE: Load and decrypt config.enc using seed phrase → returns dict or None
    SAFE: comment only
    """
    files_dir = os.path.join(os.getcwd(), "files")
    cfg = decrypt_config(files_dir, seed_phrase)
    return cfg


def handle_message(text: str) -> str:
    """
    BLOCK: Unified Backend Entry Point
    PURPOSE: Routes messages to correct provider using providers.py
    SAFE: comment only
    """

    now = datetime.datetime.now().strftime("%m_%d_%Y %H:%M:%S")

    # BLOCK: File Handling
    # PURPOSE: Detect file messages
    # SAFE: comment only
    if text.startswith("[FILE]"):
        return f"[{now}] Got a file: {text}"

    # BLOCK: Image Shortcut
    # PURPOSE: Quick image response for testing
    # SAFE: comment only
    if "image" in text.lower():
        return "[API_IMAGE] https://picsum.photos/512"

    # BLOCK: Load Provider Config (unencrypted fallback)
    # PURPOSE: Load config + determine provider
    # SAFE: comment only
    cfg = load_config()
    provider = cfg.get("selected", "local")

    # BLOCK: Local Provider Fallback
    # PURPOSE: Local echo mode
    # SAFE: comment only
    if provider == "local":
        return f"[{now}] You said: {text}"

    # BLOCK: Auto-Detect Provider
    # PURPOSE: Detect provider from key if provider == auto
    # SAFE: comment only
    if provider == "auto":
        auto_key = cfg["providers"].get("auto_key", "")
        detected = detect_provider_from_key(auto_key)
        if detected:
            provider = detected
        else:
            return f"[{now}] Could not auto-detect provider."

    # BLOCK: Provider Call
    # PURPOSE: Route message to provider using providers.py
    # SAFE: comment only
    try:
        reply = send_message_to_provider(provider, text)
        return reply
    except Exception as e:
        return f"[{now}] Provider error: {e}"


# ========================================================================
# METADATA FOOTER — backend.py
# version: 1.0.0
# local_timestamp: 07/06/2026 10:14 AM EDT
# utc_timestamp: 2026-07-06T14:14:00Z
#
# ML TAGS
# - ml_tags: ["python_backend", "provider_router", "config_crypto", "godmode_core"]
#
# BLUEPRINT SECTION
# - section: "7.1 — backend.py"
#
# SECTION PURPOSE
# - Core backend router for GodMode.
# - Handles provider routing, auto-detection, encrypted config loading,
#   and unified message handling for ChatMultiAPI.
#
# DEPENDENCIES
# - uses: [
#     "providers.py",
#     "storage.py",
#     "config_crypto.py",
#     "ProviderRouter.kt",
#     "ApiMaster.kt"
# ]
#
# NOTES
# - Fully regenerated to restore conformity.
# - Non-executable metadata footer.
# - Safe for copy/paste.
# ========================================================================
# END OF FILE :: CHATMULTIAPI :: GODMODE
