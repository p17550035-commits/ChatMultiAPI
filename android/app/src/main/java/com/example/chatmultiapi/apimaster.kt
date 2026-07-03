package com.example.chatmultiapi

import android.net.Uri
import okhttp3.*
import org.json.JSONObject
import java.io.File

object ApiMaster {

    private val client = OkHttpClient()

    // -----------------------------
    // SEND TEXT (ALL PROVIDERS)
    // -----------------------------
    fun sendText(
        provider: String,
        text: String,
        key: String,
        url: String,
        model: String = ""
    ): String {

        if (key.isBlank() && provider != "local" && provider != "custom") {
            return "$provider Error: Missing API key"
        }

        val json = JSONObject()

        when (provider) {

            "openai" -> {
                json.put("model", model.ifBlank { "gpt-4o-mini" })
                json.put("messages", listOf(
                    JSONObject().put("role", "user").put("content", text)
                ))
            }

            "anthropic" -> {
                json.put("model", model.ifBlank { "claude-3-sonnet-20240229" })
                json.put("max_tokens", 1024)
                json.put("messages", listOf(
                    JSONObject().put("role", "user").put("content", text)
                ))
            }

            "groq" -> {
                json.put("model", model.ifBlank { "llama3-70b-8192" })
                json.put("messages", listOf(
                    JSONObject().put("role", "user").put("content", text)
                ))
            }

            "nvidia" -> {
                json.put("model", model.ifBlank { "nemotron-4-340b" })
                json.put("messages", listOf(
                    JSONObject().put("role", "user").put("content", text)
                ))
            }

            "lmstudio" -> {
                json.put("model", model.ifBlank { "local-model" })
                json.put("messages", listOf(
                    JSONObject().put("role", "user").put("content", text)
                ))
            }

            "custom" -> {
                json.put("model", model)
                json.put("input", text)
            }

            "local" -> {
                return "Local Response: $text"
            }
        }

        val body = RequestBody.create(
            MediaType.parse("application/json"),
            json.toString()
        )

        val request = Request.Builder()
            .url(url)
            .apply {
                when (provider) {
                    "openai", "groq", "nvidia", "lmstudio" ->
                        addHeader("Authorization", "Bearer $key")

                    "anthropic" ->
                        addHeader("x-api-key", key)

                    "custom" -> {
                        if (key.isNotBlank()) addHeader("Authorization", key)
                    }
                }
            }
            .addHeader("Content-Type", "application/json")
            .post(body)
            .build()

        return try {
            val response = client.newCall(request).execute()
            response.body()?.string() ?: "$provider Error: Empty response"
        } catch (e: Exception) {
            "$provider Error: ${e.message}"
        }
    }

    // -----------------------------
    // SEND FILE (ALL PROVIDERS)
    // -----------------------------
    fun sendFile(
        provider: String,
        uri: Uri,
        key: String,
        url: String,
        model: String = ""
    ) {

        if (key.isBlank() && provider != "local" && provider != "custom") {
            println("$provider Error: Missing API key for file upload")
            return
        }

        val file = File(uri.path ?: return)

        val body = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart(
                "file",
                file.name,
                RequestBody.create(MediaType.parse("application/octet-stream"), file)
            )
            .build()

        val request = Request.Builder()
            .url(url)
            .apply {
                when (provider) {
                    "openai", "groq", "nvidia", "lmstudio" ->
                        addHeader("Authorization", "Bearer $key")

                    "anthropic" ->
                        addHeader("x-api-key", key)

                    "custom" -> {
                        if (key.isNotBlank()) addHeader("Authorization", key)
                    }
                }
            }
            .post(body)
            .build()

        try {
            val response = client.newCall(request).execute()
            println("$provider File Upload Response: ${response.body()?.string()}")
        } catch (e: Exception) {
            println("$provider File Upload Error: ${e.message}")
        }
    }
}
