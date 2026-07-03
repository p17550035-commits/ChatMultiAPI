// bubblemodel.kt — v1.0.0

package com.chatmultiapi.godmode.model

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
version: 1.0.0
origin: bubblemodel.kt
mode: embedded editor mode

dependencies:
- item_bubble.xml
- bubbleadapter.kt
- chatactivity.kt

ml tags:
- data_model
- chat_bubble
- timestamp_engine
- metadata_system
- godmode_core
- v1_ruleset

end of file :: godmode :: chatmultiapi
================================================================================
