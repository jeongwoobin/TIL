/**
 * ChatActivity.class
 * 기능 : 채팅 UI 라이브러리를 이용하여 채팅화면 구성
 *        메시지 송신 클릭메소드
 *        메시지 수신 BroadcastReceiver
 */

package com.geurimsoft.gmessenger.view.chat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
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

    /**
     * @func 레이아웃 위젯 연결
     * @param savedInstanceState
     * @return
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Log.d("DEBUG", "ChatActivity : onCreate()");
        chat_view = findViewById(R.id.chat_view);
    }

    /**
     * @func Receiver 등록해제
     * @param
     * @return
     */
    @Override
    protected void onPause() {
        super.onPause();
        Log.d("DEBUG", "ChatActivity : onPause()");
        unregisterReceiver(mBroadcastReceiver);
    }

    /**
     * @func UserListRvAdapter에서 보낸 intentExtra 받아서 액티비티 Title 변경
     *       메시지 송수신 기능
     * @param
     * @return
     */
    @Override
    protected void onResume() {
        super.onResume();
        Log.d("DEBUG", "ChatActivity : onResume()");

        Intent intent = getIntent();
        contactJid = intent.getStringExtra("ID");
        Log.d("DEBUG", "conJid : " + contactJid);
        setTitle(contactJid);

        /**
         * @func 메시지 송신기능
         *       보내기버튼 클릭시 채팅내용, 현재 대화상대, Preference에서 로그인아이디 이하 3항목을 intent에 넣어 BroadCast
         * @param chatMessage
         * @return true
         */
        chat_view.setOnSentMessageListener(new ChatView.OnSentMessageListener() {
            @Override
            public boolean sendMessage(ChatMessage chatMessage) {
                Log.d("DEBUG", "ChatActivity : sendMessage()");
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

        /**
         * @func 메시지 수신 기능
         *       BroadcastReceiver 생성
         *       새로운 메시지 있을 시 현재 대화상대와 송신자가 같다면 메시지 띄워줌
         *       BroadcastReceiver와 인텐트 필터를 이용하여 Receiver 등록
         * @param context, intent
         * @return
         */
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("DEBUG", "ChatActivity : onReceive()");
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