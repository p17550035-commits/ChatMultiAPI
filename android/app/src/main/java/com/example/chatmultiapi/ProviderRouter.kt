package com.example.chatmultiapi

import android.content.Context
import android.net.Uri
import org.json.JSONObject
import java.io.File

object ProviderRouter {

    private const val CONFIG_NAME = "app_config.json"

    // Load config JSON
    private fun loadConfig(context: Context): JSONObject {
        val file = File(context.filesDir, CONFIG_NAME)
        if (!file.exists()) {
            return JSONObject().apply {
                put("selected", "local")
                put("providers", JSONObject())
            }
        }
        return JSONObject(file.readText())
    }

    // Get selected provider
    private fun getSelectedProvider(context: Context): String {
        val json = loadConfig(context)
        return json.optString("selected", "local")
    }

    // Get provider key
    private fun getProviderKey(context: Context, provider: String): String {
        val json = loadConfig(context)
        val providers = json.getJSONObject("providers")

        return if (provider == "custom") {
            providers.getJSONObject("custom").optString("key", "")
        } else {
            providers.optString(provider, "")
        }
    }

    // Get custom provider URL
    private fun getCustomUrl(context: Context): String {
        val json = loadConfig(context)
        val providers = json.getJSONObject("providers")
        return providers.getJSONObject("custom").optString("url", "")
    }

    // Main message routing
    fun sendMessage(context: Context, text: String): String {
        val provider = getSelectedProvider(context)
        val key = getProviderKey(context, provider)

        return when (provider) {
            "openai" -> OpenAIProvider.send(text, key)
            "anthropic" -> AnthropicProvider.send(text, key)
            "groq" -> GroqProvider.send(text, key)
            "nvidia" -> NvidiaProvider.send(text, key)
            "lmstudio" -> LMStudioProvider.send(text)
            "custom" -> CustomProvider.send(text, key, getCustomUrl(context))
            else -> LocalProvider.send(text)
        }
    }

    // File routing
    fun sendFile(context: Context, uri: Uri) {
        val provider = getSelectedProvider(context)
        val key = getProviderKey(context, provider)

        when (provider) {
            "openai" -> OpenAIProvider.sendFile(uri, key)
            "anthropic" -> AnthropicProvider.sendFile(uri, key)
            "groq" -> GroqProvider.sendFile(uri, key)
            "nvidia" -> NvidiaProvider.sendFile(uri, key)
            "lmstudio" -> LMStudioProvider.sendFile(uri)
            "custom" -> CustomProvider.sendFile(uri, key, getCustomUrl(context))
            else -> LocalProvider.sendFile(uri)
        }
    }
}
