package com.example.chatmultiapi

import android.os.Bundle
import android.widget.Button
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_projects)

        // BLOCK: Neon Background
        // PURPOSE: Apply neon theme to project zone
        window.decorView.setBackgroundColor(
            ContextCompat.getColor(this, R.color.neonBackground)
        )

        projectListView = findViewById(R.id.projectListView)
        newProjectBtn = findViewById(R.id.newProjectBtn)

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
}

/* ========================================================================
   METADATA FOOTER — ProjectsActivity.kt
   version: 1.0.0
   local_timestamp: 07/06/2026 10:46 AM EDT
   utc_timestamp: 2026-07-06T14:46:00Z

   ML TAGS
   - ml_tags: ["kotlin_activity", "projects_ui", "neon_theme", "godmode_core"]

   BLUEPRINT SECTION
   - section: "4.0 — ProjectsActivity.kt"

   SECTION PURPOSE
   - Displays project list and allows creation of new projects.
   - Interfaces directly with activity
