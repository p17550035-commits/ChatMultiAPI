# Clean backend entry point for Chaquopy-based Android app.
# No Kivy. No GUI. Android UI is handled in Kotlin.

from backend import handle_message

def main():
    # Nothing to initialize here.
    # Android calls backend.handle_message() directly from MainActivity.
    return "Python backend ready."

if __name__ == "__main__":
    print(main())
