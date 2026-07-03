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
- settingsactivity.kt

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
*/


// chatactivity.kt — v1.0.0

package com.chatmultiapi.godmode.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chatmultiapi.godmode.R
import com.chatmultiapi.godmode.adapter.BubbleAdapter
import com.chatmultiapi.godmode.model.BubbleModel
import com.chatmultiapi.godmode.backend.ProviderRouter
import com.chatmultiapi.godmode.util.FileUtils
import java.util.UUID

/**
 * chatactivity.kt — Main chat screen logic (Section 2.5)
 * ------------------------------------------------------
 * Purpose:
 * Merge old ChatActivity behavior (ProviderRouter + file attach)
 * with new BubbleModel/BubbleAdapter + RecyclerView architecture.
 */
class ChatActivity : AppCompatActivity() {

    private lateinit var bubbleRecycler: RecyclerView
    private lateinit var bubbleAdapter: BubbleAdapter
    private lateinit var inputField: EditText
    private lateinit var sendButton: ImageButton
    private lateinit var attachButton: ImageButton

    private val bubbleList = mutableListOf<BubbleModel>()
    private var timestampMode: String = "local"

    private val FILE_PICK_REQUEST = 777

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        // neon background (chat zone)
        window.decorView.setBackgroundColor(
            ContextCompat.getColor(this, R.color.neonBackground)
        )

        bubbleRecycler = findViewById(R.id.chatRecycler)
        inputField = findViewById(R.id.chatInput)
        sendButton = findViewById(R.id.chatSend)
        attachButton = findViewById(R.id.chatAttach)

        bubbleAdapter = BubbleAdapter(bubbleList, timestampMode)
        bubbleRecycler.layoutManager = LinearLayoutManager(this)
        bubbleRecycler.adapter = bubbleAdapter

        sendButton.setOnClickListener {
            val text = inputField.text.toString().trim()
            if (text.isNotEmpty()) {
                addUserBubble(text)
                inputField.setText("")
                sendToProvider(text)
            }
        }

        attachButton.setOnClickListener {
            pickFile()
        }
    }

    private fun addUserBubble(text: String) {
        val bubble = BubbleModel(
            id = UUID.randomUUID().toString(),
            role = "user",
            text = text,
            imageUri = null,
            localTimestamp = generateLocalTimestamp(),
            utcTimestamp = generateUtcTimestamp(),
            metadataPreview = null,
            dependencies = listOf("bubbleadapter.kt", "item_bubble.xml", "chatactivity.kt"),
            mlTags = listOf("user_message", "chat_bubble", "godmode_core")
        )
        bubbleAdapter.addBubble(bubble)
        bubbleRecycler.scrollToPosition(bubbleList.size - 1)
    }

    private fun addAssistantBubble(text: String) {
        val bubble = BubbleModel(
            id = UUID.randomUUID().toString(),
            role = "assistant",
            text = text,
            imageUri = null,
            localTimestamp = generateLocalTimestamp(),
            utcTimestamp = generateUtcTimestamp(),
            metadataPreview = null,
            dependencies = listOf("bubbleadapter.kt", "item_bubble.xml", "chatactivity.kt"),
            mlTags = listOf("assistant_message", "chat_bubble", "godmode_core")
        )
        bubbleAdapter.addBubble(bubble)
        bubbleRecycler.scrollToPosition(bubbleList.size - 1)
    }

    private fun addFileBubble(fileName: String, uri: Uri) {
        val bubble = BubbleModel(
            id = UUID.randomUUID().toString(),
            role = "user",
            text = "Attached: $fileName",
            imageUri = uri,
            localTimestamp = generateLocalTimestamp(),
            utcTimestamp = generateUtcTimestamp(),
            metadataPreview = null,
            dependencies = listOf("bubbleadapter.kt", "item_bubble.xml", "chatactivity.kt"),
            mlTags = listOf("file_attachment", "chat_bubble", "godmode_core")
        )
        bubbleAdapter.addBubble(bubble)
        bubbleRecycler.scrollToPosition(bubbleList.size - 1)
    }

    private fun sendToProvider(text: String) {
        val reply = ProviderRouter.sendMessage(this, text)
        addAssistantBubble(reply)
    }

    private fun pickFile() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        startActivityForResult(intent, FILE_PICK_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == FILE_PICK_REQUEST && resultCode == Activity.RESULT_OK) {
            val uri: Uri? = data?.data
            if (uri != null) {
                val fileName = FileUtils.getFileName(this, uri)
                addFileBubble(fileName, uri)
                ProviderRouter.sendFile(this, uri)
            }
        }
    }

    // placeholder timestamp generators (to be replaced by TimestampEngine)
    private fun generateLocalTimestamp(): String {
        return "07/03/2026 09:20 EDT"
    }

    private fun generateUtcTimestamp(): String {
        return "2026-07-03T13:20:00Z"
    }
}

/*
================================================================================
METADATA :: GODMODE :: chatmultiapi
section: 2.5 chatactivity.kt (chat ui — main logic, merged old/new)
version: 1.0.0
origin: chatactivity.kt
mode: embedded editor mode

dependencies:
- activity_chat.xml
- bubbleadapter.kt
- bubblemodel.kt
- providerrouter.kt
- fileutils.kt
- settingsactivity.kt

ml tags:
- ui_controller
- chat_screen
- bubble_system
- backend_bridge
- file_attachment
- godmode_core
- v1_ruleset

end of file :: godmode :: chatmultiapi
================================================================================
