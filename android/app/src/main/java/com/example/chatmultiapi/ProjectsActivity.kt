package com.example.chatmultiapi

import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class ProjectsActivity : AppCompatActivity() {

    private lateinit var projectListView: ListView
    private lateinit var newProjectBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_projects)

        // Neon theme background (chat/project zone only)
        window.decorView.setBackgroundColor(
            ContextCompat.getColor(this, R.color.neonBackground)
        )

        projectListView = findViewById(R.id.projectListView)
        newProjectBtn = findViewById(R.id.newProjectBtn)

        // Load existing projects (placeholder for now)
        val projects = ProjectManager.loadProjects(this)
        val adapter = ProjectAdapter(this, projects)
        projectListView.adapter = adapter

        // Create new project
        newProjectBtn.setOnClickListener {
            ProjectManager.createNewProject(this)
            val updated = ProjectManager.loadProjects(this)
            projectListView.adapter = ProjectAdapter(this, updated)
        }
    }
}
