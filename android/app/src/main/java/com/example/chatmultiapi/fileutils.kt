package com.example.chatmultiapi

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.OpenableColumns

/**
 * BLOCK: FileUtils
 * PURPOSE: Extract filenames from content URIs for file attachment bubbles.
 * SAFE: comment only
 *
 * Responsibilities:
 * - Resolve display names from Android content providers
 * - Provide fallback names when metadata is missing
 * - Keep logic simple, predictable, and legacy‑compatible
 *
 * Notes:
 * - No GodMode namespace usage (legacy rule)
 * - Used by ChatActivity + ProviderRouter
 * - TimestampEngine does not interact with this module
 */
object FileUtils {

    /**
     * BLOCK: getFileName()
     * PURPOSE: Extract filename from content URI with safe fallback.
     * SAFE: comment only
     */
    fun getFileName(context: Context, uri: Uri): String {
        var result: String? = null

        val cursor: Cursor? = context.contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (nameIndex >= 0 && it.moveToFirst()) {
                result = it.getString(nameIndex)
            }
        }

        if (result == null) {
            result = uri.lastPathSegment ?: "unknown_file"
        }

        return result ?: "unknown_file"
    }
}

/* ========================================================================
   METADATA FOOTER — fileutils.kt
   version: 1.0.0
   local_timestamp: 07/06/2026 11:07 AM EDT
   utc_timestamp: 2026-07-06T15:07:00Z

   ML TAGS
   - ml_tags: ["file_utils", "uri_parser", "metadata_system", "godmode_core"]

   BLUEPRINT SECTION
   - section: "3.4 — fileutils.kt"

   SECTION PURPOSE
   - Extract filenames from content URIs for file bubbles.
   - Provide safe fallback when metadata is missing.
   - Used by ChatActivity + ProviderRouter.

   DEPENDENCIES
   - uses: [
       "ChatActivity.kt",
       "ProviderRouter.kt",
       "item_bubble.xml"
     ]

   NOTES
   - Fully regenerated to restore conformity.
   - Non-executable metadata footer.
   - Safe for copy/paste.
   ========================================================================
   END OF FILE :: CHATMULTIAPI :: GODMODE
*/
