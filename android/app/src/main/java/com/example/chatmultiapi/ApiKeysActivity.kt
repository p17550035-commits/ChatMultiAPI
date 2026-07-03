package com.example.chatmultiapi

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class ApiKeysActivity : AppCompatActivity() {

    // SharedPreferences for UI mode + API keys
    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_api_keys)

        prefs = getSharedPreferences("app_settings", Context.MODE_PRIVATE)

        // Apply UI mode immediately
        applyUiMode()

        // API key fields
        val openAiInput = findViewById<EditText>(R.id.openAiKeyInput)
        val anthropicInput = findViewById<EditText>(R.id.anthropicKeyInput)
        val geminiInput = findViewById<EditText>(R.id.geminiKeyInput)
        val localInput = findViewById<EditText>(R.id.localKeyInput)

        // Load saved keys
        openAiInput.setText(prefs.getString("openai_key", ""))
        anthropicInput.setText(prefs.getString("anthropic_key", ""))
        geminiInput.setText(prefs.getString("gemini_key", ""))
        localInput.setText(prefs.getString("local_key", ""))

        // Save keys button
        val saveBtn = findViewById<Button>(R.id.saveApiKeysBtn)
        saveBtn.setOnClickListener {
            prefs.edit().apply {
                putString("openai_key", openAiInput.text.toString())
                putString("anthropic_key", anthropicInput.text.toString())
                putString("gemini_key", geminiInput.text.toString())
                putString("local_key", localInput.text.toString())
                apply()
            }
        }

        // Danger zone: clear all keys
        val clearBtn = findViewById<Button>(R.id.clearApiKeysBtn)
        clearBtn.setOnClickListener {
            prefs.edit().apply {
                remove("openai_key")
                remove("anthropic_key")
                remove("gemini_key")
                remove("local_key")
                apply()
            }

            openAiInput.setText("")
            anthropicInput.setText("")
            geminiInput.setText("")
            localInput.setText("")
        }
    }

    // Apply UI mode (light/dark)
    private fun applyUiMode() {
        val mode = prefs.getString("critical_ui_mode", "light")

        if (mode == "dark") {
            window.decorView.setBackgroundColor(
                ContextCompat.getColor(this, R.color.securityDarkBackground)
            )
        } else {
            window.decorView.setBackgroundColor(
                ContextCompat.getColor(this, R.color.securityLightBackground)
            )
        }
    }
}
