package com.example.chatmultiapi

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject
import java.io.File

class ProviderSettingsActivity : AppCompatActivity() {

    private lateinit var providerSpinner: Spinner
    private lateinit var keyInput: EditText
    private lateinit var customUrlInput: EditText
    private lateinit var btnSave: Button
    private lateinit var btnLoad: Button

    private val configName = "app_config.json"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_provider_settings)

        providerSpinner = findViewById(R.id.providerSpinner)
        keyInput = findViewById(R.id.keyInput)
        customUrlInput = findViewById(R.id.customUrlInput)
        btnSave = findViewById(R.id.btnSave)
        btnLoad = findViewById(R.id.btnLoad)

        val providers = listOf("local", "groq", "openai", "anthropic", "nvidia", "lmstudio", "custom")
        providerSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, providers)

        btnSave.setOnClickListener { saveConfig() }
        btnLoad.setOnClickListener { loadConfig() }
    }

    private fun getConfigFile(): File {
        return File(filesDir, configName)
    }

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

        // Insert key into correct provider slot
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
