// bubblemodel.kt — v1.0.1

package com.example.chatmultiapi

import android.net.Uri

/**
 * BubbleModel — Chat Bubble Data Model (v1)
 * -----------------------------------------
 * Pure data model for all chat bubble types.
 * Supports:
 * - text bubbles
 * - image bubbles
 * - metadata preview
 * - timestamp engine (local + utc)
 * - dependency + ml tag routing
 *
 * Notes:
 * - Legacy namespace preserved (no GodMode package usage)
 * - Blueprint metadata handled in footer only
 * - Class name remains capitalized (Kotlin requirement)
 */
data class BubbleModel(

    val id: String,                 // unique bubble id
    val role: String,               // "user" or "assistant"

    val text: String? = null,       // text bubble
    val imageUri: Uri? = null,      // image bubble

    val localTimestamp: String,     // visible timestamp
    val utcTimestamp: String,       // stored timestamp

    val metadataPreview: String? = null, // optional metadata preview

    val dependencies: List<String>, // crash recovery + routing
    val mlTags: List<String>        // classification + routing
)

/*
================================================================================
METADATA :: GODMODE :: chatmultiapi
section: 2.3 bubblemodel.kt (chat ui — data model)
version: 1.0.1
origin: bubblemodel.kt
mode: embedded editor mode

dependencies:
- item_bubble.xml
- bubbleadapter.kt
- chatactivity.kt

blueprint:
- data_model_core
- bubble_structure
- timestamp_fields
- metadata_system
- v1_ruleset

ml tags:
- data_model
- chat_bubble
- timestamp_engine
- metadata_system
- godmode_core

end of file :: godmode :: chatmultiapi
================================================================================
