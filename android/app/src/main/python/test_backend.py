from backend import handle_message
from providers import send_message_to_provider
from storage import load_config

print("=== Backend Test ===")

print("Config:", load_config())
print("Handle:", handle_message("hello world"))
print("Groq:", send_message_to_provider("groq", "test message"))
