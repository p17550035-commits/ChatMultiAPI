// bubbleadapter.kt — v1.0.1

package com.example.chatmultiapi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.chatmultiapi.BubbleModel
import com.example.chatmultiapi.R

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

        if (bubble.text != null) {
            holder.bubbleText.visibility = View.VISIBLE
            holder.bubbleText.text = bubble.text
        } else holder.bubbleText.visibility = View.GONE

        if (bubble.imageUri != null) {
            holder.bubbleImage.visibility = View.VISIBLE
            holder.bubbleImage.setImageURI(bubble.imageUri)
        } else holder.bubbleImage.visibility = View.GONE

        if (bubble.metadataPreview != null) {
            holder.bubbleMeta.visibility = View.VISIBLE
            holder.bubbleMeta.text = bubble.metadataPreview
        } else holder.bubbleMeta.visibility = View.GONE

        when (timestampMode.lowercase()) {
            "local" -> holder.bubbleMeta.append("\n${bubble.localTimestamp}")
            "utc" -> holder.bubbleMeta.append("\n${bubble.utcTimestamp}")
            "both" -> holder.bubbleMeta.append("\n${bubble.localTimestamp} | ${bubble.utcTimestamp}")
        }

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
METADATA :: GODMODE :: chatmultiGot you, Peter — **short, clean fix**.  
Here’s the corrected `bubbleadapter.kt` with **no GodMode package**, **no drift**, **no extra talk**, and **nothing removed**.

---

## ⭐ bubbleadapter.kt — fixed package + version bump (v1.0.1)

```kotlin
// bubbleadapter.kt — v1.0.1

package com.example.chatmultiapi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.chatmultiapi.BubbleModel
import com.example.chatmultiapi.R

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

        if (bubble.text != null) {
            holder.bubbleText.visibility = View.VISIBLE
            holder.bubbleText.text = bubble.text
        } else holder.bubbleText.visibility = View.GONE

        if (bubble.imageUri != null) {
            holder.bubbleImage.visibility = View.VISIBLE
            holder.bubbleImage.setImageURI(bubble.imageUri)
        } else holder.bubbleImage.visibility = View.GONE

        if (bubble.metadataPreview != null) {
            holder.bubbleMeta.visibility = View.VISIBLE
            holder.bubbleMeta.text = bubble.metadataPreview
        } else holder.bubbleMeta.visibility = View.GONE

        when (timestampMode.lowercase()) {
            "local" -> holder.bubbleMeta.append("\n${bubble.localTimestamp}")
            "utc" -> holder.bubbleMeta.append("\n${bubble.utcTimestamp}")
            "both" -> holder.bubbleMeta.append("\n${bubble.localTimestamp} | ${bubble.utcTimestamp}")
        }

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
version: 1.0.1
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
