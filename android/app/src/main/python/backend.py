import datetime
import os

from providers import (
    detect_provider_from_key,
    send_message_to_provider
)

from storage import load_config, get_selected_provider

# NEW: encrypted config loader
from config_crypto import decrypt_config


def load_encrypted_config(seed_phrase: str):
    """
    Load and decrypt config.enc using the seed phrase.
    Returns a Python dict or None.
    """
    files_dir = os.path.join(os.getcwd(), "files")
    cfg = decrypt_config(files_dir, seed_phrase)
    return cfg


def handle_message(text: str) -> str:
    """
    Unified backend entry point.
    Routes messages to the correct provider using providers.py.
    """

    now = datetime.datetime.now().strftime("%m_%d_%Y %H:%M:%S")

    # File handling
    if text.startswith("[FILE]"):
        return f"[{now}] Got a file: {text}"

    # Image shortcut
    if "image" in text.lower():
        return "[API_IMAGE] https://picsum.photos/512"

    # Load provider config (unencrypted fallback)
    cfg = load_config()
    provider = cfg.get("selected", "local")

    # Local fallback
    if provider == "local":
        return f"[{now}] You said: {text}"

    # Auto-detect provider from key if needed
    if provider == "auto":
        auto_key = cfg["providers"].get("auto_key", "")
        detected = detect_provider_from_key(auto_key)
        if detected:
            provider = detected
        else:
            return f"[{now}] Could not auto-detect provider."

    # Call provider using providers.py
    try:
        reply = send_message_to_provider(provider, text)
        return reply
    except Exception as e:
        return f"[{now}] Provider error: {e}"
