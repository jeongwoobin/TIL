/**
 * ChatConnection.class
 * 기능 : 서버 연결, 해제 및 상태관리
 *        (Group)ChatActivity 에서 Broadcast 한 메시지 수신하여 백그라운드 스레드로 메시지 전달(서버로 송신)
 */

package com.geurimsoft.gmessenger.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.preference.PreferenceManager;
import android.util.Log;

import com.geurimsoft.gmessenger.data.AppConfig;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.MessageListener;
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
import org.jivesoftware.smackx.muc.InvitationListener;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChatManager;
import org.jivesoftware.smackx.muc.packet.MUCUser;
import org.jxmpp.jid.DomainBareJid;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.EntityJid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.jid.parts.Resourcepart;
import org.jxmpp.stringprep.XmppStringprepException;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Iterator;
import java.util.Set;

import static com.geurimsoft.gmessenger.data.AppConfig.CHAT_ADDR;
import static com.geurimsoft.gmessenger.data.AppConfig.CHAT_PORT;

public class ChatConnection implements ConnectionListener
{

    private EntityBareJid mucName;
    private String mId;
    private XMPPTCPConnection mConnection;
    private BroadcastReceiver uiThreadMessageReceiver;

    private final Context mApplicationContext;
    private final String mUserName;
    private final String mPassWord;
    private final String mServiceName;

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

        Log.d(AppConfig.APP_DEBUG, this.getClass().getName() + " : ChatConnection()");
        mApplicationContext = context.getApplicationContext();
        mId = PreferenceManager.getDefaultSharedPreferences(mApplicationContext).getString("xmpp_mId", null);
        mPassWord = PreferenceManager.getDefaultSharedPreferences(mApplicationContext).getString("xmpp_mPw", null);

