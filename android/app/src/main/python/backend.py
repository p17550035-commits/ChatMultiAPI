# backend.py

import datetime

def handle_message(text: str) -> str:
    """
    Core backend entry point.
    text: user message or file URI marker like "[FILE] content://..."
    """

    now = datetime.datetime.now().strftime("%m_%d_%Y %H:%M:%S")

    # File handling placeholder
    if text.startswith("[FILE]"):
        return f"[{now}] Got a file: {text}"

    # If user asks for an image, return a sample API image
    if "image" in text.lower() or "pic" in text.lower():
        # Example remote image URL (works with Glide)
        url = "https://picsum.photos/512"
        return f"[API_IMAGE] {url}"

    # Normal text response
    return f"[{now}] You said: {text}"
