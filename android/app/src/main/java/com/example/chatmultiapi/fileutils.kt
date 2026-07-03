// fileutils.kt — v1.0.1

package com.example.chatmultiapi

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.OpenableColumns

/**
 * fileutils.kt — File Utility Module (Section 3.4)
 * ------------------------------------------------
 * Purpose:
 * Extract filenames from content URIs for file attachment bubbles.
 *
 * Responsibilities:
 * - Safely resolve display names from Android content providers
 * - Provide fallback names when metadata is missing
 * - Keep logic simple, predictable, and legacy‑compatible
 *
 * Notes:
 * - No GodMode namespace usage (legacy rule)
 * - Only used by ChatActivity + ProviderRouter
 * - TimestampEngine does not interact with this module
 */
object FileUtils {

    /**
     * Extract a filename from a content URI.
     * Returns a safe fallback if metadata is unavailable.
     */
    fun getFileName(context: Context, uri: Uri): String {
        var result: String? = null

        // Try reading metadata from the content resolver
        val cursor: Cursor? = context.contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (nameIndex >= 0 && it.moveToFirst()) {
                result = it.getString(nameIndex)
            }
        }

        // Fallback: extract from URI path
        if (result == null) {
            result = uri.lastPathSegment ?: "unknown_file"
        }

        return result ?: "unknown_file"
    }
}

/*
================================================================================
METADATA :: GODMODE :: chatmultiapi
section: 3.4 fileutils.kt (backend utility — filename extraction)
version: 1.0.1
origin: fileutils.kt
mode: embedded editor mode

dependencies:
- chatactivity.kt
- providerrouter.kt

blueprint:
- file_system_core
- uri_resolution
- metadata_extraction
- v1_ruleset

ml tags:
- file_utils
- uri_parser
- metadata_system
- godmode_core

end of file :: godmode :: chatmultiapi
================================================================================
