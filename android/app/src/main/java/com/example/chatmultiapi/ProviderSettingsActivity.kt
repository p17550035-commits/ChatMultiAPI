package com.example.chatmultiapi

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject
import java.io.File

/**
 * BLOCK: ProviderSettingsActivity
 * PURPOSE: Manage provider selection, API keys, custom URLs, and dark mode unlock.
 * SAFE: comment only
 */
class ProviderSettingsActivity : AppCompatActivity() {

    /** BLOCK: UI Bindings
     *  PURPOSE: Connect Kotlin to XML components.
     *  SAFE: comment only
     */
    private lateinit var providerSpinner: Spinner
    private lateinit var keyInput: EditText
    private lateinit var customUrlInput: EditText
    private lateinit var btnSave: Button
    private lateinit var btnLoad: Button

    /** BLOCK: Dark Mode UI
     *  PURPOSE: Toggle + disabled message.
     *  SAFE: comment only
     */
    private lateinit var darkModeToggle: Switch
    private lateinit var darkModeToggleDisabledMsg: TextView

    private val configName = "app_config.json"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_provider_settings)

        // BLOCK: Bind XML IDs
        providerSpinner = findViewById(R.id.providerSpinner)
        keyInput = findViewById(R.id.keyInput)
        customUrlInput = findViewById(R.id.customUrlInput)
        btnSave = findViewById(R.id.btnSave)
        btnLoad = findViewById(R.id.btnLoad)
        darkModeToggle = findViewById(R.id.darkModeToggle)
        darkModeToggleDisabledMsg = findViewById(R.id.darkModeToggleDisabledMsg)

        // BLOCK: Provider list
        val providers = listOf("local", "groq", "openai", "anthropic", "nvidia", "lmstudio", "custom")
        providerSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, providers)

        // BLOCK: Button listeners
        btnSave.setOnClickListener { saveConfig() }
        btnLoad.setOnClickListener { loadConfig() }

        // BLOCK: Dark Mode setup
        initializeDarkModeToggle()
    }

    /**
     * BLOCK: initializeDarkModeToggle()
     * PURPOSE: Lock/unlock toggle based on setupComplete.
     * SAFE: comment only
     */
    private fun initializeDarkModeToggle() {
        val prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val setupComplete = prefs.getBoolean("setupComplete", false)

        if (!setupComplete) {
            darkModeToggle.isEnabled = false
            darkModeToggle.visibility = View.GONE
            darkModeToggleDisabledMsg.visibility = View.VISIBLE
        } else {
            darkModeToggle.isEnabled = true
            darkModeToggle.visibility = View.VISIBLE
            darkModeToggleDisabledMsg.visibility = View.GONE
        }

        // BLOCK: Toggle listener
        darkModeToggle.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("darkModeEnabled", isChecked).apply()
            Toast.makeText(this, "Dark Mode: $isChecked", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * BLOCK: onApiResponseSuccess()
     * PURPOSE: Unlock Dark Mode after first successful API response.
     * SAFE: comment only
     */
    fun onApiResponseSuccess() {
        val prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val setupComplete = prefs.getBoolean("setupComplete", false)

        if (!setupComplete) {
            prefs.edit().putBoolean("setupComplete", true).apply()

            darkModeToggle.isEnabled = true
            darkModeToggle.visibility = View.VISIBLE
            darkModeToggleDisabledMsg.visibility = View.GONE
        }
    }

    /**
     * BLOCK: getConfigFile()
     * PURPOSE: Centralized config file reference.
     * SAFE: comment only
     */
    private fun getConfigFile(): File {
        return File(filesDir, configName)
    }

    /**
     * BLOCK: saveConfig()
     * PURPOSE: Write provider config to storage.
     * SAFE: comment only
     */
    private fun saveConfig() {
        val provider = providerSpinner.selectedItem.toString()
        val key = keyInput.text.toString().trim()
        val url = customUrlInput.text.toString().trim()

        val json = JSONObject().apply {
            put("selected", provider)
            put("providers", JSONObject().apply {
                put("groq", "")
                put("openai", "")
                put("anthropic", "")
                put("nvidia", "")
                put("lmstudio", "")
                put("custom", JSONObject().apply {
                    put("key", "")
                    put("url", "")
                })
            })
        }

        val providersObj = json.getJSONObject("providers")
        if (provider == "custom") {
            providersObj.getJSONObject("custom").put("key", key)
            providersObj.getJSONObject("custom").put("url", url)
        } else {
            providersObj.put(provider, key)
        }

        getConfigFile().writeText(json.toString(4))
        Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show()
    }

    /**
     * BLOCK: loadConfig()
     * PURPOSE: Load provider config from storage.
     * SAFE: comment only
     */
    private fun loadConfig() {
        val file = getConfigFile()
        if (!file.exists()) {
            Toast.makeText(this, "No config found", Toast.LENGTH_SHORT).show()
            return
        }

        val json = JSONObject(file.readText())
        val provider = json.getString("selected")
        val providersObj = json.getJSONObject("providers")

        providerSpinner.setSelection(
            (providerSpinner.adapter as ArrayAdapter<String>).getPosition(provider)
        )

        if (provider == "custom") {
            val custom = providersObj.getJSONObject("custom")
            keyInput.setText(custom.getString("key"))
            customUrlInput.setText(custom.getString("url"))
        } else {
            keyInput.setText(providersObj.getString(provider))
            customUrlInput.setText("")
        }

        Toast.makeText(this, "Loaded!", Toast.LENGTH_SHORT).show()
    }
}

/* ========================================================================
   METADATA FOOTER — ProviderSettingsActivity.kt
   version: 1.0.0
   local_timestamp: 07/06/2026 10:53 AM EDT
   utc_timestamp: 2026-07-06T14:53:00Z

   ML TAGS
   - ml_tags: ["provider_settings", "config_ui", "dark_mode_unlock", "godmode_core"]

   BLUEPRINT SECTION
   - section: "5.0 — ProviderSettingsActivity.kt"

   SECTION PURPOSE
   - Handles provider selection, API key storage, custom URL input, and dark mode unlock.
   - Interfaces directly with activity_provider_settings.xml.
   - Writes/reads app_config.json for provider routing.

   DEPENDENCIES
   - uses: [
       "activity_provider_settings.xml",
       "ProviderRouter.kt",
       "ApiMaster.kt",
       "app_config.json"
     ]

   NOTES
   - Fully regenerated to restore conformity.
   - Non-executable metadata footer.
   - Safe for copy/paste.
   ========================================================================
   END OF FILE :: CHATMULTIAPI :: GODMODE
*/
