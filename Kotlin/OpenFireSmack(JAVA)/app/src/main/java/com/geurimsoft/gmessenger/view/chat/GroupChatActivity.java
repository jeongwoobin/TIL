/**
 * GroupChatActivity.class
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
import android.util.Log;

import com.geurimsoft.gmessenger.R;
import com.geurimsoft.gmessenger.data.AppConfig;
import com.geurimsoft.gmessenger.service.ChatConnectionService;


import co.intentservice.chatui.ChatView;
import co.intentservice.chatui.models.ChatMessage;

public class GroupChatActivity extends AppCompatActivity
{

    private BroadcastReceiver mBroadcastReceiver;
    private String contactJid;
    private ChatView chat_view;

    /**
     * 레이아웃 위젯 연결
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);
        Log.d(AppConfig.APP_DEBUG, this.getClass().getName() + " : onCreate()");

        chat_view = findViewById(R.id.group_chat_view);


    }

    /**
     * Receiver 등록 해제
     */
    @Override
    protected void onPause()
    {

        super.onPause();
        Log.d(AppConfig.APP_DEBUG, this.getClass().getName() + " : onPause()");
        unregisterReceiver(mBroadcastReceiver);

    }

    /**
     * ChatListRvAdapter 에서 보낸 intentExtra 받아서 액티비티 Title 변경
     * intentFilter 로 그룹채팅 메시지만 받음
     * 메시지 송,수신 기능
     */
    @Override
    protected void onResume()
    {

        super.onResume();
        Log.d(AppConfig.APP_DEBUG, this.getClass().getName() + " : onResume()");

        Intent intent = getIntent();
        contactJid = intent.getStringExtra("ID");
        Log.d(AppConfig.APP_DEBUG, this.getClass().getName() + " : contactJid = " + contactJid);
        setTitle(contactJid);

//        chat_view.setOnSentMessageListener(new ChatView.OnSentMessageListener()
//        {
//
//            /**
//             * 메시지 송신기능
//             * 보내기버튼 클릭 시 채팅내용, 현재 대화상대, Preference 에서 로그인아이디 이하 3개의 항목을 intent 에 넣어 BroadCast
//             * @param chatMessage 송신하려는 메시지데이터
//             * @return Boolean
//             */
//            @Override
//            public boolean sendMessage(ChatMessage chatMessage)
//            {
//
//                Log.d(AppConfig.APP_DEBUG, this.getClass().getName() + " : sendMessage()");
//                String mName = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("xmpp_mId", null);
//
//                if(ChatConnectionService.getState() == ChatConnection.ConnectionState.CONNECTED)
//                {
//
//                    Log.d(AppConfig.APP_DEBUG, this.getClass().getName() + " : sendMessage() : Message send!");
//
//                    Intent i1 = new Intent(ChatConnectionService.SEND_MESSAGE);
//                    i1.putExtra(ChatConnectionService.BUNDLE_MESSAGE_BODY, chat_view.getTypedMessage());
//                    i1.putExtra(ChatConnectionService.BUNDLE_TO, contactJid);
//                    i1.putExtra(ChatConnectionService.BUNDLE_FROM, mName);
//
//                    sendBroadcast(i1);
//
//                }
//                else
//                {
//
//                    Log.d(AppConfig.APP_DEBUG, this.getClass().getName() + " : sendMessage() : Client not connected to server ,Message not sent!");
//                    Toast.makeText(getApplicationContext(), "Client not connected to server ,Message not sent!", Toast.LENGTH_LONG).show();
//
//                }
//                return true;
//
//            }
//
//        });

        mBroadcastReceiver = new BroadcastReceiver()
        {

            /**
             * 그룹채팅 메시지 수신 기능
             * BroadcastReceiver 생성
             * 새로운 메시지 있을 시 현재 그룹채팅방 이름과 송신한 그룹채팅방 이름이 같다면 메시지 띄워줌
             * BroadcastReceiver 와 인텐트 필터를 이용하여 Receiver 등록
             * @param context
             * @param intent 메시지의 Action 을 받아 switch 문으로 처리
             */
            @Override
            public void onReceive(Context context, Intent intent)
            {

                Log.d(AppConfig.APP_DEBUG, this.getClass().getName() + " : onReceive()");
                String action = intent.getAction();
                String from;
                String body;
                String gfrom;

                switch(action)
                {

                    case ChatConnectionService.GROUP_NEW_MESSAGE:
                        Log.d(AppConfig.APP_DEBUG, this.getClass().getName() + " : GROUP_NEW_MESSAGE");

                        gfrom = intent.getStringExtra(ChatConnectionService.BUNDLE_GROUP_FROM);
                        from = intent.getStringExtra(ChatConnectionService.BUNDLE_FROM);
                        body = intent.getStringExtra(ChatConnectionService.BUNDLE_MESSAGE_BODY);

                        if(gfrom.equals(contactJid))
                        {

                            Log.d(AppConfig.APP_DEBUG, this.getClass().getName() + " : onReceive if");

                            ChatMessage chatMessage = new ChatMessage( body, System.currentTimeMillis(), ChatMessage.Type.RECEIVED, from);
                            chat_view.addMessage(chatMessage);

                        }
                        else
                        {
                            Log.d(AppConfig.APP_DEBUG, this.getClass().getName() + " : onReceive else");
                        }
                        return;

                }

            }

        };

        IntentFilter filter = new IntentFilter(ChatConnectionService.GROUP_NEW_MESSAGE);
        registerReceiver(mBroadcastReceiver, filter);

    }

}