package com.example.openfiresmack.view.chat

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import co.intentservice.chatui.models.ChatMessage
import com.example.openfiresmack.R
import com.example.openfiresmack.service.ChatConnection
import com.example.openfiresmack.service.ChatConnectionService
import kotlinx.android.synthetic.main.activity_chat.*


class ChatActivity : AppCompatActivity() {
    private var mBroadcastReceiver: BroadcastReceiver? = null
    private var contactJid: String? = null

    companion object {
        lateinit var cId:String
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val intent:Intent = intent
        val mId = intent.getStringExtra("ID")
        cId = mId!!
        this.title = mId

        //보내기버튼 클릭
        chat_view.setOnSentMessageListener {
            // perform actual message sending
            if(ChatConnectionService.getState() == ChatConnection.ConnectionState.CONNECTED) {
                Log.d("DEBUG", "ChatActivity : The client is connected to the server,Sending Message")
                //Send Message to Server

                val intent = Intent(ChatConnectionService.SEND_MESSAGE)
                intent.putExtra(ChatConnectionService.BUNDLE_MESSAGE_BODY, chat_view.typedMessage)      // chat_view.get
                intent.putExtra(ChatConnectionService.BUNDLE_TO, contactJid)

                sendBroadcast(intent)

                chat_view.addMessage(it)
            } else {
                Log.d("DEBUG", "ChatActivity : The client is disconnected to the server")
                Toast.makeText(getApplicationContext(),
                    "Client not connected to server ,Message not sent!",
                    Toast.LENGTH_LONG).show();
            }
            true
        }

        contactJid = mId
        Log.d("DEBUG", "ChatActivity : contactJid : $contactJid")
//        title = contactJid
        Log.d("DEBUG", "ChatActivity : onCreate END")
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(mBroadcastReceiver)
    }

    override fun onResume() {
        super.onResume()
        Log.d("DEBUG", "ChatActivity : onResume START")
        mBroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val action = intent.action
                when (action) {
                    ChatConnectionService.NEW_MESSAGE -> {
                        Log.d("DEBUG", "ChatActivity : when START")
                        val from =
                            intent.getStringExtra(ChatConnectionService.BUNDLE_FROM)
                        val body =
                            intent.getStringExtra(ChatConnectionService.BUNDLE_MESSAGE_BODY)
                        if (from == contactJid) {
                            Log.d("DEBUG", "ChatActivity : when.if")
                            val chatMessage = ChatMessage(
                                body,
                                System.currentTimeMillis(),
                                ChatMessage.Type.RECEIVED
                            )
                            chat_view.addMessage(chatMessage)
                        } else {
                            Log.d("DEBUG", "ChatActivity : when.else")
                            Log.d("DEBUG", "ChatActivity : Got a message from jid :$from")
                        }
                        return
                    }
                }
            }
        }
        val filter = IntentFilter(ChatConnectionService.NEW_MESSAGE)
        this.registerReceiver(mBroadcastReceiver, filter)

        Log.d("DEBUG", "ChatActivity : onResume END")
    }
}