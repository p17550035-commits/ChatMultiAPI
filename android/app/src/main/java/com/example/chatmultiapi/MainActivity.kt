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
 * PURPOSE: Primary chat UI controller for GodMode + titanium top bar navigation
 * SAFE: comment only
 */
class MainActivity : AppCompatActivity() {

    private lateinit var chatRecycler: RecyclerView
    private lateinit var inputText: EditText
    private lateinit var btnSend: ImageButton
    private lateinit var btnVoice: ImageButton
    private lateinit var btnFile: ImageButton

    // Top bar buttons
    private lateinit var btnChat: ImageButton
    private lateinit var btnProjects: ImageButton
    private lateinit var btnTerminal: ImageButton
    private lateinit var btnAPI: ImageButton
    private lateinit var btnSecurity: ImageButton
    private lateinit var btnSettings: ImageButton

    private val chatList = mutableListOf<ChatBubble>()
    private lateinit var adapter: ChatAdapter

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

        // ⭐ GLOBAL CRASH LOGGER ACTIVATION
        Thread.setDefaultUncaughtExceptionHandler(CrashLogger(this))

        setContentView(R.layout.activity_main)

        chatRecycler = findViewById(R.id.chatRecycler)
        inputText = findViewById(R.id.inputText)
        btnSend = findViewById(R.id.btnSend)
        btnVoice = findViewById(R.id.btnVoice)
        btnFile = findViewById(R.id.btnFile)

        // Top bar buttons
        btnChat = findViewById(R.id.btnChat)
        btnProjects = findViewById(R.id.btnProjects)
        btnTerminal = findViewById(R.id.btnTerminal)
        btnAPI = findViewById(R.id.btnAPI)
        btnSecurity = findViewById(R.id.btnSecurity)
        btnSettings = findViewById(R.id.btnSettings)

        adapter = ChatAdapter(this, chatList) { bubble -> saveBubble(bubble) }
        chatRecycler.layoutManager = LinearLayoutManager(this)
        chatRecycler.adapter = adapter

        btnSend.setOnClickListener { sendMessage(inputText.text.toString()) }
        btnVoice.setOnClickListener { startVoiceInput() }
        btnFile.setOnClickListener { openFilePicker() }

        // Top bar navigation wiring
        btnChat.setOnClickListener {
            setActiveTab(btnChat)
            // Already on Chat; no navigation needed
        }

        btnProjects.setOnClickListener {
            setActiveTab(btnProjects)
            startActivity(Intent(this, ProjectsActivity::class.java))
        }

        btnTerminal.setOnClickListener {
            setActiveTab(btnTerminal)
            Toast.makeText(this, "Terminal module coming soon.", Toast.LENGTH_SHORT).show()
        }

        btnAPI.setOnClickListener {
            setActiveTab(btnAPI)
            startActivity(Intent(this, ApiKeysActivity::class.java))
        }

        btnSecurity.setOnClickListener {
            setActiveTab(btnSecurity)
            startActivity(Intent(this, SecurityActivity::class.java))
        }

        btnSettings.setOnClickListener {
            setActiveTab(btnSettings)
            startActivity(Intent(this, ProviderSettingsActivity::class.java))
        }

        // Default active tab: Chat
        setActiveTab(btnChat)
    }

    private fun setActiveTab(active: ImageButton) {
        val tabs = listOf(btnChat, btnProjects, btnTerminal, btnAPI, btnSecurity, btnSettings)
        tabs.forEach { button ->
            button.scaleX = 1.0f
            button.scaleY = 1.0f
            button.alpha = 1.0f
        }
        // Neon-style subtle indent effect (no extra resources required)
        active.scaleX = 0.95f
        active.scaleY = 0.95f
        active.alpha = 1.0f
    }

    private fun startVoiceInput() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        }
        voiceLauncher.launch(intent)
    }

    private fun openFilePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "*/*"
        }
        fileLauncher.launch(intent)
    }

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
   local_timestamp: 07/07/2026 06:10 AM EDT
   utc_timestamp: 2026-07-07T10:10:00Z

   ML TAGS
   - ml_tags: [
       "kotlin_activity",
       "chat_ui",
       "python_bridge",
       "godmode_core",
       "crash_logger",
       "titanium_top_bar",
       "tab_navigation"
     ]

   SECTION PURPOSE
   - Primary chat UI controller for GodMode.
   - Includes titanium top bar navigation for Chat, Projects, Terminal (placeholder), API, Security, and Settings.
   - Preserves global crash logger activation and existing chat pipeline.

   NOTES
   - Terminal button is a placeholder; wiring to full TerminalActivity will be added when terminal module is implemented.
   - Safe for full-file replacement in ChatMultiAPI :: GodMode.
   ========================================================================
   END OF FILE :: CHATMULTIAPI :: GODMODE
*/
