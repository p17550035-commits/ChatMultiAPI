package com.example.chatmultiapi

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class ChatActivity : AppCompatActivity() {

    private lateinit var chatListView: ListView
    private lateinit var chatInput: EditText
    private lateinit var sendBtn: Button
    private lateinit var attachBtn: Button

    private val messages = mutableListOf<ChatMessage>()
    private lateinit var adapter: ChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        // Neon theme background (chat zone only)
        window.decorView.setBackgroundColor(
            ContextCompat.getColor(this, R.color.neonBackground)
        )

        chatListView = findViewById(R.id.chatListView)
        chatInput = findViewById(R.id.chatInput)
        sendBtn = findViewById(R.id.sendBtn)
        attachBtn = findViewById(R.id.attachBtn)

        adapter = ChatAdapter(this, messages)
        chatListView.adapter = adapter

        // Send message
        sendBtn.setOnClickListener {
            val text = chatInput.text.toString().trim()
            if (text.isNotEmpty()) {
                addMessage(ChatMessage(text, false))
                chatInput.setText("")
                sendToProvider(text)
            }
        }

        // Attach file
        attachBtn.setOnClickListener {
            pickFile()
        }
    }

    private fun addMessage(msg: ChatMessage) {
        messages.add(msg)
        adapter.notifyDataSetChanged()
        chatListView.smoothScrollToPosition(messages.size - 1)
    }

    private fun sendToProvider(text: String) {
        // Placeholder routing — real routing added later
        val reply = ProviderRouter.sendMessage(this, text)
        addMessage(ChatMessage(reply, true))
    }

    private fun pickFile() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        startActivityForResult(intent, 777)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 777 && resultCode == Activity.RESULT_OK) {
            val uri: Uri? = data?.data
            if (uri != null) {
                val fileName = FileUtils.getFileName(this, uri)
                addMessage(ChatMessage("Attached: $fileName", false))
                ProviderRouter.sendFile(this, uri)
            }
        }
    }
}
