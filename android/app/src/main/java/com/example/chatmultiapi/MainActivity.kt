package com.example.chatmultiapi

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chaquo.python.PyObject
import com.chaquo.python.Python
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * BLOCK: ChatBubble Data Model
 * PURPOSE: Represents a single chat bubble (user or agent)
 * SAFE: comment only
 */
data class ChatBubble(
    val sender: String,   // "user" or "agent"
    val text: String,
    val timestamp: Long
)

/**
 * BLOCK: ChatAdapter
 * PURPOSE: RecyclerView adapter for chat bubbles
 * SAFE: comment only
 */
class ChatAdapter(
    private val context: Context,
    private val items: MutableList<ChatBubble>,
    private val onSaveBubble: (ChatBubble) -> Unit
) : RecyclerView.Adapter<ChatAdapter.BubbleViewHolder>() {

    inner class BubbleViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val text: TextView = view.findViewById(R.id.bubbleText)
        val image: ImageView = view.findViewById(R.id.bubbleImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BubbleViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_bubble, parent, false)
        return BubbleViewHolder(view)
    }

    override fun onBindViewHolder(holder: BubbleViewHolder, position: Int) {
        val bubble = items[position]
        val content = bubble.text

        val isUserImage = content.startsWith("[IMAGE]")
        val isApiImage = content.startsWith("[API_IMAGE]")

        if (isUserImage || isApiImage) {
            holder.text.visibility = View.GONE
            holder.image.visibility = View.VISIBLE

            val path = content.substringAfter("] ").trim()

            try {
                if (path.startsWith("http")) {
                    Glide.with(context).load(path).into(holder.image)
                } else {
                    val uri = Uri.parse(path)
                    holder.image.setImageURI(uri)
                }
            } catch (e: Exception) {
                holder.image.setImageResource(android.R.drawable.ic_menu_report_image)
            }

            holder.image.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse(path)
                    flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                }
                context.startActivity(intent)
            }

            holder.image.setOnLongClickListener {
                onSaveBubble(bubble)
                true
            }

        } else {
            holder.image.visibility = View.GONE
            holder.text.visibility = View.VISIBLE

            holder.text.text = content
            holder.text.isSelected = bubble.sender == "user"

            holder.text.setOnClickListener {
                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                clipboard.setPrimaryClip(ClipData.newPlainText("bubble", bubble.text))
                Toast.makeText(context, "Copied bubble", Toast.LENGTH_SHORT).show()
            }

            holder.text.setOnLongClickListener {
                onSaveBubble(bubble)
                true
            }
        }
    }

    override fun getItemCount(): Int = items.size
}

/**
 * BLOCK: MainActivity
 * PURPOSE: Primary chat UI controller for GodMode
 * SAFE: comment only
 */
class MainActivity : AppCompatActivity() {

    private lateinit var chatRecycler: RecyclerView
    private lateinit var inputText: EditText
    private lateinit var btnSend: ImageButton
    private lateinit var btnVoice: ImageButton
    private lateinit var btnFile: ImageButton
    private lateinit var btnSettings: ImageButton

    private val chatList = mutableListOf<ChatBubble>()
    private lateinit var adapter: ChatAdapter

