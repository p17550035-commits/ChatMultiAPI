package com.example.chatmultiapi

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

/**
 * BLOCK: TerminalActivity
 * PURPOSE: Placeholder terminal screen until full shell is chosen
 * SAFE: comment only
 */
class TerminalActivity : AppCompatActivity() {

    /** BLOCK: Titanium Top Bar Buttons
     *  PURPOSE: Navigation between core modules
     *  SAFE: comment only
     */
    private lateinit var btnChat: ImageButton
    private lateinit var btnProjects: ImageButton
    private lateinit var btnTerminal: ImageButton
    private lateinit var btnAPI: ImageButton
    private lateinit var btnSecurity: ImageButton
    private lateinit var btnSettings: ImageButton

    /** BLOCK: Placeholder Terminal Label
     *  PURPOSE: Temporary content until shell is implemented
     */
    private lateinit var terminalPlaceholder: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terminal)

        // BLOCK: Titanium Background
        // PURPOSE: Apply titanium-rainbow theme to terminal zone
        window.decorView.setBackgroundResource(R.drawable.titanium_rainbow_background)

        // BLOCK: Bind Titanium Top Bar Buttons
        btnChat = findViewById(R.id.btnChat)
        btnProjects = findViewById(R.id.btnProjects)
        btnTerminal = findViewById(R.id.btnTerminal)
        btnAPI = findViewById(R.id.btnAPI)
        btnSecurity = findViewById(R.id.btnSecurity)
        btnSettings = findViewById(R.id.btnSettings)

        // BLOCK: Active Tab Highlight
        setActiveTab(btnTerminal)

        // BLOCK: Navigation Wiring
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
            // Already here — placeholder screen
        }

        btnAPI.setOnClickListener {
            setActiveTab(btnAPI)
            startActivity(Intent(this, ApiActivity::class.java))
        }

        btnSecurity.setOnClickListener {
            setActiveTab(btnSecurity)
            startActivity(Intent(this, SecurityActivity::class.java))
        }

        btnSettings.setOnClickListener {
            setActiveTab(btnSettings)
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        // BLOCK: Placeholder Terminal Content
        terminalPlaceholder = findViewById(R.id.terminalPlaceholder)
        terminalPlaceholder.text = "Terminal module placeholder.\nFull shell coming soon."
    }

    /**
     * BLOCK: setActiveTab
     * PURPOSE: Apply indent + glow to active tab
     * SAFE: comment only
     */
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
   METADATA FOOTER — TerminalActivity.kt
   version: 1.0.0
   local_timestamp: 07/07/2026 09:14 AM EDT
   utc_timestamp: 2026-07-07T13:14:00Z

   ML TAGS
   - ml_tags: [
       "kotlin_activity",
       "terminal_placeholder",
       "titanium_rainbow",
       "tab_navigation",
       "godmode_core"
     ]

   BLUEPRINT SECTION
   - section: "5.0 — TerminalActivity.kt"

   SECTION PURPOSE
   - Placeholder terminal screen until full shell is implemented.
   - Provides navigation + conformity with titanium top bar.
   - Integrates with activity_terminal.xml.

   DEPENDENCIES
   - uses: [
       "activity_terminal.xml",
       "titanium_rainbow_background",
       "titanium_button",
       "titanium_button_active"
     ]

   NOTES
   - Fully generated baseline placeholder.
   - Non-executable metadata footer.
   - Safe for copy/paste.
   ========================================================================
   END OF FILE :: CHATMULTIAPI :: GODMODE
*/
