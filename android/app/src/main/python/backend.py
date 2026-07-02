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

    # Expand this later into multi-agent logic
    return f"[{now}] You said: {text}"
