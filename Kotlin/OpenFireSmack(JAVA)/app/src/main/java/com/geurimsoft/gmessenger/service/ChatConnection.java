/**
 * ChatConnection.class
 * 기능 : 서버 연결, 해제 및 상태관리
 *        ChatActivity에서 Broadcast한 메시지 수신하여 백그라운드 스레드로 메시지 전달(서버로 송신)
 */

package com.geurimsoft.gmessenger.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.preference.PreferenceManager;
import android.util.Log;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.ReconnectionManager;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.chat2.IncomingChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jxmpp.jid.DomainBareJid;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;

import java.io.IOException;
import java.net.InetAddress;

public class ChatConnection implements ConnectionListener {
    private  final Context mApplicationContext;
    private  final String mUserName;
    private  final String mPassWord;
    private  final String mServiceName;
    private XMPPTCPConnection mConnection;
    private BroadcastReceiver uiTHreadMessageReceiver;

    public enum ConnectionState
    {
        CONNECTED ,AUTHENTICATED, CONNECTING ,DISCONNECTING ,DISCONNECTED;
    }

    public enum LoggedInState
    {
        LOGGED_IN , LOGGED_OUT;
    }

    /**
     * @func ChatConnection 생성자
     *       Preference에서 로그인 ID와 PW받아 저장 후 아이디는 mUserName과 mServiceName으로 분할(서버연결 및 메시지 송수신에 필요)
     * @param context
     * @return
     */
    public ChatConnection(Context context) {
        Log.d("DEBUG", "ChatConnection : ChatConnection()");
        mApplicationContext = context.getApplicationContext();
        String mId = PreferenceManager.getDefaultSharedPreferences(mApplicationContext).getString("xmpp_mId", null);
        mPassWord = PreferenceManager.getDefaultSharedPreferences(mApplicationContext).getString("xmpp_mPw", null);

        if(mId != null) {
            mUserName = mId.split("@")[0];
            mServiceName = mId.split("@")[1];
            Log.d("DEBUG", "ChatConnection : mUserName : " + mUserName);
            Log.d("DEBUG", "ChatConnection : mServiceName : " + mServiceName);
        }
        else {
            Log.d("DEBUG", "ChatConnection : mId == null");
            mUserName = "";
            mServiceName = "";
        }
    }

