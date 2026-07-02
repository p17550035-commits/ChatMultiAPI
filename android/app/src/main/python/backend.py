# backend.py

import datetime
from providers import (
    detect_provider_from_key,
    send_message_to_provider
)
from storage import load_config

def handle_message(text: str) -> str:
    """
    Unified backend entry point.
    Routes messages to the correct provider using your existing providers.py.
    """

    now = datetime.datetime.now().strftime("%m_%d_%Y %H:%M:%S")

    # File handling
    if text.startswith("[FILE]"):
        return f"[{now}] Got a file: {text}"

    # Image shortcut
    if "image" in text.lower():
        return "[API_IMAGE] https://picsum.photos/512"

    # Load provider config
    cfg = load_config()
    provider = cfg.get("selected", "local")

    # Local fallback
    if provider == "local":
        return f"[{now}] You said: {text}"

    # Auto-detect provider from key if needed
    if provider == "auto":
        key = cfg["providers"].get("auto_key", "")
        detected = detect_provider_from_key(key)
        if detected:
            provider = detected
        else:
            return f"[{now}] Could not auto-detect provider."

    # Call provider using your OG providers.py
    try:
        reply = send_message_to_provider(provider, text)
        return reply
    except Exception as e:
        return f"[{now}] Provider error: {e}"
