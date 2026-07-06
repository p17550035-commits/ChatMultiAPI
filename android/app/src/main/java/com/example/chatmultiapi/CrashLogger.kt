package com.example.chatmultiapi

import android.content.Context
import android.content.Intent
import java.io.File

class CrashLogger(private val context: Context) : Thread.UncaughtExceptionHandler {

    private val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()

    override fun uncaughtException(thread: Thread, throwable: Throwable) {
        try {
            val file = File(context.filesDir, "last_crash.txt")
            file.writeText(throwable.stackTraceToString())

            val intent = Intent(context, CrashLogActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            }
            context.startActivity(intent)
        } catch (_: Exception) {
            // fallback to system handler
        }

        defaultHandler?.uncaughtException(thread, throwable)
    }
}

/* ========================================================================
   METADATA FOOTER — CrashLogger.kt
   version: 1.0.0
   local_timestamp: 07/06/2026 07:41 PM EDT
   utc_timestamp: 2026-07-06T23:41:00Z

   ML TAGS
   - ml_tags: ["crash_logger", "global_handler", "diagnostics"]

   SECTION PURPOSE
   - Captures uncaught exceptions and writes them to last_crash.txt.
   - Launches CrashLogActivity to display the crash.

   NOTES
   - Minimal, functional, no styling.
   ========================================================================
   END OF FILE :: CHATMULTIAPI :: GODMODE
*/
