// chatactivity.kt — v1.0.0

package com.example.chatmultiapi

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
import com.example.chatmultiapi.R
import com.example.chatmultiapi.BubbleAdapter
import com.example.chatmultiapi.BubbleModel
import com.example.chatmultiapi.ProviderRouter
import com.example.chatmultiapi.FileUtils
import com.example.chatmultiapi.TimestampEngine
import java.util.UUID

class ChatActivity : AppCompatActivity() {

    // [UI VARS] declare UI elements + adapters
    private lateinit var bubbleRecycler: RecyclerView
    private lateinit var bubbleAdapter: BubbleAdapter
    private lateinit var inputField: EditText
    private lateinit var sendButton: ImageButton
    private lateinit var attachButton: ImageButton

    // [STATE] bubble list + timestamp mode
    private val bubbleList = mutableListOf<BubbleModel>()
    private var timestampMode: String = "local"

    // [CONST] file picker request code
    private val FILE_PICK_REQUEST = 777

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        // [THEME] apply neon background
        window.decorView.setBackgroundColor(
            ContextCompat.getColor(this, R.color.neonBackground)
        )

        // [UI INIT] bind UI elements
        bubbleRecycler = findViewById(R.id.chatRecycler)
        inputField = findViewById(R.id.chatInput)
        sendButton = findViewById(R.id.chatSend)
        attachButton = findViewById(R.id.chatAttach)

        // [ADAPTER INIT] setup RecyclerView + adapter
        bubbleAdapter = BubbleAdapter(bubbleList, timestampMode)
        bubbleRecycler.layoutManager = LinearLayoutManager(this)
        bubbleRecycler.adapter = bubbleAdapter

        // [SEND FLOW] send button → create bubble + backend call
        sendButton.setOnClickListener {
            val text = inputField.text.toString().trim()
            if (text.isNotEmpty()) {
                addUserBubble(text)
                inputField.setText("")
                sendToProvider(text)
            }
        }

        // [ATTACH FLOW] attach button → file picker
        attachButton.setOnClickListener {
            pickFile()
        }
    }

    // [BUBBLE USER] create user bubble
    private fun addUserBubble(text: String) {
        val bubble = BubbleModel(
            id = UUID.randomUUID().toString(),
            role = "user",
            text = text,
            imageUri = null,
            localTimestamp = TimestampEngine.nowLocal(),
            utcTimestamp = TimestampEngine.nowUtc(),
            metadataPreview = null,
            dependencies = listOf("bubbleadapter.kt", "item_bubble.xml", "chatactivity.kt"),
            mlTags = listOf("user_message", "chat_bubble", "godmode_core")
        )
        bubbleAdapter.addBubble(bubble)
        bubbleRecycler.scrollToPosition(bubbleList.size - 1)
    }

    // [BUBBLE ASSISTANT] create assistant bubble
    private fun addAssistantBubble(text: String) {
        val bubble = BubbleModel(
            id = UUID.randomUUID().toString(),
            role = "assistant",
            text = text,
            imageUri = null,
            localTimestamp = TimestampEngine.nowLocal(),
            utcTimestamp = TimestampEngine.nowUtc(),
            metadataPreview = null,
            dependencies = listOf("bubbleadapter.kt", "item_bubble.xml", "chatactivity.kt"),
            mlTags = listOf("assistant_message", "chat_bubble", "godmode_core")
        )
        bubbleAdapter.addBubble(bubble)
        bubbleRecycler.scrollToPosition(bubbleList.size - 1)
    }

    // [BUBBLE FILE] create file bubble
    private fun addFileBubble(fileName: String, uri: Uri) {
        val bubble = BubbleModel(
            id = UUID.randomUUID().toString(),
            role = "user",
            text = "Attached: $fileName",
            imageUri = uri,
            localTimestamp = TimestampEngine.nowLocal(),
            utcTimestamp = TimestampEngine.nowUtc(),
            metadataPreview = null,
            dependencies = listOf("bubbleadapter.kt", "item_bubble.xml", "chatactivity.kt"),
            mlTags = listOf("file_attachment", "chat_bubble", "godmode_core")
        )
        bubbleAdapter.addBubble(bubble)
        bubbleRecycler.scrollToPosition(bubbleList.size - 1)
    }

    // [BACKEND] send text to provider + create assistant bubble
    private fun sendToProvider(text: String) {
        val reply = ProviderRouter.sendMessage(this, text)
        addAssistantBubble(reply)
    }

    // [PICKER] launch file picker
    private fun pickFile() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        startActivityForResult(intent, FILE_PICK_REQUEST)
    }

    // [RESULT] handle file picker result
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
}

/*
================================================================================
METADATA :: GODMODE :: chatmultiapi
section: 2.5 — ChatActivity.kt
version: 1.0.0
origin: chatactivity.kt
mode: embedded editor mode

local timestamp: 07/04/2026 09:49 EDT
utc timestamp: 2026-07-04T13:49:00Z

dependencies:
- activity_chat.xml
- bubbleadapter.kt
- bubblemodel.kt
- providerrouter.kt
- fileutils.kt
- timestampengine.kt

ml tags:
- ui_controller
- chat_screen
- bubble_system
- backend_bridge
- file_attachment
- timestamp_engine
- godmode_core
- v1_ruleset

end of file :: godmode :: chatmultiapi
================================================================================
