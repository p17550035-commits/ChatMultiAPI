import json
import os

# Internal storage path used by Chaquopy (Android internal app directory)
CONFIG_PATH = os.path.join(os.environ.get("HOME", ""), "files", "app_config.json")

def load_config():
    """
    Load provider configuration from internal storage.
    If the file does not exist, return a default structure.
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
        # Fallback if file is corrupted
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
    Save provider configuration to internal storage.
    """
    try:
        with open(CONFIG_PATH, "w") as f:
            json.dump(cfg, f, indent=4)
    except Exception:
        pass


def set_provider_key(provider, key):
    """
    Update the API key for a provider.
    """
    cfg = load_config()

    if provider == "custom":
        cfg["providers"]["custom"]["key"] = key
    else:
        cfg["providers"][provider] = key

    save_config(cfg)


def set_custom_url(url):
    """
    Update the custom provider URL.
    """
    cfg = load_config()
    cfg["providers"]["custom"]["url"] = url
    save_config(cfg)


def set_selected_provider(provider):
    """
    Update the selected provider.
    """
    cfg = load_config()
    cfg["selected"] = provider
    save_config(cfg)


def get_selected_provider():
    """
    Return the currently selected provider.
    """
    cfg = load_config()
    return cfg.get("selected", "groq")


def get_provider_key(provider):
    """
    Return the API key for a provider.
    """
    cfg = load_config()

    if provider == "custom":
        return cfg["providers"]["custom"].get("key", "")

    return cfg["providers"].get(provider, "")


def get_custom_url():
    """
    Return the custom provider URL.
    """
    cfg = load_config()
    return cfg["providers"]["custom"].get("url", "")
