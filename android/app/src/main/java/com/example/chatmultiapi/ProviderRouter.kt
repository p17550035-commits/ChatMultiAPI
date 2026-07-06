package com.example.chatmultiapi

import android.content.Context
import android.net.Uri
import org.json.JSONObject
import java.io.File

/**
 * BLOCK: ProviderRouter
 * PURPOSE: Select provider, load config, and route text/files to ApiMaster.
 * SAFE: comment only
 */
object ProviderRouter {

    private const val CONFIG_NAME = "app_config.json"

    /**
     * BLOCK: loadConfig()
     * PURPOSE: Load provider config JSON from internal storage.
     * SAFE: comment only
     */
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

    /**
     * BLOCK: getSelectedProvider()
     * PURPOSE: Return active provider from config.
     * SAFE: comment only
     */
    private fun getSelectedProvider(context: Context): String {
        val json = loadConfig(context)
        return json.optString("selected", "local")
    }

    /**
     * BLOCK: getProviderKey()
     * PURPOSE: Return API key for provider.
     * SAFE: comment only
     */
    private fun getProviderKey(context: Context, provider: String): String {
        val json = loadConfig(context)
        val providers = json.getJSONObject("providers")

        return if (provider == "custom") {
            providers.getJSONObject("custom").optString("key", "")
        } else {
            providers.optString(provider, "")
        }
    }

    /**
     * BLOCK: getCustomUrl()
     * PURPOSE: Return custom provider URL.
     * SAFE: comment only
     */
    private fun getCustomUrl(context: Context): String {
        val json = loadConfig(context)
        return json.getJSONObject("providers")
            .getJSONObject("custom")
            .optString("url", "")
    }

    /**
     * BLOCK: getCustomModel()
     * PURPOSE: Return custom provider model name.
     * SAFE: comment only
     */
    private fun getCustomModel(context: Context): String {
        val json = loadConfig(context)
        return json.getJSONObject("providers")
            .getJSONObject("custom")
            .optString("model", "")
    }

    // --------------------------------------------------------------------
    // TEXT ROUTING → ApiMaster
    // --------------------------------------------------------------------
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

    // --------------------------------------------------------------------
    // FILE ROUTING → ApiMaster
    // --------------------------------------------------------------------
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

/* ========================================================================
   METADATA FOOTER — ProviderRouter.kt
   version: 1.0.0
   local_timestamp: 07/06/2026 10:50 AM EDT
   utc_timestamp: 2026-07-06T14:50:00Z

   ML TAGS
   - ml_tags: ["backend_router", "provider_system", "api_bridge", "godmode_core"]

   BLUEPRINT SECTION
   - section: "3.1 — ProviderRouter.kt"

   SECTION PURPOSE
   - Handles provider selection, config loading, and routing text/files to ApiMaster.
   - Bridges Android → Python backend → external APIs.

   DEPENDENCIES
   - uses: [
       "ApiMaster.kt",
       "ProvidersUrl.kt",
       "LocalProvider.kt",
       "ChatActivity.kt",
       "app_config.json"
     ]

   NOTES
   - Fully regenerated to restore conformity.
   - Non-executable metadata footer.
   - Safe for copy/paste.
   ========================================================================
   END OF FILE :: CHATMULTIAPI :: GODMODE
*/
