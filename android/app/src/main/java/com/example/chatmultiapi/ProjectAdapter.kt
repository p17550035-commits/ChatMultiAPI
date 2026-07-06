package com.example.chatmultiapi

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

/**
 * BLOCK: ProjectAdapter
 * PURPOSE: Simple ListView adapter for project names.
 * SAFE: comment only
 */
class ProjectAdapter(
    context: Context,
    private val projects: List<String>
) : ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, projects) {

    /**
     * BLOCK: getView()
     * PURPOSE: Inflate + bind project name.
     * SAFE: comment only
     */
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)

        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.text = projects[position]

        return view
    }
}

/* ========================================================================
   METADATA FOOTER — ProjectAdapter.kt
   version: 1.0.0
   local_timestamp: 07/06/2026 03:45 PM EDT
   utc_timestamp: 2026-07-06T19:45:00Z

   ML TAGS
   - ml_tags: ["project_adapter", "listview", "projects_ui", "godmode_core"]

   BLUEPRINT SECTION
   - section: "4.2 — ProjectAdapter.kt"

   SECTION PURPOSE
   - Provides ListView adapter for project names.
   - Used directly by ProjectsActivity.
   - Lightweight + conforms to Android simple_list_item_1.

   DEPENDENCIES
   - uses: [
       "ProjectsActivity.kt",
       "ProjectManager.kt"
     ]

   NOTES
   - Fully generated to restore conformity.
   - Non-executable metadata footer.
   - Safe for copy/paste.
   ========================================================================
   END OF FILE :: CHATMULTIAPI :: GODMODE
*/