    /**
     * @func action이 SEND_MESSAGE면 sendMessage메소드 호출하는 Reciever 등록
     * @param context, intent
     * @return
     */
    private void setupUiThreadBroadCastMessageReceiver() {
        Log.d("DEBUG", "ChatConnection : setupUiThreadBroadCastMessageReceiver()");
        uiTHreadMessageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction().equals(ChatConnectionService.SEND_MESSAGE)) {
                    sendMessage(intent.getStringExtra(ChatConnectionService.BUNDLE_MESSAGE_BODY),
                            intent.getStringExtra(ChatConnectionService.BUNDLE_TO));
                }
            }
        };

        IntentFilter filter = new IntentFilter();
        filter.addAction(ChatConnectionService.SEND_MESSAGE);
        mApplicationContext.registerReceiver(uiTHreadMessageReceiver, filter);
    }

    /**
     * @func 메시지 송신 메소드
     *       chat객체 생성하고 Message객체에 채팅내용과 수신인 패키징하여 chat.send메소드로 전송
     * @param body, mId
     * @return
     */
    private void sendMessage(String body, String mId) {
        Log.d("DEBUG", "ChatConnection : sendMessage()");
        EntityBareJid id = null;

        if(mConnection != null) {
            Log.d("DEBUG", "ChatConnection : sendMessage() - mConnection != null");
            ChatManager chatManager = ChatManager.getInstanceFor(mConnection);

            try {
                id = JidCreate.entityBareFrom(mId);
            } catch (XmppStringprepException e) {
                e.printStackTrace();
                Log.d("DEBUG", "ChatConnection : sendMessage() - catch(XmppString) : " + e.toString());
            }
            Chat chat = chatManager.chatWith(id);

            try {
                Message message = new Message(id, Message.Type.chat);
                message.setBody(body);
                chat.send(message);
            } catch (SmackException.NotConnectedException e) {
                e.printStackTrace();
                Log.d("DEBUG", "ChatConnection : sendMessage() - catch(Smack.NotConnect) : " + e.toString());
            } catch (InterruptedException e) {
                e.printStackTrace();
                Log.d("DEBUG", "ChatConnection : sendMessage() - catch(Interrupted) : " + e.toString());
            }
        }
        else {
            Log.d("DEBUG", "ChatConnection : sendMessage() - mConnection == null");
        }
    }
    
    /**
     * @func 사용자 인증되었을 때 setpackage?????
     * @param 
     * @return 
     */
    // 사용자가 인증되면 호출
    private void showUserListActivityWhenAuthenticated() {
        Log.d("DEBUG", "ChatConnection : showUserListActivityWhenAuthenticated()");
        Intent i = new Intent(ChatConnectionService.UI_AUTHENTICATED);
        i.setPackage(mApplicationContext.getPackageName());
        mApplicationContext.sendBroadcast(i);
    }

    public void connect() throws IOException, XMPPException, SmackException {
        Log.d("DEBUG", "ChatConnection : connect()");
        DomainBareJid jId = JidCreate.domainBareFrom(mServiceName);
        XMPPTCPConnectionConfiguration.Builder builder =
                XMPPTCPConnectionConfiguration.builder()
                        .setUsernameAndPassword(mUserName, mPassWord)
                        .setHostAddress(InetAddress.getByName("192.168.0.6"))
                        .setXmppDomain(jId)
                        .setPort(5222)
                        .setResource("Chat")
                        .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);

        setupUiThreadBroadCastMessageReceiver();

        mConnection = new XMPPTCPConnection(builder.build());
        mConnection.addConnectionListener(this);
        try {
            mConnection.connect();
            mConnection.login(mUserName, mPassWord);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.d("DEBUG", "ChatConnection : connect() - catch : " + e.toString());
        }

        ChatManager.getInstanceFor(mConnection).addIncomingListener(new IncomingChatMessageListener() {
            @Override
            public void newIncomingMessage(EntityBareJid messageFrom, Message message, Chat chat) {
                Log.d("DEBUG", "ChatConnection : newIncomingMessage()");
                Log.d("DEBUG", "ChatConnection : message.getbody() : " + message.getBody());
                Log.d("DEBUG", "ChatConnection : message.getfrom() : " + message.getFrom());

                String from = message.getFrom().toString();
                String contactJid = "";

                // 가끔 from에 /~가 붙는 경우가 잇음 찾아볼것
                if(from.contains("/")) {
                    contactJid = from.split("/")[0];
                }
                else {
                    contactJid = from;
                }

                Intent intent = new Intent(ChatConnectionService.NEW_MESSAGE);
                intent.setPackage(mApplicationContext.getPackageName());
                intent.putExtra(ChatConnectionService.BUNDLE_FROM, contactJid);
                intent.putExtra(ChatConnectionService.BUNDLE_MESSAGE_BODY, message.getBody());
                mApplicationContext.sendBroadcast(intent);
            }
        });


        ReconnectionManager reconnectionManager = ReconnectionManager.getInstanceFor(mConnection);
        reconnectionManager.setEnabledPerDefault(true);
        reconnectionManager.enableAutomaticReconnection();
    }

    public void disconnect() {
        Log.d("DEBUG", "ChatConnection : disconnect()");
        if (mConnection != null) {
            Log.d("DEBUG", "ChatConnection : disconnect() - if(mConnection != null)");
            mConnection.disconnect();
        }
        mConnection = null;
    }

    @Override
    public void connected(XMPPConnection connection) {
        Log.d("DEBUG", "ChatConnection : connected()");
        ChatConnectionService.sConnectionState = ConnectionState.CONNECTED;
    }

    // 사용자가 인증되면 호출
    @Override
    public void authenticated(XMPPConnection connection, boolean resumed) {
        Log.d("DEBUG", "ChatConnection : authenticated()");
        ChatConnectionService.sConnectionState = ConnectionState.CONNECTED;
        showUserListActivityWhenAuthenticated();
    }

    @Override
    public void connectionClosed() {
        Log.d("DEBUG", "ChatConnection : connectionClosed()");
        ChatConnectionService.sConnectionState = ConnectionState.DISCONNECTED;
    }

    @Override
    public void connectionClosedOnError(Exception e) {
        Log.d("DEBUG", "ChatConnection : connectionClosedOnError()");
        ChatConnectionService.sConnectionState = ConnectionState.DISCONNECTED;
    }

    @Override
    public void reconnectionSuccessful() {
        Log.d("DEBUG", "ChatConnection : reconnectionSuccessful()");
        ChatConnectionService.sConnectionState = ConnectionState.CONNECTED;
    }

    @Override
    public void reconnectingIn(int seconds) {
        Log.d("DEBUG", "ChatConnection : reconnectingIn()");
        ChatConnectionService.sConnectionState = ConnectionState.CONNECTING;
    }

    @Override
    public void reconnectionFailed(Exception e) {
        Log.d("DEBUG", "ChatConnection : reconnectionFailed()");
        ChatConnectionService.sConnectionState = ConnectionState.DISCONNECTED;
    }
}
