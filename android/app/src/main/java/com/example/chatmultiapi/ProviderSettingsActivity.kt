package com.example.chatmultiapi

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject
import java.io.File

class ProviderSettingsActivity : AppCompatActivity() {

    // BLOCK: UI bindings
    // PURPOSE: Connect Kotlin to XML components.
    private lateinit var providerSpinner: Spinner
    private lateinit var keyInput: EditText
    private lateinit var customUrlInput: EditText
    private lateinit var btnSave: Button
    private lateinit var btnLoad: Button

    // BLOCK: Dark Mode UI
    // PURPOSE: Toggle + disabled message.
    private lateinit var darkModeToggle: Switch
    private lateinit var darkModeToggleDisabledMsg: TextView

    private val configName = "app_config.json"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_provider_settings)

        // BLOCK: Bind XML IDs
        // PURPOSE: Initialize UI references.
        providerSpinner = findViewById(R.id.providerSpinner)
        keyInput = findViewById(R.id.keyInput)
        customUrlInput = findViewById(R.id.customUrlInput)
        btnSave = findViewById(R.id.btnSave)
        btnLoad = findViewById(R.id.btnLoad)
        darkModeToggle = findViewById(R.id.darkModeToggle)
        darkModeToggleDisabledMsg = findViewById(R.id.darkModeToggleDisabledMsg)

        // BLOCK: Provider list
        // PURPOSE: Populate provider dropdown.
        val providers = listOf("local", "groq", "openai", "anthropic", "nvidia", "lmstudio", "custom")
        providerSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, providers)

        // BLOCK: Button listeners
        // PURPOSE: Save/load config.
        btnSave.setOnClickListener { saveConfig() }
        btnLoad.setOnClickListener { loadConfig() }

        // BLOCK: Dark Mode setup
        // PURPOSE: Lock/unlock toggle based on setupComplete.
        initializeDarkModeToggle()
    }

    private fun initializeDarkModeToggle() {
        // BLOCK: Read setupComplete
        // PURPOSE: Determine toggle availability.
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
        // PURPOSE: Save Dark Mode preference.
        darkModeToggle.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("darkModeEnabled", isChecked).apply()
            Toast.makeText(this, "Dark Mode: $isChecked", Toast.LENGTH_SHORT).show()
        }
    }

    fun onApiResponseSuccess() {
        // BLOCK: Unlock Dark Mode
        // PURPOSE: Called after first successful API response.
        val prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val setupComplete = prefs.getBoolean("setupComplete", false)

        if (!setupComplete) {
            prefs.edit().putBoolean("setupComplete", true).apply()

            darkModeToggle.isEnabled = true
            darkModeToggle.visibility = View.VISIBLE
            darkModeToggleDisabledMsg.visibility = View.GONE
        }
    }

    private fun getConfigFile(): File {
        // BLOCK: Config file path
        // PURPOSE: Centralized reference.
        return File(filesDir, configName)
    }

    private fun saveConfig() {
        // BLOCK: Read UI values
        // PURPOSE: Capture provider + key + URL.
        val provider = providerSpinner.selectedItem.toString()
        val key = keyInput.text.toString().trim()
        val url = customUrlInput.text.toString().trim()

        // BLOCK: Build JSON
        // PURPOSE: Store provider config.
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

        // BLOCK: Insert key
        // PURPOSE: Handle custom provider differently.
        val providersObj = json.getJSONObject("providers")
        if (provider == "custom") {
            providersObj.getJSONObject("custom").put("key", key)
            providersObj.getJSONObject("custom").put("url", url)
        } else {
            providersObj.put(provider, key)
        }

        // BLOCK: Write file
        // PURPOSE: Save config to storage.
        getConfigFile().writeText(json.toString(4))
        Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show()
    }

    private fun loadConfig() {
        // BLOCK: Check file
        // PURPOSE: Avoid crash if missing.
        val file = getConfigFile()
        if (!file.exists()) {
            Toast.makeText(this, "No config found", Toast.LENGTH_SHORT).show()
            return
        }

        // BLOCK: Parse JSON
        // PURPOSE: Load provider config.
        val json = JSONObject(file.readText())
        val provider = json.getString("selected")
        val providersObj = json.getJSONObject("providers")

        // BLOCK: Update spinner
        // PURPOSE: Reflect saved provider.
        providerSpinner.setSelection(
            (providerSpinner.adapter as ArrayAdapter<String>).getPosition(provider)
        )

        // BLOCK: Load fields
        // PURPOSE: Handle custom provider.
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

/* 
GODMODE FOOTER :: ProviderSettingsActivity.kt
Version: 1.0.6
Updated: 07-03-2026 @ 14:45 EDT
Blueprint: UI Layer → Settings Module → Provider Config
Dependencies: XML layout, app_config.json, ApiMaster.kt
END OF FILE
*/
