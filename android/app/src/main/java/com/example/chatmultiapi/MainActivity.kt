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

data class ChatBubble(
    val sender: String, // "user" or "agent"
    val text: String,
    val timestamp: Long
)

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

class MainActivity : AppCompatActivity() {

    private lateinit var chatRecycler: RecyclerView
    private lateinit var inputText: EditText
    private lateinit var btnSend: ImageButton
    private lateinit var btnVoice: ImageButton
    private lateinit var btnFile: ImageButton
    private lateinit var gutsPanel: TextView

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
                gutsPanel.text = "File selected: $uri"
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
        gutsPanel = findViewById(R.id.gutsPanel)

        adapter = ChatAdapter(this, chatList) { bubble ->
            saveBubbleToFile(bubble)
        }

        chatRecycler.layoutManager = LinearLayoutManager(this)
        chatRecycler.adapter = adapter

        btnSend.setOnClickListener {
            val text = inputText.text.toString().trim()
            if (text.isNotEmpty()) {
                sendMessage(text)
                inputText.setText("")
            }
        }

        btnVoice.setOnClickListener {
            startVoiceInput()
        }

        btnFile.setOnClickListener {
            openFilePicker()
        }
    }

    private fun startVoiceInput() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak…")
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
        val now = System.currentTimeMillis()
        val userBubble = ChatBubble("user", text, now)
        chatList.add(userBubble)
        adapter.notifyItemInserted(chatList.size - 1)
        chatRecycler.scrollToPosition(chatList.size - 1)

        gutsPanel.text = "Thinking…"

        try {
            val py = Python.getInstance()
            val module: PyObject = py.getModule("backend")
            val result: PyObject = module.callAttr("handle_message", text)
            val reply = result.toString()

            val agentBubble = ChatBubble("agent", reply, System.currentTimeMillis())
            chatList.add(agentBubble)
            adapter.notifyItemInserted(chatList.size - 1)
            chatRecycler.scrollToPosition(chatList.size - 1)

            gutsPanel.text = "Done."
        } catch (e: Exception) {
            val errorBubble = ChatBubble("agent", "Error: ${e.message}", System.currentTimeMillis())
            chatList.add(errorBubble)
            adapter.notifyItemInserted(chatList.size - 1)
            chatRecycler.scrollToPosition(chatList.size - 1)
            gutsPanel.text = "Error."
        }

        saveChatToFile()
    }

    private fun getDateString(): String {
        val sdf = SimpleDateFormat("MM_dd_yyyy", Locale.getDefault())
        return sdf.format(Date())
    }

    private fun getBaseDir(): File {
        val dir = File(getExternalFilesDir(null), "ChatMultiAPI")
        if (!dir.exists()) dir.mkdirs()
        return dir
    }

    private fun saveChatToFile() {
        try {
            val dir = File(getBaseDir(), "chats")
            if (!dir.exists()) dir.mkdirs()
            val filename = "chat_${getDateString()}.json"
            val file = File(dir, filename)

            val json = buildString {
                append("[\n")
                chatList.forEachIndexed { index, bubble ->
                    append("  {\"sender\":\"${bubble.sender}\",\"text\":")
                    append("\"${bubble.text.replace("\"", "\\\"")}\",\"timestamp\":${bubble.timestamp}}")
                    if (index < chatList.size - 1) append(",")
                    append("\n")
                }
                append("]\n")
            }

            file.writeText(json)
        } catch (_: Exception) {
        }
    }

    private fun saveBubbleToFile(bubble: ChatBubble) {
        try {
            val dir = File(getBaseDir(), "bubbles")
            if (!dir.exists()) dir.mkdirs()
            val filename = "bubble_${getDateString()}.txt"
            val file = File(dir, filename)
            file.writeText(bubble.text)
            Toast.makeText(this, "Bubble saved", Toast.LENGTH_SHORT).show()
        } catch (_: Exception) {
        }
    }
}
