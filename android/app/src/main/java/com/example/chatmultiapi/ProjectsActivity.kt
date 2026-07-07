package com.example.chatmultiapi

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

/**
 * BLOCK: ProjectsActivity
 * PURPOSE: Display project list + create new projects
 * SAFE: comment only
 */
class ProjectsActivity : AppCompatActivity() {

    /** BLOCK: UI Elements
     *  PURPOSE: ListView + New Project button
     *  SAFE: comment only
     */
    private lateinit var projectListView: ListView
    private lateinit var newProjectBtn: Button

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_projects)

        // BLOCK: Titanium Background
        // PURPOSE: Apply titanium-rainbow theme to project zone
        window.decorView.setBackgroundResource(R.drawable.titanium_rainbow_background)

        // BLOCK: Bind UI Elements
        projectListView = findViewById(R.id.projectListView)
        newProjectBtn = findViewById(R.id.newProjectBtn)

        // BLOCK: Bind Titanium Top Bar Buttons
        btnChat = findViewById(R.id.btnChat)
        btnProjects = findViewById(R.id.btnProjects)
        btnTerminal = findViewById(R.id.btnTerminal)
        btnAPI = findViewById(R.id.btnAPI)
        btnSecurity = findViewById(R.id.btnSecurity)
        btnSettings = findViewById(R.id.btnSettings)

        // BLOCK: Active Tab Highlight
        // PURPOSE: Indent + glow effect for current tab
        setActiveTab(btnProjects)

        // BLOCK: Navigation Wiring
        // PURPOSE: Move between core modules
        btnChat.setOnClickListener {
            setActiveTab(btnChat)
            startActivity(Intent(this, MainActivity::class.java))
        }

        btnProjects.setOnClickListener {
            setActiveTab(btnProjects)
            // Already here — no navigation
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
            startActivity(Intent(this, SecurityActivity::class.java))
        }

        btnSettings.setOnClickListener {
            setActiveTab(btnSettings)
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        // BLOCK: Load Existing Projects
        // PURPOSE: Populate ListView with saved projects
        val projects = ProjectManager.loadProjects(this)
        val adapter = ProjectAdapter(this, projects)
        projectListView.adapter = adapter

        // BLOCK: Create New Project
        // PURPOSE: Add new project + refresh list
        newProjectBtn.setOnClickListener {
            ProjectManager.createNewProject(this)
            val updated = ProjectManager.loadProjects(this)
            projectListView.adapter = ProjectAdapter(this, updated)
        }
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
   METADATA FOOTER — ProjectsActivity.kt
   version: 1.0.0
   local_timestamp: 07/07/2026 09:01 AM EDT
   utc_timestamp: 2026-07-07T13:01:00Z

   ML TAGS
   - ml_tags: [
       "kotlin_activity",
       "projects_ui",
       "titanium_rainbow",
       "tab_navigation",
       "godmode_core"
     ]

   BLUEPRINT SECTION
   - section: "4.0 — ProjectsActivity.kt"

   SECTION PURPOSE
   - Displays project list and allows creation of new projects.
   - Interfaces directly with activity_projects.xml.
   - Uses ProjectManager + ProjectAdapter for data handling.
   - Upgraded to titanium-rainbow theme + top bar navigation.

   DEPENDENCIES
   - uses: [
       "activity_projects.xml",
       "ProjectManager.kt",
       "ProjectAdapter.kt",
       "titanium_rainbow_background",
       "titanium_button",
       "titanium_button_active"
     ]

   NOTES
   - Fully upgraded with zero drift.
   - Non-executable metadata footer.
   - Safe for copy/paste.
   ========================================================================
   END OF FILE :: CHATMULTIAPI :: GODMODE
*/
