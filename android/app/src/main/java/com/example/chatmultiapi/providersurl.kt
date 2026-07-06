package com.example.chatmultiapi

/**
 * BLOCK: ProvidersUrl
 * PURPOSE: Central registry of provider base URLs + endpoints.
 * SAFE: comment only
 */
object ProvidersUrl {

    // --------------------------------------------------------------------
    // OPENAI (REAL ENDPOINTS)
    // --------------------------------------------------------------------
    const val OPENAI_BASE = "https://api.openai.com/v1"
    const val OPENAI_CHAT = "$OPENAI_BASE/chat/completions"
    const val OPENAI_COMPLETIONS = "$OPENAI_BASE/completions"
    const val OPENAI_EMBEDDINGS = "$OPENAI_BASE/embeddings"
    const val OPENAI_FILES = "$OPENAI_BASE/files"
    const val OPENAI_MODELS = "$OPENAI_BASE/models"

    // --------------------------------------------------------------------
    // ANTHROPIC (REAL ENDPOINTS)
    // --------------------------------------------------------------------
    const val ANTHROPIC_BASE = "https://api.anthropic.com/v1"
    const val ANTHROPIC_MESSAGES = "$ANTHROPIC_BASE/messages"
    const val ANTHROPIC_COMPLETIONS = "$ANTHROPIC_BASE/complete"
    const val ANTHROPIC_MODELS = "$ANTHROPIC_BASE/models"

    // --------------------------------------------------------------------
    // GROQ (REAL ENDPOINTS)
    // --------------------------------------------------------------------
    const val GROQ_BASE = "https://api.groq.com/openai/v1"
    const val GROQ_CHAT = "$GROQ_BASE/chat/completions"
    const val GROQ_EMBEDDINGS = "$GROQ_BASE/embeddings"
    const val GROQ_MODELS = "$GROQ_BASE/models"

    // --------------------------------------------------------------------
    // NVIDIA (REAL ENDPOINTS)
    // --------------------------------------------------------------------
    const val NVIDIA_BASE = "https://api.nvidia.com/v1"
    const val NVIDIA_CHAT = "$NVIDIA_BASE/chat/completions"
    const val NVIDIA_EMBEDDINGS = "$NVIDIA_BASE/embeddings"
    const val NVIDIA_MODELS = "$NVIDIA_BASE/models"

    // --------------------------------------------------------------------
    // LM STUDIO (REAL ENDPOINTS)
    // --------------------------------------------------------------------
    const val LMSTUDIO_BASE = "http://localhost:1234/v1"
    const val LMSTUDIO_CHAT = "$LMSTUDIO_BASE/chat/completions"
    const val LMSTUDIO_MODELS = "$LMSTUDIO_BASE/models"

    // --------------------------------------------------------------------
    // LOCAL PROVIDER (FLEXIBLE)
    // --------------------------------------------------------------------
    const val LOCAL_BASE = ""
    const val LOCAL_CHAT = ""
    const val LOCAL_MODELS = ""

    // --------------------------------------------------------------------
    // CUSTOM PROVIDER (FULLY FLEXIBLE)
    // --------------------------------------------------------------------
    var CUSTOM_BASE: String = ""
    var CUSTOM_CHAT: String = ""
    var CUSTOM_MODELS: String = ""
}

/* ========================================================================
   METADATA FOOTER — ProvidersUrl.kt
   version: 1.0.0
   local_timestamp: 07/06/2026 11:09 AM EDT
   utc_timestamp: 2026-07-06T15:09:00Z

   ML TAGS
   - ml_tags: ["provider_endpoints", "api_registry", "backend_system", "godmode_core"]

   BLUEPRINT SECTION
   - section: "3.2 — ProvidersUrl.kt"

   SECTION PURPOSE
   - Central registry of provider base URLs and endpoints.
   - Used by ProviderRouter + ApiMaster for routing.
   - Supports OpenAI, Anthropic, Groq, NVIDIA, LM Studio, custom, and local.

   DEPENDENCIES
   - uses: [
       "ProviderRouter.kt",
       "ApiMaster.kt",
       "LocalProvider.kt"
     ]

   NOTES
   - Fully regenerated to restore conformity.
   - Non-executable metadata footer.
   - Safe for copy/paste.
   ========================================================================
   END OF FILE :: CHATMULTIAPI :: GODMODE
*/
