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
 * chatactivity.kt — merged old + new (Section 2.5)
 * ------------------------------------------------
 * Purpose:
 * Preserve old backend wiring (ProviderRouter + file attach)
 * while upgrading UI to RecyclerView + BubbleAdapter + BubbleModel.
 *
 * This file intentionally contains:
 * - old message flow
 * - old file attach flow
 * - old neon theme
 * - new bubble system
 * - new metadata/timestamp placeholders
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

        // preserve neon theme from legacy version
        window.decorView.setBackgroundColor(
            ContextCompat.getColor(this, R.color.neonBackground)
        )

        // new UI wiring
        bubbleRecycler = findViewById(R.id.chatRecycler)
        inputField = findViewById(R.id.chatInput)
        sendButton = findViewById(R.id.chatSend)
        attachButton = findViewById(R.id.chatAttach)

        bubbleAdapter = BubbleAdapter(bubbleList, timestampMode)
        bubbleRecycler.layoutManager = LinearLayoutManager(this)
        bubbleRecycler.adapter = bubbleAdapter

        // preserve old send flow
        sendButton.setOnClickListener {
            val text = inputField.text.toString().trim()
            if (text.isNotEmpty()) {
                addUserBubble(text)
                inputField.setText("")
                sendToProvider(text)
            }
        }

        // preserve old file attach flow
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

    // preserve old backend routing
    private fun sendToProvider(text: String) {
        val reply = ProviderRouter.sendMessage(this, text)
        addAssistantBubble(reply)
    }

    // preserve old file picker
    private fun pickFile() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        startActivityForResult(intent, FILE_PICK_REQUEST)
    }

    // preserve old file routing
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

    // placeholder timestamps (TimestampEngine v1 not implemented yet)
    private fun generateLocalTimestamp(): String = "07/03/2026 09:34 EDT"
    private fun generateUtcTimestamp(): String = "2026-07-03T13:34:00Z"
}

/*
================================================================================
METADATA :: GODMODE :: chatmultiapi
section: 2.5 chatactivity.kt (merged old/new)
version: 1.0.0
origin: chatactivity.kt
mode: embedded editor mode

dependencies:
- activity_chat.xml
- bubbleadapter.kt
- bubblemodel.kt
- providerrouter.kt
- fileutils.kt

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