        if(mId != null)
        {

            mUserName = mId.split("@")[0];
            mServiceName = mId.split("@")[1];
            Log.d(AppConfig.APP_DEBUG, this.getClass().getName() + " : ChatConnection : mUserName = " + mUserName);
            Log.d(AppConfig.APP_DEBUG, this.getClass().getName() + " : ChatConnection : mServiceName = " + mServiceName);

        }
        else
        {

            Log.d(AppConfig.APP_DEBUG, this.getClass().getName() + " : ChatConnection : mId == null");
            mUserName = "";
            mServiceName = "";

        }

    }

    /**
     * action 이 SEND_MESSAGE 면 sendMessage 메소드 호출하는 Receiver 등록
     */
    private void setupUiThreadBroadCastMessageReceiver()
    {

        Log.d(AppConfig.APP_DEBUG, this.getClass().getName() + " : setupUiThreadBroadCastMessageReceiver()");
        uiThreadMessageReceiver = new BroadcastReceiver()
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
        mApplicationContext.registerReceiver(uiThreadMessageReceiver, filter);

    }

    /**
     * 메시지 송신 메소드
     * Chat 객체 생성하고 Message 객체에 채팅내용과 수신인 패키징하여 chat.send 메소드로 전송
     * @param body 메시지 내용
     * @param mId 메시지 수신인 ID
     */
    private void sendMessage(String body, String mId)
    {

        Log.d(AppConfig.APP_DEBUG, this.getClass().getName() + " : sendMessage()");
        EntityBareJid id = null;

        if(mConnection != null)
        {

            Log.d(AppConfig.APP_DEBUG, this.getClass().getName() + " : sendMessage() : mConnection != null");
            ChatManager chatManager = ChatManager.getInstanceFor(mConnection);

            try
            {
                id = JidCreate.entityBareFrom(mId);
            }
            catch (XmppStringprepException e)
            {

                e.printStackTrace();
                Log.d(AppConfig.APP_DEBUG, this.getClass().getName() + " : sendMessage() : catch(XmppString) = " + e.toString());

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
                Log.d(AppConfig.APP_DEBUG, this.getClass().getName() + " : sendMessage() : catch(Smack.NotConnect) = " + e.toString());

            }
            catch (InterruptedException e)
            {

                e.printStackTrace();
                Log.d(AppConfig.APP_DEBUG, this.getClass().getName() + " : sendMessage() : catch(Interrupted) = " + e.toString());

            }

        }
        else
        {
            Log.d(AppConfig.APP_DEBUG, this.getClass().getName() + " : sendMessage() : mConnection == null");
        }

    }

    /**
     * 사용자인증 확인되었을 때 호출되는 메소드
     * 인증되었다는 정보 Broadcast
     */
    private void showUserListActivityWhenAuthenticated()
    {

        Log.d(AppConfig.APP_DEBUG, this.getClass().getName() + " : showUserListActivityWhenAuthenticated()");

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

        Log.d(AppConfig.APP_DEBUG, this.getClass().getName() + " : connect()");

        DomainBareJid jId = JidCreate.domainBareFrom(mServiceName);
        XMPPTCPConnectionConfiguration.Builder builder =
                XMPPTCPConnectionConfiguration.builder()
                        .setUsernameAndPassword(mUserName, mPassWord)
                        .setHostAddress(InetAddress.getByName(CHAT_ADDR))
                        .setXmppDomain(jId)
                        .setPort(CHAT_PORT)
                        .setResource(mUserName)
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
            Log.d(AppConfig.APP_DEBUG, this.getClass().getName() + " : connect() : catch = " + e.toString());

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

                Log.d(AppConfig.APP_DEBUG, this.getClass().getName() + " : newIncomingMessage()");
                Log.d(AppConfig.APP_DEBUG, this.getClass().getName() + " : message.getbody() = " + message.getBody());
                Log.d(AppConfig.APP_DEBUG, this.getClass().getName() + " : message.getfrom() = " + message.getFrom());

                String from = message.getFrom().toString();
                String contactJid = "";

                // getFrom()끝에 '/setResource' 가 붙음
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

        MultiUserChatManager.getInstanceFor(mConnection).addInvitationListener(new InvitationListener()
        {

            /**
             * 그룹채팅 초대 listener
             * 그룹채팅방 정보들이 들어있음
             * 이 정보들로 해당 그룹채팅에 join
             * @param conn 현재 사용중인 계정정보
             * @param room 초대받은 그룹채팅방 정보(그룹채팅방 이름, 초대받은계정)
             * @param inviter 그룹채팅 생성자
             * @param reason 그룹채팅 초대 메시지
             * @param password 비밀번호
             * @param message 그룹채팅 초대 받은계정, 그룹채팅방 이름
             * @param invitation smack 코드
             */
            @Override
            public void invitationReceived(XMPPConnection conn, MultiUserChat room, EntityJid inviter, String reason, String password, Message message, MUCUser.Invite invitation)
            {

                Log.d(AppConfig.APP_DEBUG, this.getClass().getName() + " : invitationReceived()");
                Log.d(AppConfig.APP_DEBUG, this.getClass().getName() + " : invitationReceived() conn : " + conn);
                Log.d(AppConfig.APP_DEBUG, this.getClass().getName() + " : invitationReceived() room : " + room);
                Log.d(AppConfig.APP_DEBUG, this.getClass().getName() + " : invitationReceived() inviter : " + inviter);
                Log.d(AppConfig.APP_DEBUG, this.getClass().getName() + " : invitationReceived() reason : " + reason);
                Log.d(AppConfig.APP_DEBUG, this.getClass().getName() + " : invitationReceived() password : " + password);
                Log.d(AppConfig.APP_DEBUG, this.getClass().getName() + " : invitationReceived() message : " + message);
                Log.d(AppConfig.APP_DEBUG, this.getClass().getName() + " : invitationReceived() invitation : " + invitation);

                try
                {

                    Log.d(AppConfig.APP_DEBUG, this.getClass().getName() + " : invitationReceived() try");

                    if(!room.isJoined())
                    {

                        room.join(Resourcepart.from(mId));
                        Log.d(AppConfig.APP_DEBUG, this.getClass().getName() + " : invitationReceived() : room.isJoined() : " + room.isJoined());

                    }


                    Set<EntityBareJid> a = MultiUserChatManager.getInstanceFor(mConnection).getJoinedRooms();
                    Iterator<EntityBareJid> ia = a.iterator();
                    mucName = ia.next();
                    Log.d(AppConfig.APP_DEBUG, this.getClass().getName() + " : invitationReceived() mucName : " + mucName);


                    MultiUserChatManager.getInstanceFor(mConnection).getMultiUserChat(mucName).addMessageListener(new MessageListener()
                    {

                        /**
                         * 메시지 수신 listener
                         * 그룹채팅 메시지이므로 그룹채팅방 이름과 메시지송신자 분리하여 GROUP_NEW_MESSAGE 객체 생성 후 데이터저장, Broadcast
                         * @param message 수신 메시지 정보
                         */
                        @Override
                        public void processMessage(Message message)
                        {

                            Log.d(AppConfig.APP_DEBUG, this.getClass().getName() + " : MessageListener() : processMessage() mucName : " + mucName);

                            if(message.getBody() != null)
                            {

                                if(message.getBody().length() > 0)
                                {

                                    Log.d(AppConfig.APP_DEBUG, this.getClass().getName() + " : MessageListener() : processMessage() message.getBody() : " + message.getBody());
                                    Log.d(AppConfig.APP_DEBUG, this.getClass().getName() + " : MessageListener() : processMessage() message.getFrom() : " + message.getFrom());

                                    String from = message.getFrom().toString();
                                    String groupName = "";
                                    String contactJid = "";

                                    // getFrom()에 방이름과 초대보낸사람이 /로 구분
                                    groupName = from.split("/")[0];
                                    contactJid = from.split("/")[1];

                                    Log.d(AppConfig.APP_DEBUG, this.getClass().getName() + " : MessageListener() : processMessage() groupName : " + groupName);
                                    Log.d(AppConfig.APP_DEBUG, this.getClass().getName() + " : MessageListener() : processMessage() contactJid : " + contactJid);

                                    Intent intent = new Intent(ChatConnectionService.GROUP_NEW_MESSAGE);
                                    intent.setPackage(mApplicationContext.getPackageName());
                                    intent.putExtra(ChatConnectionService.BUNDLE_GROUP_FROM, groupName);
                                    intent.putExtra(ChatConnectionService.BUNDLE_FROM, contactJid);
                                    intent.putExtra(ChatConnectionService.BUNDLE_MESSAGE_BODY, message.getBody());
                                    mApplicationContext.sendBroadcast(intent);

                                }

                            }
                            else
                            {
                                Log.d(AppConfig.APP_DEBUG, this.getClass().getName() + " : MessageListener() : processMessage() message fail");
                            }

                        }

                    });

                }

                catch (Exception e)
                {

                    e.printStackTrace();
                    Log.d(AppConfig.APP_DEBUG, this.getClass().getName() + " : invitationReceived() catch");

                }

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

        Log.d(AppConfig.APP_DEBUG, this.getClass().getName() + " : disconnect()");

        if (mConnection != null)
        {

            Log.d(AppConfig.APP_DEBUG, this.getClass().getName() + " : disconnect() : if(mConnection != null)");
            mConnection.disconnect();

        }
        else
        {
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

        Log.d(AppConfig.APP_DEBUG, this.getClass().getName() + " : connected()");
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

        Log.d(AppConfig.APP_DEBUG, this.getClass().getName() + " : authenticated()");
        ChatConnectionService.sConnectionState = ConnectionState.CONNECTED;
        showUserListActivityWhenAuthenticated();

    }

    /**
     * 연결끊겼을 때 호출
     */
    @Override
    public void connectionClosed()
    {

        Log.d(AppConfig.APP_DEBUG, this.getClass().getName() + " : connectionClosed()");
        ChatConnectionService.sConnectionState = ConnectionState.DISCONNECTED;

    }

    /**
     * error 로 연결 끊겼을 때 호출
     * @param e Exception
     */
    @Override
    public void connectionClosedOnError(Exception e)
    {

        Log.d(AppConfig.APP_DEBUG, this.getClass().getName() + " : connectionClosedOnError()");
        ChatConnectionService.sConnectionState = ConnectionState.DISCONNECTED;

    }

    /**
     * 재연결 성공시 호출
     */
    @Override
    public void reconnectionSuccessful()
    {

        Log.d(AppConfig.APP_DEBUG, this.getClass().getName() + " : reconnectionSuccessful()");
        ChatConnectionService.sConnectionState = ConnectionState.CONNECTED;

    }

    /**
     * 재연결중일 때 호출
     * @param seconds reconnecting 시도 전 남은 시간(초)
     */
    @Override
    public void reconnectingIn(int seconds)
    {

        Log.d(AppConfig.APP_DEBUG, this.getClass().getName() + " : reconnectingIn()");
        ChatConnectionService.sConnectionState = ConnectionState.CONNECTING;

    }

    /**
     * 재연결 실패시 호출
     * @param e Exception
     */
    @Override
    public void reconnectionFailed(Exception e)
    {

        Log.d(AppConfig.APP_DEBUG, this.getClass().getName() + " : reconnectionFailed()");
        ChatConnectionService.sConnectionState = ConnectionState.DISCONNECTED;

    }















    /**
     * 그룹 채팅 구현
     */


//    // 그룹 채팅방 만들기
//    public void createGroupChat() throws XmppStringprepException, InterruptedException, SmackException.NoResponseException, MultiUserChatException.MucAlreadyJoinedException,
//            SmackException.NotConnectedException, XMPPException.XMPPErrorException, MultiUserChatException.MissingMucCreationAcknowledgeException,
//            MultiUserChatException.NotAMucServiceException, MultiUserChatException.MucConfigurationNotSupportedException {
//        Log.d(AppConfig.APP_DEBUG, this.getClass().getName() + " : createGroupChat()");
//
//            mucName = JidCreate.entityBareFrom("chat1@conference." + CHAT_ADDR);
//            nickname = Resourcepart.from("Mobile");
//
//            MultiUserChatManager mucManager = MultiUserChatManager.getInstanceFor(mConnection);
//            muc = mucManager.getMultiUserChat(mucName);
//
//            Set<Jid> owners = JidUtil.jidSetFrom(new String [] { "user1@" + CHAT_ADDR, "user2@" + CHAT_ADDR, "user3@" + CHAT_ADDR});
//
//            otherJid = JidCreate.fullFrom("user3@" + CHAT_ADDR + "/Mobile");
//
//            muc.create(nickname)
//                    .getConfigFormManager()
//                    .setRoomOwners(owners)
//                    .submitConfigurationForm();
//
//            MultiUserChatManager.getInstanceFor(mConnection).addInvitationListener(new InvitationListener() {
//                @Override
//                public void invitationReceived(XMPPConnection conn, MultiUserChat room, EntityJid inviter, String reason, String password, Message message, MUCUser.Invite invitation) {
//                    try {
//                        muc.invite((EntityBareJid) otherJid, "hi");
//                    } catch (SmackException.NotConnectedException | InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//    }


//    // 그룹 채팅방 참여
//    public void joinChat(Resourcepart nickname) throws XMPPException.XMPPErrorException, MultiUserChatException.NotAMucServiceException, SmackException.NotConnectedException,
//            InterruptedException, SmackException.NoResponseException {
//        Log.d(AppConfig.APP_DEBUG, this.getClass().getName() + " : joinChat()");
//
//        MultiUserChatManager manager = MultiUserChatManager.getInstanceFor(mConnection);
//        MultiUserChat muc2 = manager.getMultiUserChat(mucName);
//        muc2.join(nickname);
//    }

//    // 초대하기
//    public void invitation() throws XMPPException.XMPPErrorException, MultiUserChatException.NotAMucServiceException, SmackException.NotConnectedException,
//            InterruptedException, SmackException.NoResponseException, XmppStringprepException {
//
//        MultiUserChatManager manager = MultiUserChatManager.getInstanceFor(mConnection);
//        MultiUserChat muc2 = manager.getMultiUserChat(mucJid);
//        otherJid = JidCreate.fullFrom("user3@" + CHAT_ADDR + "/Mobile");
//
//        muc2.join(nickname);
//        muc2.addInvitationRejectionListener(new InvitationRejectionListener() {
//            @Override
//            public void invitationDeclined(EntityBareJid invitee, String reason, Message message, MUCUser.Decline rejection) {
//
//            }
//        });
//        muc2.invite((EntityBareJid) otherJid, "Meet me in this excellent room");
//    }

//    // 거절하기
//    public void decline() {
//        MultiUserChatManager.getInstanceFor(mConnection).addInvitationListener(new InvitationListener() {
//            @Override
//            public void invitationReceived(XMPPConnection conn, MultiUserChat room, EntityJid inviter, String reason, String password, Message message, MUCUser.Invite invitation) {
//                try {
//                    MultiUserChatManager.getInstanceFor(conn).decline(mucName, (EntityBareJid) inviter, "I'm busy right now");
//                } catch (SmackException.NotConnectedException e) {
//                    e.printStackTrace();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//
//        });
//    }

}

