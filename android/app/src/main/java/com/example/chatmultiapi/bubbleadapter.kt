package com.chatmultiapi.godmode.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.chatmultiapi.godmode.model.BubbleModel
import com.chatmultiapi.godmode.R

/**
 * bubbleadapter.kt — RecyclerView adapter for chat bubbles (Blueprint Section 2.4)
 * --------------------------------------------------------------------------------
 * Purpose:
 * Bind BubbleModel data into item_bubble.xml views.
 *
 * Responsibilities:
 * - Inflate bubble layout
 * - Bind text, image, metadata preview
 * - Apply role-based styling (user vs assistant)
 * - Apply timestamp visibility rules (local / utc / both)
 * - Keep logic simple, predictable, and stable
 *
 * Dependencies:
 * - item_bubble.xml
 * - BubbleModel.kt
 * - ChatActivity.kt
 *
 * Notes:
 * - No business logic here — only UI binding.
 * - All metadata/timestamp decisions come from BubbleModel + SettingsActivity.
 */
class BubbleAdapter(
    private val bubbles: MutableList<BubbleModel>,
    private val timestampMode: String // "local", "utc", "both"
) : RecyclerView.Adapter<BubbleAdapter.BubbleViewHolder>() {

    /**
     * ViewHolder — holds references to bubble views
     */
    class BubbleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bubbleText: TextView = itemView.findViewById(R.id.bubbletext)
        val bubbleImage: ImageView = itemView.findViewById(R.id.bubbleimage)
        val bubbleMeta: TextView = itemView.findViewById(R.id.bubblemeta)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BubbleViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_bubble, parent, false)
        return BubbleViewHolder(view)
    }

    override fun onBindViewHolder(holder: BubbleViewHolder, position: Int) {
        val bubble = bubbles[position]

        // -----------------------------
        // TEXT BUBBLE HANDLING
        // -----------------------------
        if (bubble.text != null) {
            holder.bubbleText.visibility = View.VISIBLE
            holder.bubbleText.text = bubble.text
        } else {
            holder.bubbleText.visibility = View.GONE
        }

        // -----------------------------
        // IMAGE BUBBLE HANDLING
        // -----------------------------
        if (bubble.imageUri != null) {
            holder.bubbleImage.visibility = View.VISIBLE
            holder.bubbleImage.setImageURI(bubble.imageUri)
        } else {
            holder.bubbleImage.visibility = View.GONE
        }

        // -----------------------------
        // METADATA PREVIEW HANDLING
        // -----------------------------
        if (bubble.metadataPreview != null) {
            holder.bubbleMeta.visibility = View.VISIBLE
            holder.bubbleMeta.text = bubble.metadataPreview
        } else {
            holder.bubbleMeta.visibility = View.GONE
        }

        // -----------------------------
        // TIMESTAMP MODE HANDLING
        // -----------------------------
        when (timestampMode.lowercase()) {
            "local" -> {
                holder.bubbleMeta.append("\n${bubble.localTimestamp}")
            }
            "utc" -> {
                holder.bubbleMeta.append("\n${bubble.utcTimestamp}")
            }
            "both" -> {
                holder.bubbleMeta.append("\n${bubble.localTimestamp} | ${bubble.utcTimestamp}")
            }
        }

        // -----------------------------
        // ROLE-BASED STYLING
        // -----------------------------
        when (bubble.role.lowercase()) {
            "user" -> {
                holder.bubbleText.setBackgroundResource(R.drawable.bubble_user)
                holder.bubbleImage.setBackgroundResource(R.drawable.bubble_user)
            }
            "assistant" -> {
                holder.bubbleText.setBackgroundResource(R.drawable.bubble_assistant)
                holder.bubbleImage.setBackgroundResource(R.drawable.bubble_assistant)
            }
        }
    }

    override fun getItemCount(): Int = bubbles.size

    /**
     * Add bubble to list and refresh UI
     */
    fun addBubble(newBubble: BubbleModel) {
        bubbles.add(newBubble)
        notifyItemInserted(bubbles.size - 1)
    }
}

/*
================================================================================
METADATA :: GODMODE :: chatmultiapi
section: 2.4 bubbleadapter.kt (chat ui — recyclerview adapter)
generated (local): 07/03/2026 @ 09:11 edt
generated (utc stored): 2026-07-03T13:11:00Z
version: 2.0.0
origin: bubbleadapter.kt
mode: embedded editor mode

dependencies:
- item_bubble.xml
- bubblemodel.kt
- chatactivity.kt
- settingsactivity.kt

ml tags:
- ui_adapter
- chat_bubble
- recyclerview
- timestamp_engine
- metadata_system
- godmode_core
- v2_ruleset

timestamp rules:
- local: mm/dd/yyyy hh:mm edt
- utc: always stored, hidden unless toggled
- mode controlled via settingsactivity

redundancy rules:
- lowercase filenames
- descriptors required
- metadata footer required
- full-file regeneration only
- ml tags required
- dependency list required

end of file :: godmode :: chatmultiapi
================================================================================
*/
