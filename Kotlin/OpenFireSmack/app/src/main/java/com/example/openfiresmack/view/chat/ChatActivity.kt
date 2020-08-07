package com.example.openfiresmack.view.chat

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.openfiresmack.R
import com.example.openfiresmack.service.ChatConnection
import com.example.openfiresmack.service.ChatConnectionService
import kotlinx.android.synthetic.main.activity_chat.*


class ChatActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val intent:Intent = intent
        val uId = intent.getStringExtra("ID")
        this.title = uId

        //보내기버튼 클릭
        chat_view.setOnSentMessageListener {
            // perform actual message sending
            if(ChatConnectionService.getState().equals(ChatConnection.ConnectionState.CONNECTED)) {
                Log.d("DEBUG", "The client is connected to the server,Sendint Message")
                //Send MEssage to Server

                val intent = Intent(ChatConnectionService.SEND_MESSAGE)
                intent.putExtra(ChatConnectionService.BUNDLE_MESSAGE_BODY, chat_view.typedMessage)      // chat_view.get
                intent.putExtra(ChatConnectionService.BUNDLE_TO, uId)

                sendBroadcast(intent)

                chat_view.addMessage(it)
            } else {
                Toast.makeText(getApplicationContext(),
                    "Client not connected to server ,Message not sent!",
                    Toast.LENGTH_LONG).show();
            }
            true
        }

    }
}