package com.example.chatmultiapi

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.UUID

/**
 * BLOCK: ChatActivity
 * PURPOSE: Main chat controller — bubbles, input, file uploads, terminal overlay.
 * SAFE: comment only
 */
class ChatActivity : AppCompatActivity() {

    // --------------------------------------------------------------------
    // UI ELEMENTS
    // --------------------------------------------------------------------
    private lateinit var bubbleRecycler: RecyclerView
    private lateinit var bubbleAdapter: BubbleAdapter
    private lateinit var inputField: EditText
    private lateinit var sendButton: ImageButton
    private lateinit var attachButton: ImageButton

    // Upload strip UI
    private lateinit var uploadStrip: View
    private lateinit var uploadStripContainer: LinearLayout
    private lateinit var uploadStripClearBtn: ImageButton

    // Mini terminal UI
    private lateinit var miniTerminalPanel: LinearLayout
    private lateinit var miniTerminalOutput: TextView
    private lateinit var miniTerminalInput: EditText
    private lateinit var miniTerminalRunBtn: ImageButton

    // --------------------------------------------------------------------
    // CONTROLLERS
    // --------------------------------------------------------------------
    private lateinit var terminalController: MiniTerminalController
    private lateinit var uploadController: UploadStripController

    // --------------------------------------------------------------------
    // STATE
    // --------------------------------------------------------------------
    private val bubbleList = mutableListOf<BubbleModel>()
    private var timestampMode: String = "local"

    private val FILE_PICK_REQUEST = 777

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        // THEME
        window.decorView.setBackgroundColor(
            ContextCompat.getColor(this, R.color.neonBackground)
        )

        // --------------------------------------------------------------------
        // BIND UI ELEMENTS
        // --------------------------------------------------------------------
        bubbleRecycler = findViewById(R.id.chatRecycler)
        inputField = findViewById(R.id.chatInput)
        sendButton = findViewById(R.id.chatSend)
        attachButton = findViewById(R.id.chatAttach)

        uploadStrip = findViewById(R.id.uploadStrip)
        uploadStripContainer = findViewById(R.id.uploadStripContainer)
        uploadStripClearBtn = findViewById(R.id.uploadStripClearBtn)

        miniTerminalPanel = findViewById(R.id.miniTerminalPanel)
        miniTerminalOutput = findViewById(R.id.miniTerminalOutput)
        miniTerminalInput = findViewById(R.id.miniTerminalInput)
        miniTerminalRunBtn = findViewById(R.id.miniTerminalRunBtn)

        // --------------------------------------------------------------------
        // INITIALIZE CONTROLLERS
        // --------------------------------------------------------------------
        terminalController = MiniTerminalController(
            this,
            miniTerminalOutput,
            miniTerminalInput,
            miniTerminalRunBtn
        )

        uploadController = UploadStripController(
            this,
            uploadStripContainer,
            findViewById(R.id.toolbarUploadBtn),
            uploadStripClearBtn
        )

        // --------------------------------------------------------------------
        // SETUP RECYCLER + ADAPTER
        // --------------------------------------------------------------------
        bubbleAdapter = BubbleAdapter(bubbleList, timestampMode)
        bubbleRecycler.layoutManager = LinearLayoutManager(this)
        bubbleRecycler.adapter = bubbleAdapter

        // --------------------------------------------------------------------
        // SEND BUTTON
        // --------------------------------------------------------------------
        sendButton.setOnClickListener {
            val text = inputField.text.toString().trim()
            if (text.isNotEmpty()) {
                addUserBubble(text)
                inputField.setText("")
                sendToProvider(text)
            }
        }

        // --------------------------------------------------------------------
        // ATTACH BUTTON → FILE PICKER
        // --------------------------------------------------------------------
        attachButton.setOnClickListener {
            pickFile()
        }

        // --------------------------------------------------------------------
        // CLEAR UPLOAD STRIP BUTTON
        // --------------------------------------------------------------------
        uploadStripClearBtn.setOnClickListener {
            uploadController.clear()
            uploadStrip.visibility = View.GONE
            uploadStripClearBtn.visibility = View.GONE
        }
    }

    // --------------------------------------------------------------------
    // BUBBLE CREATION
    // --------------------------------------------------------------------
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

    // --------------------------------------------------------------------
    // BACKEND ROUTING
    // --------------------------------------------------------------------
    private fun sendToProvider(text: String) {
        val reply = ProviderRouter.sendMessage(this, text)
        addAssistantBubble(reply)
    }

    // --------------------------------------------------------------------
    // FILE PICKER
    // --------------------------------------------------------------------
    private fun pickFile() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        startActivityForResult(intent, FILE_PICK_REQUEST)
    }

    // --------------------------------------------------------------------
    // FILE PICKER RESULT
    // --------------------------------------------------------------------
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == FILE_PICK_REQUEST && resultCode == Activity.RESULT_OK) {
            val uri: Uri? = data?.data
            if (uri != null) {

                // Add to upload strip
                uploadController.addFile(uri)
                uploadStrip.visibility = View.VISIBLE
                uploadStripClearBtn.visibility = View.VISIBLE

                // Add bubble + send to provider
                val fileName = FileUtils.getFileName(this, uri)
                addFileBubble(fileName, uri)
                ProviderRouter.sendFile(this, uri)
            }
        }
    }
}

/* ========================================================================
   METADATA FOOTER — ChatActivity.kt
   version: 1.1.0
   local_timestamp: 07/06/2026 11:55 AM EDT
   utc_timestamp: 2026-07-06T15:55:00Z

   ML TAGS
   - ml_tags: [
       "ui_controller", "chat_screen", "bubble_system",
       "backend_bridge", "file_attachment", "upload_strip",
       "terminal_overlay", "timestamp_engine", "godmode_core"
     ]

   BLUEPRINT SECTION
   - section: "2.5 — ChatActivity.kt"

   SECTION PURPOSE
   - Main chat controller: bubbles, input, file uploads, terminal overlay.
   - Wires MiniTerminalController + UploadStripController.
   - Routes text + files to ProviderRouter → ApiMaster.

   DEPENDENCIES
   - uses: [
       "activity_chat.xml",
       "BubbleAdapter.kt",
       "BubbleModel.kt",
       "MiniTerminalController.kt",
       "UploadStripController.kt",
       "ProviderRouter.kt",
       "FileUtils.kt",
       "TimestampEngine.kt"
     ]

   NOTES
   - Fully updated to integrate new controllers.
   - Non-executable metadata footer.
   - Safe for copy/paste.
   ========================================================================
   END OF FILE :: CHATMULTIAPI :: GODMODE
*/
