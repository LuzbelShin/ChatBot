package valenzuela.carlos.chatbot.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*
import valenzuela.carlos.chatbot.R
import valenzuela.carlos.chatbot.utils.BotResponse
import valenzuela.carlos.chatbot.utils.Constans
import valenzuela.carlos.chatbot.utils.Constans.OPEN_GOOGLE
import valenzuela.carlos.chatbot.utils.Constans.OPEN_SEARCH
import valenzuela.carlos.chatbot.utils.Time

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"

    var messagesList = mutableListOf<Message>()

    private lateinit var adapter: MessangingAdapter
    private val botList = listOf("Lazaro", "Meimei", "Neko", "Mori")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView()

        clickEvents()

        val random =(0..3).random()
        customBotMessage("Hello! Today you're speaking with ${botList[random]}, how may I help you today?")
    }

    private fun clickEvents(){
        val send = findViewById<Button>(R.id.send)
        val message = findViewById<EditText>(R.id.message)
        val rvMessages = findViewById<RecyclerView>(R.id.rv_messages)
        send.setOnClickListener {
            sendMessage()
        }

        message.setOnClickListener {
            GlobalScope.launch {
                delay(100)
                withContext(Dispatchers.Main){
                    rvMessages.scrollToPosition(adapter.itemCount - 1)
                }
            }
        }
    }

    private fun recyclerView(){
        adapter = MessangingAdapter()
        val rvMessages = findViewById<RecyclerView>(R.id.rv_messages)
        rvMessages.adapter = adapter
        rvMessages.layoutManager = LinearLayoutManager(applicationContext)
    }

    override fun onStart(){
        super.onStart()
        val rvMessages = findViewById<RecyclerView>(R.id.rv_messages)

        GlobalScope.launch {
            delay(100)
            withContext(Dispatchers.Main){
                rvMessages.scrollToPosition(adapter.itemCount - 1)
            }
        }
    }

    private fun sendMessage(){
        val messages = findViewById<EditText>(R.id.message)
        val rvMessages = findViewById<RecyclerView>(R.id.rv_messages)

        val message = messages.text.toString()
        val timeStamp = Time.timeStamp()

        if(message.isNotEmpty()){
            //messagesList.add(Message(message, Constans.SEND_ID, timeStamp))
            messages.setText("")

            adapter.insertMessage(valenzuela.carlos.chatbot.data.Message(message, Constans.SEND_ID, timeStamp))
            rvMessages.scrollToPosition(adapter.itemCount - 1)
            botResponse(message)
        }
    }

    private fun botResponse(message: String) {
        val timeStamp = Time.timeStamp()
        val rvMessages = findViewById<RecyclerView>(R.id.rv_messages)

        GlobalScope.launch {
            //Fake response delay
            delay(1000)
            withContext(Dispatchers.Main) {
                //Gets the response
                val response = BotResponse.basicResponse(message)

                //Adds it to our local list
                //messagesList.add(valenzuela.carlos.chatbot.data.Message(response, Constans.RECEIVE_ID, timeStamp))

                //Inserts our message into the adapter
                adapter.insertMessage(valenzuela.carlos.chatbot.data.Message(response, Constans.RECEIVE_ID, timeStamp))

                //Scrolls us to the position of the latest message
                rvMessages.scrollToPosition(adapter.itemCount - 1)
//Starts Google
                when (response) {
                    OPEN_GOOGLE -> {
                        val site = Intent(Intent.ACTION_VIEW)
                        site.data = Uri.parse("https://www.google.com/")
                        startActivity(site)
                    }
                    OPEN_SEARCH -> {
                        val site = Intent(Intent.ACTION_VIEW)
                        val searchTerm: String? = message.substringAfterLast("search")
                        site.data = Uri.parse("https://www.google.com/search?&q=$searchTerm")
                        startActivity(site)
                    }

                    }
                }
            }
        }

    private fun customBotMessage(message: String){
        val rvMessages = findViewById<RecyclerView>(R.id.rv_messages)
        GlobalScope.launch {
            delay(1000)
            withContext(Dispatchers.Main) {
                val timeStamp = Time.timeStamp()
                //messagesList.add(valenzuela.carlos.chatbot.data.Message(message, Constans.RECEIVE_ID, timeStamp))
                adapter.insertMessage(valenzuela.carlos.chatbot.data.Message(message, Constans.RECEIVE_ID, timeStamp))

                rvMessages.scrollToPosition(adapter.itemCount - 1)
            }
        }
    }
}