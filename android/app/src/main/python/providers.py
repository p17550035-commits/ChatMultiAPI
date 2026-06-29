import requests
from storage import get_provider_key

def detect_provider_from_key(key):
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
    r = requests.post(url, headers=headers, json=payload, timeout=60)
    r.raise_for_status()
    return r.json()

def send_message_to_provider(provider, message, file_bytes=None):
    key = get_provider_key(provider)

    if provider == "groq":
        url = "https://api.groq.com/openai/v1/chat/completions"
        headers = {"Authorization": f"Bearer {key}", "Content-Type": "application/json"}
        payload = {
            "model": "llama-3.1-8b-instant",
            "messages": [{"role": "user", "content": message}]
        }
        data = _post_json(url, headers, payload)
        return data["choices"][0]["message"]["content"]

    if provider == "openai":
        url = "https://api.openai.com/v1/chat/completions"
        headers = {"Authorization": f"Bearer {key}", "Content-Type": "application/json"}
        payload = {
            "model": "gpt-4o-mini",
            "messages": [{"role": "user", "content": message}]
        }
        data = _post_json(url, headers, payload)
        return data["choices"][0]["message"]["content"]

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

    if provider == "nvidia":
        # Example NIM-style endpoint; adjust to your actual URL/model
        url = "https://integrate.api.nvidia.com/v1/chat/completions"
        headers = {"Authorization": f"Bearer {key}", "Content-Type": "application/json"}
        payload = {
            "model": "meta/llama3-8b-instruct",
            "messages": [{"role": "user", "content": message}]
        }
        data = _post_json(url, headers, payload)
        return data["choices"][0]["message"]["content"]

    if provider == "lmstudio":
        # LM Studio local server (adjust host/port if needed)
        url = "http://127.0.0.1:1234/v1/chat/completions"
        headers = {"Content-Type": "application/json"}
        payload = {
            "model": "local-model",
            "messages": [{"role": "user", "content": message}]
        }
        data = _post_json(url, headers, payload)
        return data["choices"][0]["message"]["content"]

    if provider == "custom":
        from storage import load_config
        cfg = load_config()
        custom = cfg["providers"]["custom"]
        url = custom["url"]
        headers = {"Authorization": f"Bearer {custom['key']}", "Content-Type": "application/json"}
        payload = {
            "messages": [{"role": "user", "content": message}]
        }
        data = _post_json(url, headers, payload)
        # assume OpenAI-style
        return data["choices"][0]["message"]["content"]

    return "Unknown provider or missing key."
