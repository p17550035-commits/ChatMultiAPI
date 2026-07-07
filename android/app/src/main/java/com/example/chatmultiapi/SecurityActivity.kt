package com.example.chatmultiapi

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

/**
 * BLOCK: SecurityActivity
 * PURPOSE: Seed phrase generation, encrypted config backup/restore, and danger zone actions.
 * SAFE: comment only
 */
class SecurityActivity : AppCompatActivity() {

    /** BLOCK: SharedPreferences */
    private lateinit var prefs: SharedPreferences

    /** BLOCK: Seed Phrase */
    private lateinit var seedPhrase: String

    /** BLOCK: Titanium Top Bar Buttons */
    private lateinit var btnChat: ImageButton
    private lateinit var btnProjects: ImageButton
    private lateinit var btnTerminal: ImageButton
    private lateinit var btnAPI: ImageButton
    private lateinit var btnSecurity: ImageButton
    private lateinit var btnSettings: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_security)

        // Titanium background
        window.decorView.setBackgroundResource(R.drawable.titanium_rainbow_background)

        prefs = getSharedPreferences("app_settings", Context.MODE_PRIVATE)

        // Apply UI mode
        applyUiMode()

        // Titanium top bar binding
        btnChat = findViewById(R.id.btnChat)
        btnProjects = findViewById(R.id.btnProjects)
        btnTerminal = findViewById(R.id.btnTerminal)
        btnAPI = findViewById(R.id.btnAPI)
        btnSecurity = findViewById(R.id.btnSecurity)
        btnSettings = findViewById(R.id.btnSettings)

        // Active tab highlight
        setActiveTab(btnSecurity)

        // Navigation wiring
        btnChat.setOnClickListener {
            setActiveTab(btnChat)
            startActivity(Intent(this, MainActivity::class.java))
        }

        btnProjects.setOnClickListener {
            setActiveTab(btnProjects)
            startActivity(Intent(this, ProjectsActivity::class.java))
        }

        btnTerminal.setOnClickListener {
            setActiveTab(btnTerminal)
            startActivity(Intent(this, TerminalActivity::class.java))
        }

        btnAPI.setOnClickListener {
            setActiveTab(btnAPI)
            startActivity(Intent(this, ApiActivity::class.java))
        }

        btnSecurity.setOnClickListener {
            setActiveTab(btnSecurity)
            // Already here
        }

        btnSettings.setOnClickListener {
            setActiveTab(btnSettings)
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        // ------------------------------------------------------------
        // Seed Phrase Initialization
        // ------------------------------------------------------------
        val securityPrefs = getSharedPreferences("security_prefs", MODE_PRIVATE)

        seedPhrase = if (securityPrefs.getBoolean("seed_phrase_saved", false)) {
            securityPrefs.getString("seed_phrase", "") ?: SecurityManager.generateSeedPhrase()
        } else {
            val newSeed = SecurityManager.generateSeedPhrase()
            securityPrefs.edit()
                .putBoolean("seed_phrase_saved", true)
                .putString("seed_phrase", newSeed)
                .apply()
            newSeed
        }

        // Seed phrase UI
        val seedPhraseText = findViewById<TextView>(R.id.seedPhraseText)
        val generateSeedBtn = findViewById<Button>(R.id.generateSeedBtn)

        seedPhraseText.text = seedPhrase

        generateSeedBtn.setOnClickListener {
            val newSeed = SecurityManager.generateSeedPhrase()
            seedPhrase = newSeed

            securityPrefs.edit()
                .putBoolean("seed_phrase_saved", true)
                .putString("seed_phrase", newSeed)
                .apply()

            seedPhraseText.text = newSeed
        }

        // Backup
        val backupBtn = findViewById<Button>(R.id.backupBtn)
        backupBtn.setOnClickListener {
            SecurityManager.backupEncryptedConfig(this)
        }

        // Restore
        val restoreBtn = findViewById<Button>(R.id.restoreBtn)
        restoreBtn.setOnClickListener {
            SecurityManager.restoreEncryptedConfig(this, seedPhrase)
        }

        // Danger Zone: Wipe
        val wipeBtn = findViewById<Button>(R.id.wipeEncryptionBtn)
        wipeBtn.setOnClickListener {
            SecurityManager.resetEncryption(this)

            val newSeed = SecurityManager.generateSeedPhrase()
            seedPhrase = newSeed

            securityPrefs.edit()
                .putBoolean("seed_phrase_saved", true)
                .putString("seed_phrase", newSeed)
                .apply()

            seedPhraseText.text = newSeed
        }
    }

    /** BLOCK: applyUiMode() */
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

    /** BLOCK: setActiveTab */
    private fun setActiveTab(active: ImageButton) {
        val buttons = listOf(btnChat, btnProjects, btnTerminal, btnAPI, btnSecurity, btnSettings)

        buttons.forEach { btn ->
            btn.setBackgroundResource(R.drawable.titanium_button)
            btn.alpha = 0.6f
        }

        active.setBackgroundResource(R.drawable.titanium_button_active)
        active.alpha = 1.0f
    }
}

/* ========================================================================
   METADATA FOOTER — SecurityActivity.kt (Titanium Version)
   version: 1.0.0
   local_timestamp: 07/07/2026 09:41 AM EDT
   utc_timestamp: 2026-07-07T13:41:00Z

   ml_tags: [
       "security_ui",
       "seed_phrase",
       "encrypted_backup",
       "titanium_rainbow",
       "tab_navigation"
   ]

   section: "7.1 — SecurityActivity.kt (Titanium Upgrade)"

   purpose:
   - Titanium version of Security module UI.
   - Preserves all seed phrase + backup/restore logic.
   - Adds navigation + conformity with titanium top bar.

   dependencies: [
       "activity_security.xml",
       "SecurityManager.kt",
       "titanium_rainbow_background",
       "titanium_button",
       "titanium_button_active"
   ]

   notes:
   - Safe drop‑in replacement.
   - No drift. No cascade.
   ========================================================================
   END OF FILE :: CHATMULTIAPI :: GODMODE
*/
