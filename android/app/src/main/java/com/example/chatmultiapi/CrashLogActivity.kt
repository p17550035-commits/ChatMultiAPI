package com.example.chatmultiapi

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.File

class CrashLogActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.crash_log)

        val crashTextView = findViewById<TextView>(R.id.crashText)

        val file = File(filesDir, "last_crash.txt")
        if (file.exists()) {
            crashTextView.text = file.readText()
        } else {
            crashTextView.text = "No crash log found."
        }
    }
}

/* ========================================================================
   METADATA FOOTER — CrashLogActivity.kt
   version: 1.0.0
   local_timestamp: 07/06/2026 07:41 PM EDT
   utc_timestamp: 2026-07-06T23:41:00Z

   ML TAGS
   - ml_tags: ["crash_screen", "diagnostics", "minimal_ui"]

   SECTION PURPOSE
   - Displays the crash log saved by CrashLogger.

   NOTES
   - Minimal, readable, no fancy UI.
   ========================================================================
   END OF FILE :: CHATMULTIAPI :: GODMODE
*/
