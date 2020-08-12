package com.geurimsoft.gmessenger.view.chat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.geurimsoft.gmessenger.R;
import com.geurimsoft.gmessenger.service.ChatConnection;
import com.geurimsoft.gmessenger.service.ChatConnectionService;

import co.intentservice.chatui.ChatView;
import co.intentservice.chatui.models.ChatMessage;

public class ChatActivity extends AppCompatActivity {
    private BroadcastReceiver mBroadcastReceiver;
    private String contactJid;
    private ChatView chat_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        chat_view = findViewById(R.id.chat_view);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = getIntent();
        contactJid = intent.getStringExtra("ID");
        Log.d("DEBUG", "conJid : " + contactJid);
        setTitle(contactJid);

        chat_view.setOnSentMessageListener(new ChatView.OnSentMessageListener() {
            @Override
            public boolean sendMessage(ChatMessage chatMessage) {
                String mName = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("xmpp_mId", null);

                if(ChatConnectionService.getState() == ChatConnection.ConnectionState.CONNECTED) {
                    Intent i1 = new Intent(ChatConnectionService.SEND_MESSAGE);
                    i1.putExtra(ChatConnectionService.BUNDLE_MESSAGE_BODY, chat_view.getTypedMessage());
                    i1.putExtra(ChatConnectionService.BUNDLE_TO, contactJid);
                    i1.putExtra(ChatConnectionService.BUNDLE_FROM, mName);

                    sendBroadcast(i1);
                }
                else {
                    Toast.makeText(getApplicationContext(), "Client not connected to server ,Message not sent!", Toast.LENGTH_LONG).show();
                }
                return true;
            }
        });

        // 메시지 수신
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                switch(action) {
                    case ChatConnectionService.NEW_MESSAGE:
                        String from = intent.getStringExtra(ChatConnectionService.BUNDLE_FROM);
                        String body = intent.getStringExtra(ChatConnectionService.BUNDLE_MESSAGE_BODY);

                        if(from.equals(contactJid)) {
                            Log.d("DEBUG", "ChatActivity : onReceive if");
//                            chat_view.receiveMessage(body);
                            ChatMessage chatMessage = new ChatMessage( body, System.currentTimeMillis(), ChatMessage.Type.RECEIVED);
                            chat_view.addMessage(chatMessage);
                        }
                        else {
                            Log.d("DEBUG", "ChatActivity : onReceive else");
                        }
                        return;
                }
            }
        };
        IntentFilter filter = new IntentFilter(ChatConnectionService.NEW_MESSAGE);
        registerReceiver(mBroadcastReceiver, filter);
    }
}