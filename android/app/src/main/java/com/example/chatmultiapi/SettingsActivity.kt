package com.example.chatmultiapi

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

/**
 * BLOCK: SettingsActivity
 * PURPOSE: Placeholder settings screen until full module is implemented
 * SAFE: comment only
 */
class SettingsActivity : AppCompatActivity() {

    /** BLOCK: Titanium Top Bar Buttons */
    private lateinit var btnChat: ImageButton
    private lateinit var btnProjects: ImageButton
    private lateinit var btnTerminal: ImageButton
    private lateinit var btnAPI: ImageButton
    private lateinit var btnSecurity: ImageButton
    private lateinit var btnSettings: ImageButton

    /** BLOCK: Placeholder Label */
    private lateinit var settingsPlaceholder: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

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
        setActiveTab(btnSettings)

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

        btnAPI.setOnClickListener
