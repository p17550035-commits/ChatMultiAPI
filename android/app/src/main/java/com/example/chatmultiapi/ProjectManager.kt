package com.example.chatmultiapi

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

/**
 * BLOCK: ProjectManager
 * PURPOSE: Load, save, and create project entries stored in JSON.
 * SAFE: comment only
 */
object ProjectManager {

    private const val PROJECTS_FILE = "projects.json"

    /**
     * BLOCK: getProjectsFile()
     * PURPOSE: Centralized file reference.
     * SAFE: comment only
     */
    private fun getProjectsFile(context: Context): File {
        return File(context.filesDir, PROJECTS_FILE)
    }

    /**
     * BLOCK: loadProjects()
     * PURPOSE: Read project list from storage.
     * SAFE: comment only
     */
    fun loadProjects(context: Context): List<String> {
        val file = getProjectsFile(context)
        if (!file.exists()) return emptyList()

        return try {
            val json = JSONArray(file.readText())
            List(json.length()) { index -> json.getString(index) }
        } catch (e: Exception) {
            emptyList()
        }
    }

    /**
     * BLOCK: saveProjects()
     * PURPOSE: Write project list to storage.
     * SAFE: comment only
     */
    private fun saveProjects(context: Context, projects: List<String>) {
        val json = JSONArray()
        projects.forEach { json.put(it) }
        getProjectsFile(context).writeText(json.toString(4))
    }

    /**
     * BLOCK: createNewProject()
     * PURPOSE: Append new project + save.
     * SAFE: comment only
     */
    fun createNewProject(context: Context) {
        val projects = loadProjects(context).toMutableList()
        val newName = "Project_${projects.size + 1}"
        projects.add(newName)
        saveProjects(context, projects)
    }
}

/* ========================================================================
   METADATA FOOTER — ProjectManager.kt
   version: 1.0.0
   local_timestamp: 07/06/2026 03:45 PM EDT
   utc_timestamp: 2026-07-06T19:45:00Z

   ML TAGS
   - ml_tags: ["project_manager", "json_storage", "projects_ui", "godmode_core"]

   BLUEPRINT SECTION
   - section: "4.1 — ProjectManager.kt"

   SECTION PURPOSE
   - Handles loading, saving, and creating project entries.
   - Stores projects.json inside app files directory.
   - Used by ProjectsActivity + ProjectAdapter.

   DEPENDENCIES
   - uses: [
       "projects.json",
       "ProjectsActivity.kt",
       "ProjectAdapter.kt"
     ]

   NOTES
   - Fully generated to restore conformity.
   - Non-executable metadata footer.
   - Safe for copy/paste.
   ========================================================================
   END OF FILE :: CHATMULTIAPI :: GODMODE
*/
