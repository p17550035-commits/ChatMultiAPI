package com.example.chatmultiapi

import android.content.Context
import android.net.Uri
import org.json.JSONObject
import java.io.File

object ProviderRouter {

    private const val CONFIG_NAME = "app_config.json"

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

    private fun getSelectedProvider(context: Context): String {
        val json = loadConfig(context)
        return json.optString("selected", "local")
    }

    private fun getProviderKey(context: Context, provider: String): String {
        val json = loadConfig(context)
        val providers = json.getJSONObject("providers")

        return if (provider == "custom") {
            providers.getJSONObject("custom").optString("key", "")
        } else {
            providers.optString(provider, "")
        }
    }

    private fun getCustomUrl(context: Context): String {
        val json = loadConfig(context)
        return json.getJSONObject("providers")
            .getJSONObject("custom")
            .optString("url", "")
    }

    private fun getCustomModel(context: Context): String {
        val json = loadConfig(context)
        return json.getJSONObject("providers")
            .getJSONObject("custom")
            .optString("model", "")
    }

    // -----------------------------
    // TEXT ROUTING → ApiMaster
    // -----------------------------
    fun sendMessage(context: Context, text: String): String {

        val provider = getSelectedProvider(context)
        val key = getProviderKey(context, provider)

        val url = when (provider) {
            "openai" -> ProvidersUrl.OPENAI_CHAT
            "anthropic" -> ProvidersUrl.ANTHROPIC_MESSAGES
            "groq" -> ProvidersUrl.GROQ_CHAT
            "nvidia" -> ProvidersUrl.NVIDIA_CHAT
            "lmstudio" -> ProvidersUrl.LMSTUDIO_CHAT
            "custom" -> getCustomUrl(context)
            else -> ""
        }

        val model = when (provider) {
            "custom" -> getCustomModel(context)
            else -> ""
        }

        return ApiMaster.sendText(
            provider = provider,
            text = text,
            key = key,
            url = url,
            model = model
        )
    }

    // -----------------------------
    // FILE ROUTING → ApiMaster
    // -----------------------------
    fun sendFile(context: Context, uri: Uri) {

        val provider = getSelectedProvider(context)
        val key = getProviderKey(context, provider)

        val url = when (provider) {
            "openai" -> ProvidersUrl.OPENAI_FILES
            "anthropic" -> ProvidersUrl.ANTHROPIC_MESSAGES
            "groq" -> ProvidersUrl.GROQ_CHAT
            "nvidia" -> ProvidersUrl.NVIDIA_CHAT
            "lmstudio" -> ProvidersUrl.LMSTUDIO_CHAT
            "custom" -> getCustomUrl(context)
            else -> ""
        }

        val model = when (provider) {
            "custom" -> getCustomModel(context)
            else -> ""
        }

        ApiMaster.sendFile(
            provider = provider,
            uri = uri,
            key = key,
            url = url,
            model = model
        )
    }
}