    /**
     * BLOCK: Voice Input Launcher
     * PURPOSE: Handle speech-to-text input
     * SAFE: comment only
     */
    private val voiceLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val matches = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            val text = matches?.firstOrNull()
            if (!text.isNullOrEmpty()) {
                inputText.setText(text)
            }
        }
    }

    /**
     * BLOCK: File Picker Launcher
     * PURPOSE: Handle file selection
     * SAFE: comment only
     */
    private val fileLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val uri = result.data?.data
            if (uri != null) {
                Toast.makeText(this, "File selected: $uri", Toast.LENGTH_SHORT).show()
                sendMessage("[IMAGE] $uri")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        chatRecycler = findViewById(R.id.chatRecycler)
        inputText = findViewById(R.id.inputText)
        btnSend = findViewById(R.id.btnSend)
        btnVoice = findViewById(R.id.btnVoice)
        btnFile = findViewById(R.id.btnFile)
        btnSettings = findViewById(R.id.btnSettings)

        adapter = ChatAdapter(this, chatList) { bubble -> saveBubble(bubble) }
        chatRecycler.layoutManager = LinearLayoutManager(this)
        chatRecycler.adapter = adapter

        btnSend.setOnClickListener { sendMessage(inputText.text.toString()) }
        btnVoice.setOnClickListener { startVoiceInput() }
        btnFile.setOnClickListener { openFilePicker() }
        btnSettings.setOnClickListener { openSettings() }
    }

    /**
     * BLOCK: Voice Input
     * PURPOSE: Launch speech recognizer
     * SAFE: comment only
     */
    private fun startVoiceInput() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        }
        voiceLauncher.launch(intent)
    }

    /**
     * BLOCK: File Picker
     * PURPOSE: Launch file chooser
     * SAFE: comment only
     */
    private fun openFilePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "*/*"
        }
        fileLauncher.launch(intent)
    }

    /**
     * BLOCK: Open Settings
     * PURPOSE: Navigate to ProviderSettingsActivity
     * SAFE: comment only
     */
    private fun openSettings() {
        val intent = Intent(this, ProviderSettingsActivity::class.java)
        startActivity(intent)
    }

    /**
     * BLOCK: Send Message
     * PURPOSE: Add user bubble → call Python backend → add agent bubble
     * SAFE: comment only
     */
    private fun sendMessage(text: String) {
        if (text.isBlank()) return

        val bubble = ChatBubble("user", text, System.currentTimeMillis())
        chatList.add(bubble)
        adapter.notifyItemInserted(chatList.size - 1)
        chatRecycler.scrollToPosition(chatList.size - 1)

        inputText.setText("")

        val py = Python.getInstance()
        val backend = py.getModule("backend")

        val response: PyObject = backend.callAttr("handle_message", text)
        val reply = response.toString()

        val agentBubble = ChatBubble("agent", reply, System.currentTimeMillis())
        chatList.add(agentBubble)
        adapter.notifyItemInserted(chatList.size - 1)
        chatRecycler.scrollToPosition(chatList.size - 1)
    }

    /**
     * BLOCK: Save Bubble
     * PURPOSE: Write bubble to internal storage
     * SAFE: comment only
     */
    private fun saveBubble(bubble: ChatBubble) {
        val sdf = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US)
        val filename = "bubble_${sdf.format(Date())}.txt"

        val file = File(filesDir, filename)
        file.writeText("${bubble.sender}: ${bubble.text}")

        Toast.makeText(this, "Saved bubble to $filename", Toast.LENGTH_SHORT).show()
    }
}

/* ========================================================================
   METADATA FOOTER — MainActivity.kt
   version: 1.0.0
   local_timestamp: 07/06/2026 10:42 AM EDT
   utc_timestamp: 2026-07-06T14:42:00Z

   ML TAGS
   - ml_tags: ["kotlin_activity", "chat_ui", "python_bridge", "godmode_core"]

   BLUEPRINT SECTION
   - section: "2.0 — MainActivity.kt"

   SECTION PURPOSE
   - Primary chat UI controller for GodMode.
   - Handles RecyclerView, voice input, file picker, and Python backend calls.
   - Interfaces directly with activity_main.xml and item_bubble.xml.

   DEPENDENCIES
   - uses: [
       "backend.py",
       "item_bubble.xml",
       "activity_main.xml",
       "ProviderSettingsActivity.kt",
       "ChatAdapter.kt"
     ]

   NOTES
   - Fully regenerated to restore conformity.
   - Non-executable metadata footer.
   - Safe for copy/paste.
   ========================================================================
   END OF FILE :: CHATMULTIAPI :: GODMODE
*/
