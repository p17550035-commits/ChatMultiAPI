package com.example.chatmultiapi

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

/**
 * BLOCK: ApiActivity
 * PURPOSE: API module placeholder until full system is implemented
 * SAFE: comment only
 */
class ApiActivity : AppCompatActivity() {

    /** BLOCK: Titanium Top Bar Buttons */
    private lateinit var btnChat: ImageButton
    private lateinit var btnProjects: ImageButton
    private lateinit var btnTerminal: ImageButton
    private lateinit var btnAPI: ImageButton
    private lateinit var btnSecurity: ImageButton
    private lateinit var btnSettings: ImageButton

    /** BLOCK: Placeholder API Label */
    private lateinit var apiPlaceholder: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_api)

        // Titanium background
        window.decorView.setBackgroundResource(R.drawable.titanium_rainbow_background)

        // Bind titanium top bar buttons
        btnChat = findViewById(R.id.btnChat)
        btnProjects = findViewById(R.id.btnProjects)
        btnTerminal = findViewById(R.id.btnTerminal)
        btnAPI = findViewById(R.id.btnAPI)
        btnSecurity = findViewById(R.id.btnSecurity)
        btnSettings = findViewById(R.id.btnSettings)

        // Active tab highlight
        setActiveTab(btnAPI)

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
            // Already here
        }

        btnSecurity.setOnClickListener {
            setActiveTab(btnSecurity)
            startActivity(Intent(this, SecurityActivity::class.java))
        }

        btnSettings.setOnClickListener {
            setActiveTab(btnSettings)
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        // Placeholder content
        apiPlaceholder = findViewById(R.id.apiPlaceholder)
        apiPlaceholder.text = "API module placeholder.\nFull API system coming soon."
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
   METADATA FOOTER — ApiActivity.kt (Titanium Version)
   version: 1.0.0
   local_timestamp: 07/07/2026 09:30 AM EDT
   utc_timestamp: 2026-07-07T13:30:00Z

   ml_tags: [
       "kotlin_activity",
       "api_module",
       "titanium_rainbow",
       "tab_navigation",
       "godmode_core"
   ]

   section: "6.1 — ApiActivity.kt (Titanium Upgrade)"

   purpose:
   - Titanium version of API module UI.
   - Provides navigation + conformity with titanium top bar.
   - Placeholder until full API system is implemented.

   dependencies: [
       "activity_api.xml",
       "titanium_rainbow_background",
       "titanium_button",
       "titanium_button_active"
   ]

   notes:
   - Safe drop‑in replacement.
   - No drift. No cascade. No surprises.
   ========================================================================
   END OF FILE :: CHATMULTIAPI :: GODMODE
*/
