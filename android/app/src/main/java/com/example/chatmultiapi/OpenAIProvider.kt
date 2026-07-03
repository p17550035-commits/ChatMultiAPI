package com.example.chatmultiapi

import android.net.Uri

object OpenAIProvider {

    // Send text message to OpenAI
    fun send(text: String, key: String): String {
        if (key.isBlank()) {
            return "OpenAI Error: Missing API key"
        }

        // Placeholder — real API call added later
        return "OpenAI Response: $text"
    }

    // Send file to OpenAI
    fun sendFile(uri: Uri, key: String) {
        if (key.isBlank()) {
            println("OpenAI Error: Missing API key for file upload")
            return
        }

        // Placeholder — real file upload added later
        println("OpenAI received file: $uri")
    }
}
