package com.example.chatmultiapi

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

/**
 * BLOCK: SecurityActivity
 * PURPOSE: Seed phrase generation, encrypted config backup/restore, and danger zone actions.
 * SAFE: comment only
 */
class SecurityActivity : AppCompatActivity() {

    /** BLOCK: SharedPreferences
     *  PURPOSE: Store critical UI mode (light/dark) + seed phrase persistence
     *  SAFE: comment only
     */
    private lateinit var prefs: SharedPreferences

    /** BLOCK: Seed Phrase
     *  PURPOSE: Permanent root key for encryption/decryption
     */
    private lateinit var seedPhrase: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_security)

        prefs = getSharedPreferences("app_settings", Context.MODE_PRIVATE)

        // BLOCK: Apply UI Mode
        applyUiMode()

        // ------------------------------------------------------------
        // BLOCK: Initialize Seed Phrase (permanent + safe)
        // ------------------------------------------------------------
        val securityPrefs = getSharedPreferences("security_prefs", MODE_PRIVATE)

        seedPhrase = if (securityPrefs.getBoolean("seed_phrase_saved", false)) {
            // Load previously saved seed phrase
            securityPrefs.getString("seed_phrase", "") ?: SecurityManager.generateSeedPhrase()
        } else {
            // First-time setup → generate new seed phrase
            val newSeed = SecurityManager.generateSeedPhrase()
            securityPrefs.edit()
                .putBoolean("seed_phrase_saved", true)
                .putString("seed_phrase", newSeed)
                .apply()
            newSeed
        }

        // BLOCK: Seed Phrase Section
        val seedPhraseText = findViewById<TextView>(R.id.seedPhraseText)
        val generateSeedBtn = findViewById<Button>(R.id.generateSeedBtn)

        // Display current seed phrase
        seedPhraseText.text = seedPhrase

        generateSeedBtn.setOnClickListener {
            val newSeed = SecurityManager.generateSeedPhrase()
            seedPhrase = newSeed

            // Persist new seed
            securityPrefs.edit()
                .putBoolean("seed_phrase_saved", true)
                .putString("seed_phrase", newSeed)
                .apply()

            seedPhraseText.text = newSeed
        }

        // BLOCK: Backup Section
        val backupBtn = findViewById<Button>(R.id.backupBtn)
        backupBtn.setOnClickListener {
            SecurityManager.backupEncryptedConfig(this)
        }

        // BLOCK: Restore Section
        val restoreBtn = findViewById<Button>(R.id.restoreBtn)
        restoreBtn.setOnClickListener {
            SecurityManager.restoreEncryptedConfig(this, seedPhrase)
        }

        // BLOCK: Danger Zone
        val wipeBtn = findViewById<Button>(R.id.wipeEncryptionBtn)
        wipeBtn.setOnClickListener {
            SecurityManager.resetEncryption(this)

            // Reset seed phrase after wipe
            val newSeed = SecurityManager.generateSeedPhrase()
            seedPhrase = newSeed

            securityPrefs.edit()
                .putBoolean("seed_phrase_saved", true)
                .putString("seed_phrase", newSeed)
                .apply()

            seedPhraseText.text = newSeed
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
   METADATA FOOTER — SecurityActivity.kt
   version: 1.0.0
   local_timestamp: 07/06/2026 10:54 AM EDT
   utc_timestamp: 2026-07-06T14:54:00Z

   ML TAGS
   - ml_tags: ["security_ui", "seed_phrase", "encrypted_backup"]

   BLUEPRINT SECTION
   - section: "6.0 — SecurityActivity.kt"

   SECTION PURPOSE
   - Handles seed phrase generation, encrypted config backup/restore, and wipe actions.
   - Controls critical UI mode (light/dark) for the security panel.
   - Interfaces directly with activity_security.xml.

   DEPENDENCIES
   - uses: [
       "SecurityManager.kt",
       "activity_security.xml",
       "app_settings SharedPreferences"
     ]

   NOTES
   - Fully regenerated to restore conformity.
   - Non-executable metadata footer.
   - Safe for copy/paste.
   ========================================================================
   END OF FILE :: CHATMULTIAPI
*/
