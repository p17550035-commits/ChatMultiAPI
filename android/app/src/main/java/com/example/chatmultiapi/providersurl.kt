package com.example.chatmultiapi

object ProvidersUrl {

    // -----------------------------
    // OPENAI (REAL ENDPOINTS)
    // -----------------------------
    const val OPENAI_BASE = "https://api.openai.com/v1"
    const val OPENAI_CHAT = "$OPENAI_BASE/chat/completions"
    const val OPENAI_COMPLETIONS = "$OPENAI_BASE/completions"
    const val OPENAI_EMBEDDINGS = "$OPENAI_BASE/embeddings"
    const val OPENAI_FILES = "$OPENAI_BASE/files"
    const val OPENAI_MODELS = "$OPENAI_BASE/models"


    // -----------------------------
    // ANTHROPIC (REAL ENDPOINTS)
    // -----------------------------
    const val ANTHROPIC_BASE = "https://api.anthropic.com/v1"
    const val ANTHROPIC_MESSAGES = "$ANTHROPIC_BASE/messages"
    const val ANTHROPIC_COMPLETIONS = "$ANTHROPIC_BASE/complete"
    const val ANTHROPIC_MODELS = "$ANTHROPIC_BASE/models"


    // -----------------------------
    // GROQ (REAL ENDPOINTS)
    // -----------------------------
    const val GROQ_BASE = "https://api.groq.com/openai/v1"
    const val GROQ_CHAT = "$GROQ_BASE/chat/completions"
    const val GROQ_EMBEDDINGS = "$GROQ_BASE/embeddings"
    const val GROQ_MODELS = "$GROQ_BASE/models"


    // -----------------------------
    // NVIDIA (REAL ENDPOINTS)
    // -----------------------------
    const val NVIDIA_BASE = "https://api.nvidia.com/v1"
    const val NVIDIA_CHAT = "$NVIDIA_BASE/chat/completions"
    const val NVIDIA_EMBEDDINGS = "$NVIDIA_BASE/embeddings"
    const val NVIDIA_MODELS = "$NVIDIA_BASE/models"


    // -----------------------------
    // LM STUDIO (REAL ENDPOINTS)
    // -----------------------------
    // LM Studio uses OpenAI-compatible routes
    const val LMSTUDIO_BASE = "http://localhost:1234/v1"
    const val LMSTUDIO_CHAT = "$LMSTUDIO_BASE/chat/completions"
    const val LMSTUDIO_MODELS = "$LMSTUDIO_BASE/models"


    // -----------------------------
    // LOCAL PROVIDER (FLEXIBLE)
    // -----------------------------
    // Local provider is intentionally bare-bones.
    // User/server defines everything.
    const val LOCAL_BASE = ""   // user/server decides
    const val LOCAL_CHAT = ""   // user/server decides
    const val LOCAL_MODELS = "" // user/server decides


    // -----------------------------
    // CUSTOM PROVIDER (FULLY FLEXIBLE)
    // -----------------------------
    // Custom provider must accept ANY structure.
    // No forced endpoints. No assumptions.
    // User defines base URL, model, path, etc.
    var CUSTOM_BASE: String = ""
    var CUSTOM_CHAT: String = ""
    var CUSTOM_MODELS: String = ""
}
