package com.example.chatmultiapi

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

/**
 * BLOCK: ApiKeysActivity
 * PURPOSE: Manage API keys + UI mode for security panel
 * SAFE: comment only
 */
class ApiKeysActivity : AppCompatActivity() {

    /** BLOCK: SharedPreferences
     *  PURPOSE: Store API keys + critical UI mode
     *  SAFE: comment only
     */
    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_api_keys)

        prefs = getSharedPreferences("app_settings", Context.MODE_PRIVATE)

        // BLOCK: Apply UI Mode
        // PURPOSE: Light/dark mode for security panel
        applyUiMode()

        // BLOCK: API Key Inputs
        // PURPOSE: Bind EditText fields
        val openAiInput = findViewById<EditText>(R.id.openAiKeyInput)
        val anthropicInput = findViewById<EditText>(R.id.anthropicKeyInput)
        val geminiInput = findViewById<EditText>(R.id.geminiKeyInput)
        val localInput = findViewById<EditText>(R.id.localKeyInput)

        // BLOCK: Load Saved Keys
        // PURPOSE: Populate fields from SharedPreferences
        openAiInput.setText(prefs.getString("openai_key", ""))
        anthropicInput.setText(prefs.getString("anthropic_key", ""))
        geminiInput.setText(prefs.getString("gemini_key", ""))
        localInput.setText(prefs.getString("local_key", ""))

        // BLOCK: Save Keys Button
        // PURPOSE: Persist all provider keys
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

        // BLOCK: Danger Zone
        // PURPOSE: Clear all API keys
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

    /**
     * BLOCK: applyUiMode()
     * PURPOSE: Apply light/dark mode for security panel
     * SAFE: comment only
     */
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

/* ========================================================================
   METADATA FOOTER — ApiKeysActivity.kt
   version: 1.0.0
   local_timestamp: 07/06/2026 10:40 AM EDT
   utc_timestamp: 2026-07-06T14:40:00Z

   ML TAGS
   - ml_tags: ["kotlin_activity", "api_keys", "security_panel", "godmode_core"]

   BLUEPRINT SECTION
   - section: "5.1 — ApiKeysActivity.kt"

   SECTION PURPOSE
   - Handles API key storage for all providers.
   - Controls critical UI mode (light/dark) for security panel.
   - Interfaces directly with activity_api_keys.xml.

   DEPENDENCIES
   - uses: [
       "activity_api_keys.xml",
       "storage.py",
       "ProviderRouter.kt",
       "ApiMaster.kt"
     ]

   NOTES
   - Fully regenerated to restore conformity.
   - Non-executable metadata footer.
   - Safe for copy/paste.
   ========================================================================
   END OF FILE :: CHATMULTIAPI :: GODMODE
*/
