# Entry point for Kivy/Python chat app

def main():
    # TODO: build GUI:
    # - dropdown (providers)
    # - API key field
    # - chat history
    # - send button
    # - attach file button
    # - save chat button
    pass

if __name__ == "__main__":
    main()
from kivy.app import App
from kivy.uix.boxlayout import BoxLayout
from kivy.uix.spinner import Spinner
from kivy.uix.textinput import TextInput
from kivy.uix.button import Button
from kivy.uix.scrollview import ScrollView
from kivy.uix.label import Label
from kivy.core.window import Window
from kivy.uix.separator import Separator

from storage import (
    load_config, save_config,
    set_provider_key, set_selected_provider,
    get_selected_provider
)

from providers import detect_provider_from_key, send_message_to_provider

import plyer
import time

class ChatUI(BoxLayout):
    def __init__(self, **kwargs):
        super().__init__(orientation="vertical", padding=10, spacing=8, **kwargs)

        cfg = load_config()

        # Top bar
        top = BoxLayout(size_hint=(1, 0.1), spacing=8)
        self.provider_spinner = Spinner(
            text=cfg["selected_provider"],
            values=["groq", "openai", "anthropic", "nvidia", "lmstudio", "custom"],
            size_hint=(0.5, 1)
        )
        self.provider_spinner.bind(text=self.on_provider_selected)
        top.add_widget(self.provider_spinner)

        self.key_input = TextInput(
            hint_text="API key",
            multiline=False,
            size_hint=(0.5, 1)
        )
        top.add_widget(self.key_input)
        self.add_widget(top)

        save_key_btn = Button(text="Save Key", size_hint=(1, 0.08))
        save_key_btn.bind(on_press=self.save_key)
        self.add_widget(save_key_btn)

        # Separator
        self.add_widget(Separator())

        # Chat history
        self.chat_history = Label(size_hint_y=None, text="", markup=True)
        self.chat_history.bind(texture_size=self.update_height)

        scroll = ScrollView(size_hint=(1, 0.55))
        scroll.add_widget(self.chat_history)
        self.add_widget(scroll)

        # Message input
        self.msg_input = TextInput(
            hint_text="Type message...",
            multiline=False,
            size_hint=(1, 0.1)
        )
        self.add_widget(self.msg_input)

        # Buttons row
        btn_row = BoxLayout(size_hint=(1, 0.1), spacing=8)

        send_btn = Button(text="Send")
        send_btn.bind(on_press=self.send_message)
        btn_row.add_widget(send_btn)

        attach_btn = Button(text="Attach File")
        attach_btn.bind(on_press=self.attach_file)
        btn_row.add_widget(attach_btn)

        save_chat_btn = Button(text="Save Chat")
        save_chat_btn.bind(on_press=self.save_chat)
        btn_row.add_widget(save_chat_btn)

        self.add_widget(btn_row)

    def update_height(self, *args):
        self.chat_history.height = self.chat_history.texture_size[1]

    def on_provider_selected(self, spinner, value):
        set_selected_provider(value)

    def save_key(self, *args):
        key = self.key_input.text.strip()
        provider = self.provider_spinner.text

        auto = detect_provider_from_key(key)
        if auto:
            self.provider_spinner.text = auto
            set_selected_provider(auto)

        set_provider_key(self.provider_spinner.text, key)

    def send_message(self, *args):
        msg = self.msg_input.text.strip()
        if not msg:
            return
        provider = self.provider_spinner.text

        try:
            reply = send_message_to_provider(provider, msg)
        except Exception as e:
            reply = f"[Error] {e}"

        self.chat_history.text += f"[You/{provider}] {msg}\n[AI] {reply}\n\n"
        self.msg_input.text = ""

    def attach_file(self, *args):
        path = plyer.filechooser.open_file()
        if path:
            self.chat_history.text += f"[System] Attached file: {path[0]}\n\n"

    def save_chat(self, *args):
        filename = f"chat_{int(time.time())}.txt"
        path = plyer.filechooser.save_file(title="Save Chat", filename=filename)

        if path:
            with open(path, "w") as f:
                f.write(self.chat_history.text)
            self.chat_history.text += f"[System] Chat saved to: {path}\n\n"

class ChatApp(App):
    def build(self):
        Window.size = (400, 700)
        self.title = "ChatMultiAPI"
        return ChatUI()

if __name__ == "__main__":
    ChatApp().run()
