/**
 * ChatConnection.class
 * 기능 : 서버 연결, 해제 및 상태관리
 *        ChatActivity 에서 Broadcast 한 메시지 수신하여 백그라운드 스레드로 메시지 전달(서버로 송신)
 */

package com.geurimsoft.gmessenger.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.preference.PreferenceManager;
import android.util.Log;

import com.geurimsoft.gmessenger.data.App_Debug;

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

public class ChatConnection implements ConnectionListener
{

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
     * ChatConnection 생성자
     * Preference 에서 로그인 ID 와 PW 받아 저장 후 아이디는 mUserName 과 mServiceName 으로 분할(서버연결 및 메시지 송수신에 필요)
     * @param context
     */
    public ChatConnection(Context context)
    {

        Log.d(App_Debug.APP_DEBUG, this.getClass().getName() + " : ChatConnection()");
        mApplicationContext = context.getApplicationContext();

        String mId = PreferenceManager.getDefaultSharedPreferences(mApplicationContext).getString("xmpp_mId", null);
        mPassWord = PreferenceManager.getDefaultSharedPreferences(mApplicationContext).getString("xmpp_mPw", null);

        if(mId != null)
        {

            mUserName = mId.split("@")[0];
            mServiceName = mId.split("@")[1];
            Log.d(App_Debug.APP_DEBUG, this.getClass().getName() + " : ChatConnection : mUserName = " + mUserName);
            Log.d(App_Debug.APP_DEBUG, this.getClass().getName() + " : ChatConnection : mServiceName = " + mServiceName);
        }
        else
        {

            Log.d(App_Debug.APP_DEBUG, this.getClass().getName() + " : ChatConnection : mId == null");
            mUserName = "";
            mServiceName = "";
        }
    }

