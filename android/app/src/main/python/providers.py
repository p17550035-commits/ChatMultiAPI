"""
BLOCK: Provider Routing Engine
PURPOSE: Detect provider, send messages, and unify API call behavior for GodMode.
SAFE: comment only
"""

import requests

# BLOCK: Storage Imports
# PURPOSE: Retrieve provider keys + config
# SAFE: comment only
from storage import get_provider_key, load_config


def detect_provider_from_key(key):
    """
    BLOCK: Provider Auto-Detection
    PURPOSE: Determine provider based on API key prefix.
    SAFE: comment only
    """
    if key.startswith("gsk_"):
        return "groq"
    if key.startswith("sk-"):
        return "openai"
    if key.startswith("anthropic-"):
        return "anthropic"
    if key.startswith("nvcf_") or key.startswith("nvapi_"):
        return "nvidia"
    if key.startswith("lm_"):
        return "lmstudio"
    if key.startswith("custom_"):
        return "custom"
    return None


def _post_json(url, headers, payload):
    """
    BLOCK: JSON POST Helper
    PURPOSE: Unified POST request wrapper with error handling.
    SAFE: comment only
    """
    r = requests.post(url, headers=headers, json=payload, timeout=60)
    r.raise_for_status()
    return r.json()


def send_message_to_provider(provider, message, file_bytes=None):
    """
    BLOCK: Provider Router
    PURPOSE: Route message to correct provider using unified API patterns.
    SAFE: comment only
    """

    key = get_provider_key(provider)

    # BLOCK: GROQ
    # PURPOSE: Groq → OpenAI-compatible endpoint
    # SAFE: comment only
    if provider == "groq":
        url = "https://api.groq.com/openai/v1/chat/completions"
        headers = {
            "Authorization": f"Bearer {key}",
            "Content-Type": "application/json"
        }
        payload = {
            "model": "llama-3.1-8b-instant",
            "messages": [{"role": "user", "content": message}]
        }
        data = _post_json(url, headers, payload)
        return data["choices"][0]["message"]["content"]

    # BLOCK: OPENAI
    # PURPOSE: OpenAI → GPT-4o-mini
    # SAFE: comment only
    if provider == "openai":
        url = "https://api.openai.com/v1/chat/completions"
        headers = {
            "Authorization": f"Bearer {key}",
            "Content-Type": "application/json"
        }
        payload = {
            "model": "gpt-4o-mini",
            "messages": [{"role": "user", "content": message}]
        }
        data = _post_json(url, headers, payload)
        return data["choices"][0]["message"]["content"]

    # BLOCK: ANTHROPIC
    # PURPOSE: Claude 3 Haiku
    # SAFE: comment only
    if provider == "anthropic":
        url = "https://api.anthropic.com/v1/messages"
        headers = {
            "x-api-key": key,
            "anthropic-version": "2023-06-01",
            "Content-Type": "application/json"
        }
        payload = {
            "model": "claude-3-haiku-20240307",
            "max_tokens": 512,
            "messages": [{"role": "user", "content": message}]
        }
        data = _post_json(url, headers, payload)
        return data["content"][0]["text"]

    # BLOCK: NVIDIA NIM
    # PURPOSE: NVIDIA → Llama3 instruct
    # SAFE: comment only
    if provider == "nvidia":
        url = "https://integrate.api.nvidia.com/v1/chat/completions"
        headers = {
            "Authorization": f"Bearer {key}",
            "Content-Type": "application/json"
        }
        payload = {
            "model": "meta/llama3-8b-instruct",
            "messages": [{"role": "user", "content": message}]
        }
        data = _post_json(url, headers, payload)
        return data["choices"][0]["message"]["content"]

    # BLOCK: LM STUDIO
    # PURPOSE: Local model via LM Studio server
    # SAFE: comment only
    if provider == "lmstudio":
        url = "http://127.0.0.1:1234/v1/chat/completions"
        headers = {"Content-Type": "application/json"}
        payload = {
            "model": "local-model",
            "messages": [{"role": "user", "content": message}]
        }
        data = _post_json(url, headers, payload)
        return data["choices"][0]["message"]["content"]

    # BLOCK: CUSTOM PROVIDER
    # PURPOSE: User-defined HTTP endpoint
    # SAFE: comment only
    if provider == "custom":
        cfg = load_config()
        custom = cfg["providers"]["custom"]

        url = custom.get("url", "")
        custom_key = custom.get("key", "")

        if not url:
            return "Custom provider URL missing."

        headers = {
            "Authorization": f"Bearer {custom_key}",
            "Content-Type": "application/json"
        }
        payload = {
            "messages": [{"role": "user", "content": message}]
        }

        data = _post_json(url, headers, payload)
        return data["choices"][0]["message"]["content"]

    return "Unknown provider or missing key."


# ========================================================================
# METADATA FOOTER — providers.py
# version: 1.0.0
# local_timestamp: 07/06/2026 10:26 AM EDT
# utc_timestamp: 2026-07-06T14:26:00Z
#
# ML TAGS
# - ml_tags: ["provider_routing", "api_engine", "multi_provider", "godmode_core"]
#
# BLUEPRINT SECTION
# - section: "7.2 — providers.py"
#
# SECTION PURPOSE
# - Implements multi-provider routing for GodMode.
# - Handles Groq, OpenAI, Anthropic, NVIDIA, LM Studio, and custom providers.
# - Provides unified JSON POST helper and auto-detection logic.
#
# DEPENDENCIES
# - uses: [
#     "backend.py",
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
