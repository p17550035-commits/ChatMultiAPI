package com.example.chatmultiapi

import android.content.Context
import android.net.Uri
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView

/**
 * BLOCK: UploadStripController
 * PURPOSE: Manage file selection, preview strip, and backend upload routing.
 * SAFE: comment only
 *
 * Responsibilities:
 * - Display selected files in a horizontal strip
 * - Provide remove(), clear(), and upload() helpers
 * - Forward files to ProviderRouter → ApiMaster
 * - Keep logic simple and UI‑agnostic until XML wiring
 *
 * Notes:
 * - UI wiring happens later (Activity + XML)
 * - No GodMode namespace usage (legacy rule)
 * - Works alongside FileUtils + ProviderRouter
 */
class UploadStripController(
    private val context: Context,
    private val stripContainer: LinearLayout,
    private val uploadButton: ImageButton,
    private val clearButton: ImageButton
) {

    /** BLOCK: Internal file list
     *  PURPOSE: Track selected files before upload.
     *  SAFE: comment only
     */
    private val selectedFiles = mutableListOf<Uri>()

    /**
     * BLOCK: init()
     * PURPOSE: Attach listeners for upload + clear.
     * SAFE: comment only
     */
    init {
        uploadButton.setOnClickListener {
            uploadAll()
        }

        clearButton.setOnClickListener {
            clear()
        }
    }

    /**
     * BLOCK: addFile()
     * PURPOSE: Add file to strip + render preview.
     * SAFE: comment only
     */
    fun addFile(uri: Uri) {
        selectedFiles.add(uri)
        addPreviewItem(uri)
    }

    /**
     * BLOCK: addPreviewItem()
     * PURPOSE: Render a simple text preview for now.
     * SAFE: comment only
     *
     * Notes:
     * - Real UI wiring will replace this with thumbnails/icons
     */
    private fun addPreviewItem(uri: Uri) {
        val name = FileUtils.getFileName(context, uri)

        val preview = TextView(context).apply {
            text = name
            textSize = 14f
            setPadding(12, 8, 12, 8)
        }

        stripContainer.addView(preview)
    }

    /**
     * BLOCK: uploadAll()
     * PURPOSE: Send all selected files to ProviderRouter.
     * SAFE: comment only
     */
    private fun uploadAll() {
        if (selectedFiles.isEmpty()) {
            appendSystemMessage("No files selected.")
            return
        }

        for (uri in selectedFiles) {
            ProviderRouter.sendFile(context, uri)
        }

        appendSystemMessage("Upload complete.")
    }

    /**
     * BLOCK: clear()
     * PURPOSE: Clear strip + file list.
     * SAFE: comment only
     */
    fun clear() {
        selectedFiles.clear()
        stripContainer.removeAllViews()
        appendSystemMessage("Upload strip cleared.")
    }

    /**
     * BLOCK: appendSystemMessage()
     * PURPOSE: Temporary system feedback (UI wiring later).
     * SAFE: comment only
     */
    private fun appendSystemMessage(msg: String) {
        val preview = TextView(context).apply {
            text = msg
            textSize = 13f
            setPadding(12, 8, 12, 8)
        }
        stripContainer.addView(preview)
    }
}

/* ========================================================================
   METADATA FOOTER — UploadStripController.kt
   version: 1.0.0
   local_timestamp: 07/06/2026 11:24 AM EDT
   utc_timestamp: 2026-07-06T15:24:00Z

   ML TAGS
   - ml_tags: ["upload_strip", "file_preview", "provider_bridge", "godmode_core"]

   BLUEPRINT SECTION
   - section: "7.1 — UploadStripController.kt"

   SECTION PURPOSE
   - Manages file selection, preview strip, and backend upload routing.
   - Works with FileUtils + ProviderRouter + ApiMaster.
   - UI wiring handled later in Activity + XML.

   DEPENDENCIES
   - uses: [
       "FileUtils.kt",
       "ProviderRouter.kt",
       "ApiMaster.kt",
       "upload_strip XML (to be created)"
     ]

   NOTES
   - Fully generated to match conformity rules.
   - Non-executable metadata footer.
   - Safe for copy/paste.
   ========================================================================
   END OF FILE :: CHATMULTIAPI :: GODMODE
*/
