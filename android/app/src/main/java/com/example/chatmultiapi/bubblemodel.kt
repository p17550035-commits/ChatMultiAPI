package com.example.chatmultiapi

import android.net.Uri

/**
 * BLOCK: BubbleModel
 * PURPOSE: Pure data model for chat bubbles (text, image, metadata, timestamps).
 * SAFE: comment only
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

/* ========================================================================
   METADATA FOOTER — BubbleModel.kt
   version: 1.0.0
   local_timestamp: 07/06/2026 11:04 AM EDT
   utc_timestamp: 2026-07-06T15:04:00Z

   ML TAGS
   - ml_tags: ["data_model", "chat_bubble", "timestamp_engine", "metadata_system", "godmode_core"]

   BLUEPRINT SECTION
   - section: "2.3 — BubbleModel.kt"

   SECTION PURPOSE
   - Defines unified bubble structure for chat UI.
   - Supports text, image, metadata preview, timestamps, and ML routing tags.
   - Used by BubbleAdapter, ChatActivity, and timestamp engine.

   DEPENDENCIES
   - uses: [
       "item_bubble.xml",
       "BubbleAdapter.kt",
       "ChatActivity.kt"
     ]

   NOTES
   - Fully regenerated to restore conformity.
   - Non-executable metadata footer.
   - Safe for copy/paste.
   ========================================================================
   END OF FILE :: CHATMULTIAPI :: GODMODE
*/
