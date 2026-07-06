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
                putString("openai_key", openAiInput.text
