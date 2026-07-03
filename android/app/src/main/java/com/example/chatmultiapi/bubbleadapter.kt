// bubbleadapter.kt — v1.0.0

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
 * bubbleadapter.kt — RecyclerView adapter for chat bubbles (Section 2.4)
 * ----------------------------------------------------------------------
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
 * - bubblemodel.kt
 * - chatactivity.kt
 *
 * Notes:
 * - No business logic here — only UI binding.
 * - All metadata/timestamp decisions come from BubbleModel + SettingsActivity.
 */
class BubbleAdapter(
    private val bubbles: MutableList<BubbleModel>,
    private val timestampMode: String // "local", "utc", "both"
) : RecyclerView.Adapter<BubbleAdapter.BubbleViewHolder>() {

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

        // text
        if (bubble.text != null) {
            holder.bubbleText.visibility = View.VISIBLE
            holder.bubbleText.text = bubble.text
        } else {
            holder.bubbleText.visibility = View.GONE
        }

        // image
        if (bubble.imageUri != null) {
            holder.bubbleImage.visibility = View.VISIBLE
            holder.bubbleImage.setImageURI(bubble.imageUri)
        } else {
            holder.bubbleImage.visibility = View.GONE
        }

        // metadata preview
        if (bubble.metadataPreview != null) {
            holder.bubbleMeta.visibility = View.VISIBLE
            holder.bubbleMeta.text = bubble.metadataPreview
        } else {
            holder.bubbleMeta.visibility = View.GONE
        }

        // timestamps
        when (timestampMode.lowercase()) {
            "local" -> holder.bubbleMeta.append("\n${bubble.localTimestamp}")
            "utc" -> holder.bubbleMeta.append("\n${bubble.utcTimestamp}")
            "both" -> holder.bubbleMeta.append("\n${bubble.localTimestamp} | ${bubble.utcTimestamp}")
        }

        // role styling
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

    fun addBubble(newBubble: BubbleModel) {
        bubbles.add(newBubble)
        notifyItemInserted(bubbles.size - 1)
    }
}

/*
================================================================================
METADATA :: GODMODE :: chatmultiapi
section: 2.4 bubbleadapter.kt (chat ui — recyclerview adapter)
version: 1.0.0
origin: bubbleadapter.kt
mode: embedded editor mode

dependencies:
- item_bubble.xml
- bubblemodel.kt
- chatactivity.kt

ml tags:
- ui_adapter
- chat_bubble
- recyclerview
- timestamp_engine
- metadata_system
- godmode_core
- v1_ruleset

end of file :: godmode :: chatmultiapi
================================================================================
