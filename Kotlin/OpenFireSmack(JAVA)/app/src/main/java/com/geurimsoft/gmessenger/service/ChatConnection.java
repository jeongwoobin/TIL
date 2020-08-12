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

    public static enum ConnectionState
    {
        CONNECTED ,AUTHENTICATED, CONNECTING ,DISCONNECTING ,DISCONNECTED;
    }

    public static enum LoggedInState
    {
        LOGGED_IN , LOGGED_OUT;
    }

    public ChatConnection(Context context) {
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

    private void setupUiThreadBroadCastMessageReceiver() {
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

    private void sendMessage(String body, String mId) {
        EntityBareJid id = null;

        ChatManager chatManager = ChatManager.getInstanceFor(mConnection);

        try {
            id = JidCreate.entityBareFrom(mId);
        } catch (XmppStringprepException e) {
            e.printStackTrace();
        }
        Chat chat = chatManager.chatWith(id);

        try {
            Message message = new Message(id, Message.Type.chat);
            message.setBody(body);
            chat.send(message);
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

//    // 잘 모르겠음..?
//    private void showUserListActivityWhenAuthenticated() {
//        Intent i = new Intent(ChatConnectionService.UI_AUTHENTICATED);
//        i.setPackage(mApplicationContext.getPackageName());
//        mApplicationContext.sendBroadcast(i);
//    }

    public void connect() throws IOException, XMPPException, SmackException {
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
        }

        ChatManager.getInstanceFor(mConnection).addIncomingListener(new IncomingChatMessageListener() {
            @Override
            public void newIncomingMessage(EntityBareJid messageFrom, Message message, Chat chat) {
                String from = message.getFrom().toString();

                String contactJid = "";
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
        if (mConnection != null) {
            mConnection.disconnect();
        }
        mConnection = null;
    }

    @Override
    public void connected(XMPPConnection connection) {
        ChatConnectionService.sConnectionState = ConnectionState.CONNECTED;
    }

    @Override
    public void authenticated(XMPPConnection connection, boolean resumed) {
        ChatConnectionService.sConnectionState = ConnectionState.CONNECTED;
//        showUserListActivityWhenAuthenticated();
    }

    @Override
    public void connectionClosed() {
        ChatConnectionService.sConnectionState = ConnectionState.DISCONNECTED;
    }

    @Override
    public void connectionClosedOnError(Exception e) {
        ChatConnectionService.sConnectionState = ConnectionState.DISCONNECTED;
    }

    @Override
    public void reconnectionSuccessful() {
        ChatConnectionService.sConnectionState = ConnectionState.CONNECTED;
    }

    @Override
    public void reconnectingIn(int seconds) {
        ChatConnectionService.sConnectionState = ConnectionState.CONNECTING;
    }

    @Override
    public void reconnectionFailed(Exception e) {
        ChatConnectionService.sConnectionState = ConnectionState.DISCONNECTED;
    }
}
