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

    // Get custom model (optional)
    private fun getCustomModel(context: Context): String {
        val json = loadConfig(context)
        val providers = json.getJSONObject("providers")
        return providers.getJSONObject("custom").optString("model", "")
    }

    // -----------------------------
    // MAIN MESSAGE ROUTING
    // -----------------------------
    fun sendMessage(context: Context, text: String): String {
        val provider = getSelectedProvider(context)
        val key = getProviderKey(context, provider)

        return when (provider) {

            "openai" -> OpenAIProvider.send(
                text,
                key,
                ProvidersUrl.OPENAI_CHAT
            )

            "anthropic" -> AnthropicProvider.send(
                text,
                key,
                ProvidersUrl.ANTHROPIC_MESSAGES
            )

            "groq" -> GroqProvider.send(
                text,
                key,
                ProvidersUrl.GROQ_CHAT
            )

            "nvidia" -> NvidiaProvider.send(
                text,
                key,
                ProvidersUrl.NVIDIA_CHAT
            )

            "lmstudio" -> LMStudioProvider.send(
                text,
                ProvidersUrl.LMSTUDIO_CHAT
            )

            "custom" -> CustomProvider.send(
                text,
                key,
                getCustomUrl(context),
                getCustomModel(context)
            )

            else -> LocalProvider.send(text)
        }
    }

    // -----------------------------
    // FILE ROUTING
    // -----------------------------
    fun sendFile(context: Context, uri: Uri) {
        val provider = getSelectedProvider(context)
        val key = getProviderKey(context, provider)

        when (provider) {

            "openai" -> OpenAIProvider.sendFile(
                uri,
                key,
                ProvidersUrl.OPENAI_FILES
            )

            "anthropic" -> AnthropicProvider.sendFile(
                uri,
                key,
                ProvidersUrl.ANTHROPIC_MESSAGES
            )

            "groq" -> GroqProvider.sendFile(
                uri,
                key,
                ProvidersUrl.GROQ_CHAT
            )

            "nvidia" -> NvidiaProvider.sendFile(
                uri,
                key,
                ProvidersUrl.NVIDIA_CHAT
            )

            "lmstudio" -> LMStudioProvider.sendFile(
                uri,
                ProvidersUrl.LMSTUDIO_CHAT
            )

            "custom" -> CustomProvider.sendFile(
                uri,
                key,
                getCustomUrl(context),
                getCustomModel(context)
            )

            else -> LocalProvider.sendFile(uri)
        }
    }
}
