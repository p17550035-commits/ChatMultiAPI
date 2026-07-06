"""
BLOCK: Storage Engine (Internal Config)
PURPOSE: Manage provider configuration inside Android's Chaquopy internal storage.
SAFE: comment only
"""

import json
import os

# BLOCK: Internal Storage Path
# PURPOSE: Points to app_config.json inside Android internal app directory
# SAFE: comment only
CONFIG_PATH = os.path.join(os.environ.get("HOME", ""), "files", "app_config.json")


def load_config():
    """
    BLOCK: Load Config
    PURPOSE: Load provider configuration from internal storage.
    SAFE: comment only
    """
    if not os.path.exists(CONFIG_PATH):
        return {
            "selected": "groq",
            "providers": {
                "groq": "",
                "openai": "",
                "anthropic": "",
                "nvidia": "",
                "lmstudio": "",
                "custom": {
                    "key": "",
                    "url": ""
                }
            }
        }

    try:
        with open(CONFIG_PATH, "r") as f:
            return json.load(f)
    except Exception:
        # BLOCK: Corruption Fallback
        # PURPOSE: Provide safe default if config is unreadable
        # SAFE: comment only
        return {
            "selected": "groq",
            "providers": {
                "groq": "",
                "openai": "",
                "anthropic": "",
                "nvidia": "",
                "lmstudio": "",
                "custom": {
                    "key": "",
                    "url": ""
                }
            }
        }


def save_config(cfg):
    """
    BLOCK: Save Config
    PURPOSE: Write provider configuration to internal storage.
    SAFE: comment only
    """
    try:
        with open(CONFIG_PATH, "w") as f:
            json.dump(cfg, f, indent=4)
    except Exception:
        pass


def set_provider_key(provider, key):
    """
    BLOCK: Set Provider Key
    PURPOSE: Update API key for a provider.
    SAFE: comment only
    """
    cfg = load_config()

    if provider == "custom":
        cfg["providers"]["custom"]["key"] = key
    else:
        cfg["providers"][provider] = key

    save_config(cfg)


def set_custom_url(url):
    """
    BLOCK: Set Custom URL
    PURPOSE: Update custom provider endpoint.
    SAFE: comment only
    """
    cfg = load_config()
    cfg["providers"]["custom"]["url"] = url
    save_config(cfg)


def set_selected_provider(provider):
    """
    BLOCK: Set Selected Provider
    PURPOSE: Update active provider.
    SAFE: comment only
    """
    cfg = load_config()
    cfg["selected"] = provider
    save_config(cfg)


def get_selected_provider():
    """
    BLOCK: Get Selected Provider
    PURPOSE: Return currently selected provider.
    SAFE: comment only
    """
    cfg = load_config()
    return cfg.get("selected", "groq")


def get_provider_key(provider):
    """
    BLOCK: Get Provider Key
    PURPOSE: Return API key for a provider.
    SAFE: comment only
    """
    cfg = load_config()

    if provider == "custom":
        return cfg["providers"]["custom"].get("key", "")

    return cfg["providers"].get(provider, "")


def get_custom_url():
    """
    BLOCK: Get Custom URL
    PURPOSE: Return custom provider endpoint.
    SAFE: comment only
    """
    cfg = load_config()
    return cfg["providers"]["custom"].get("url", "")


# ========================================================================
# METADATA FOOTER — storage.py
# version: 1.0.0
# local_timestamp: 07/06/2026 10:29 AM EDT
# utc_timestamp: 2026-07-06T14:29:00Z
#
# ML TAGS
# - ml_tags: ["python_storage", "config_manager", "provider_keys", "godmode_core"]
#
# BLUEPRINT SECTION
# - section: "7.3 — storage.py"
#
# SECTION PURPOSE
# - Manages provider configuration for GodMode.
# - Handles reading/writing app_config.json inside Android internal storage.
# - Supports custom provider endpoints and key updates.
#
# DEPENDENCIES
# - uses: [
#     "providers.py",
#     "backend.py",
#     "main.py",
#     "bootstrap.py",
#     "config_crypto.py"
# ]
#
# NOTES
# - Fully regenerated to restore conformity.
# - Non-executable metadata footer.
# - Safe for copy/paste.
# ========================================================================
# END OF FILE :: CHATMULTIAPI :: GODMODE
