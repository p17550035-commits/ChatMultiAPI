package com.example.chatmultiapi

import android.net.Uri

object LocalProvider {

    // Simple offline response generator
    fun send(text: String): String {
        return "Local Response: $text"
    }

    // Simple offline file handler
    fun sendFile(uri: Uri) {
        // Placeholder — later we can parse file content or metadata
        println("LocalProvider received file: $uri")
    }
}
