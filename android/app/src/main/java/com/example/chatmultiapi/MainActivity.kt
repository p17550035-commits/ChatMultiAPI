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
    private lateinit var btnSettings: ImageButton   // NEW

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
        btnSettings = findViewById(R.id.btnSettings)   // NEW

        adapter = ChatAdapter(this, chatList) { bubble
