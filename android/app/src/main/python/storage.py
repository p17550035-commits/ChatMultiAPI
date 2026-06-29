import json
import os

APP_CONFIG = "app_config.json"

def load_config():
    if not os.path.exists(APP_CONFIG):
        return {"providers": {}, "selected_provider": "groq"}
    with open(APP_CONFIG, "r") as f:
        return json.load(f)

def save_config(cfg):
    with open(APP_CONFIG, "w") as f:
        json.dump(cfg, f, indent=4)

def set_provider_key(provider, key):
    cfg = load_config()
    if provider == "custom":
        cfg["providers"]["custom"]["key"] = key
    else:
        cfg["providers"][provider] = key
    save_config(cfg)

def set_selected_provider(provider):
    cfg = load_config()
    cfg["selected_provider"] = provider
    save_config(cfg)

def get_selected_provider():
    return load_config().get("selected_provider", "groq")

def get_provider_key(provider):
    cfg = load_config()
    if provider == "custom":
        return cfg["providers"]["custom"]["key"]
    return cfg["providers"].get(provider, "")