    /**
     * action 이 SEND_MESSAGE 면 sendMessage 메소드 호출하는 Receiver 등록
     */
    private void setupUiThreadBroadCastMessageReceiver()
    {

        Log.d(App_Debug.APP_DEBUG, this.getClass().getName() + " : setupUiThreadBroadCastMessageReceiver()");
        uiTHreadMessageReceiver = new BroadcastReceiver()
        {

            @Override
            public void onReceive(Context context, Intent intent)
            {

                if(intent.getAction().equals(ChatConnectionService.SEND_MESSAGE))
                {

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
     * 메시지 송신 메소드
     * Chat 객체 생성하고 Message 객체에 채팅내용과 수신인 패키징하여 chat.send 메소드로 전송
     * @param body 메시지 내용
     * @param mId 메시지 수신인 ID
     */
    private void sendMessage(String body, String mId)
    {

        Log.d(App_Debug.APP_DEBUG, this.getClass().getName() + " : sendMessage()");
        EntityBareJid id = null;

        if(mConnection != null)
        {

            Log.d(App_Debug.APP_DEBUG, this.getClass().getName() + " : sendMessage() : mConnection != null");
            ChatManager chatManager = ChatManager.getInstanceFor(mConnection);

            try
            {

                id = JidCreate.entityBareFrom(mId);
            }
            catch (XmppStringprepException e)
            {

                e.printStackTrace();
                Log.d(App_Debug.APP_DEBUG, this.getClass().getName() + " : sendMessage() : catch(XmppString) = " + e.toString());
            }

            Chat chat = chatManager.chatWith(id);

            try
            {

                Message message = new Message(id, Message.Type.chat);
                message.setBody(body);
                chat.send(message);
            }
            catch (SmackException.NotConnectedException e)
            {

                e.printStackTrace();
                Log.d(App_Debug.APP_DEBUG, this.getClass().getName() + " : sendMessage() : catch(Smack.NotConnect) = " + e.toString());
            }
            catch (InterruptedException e)
            {

                e.printStackTrace();
                Log.d(App_Debug.APP_DEBUG, this.getClass().getName() + " : sendMessage() : catch(Interrupted) = " + e.toString());
            }
        }
        else
        {

            Log.d(App_Debug.APP_DEBUG, this.getClass().getName() + " : sendMessage() : mConnection == null");
        }
    }

    /**
     * 사용자인증 확인되었을 때 호출되는 메소드
     * 인증되었다는 정보 Broadcast
     */
    private void showUserListActivityWhenAuthenticated()
    {

        Log.d(App_Debug.APP_DEBUG, this.getClass().getName() + " : showUserListActivityWhenAuthenticated()");

        Intent i = new Intent(ChatConnectionService.UI_AUTHENTICATED);
        i.setPackage(mApplicationContext.getPackageName());
        mApplicationContext.sendBroadcast(i);
    }

    /**
     * 직접적인 서버 연결 메소드
     * 로그인 정보와 IP, Domain, Port 로 XMPP 객체 생성하여 서버에 연결
     * UI 스레드 BroadcastMessageReceiver 설정
     * @throws IOException
     * @throws XMPPException
     * @throws SmackException
     */
    public void connect() throws IOException, XMPPException, SmackException
    {

        Log.d(App_Debug.APP_DEBUG, this.getClass().getName() + " : connect()");

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

        try
        {

            mConnection.connect();
            mConnection.login(mUserName, mPassWord);
        }
        catch (InterruptedException e)
        {

            e.printStackTrace();
            Log.d(App_Debug.APP_DEBUG, this.getClass().getName() + " : connect() : catch = " + e.toString());
        }

        ChatManager.getInstanceFor(mConnection).addIncomingListener(new IncomingChatMessageListener()
        {
            /**
             * 새로 들어온 메시지가 있을 때 ChatConnectionService.NEW_MESSAGE 객체 생성하여 메시지데이터 저장 후 Broadcast
             * @param messageFrom 메시지 송신인 ID
             * @param message 메시지 내용
             * @param chat
             */
            @Override
            public void newIncomingMessage(EntityBareJid messageFrom, Message message, Chat chat)
            {

                Log.d(App_Debug.APP_DEBUG, this.getClass().getName() + " : newIncomingMessage()");
                Log.d(App_Debug.APP_DEBUG, this.getClass().getName() + " : message.getbody() = " + message.getBody());
                Log.d(App_Debug.APP_DEBUG, this.getClass().getName() + " : message.getfrom() = " + message.getFrom());

                String from = message.getFrom().toString();
                String contactJid = "";

                // getFrom()끝에 /가 붙는 경우가 잇음
                if(from.contains("/"))
                {

                    contactJid = from.split("/")[0];
                }
                else
                {

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

    /**
     * UserListActivity 가 destroy 되면  ChatConnectionService 에서 호출
     * mConnection 에 저장되어있던 XMPP 연결 해제
     */
    public void disconnect()
    {

        Log.d(App_Debug.APP_DEBUG, this.getClass().getName() + " : disconnect()");

        if (mConnection != null)
        {

            Log.d(App_Debug.APP_DEBUG, this.getClass().getName() + " : disconnect() : if(mConnection != null)");
            mConnection.disconnect();
        }
        else {
            ChatConnectionService.sConnectionState = ConnectionState.DISCONNECTED;
        }
        mConnection = null;
    }

    /**
     * 서버와 연결되면 호출
     * @param connection XMPP 연결된 connection
     */
    @Override
    public void connected(XMPPConnection connection)
    {

        Log.d(App_Debug.APP_DEBUG, this.getClass().getName() + " : connected()");
        ChatConnectionService.sConnectionState = ConnectionState.CONNECTED;
    }

    /**
     * 사용자가 인증되면 호출
     * @param connection XMPP 연결된 connection
     * @param resumed 이전 XMPP 세션의 스트림이 resume 되면 true
     */
    @Override
    public void authenticated(XMPPConnection connection, boolean resumed)
    {

        Log.d(App_Debug.APP_DEBUG, this.getClass().getName() + " : authenticated()");
        ChatConnectionService.sConnectionState = ConnectionState.CONNECTED;
        showUserListActivityWhenAuthenticated();
    }

    /**
     * 연결끊겼을 때 호출
     */
    @Override
    public void connectionClosed()
    {

        Log.d(App_Debug.APP_DEBUG, this.getClass().getName() + " : connectionClosed()");
        ChatConnectionService.sConnectionState = ConnectionState.DISCONNECTED;
    }

    /**
     * error 로 연결 끊겼을 때 호출
     * @param e Exception
     */
    @Override
    public void connectionClosedOnError(Exception e)
    {

        Log.d(App_Debug.APP_DEBUG, this.getClass().getName() + " : connectionClosedOnError()");
        ChatConnectionService.sConnectionState = ConnectionState.DISCONNECTED;
    }

    /**
     * 재연결 성공시 호출
     */
    @Override
    public void reconnectionSuccessful()
    {

        Log.d(App_Debug.APP_DEBUG, this.getClass().getName() + " : reconnectionSuccessful()");
        ChatConnectionService.sConnectionState = ConnectionState.CONNECTED;
    }

    /**
     * 재연결중일 때 호출
     * @param seconds reconnecting 시도 전 남은 시간(초)
     */
    @Override
    public void reconnectingIn(int seconds)
    {

        Log.d(App_Debug.APP_DEBUG, this.getClass().getName() + " : reconnectingIn()");
        ChatConnectionService.sConnectionState = ConnectionState.CONNECTING;
    }

    /**
     * 재연결 실패시 호출
     * @param e Exception
     */
    @Override
    public void reconnectionFailed(Exception e)
    {

        Log.d(App_Debug.APP_DEBUG, this.getClass().getName() + " : reconnectionFailed()");
        ChatConnectionService.sConnectionState = ConnectionState.DISCONNECTED;
    }
}
