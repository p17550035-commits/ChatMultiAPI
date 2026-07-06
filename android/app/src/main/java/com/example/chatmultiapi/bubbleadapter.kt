package com.example.chatmultiapi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * BLOCK: BubbleAdapter
 * PURPOSE: RecyclerView adapter for chat bubbles (text, image, metadata).
 * SAFE: comment only
 */
class BubbleAdapter(
    private val bubbles: MutableList<BubbleModel>,
    private val timestampMode: String
) : RecyclerView.Adapter<BubbleAdapter.BubbleViewHolder>() {

    /**
     * BLOCK: BubbleViewHolder
     * PURPOSE: Holds references to bubble UI components.
     * SAFE: comment only
     */
    class BubbleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bubbleText: TextView = itemView.findViewById(R.id.bubbleText)
        val bubbleImage: ImageView = itemView.findViewById(R.id.bubbleImage)
        val bubbleMeta: TextView = itemView.findViewById(R.id.bubbleMeta)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BubbleViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_bubble, parent, false)
        return BubbleViewHolder(view)
    }

    override fun onBindViewHolder(holder: BubbleViewHolder, position: Int) {
        val bubble = bubbles[position]

        // BLOCK: Text bubble
        if (bubble.text != null) {
            holder.bubbleText.visibility = View.VISIBLE
            holder.bubbleText.text = bubble.text
        } else {
            holder.bubbleText.visibility = View.GONE
        }

        // BLOCK: Image bubble
        if (bubble.imageUri != null) {
            holder.bubbleImage.visibility = View.VISIBLE
            holder.bubbleImage.setImageURI(bubble.imageUri)
        } else {
            holder.bubbleImage.visibility = View.GONE
        }

        // BLOCK: Metadata preview
        if (bubble.metadataPreview != null) {
            holder.bubbleMeta.visibility = View.VISIBLE
            holder.bubbleMeta.text = bubble.metadataPreview
        } else {
            holder.bubbleMeta.visibility = View.GONE
        }

        // BLOCK: Timestamp mode
        when (timestampMode.lowercase()) {
            "local" -> holder.bubbleMeta.append("\n${bubble.localTimestamp}")
            "utc" -> holder.bubbleMeta.append("\n${bubble.utcTimestamp}")
            "both" -> holder.bubbleMeta.append("\n${bubble.localTimestamp} | ${bubble.utcTimestamp}")
        }

        // BLOCK: Role-based styling
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
     * BLOCK: addBubble()
     * PURPOSE: Append bubble + notify adapter.
     * SAFE: comment only
     */
    fun addBubble(newBubble: BubbleModel) {
        bubbles.add(newBubble)
        notifyItemInserted(bubbles.size - 1)
    }
}

/* ========================================================================
   METADATA FOOTER — BubbleAdapter.kt
   version: 1.0.0
   local_timestamp: 07/06/2026 11:01 AM EDT
   utc_timestamp: 2026-07-06T15:01:00Z

   ML TAGS
   - ml_tags: ["ui_adapter", "chat_bubble", "recyclerview", "timestamp_engine"]

   BLUEPRINT SECTION
   - section: "2.4 — BubbleAdapter.kt"

   SECTION PURPOSE
   - Binds BubbleModel data into item_bubble.xml.
   - Handles text, image, metadata preview, timestamps, and role styling.
   - Used by ChatActivity and other chat UI controllers.

   DEPENDENCIES
   - uses: [
       "item_bubble.xml",
       "BubbleModel.kt",
       "ChatActivity.kt",
       "SettingsActivity.kt"
     ]

   NOTES
   - Fully regenerated to restore conformity.
   - Non-executable metadata footer.
   - Safe for copy/paste.
   ========================================================================
   END OF FILE :: CHATMULTIAPI
*/
