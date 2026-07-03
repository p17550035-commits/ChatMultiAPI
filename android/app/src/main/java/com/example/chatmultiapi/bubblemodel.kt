package com.chatmultiapi.godmode.model

import android.net.Uri

/**
 * BubbleModel — Chat Bubble Data Model (Blueprint v2)
 * ---------------------------------------------------
 * Purpose:
 * Represents a single chat bubble in the UI.
 *
 * What it supports:
 * - Text bubbles
 * - Image bubbles
 * - Metadata preview bubbles
 *
 * What it needs:
 * - item_bubble.xml layout IDs
 * - Timestamp engine (local + UTC)
 * - Dependency list for crash recovery
 * - ML tags for future routing
 *
 * Why descriptors exist:
 * To make debugging easier and keep the machine aware of intent.
 */
data class BubbleModel(

    /** Unique bubble ID for tracking + RecyclerView diffing */
    val id: String,

    /** "user" or "assistant" — controls bubble alignment + styling */
    val role: String,

    /** Text content for text bubbles (bubbleText in XML) */
    val text: String? = null,

    /** Image URI for image bubbles (bubbleImage in XML) */
    val imageUri: Uri? = null,

    /** Local timestamp (MM/DD/YYYY HH:MM EDT) — visible to user */
    val localTimestamp: String,

    /** UTC timestamp — always stored, hidden unless toggled */
    val utcTimestamp: String,

    /** Optional metadata preview (bubbleMeta in XML) */
    val metadataPreview: String? = null,

    /** Dependency list — used for workflow continuity + crash recovery */
    val dependencies: List<String>,

    /** ML tags — used by GodMode machine for classification + routing */
    val mlTags: List<String>
)

/*
================================================================================
METADATA :: GODMODE :: chatmultiapi
section: 2.3 bubblemodel.kt (chat ui — data model)
generated (local): 07/03/2026 @ 08:15 edt
generated (utc stored): 2026-07-03T12:15:00Z
version: 2.0.0
origin: bubblemodel.kt
mode: embedded editor mode

dependencies:
- item_bubble.xml
- bubbleadapter.kt
- chatactivity.kt
- devtoolbarcontroller.kt
- uploadstripcontroller.kt

ml tags:
- data_model
- chat_bubble
- timestamp_engine
- metadata_system
- godmode_core
- v2_ruleset

timestamp rules:
- local: mm/dd/yyyy hh:mm edt
- utc: always stored, hidden unless toggled
- controlled via settingsactivity (local / utc / both)

redundancy rules (blueprint v2):
- lowercase filenames required
- descriptors required in all code files
- version required in metadata footer
- full-file regeneration only
- no partial edits
- metadata footer required
- ml tags required
- dependency list required

god mode™ — born july 3rd, 2026
================================================================================
end of file :: godmode :: chatmultiapi
*/
