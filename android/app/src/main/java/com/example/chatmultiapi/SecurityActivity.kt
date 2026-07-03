package com.example.chatmultiapi

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class SecurityActivity : AppCompatActivity() {

    // SharedPreferences for UI mode
    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_security)

        prefs = getSharedPreferences("app_settings", Context.MODE_PRIVATE)

        // Apply UI mode immediately
        applyUiMode()

        // Seed phrase section
        val seedPhraseText = findViewById<TextView>(R.id.seedPhraseText)
        val generateSeedBtn = findViewById<Button>(R.id.generateSeedBtn)

        generateSeedBtn.setOnClickListener {
            val seed = SecurityManager.generateSeedPhrase()
            seedPhraseText.text = seed
        }

        // Backup section
        val backupBtn = findViewById<Button>(R.id.backupBtn)
        backupBtn.setOnClickListener {
            SecurityManager.backupEncryptedConfig(this)
        }

        // Restore section
        val restoreBtn = findViewById<Button>(R.id.restoreBtn)
        restoreBtn.setOnClickListener {
            SecurityManager.restoreEncryptedConfig(this)
        }

        // Danger zone section
        val wipeBtn = findViewById<Button>(R.id.wipeEncryptionBtn)
        wipeBtn.setOnClickListener {
            SecurityManager.resetEncryption(this)
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
