package com.example.openfiresmack.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.preference.PreferenceManager
import android.util.Log
import org.jivesoftware.smack.*
import org.jivesoftware.smack.SmackException.NotConnectedException
import org.jivesoftware.smack.chat2.Chat
import org.jivesoftware.smack.chat2.ChatManager
import org.jivesoftware.smack.packet.Message
import org.jivesoftware.smack.tcp.XMPPTCPConnection
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration
import org.jxmpp.jid.EntityBareJid
import org.jxmpp.jid.impl.JidCreate
import org.jxmpp.stringprep.XmppStringprepException
import java.net.InetAddress

class ChatConnection: ConnectionListener {

    lateinit var uiThreadMessageReceiver: BroadcastReceiver
    var mUserName: String
    var mPassWord: String
    var mServiceName: String
    var mApplicationContext: Context
    var mConnection: XMPPTCPConnection? = null

    enum class ConnectionState {
        CONNECTED, AUTHENTICATED, CONNECTING, DISCONNECTING, DISCONNECTED
    }

    enum class LoggedInState {
        LOGGED_IN, LOGGED_OUT
    }

    // 생성자
    constructor(context: Context) {
        Log.d("DEBUG", "ChatConnection : chatConnection Constructor called.")
        mApplicationContext = context.applicationContext

        val mId = PreferenceManager.getDefaultSharedPreferences(mApplicationContext).getString("xmpp_mId", null)
        mPassWord = PreferenceManager.getDefaultSharedPreferences(mApplicationContext).getString("xmpp_mPw", null)
            .toString()
        Log.d("DEBUG", "ChatConnection : mId : $mId")
        Log.d("DEBUG", "ChatConnection : mPassWord : $mPassWord")


        if(mId != null) {
            mUserName = mId.split("@")[0]
            mServiceName = mId.split("@")[1]
            Log.d("DEBUG", "ChatConnection : mUserName : " + mUserName)
            Log.d("DEBUG", "ChatConnection : mServiceName : " + mServiceName)
        }
        else {
            Log.d("DEBUG", "ChatConnection : mId == null")
            mUserName = ""
            mServiceName = ""
        }
    }

    private fun setupUiThreadBroadCastMessageReceiver() {
        Log.d("DEBUG", "ChatConnection : setupUiThreadBroadCastMessageReceiver")
        uiThreadMessageReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                //Check if the Intents purpose is to send the message.
                val action = intent.action
                if (action == ChatConnectionService.SEND_MESSAGE) {
                    //SENDS THE ACTUAL MESSAGE TO THE SERVER
                    sendMessage(
                        intent.getStringExtra(ChatConnectionService.BUNDLE_MESSAGE_BODY)!!,
                        intent.getStringExtra(ChatConnectionService.BUNDLE_TO)!!
                    )
                }
            }
        }

        val filter = IntentFilter()
        filter.addAction(ChatConnectionService.SEND_MESSAGE)
        mApplicationContext?.registerReceiver(uiThreadMessageReceiver, filter)
    }

    fun sendMessage(body: String, mId: String) {
        Log.d("DEBUG", "ChatConnection : Sending Message to : " + mId)
        var id: EntityBareJid? = null
        val chatManager: ChatManager = ChatManager.getInstanceFor(mConnection)

        try {
            id = JidCreate.entityBareFrom(mId)
        } catch (e: XmppStringprepException) {
            e.printStackTrace()
        }

        val chat: Chat = chatManager.chatWith(id)

        try {
            val message = Message(id, Message.Type.chat)
            message.setBody(body)
            chat.send(message)
        } catch (e: NotConnectedException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    private fun showUserListActivityWhenAuthenticated() {
        val i = Intent(ChatConnectionService.UI_AUTHENTICATED)
        i.setPackage(mApplicationContext?.packageName)
        mApplicationContext?.sendBroadcast(i)
        Log.d("DEBUG", "ChatConnection : Sent the broadcast that we are authenticated")
    }

    // ############################################## 2020/08/06 #################################################
    fun connect() {
        Log.d("DEBUG", "ChatConnection : Connecting to Server : " + mServiceName)
        val jId = JidCreate.domainBareFrom(mServiceName)
        Log.d("DEBUG", "ChatConnection : Jid : $jId")
        val builder = XMPPTCPConnectionConfiguration.builder()
            .setUsernameAndPassword(mUserName, mPassWord)
            .setHostAddress(InetAddress.getByName("192.168.0.6"))
//            .setHostAddress(InetAddress.getByName("10.0.2.2"))
            .setXmppDomain(jId)
            .setPort(5222)
    //        builder.setRosterLoadedAtLogin(true)  // 없음
            .setResource("Chat")
            .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)

        setupUiThreadBroadCastMessageReceiver()

        mConnection = XMPPTCPConnection(builder.build())
        mConnection!!.addConnectionListener(this)
        Log.d("DEBUG", "ChatConnection : connect.calling connect()")
        mConnection!!.connect()
        Log.d("DEBUG", "ChatConnection : connect.calling login()")
        mConnection!!.login()


        ChatManager.getInstanceFor(mConnection)
            .addIncomingListener { messageFrom, message, chat ->
                ///ADDED
                Log.d( "DEBUG", "ChatConnection : message.getBody() :" + message.body)
                Log.d("DEBUG","ChatConnection : message.getFrom() :" + message.from)

                val from = message.from.toString()
                var contactJid = ""
                if (from.contains("/")) {
                    contactJid = from.split("/".toRegex()).toTypedArray()[0]
                    Log.d("DEBUG","ChatConnection : The real jid is :$contactJid")
                    Log.d( "DEBUG","ChatConnection : The message is from :$from")
                } else {
                    contactJid = from
                }

                //Bundle up the intent and send the broadcast.
                val intent = Intent(ChatConnectionService.NEW_MESSAGE)
                intent.setPackage(mApplicationContext!!.packageName)
                intent.putExtra(ChatConnectionService.BUNDLE_FROM, contactJid)
                intent.putExtra(ChatConnectionService.BUNDLE_MESSAGE_BODY, message.body)
                mApplicationContext!!.sendBroadcast(intent)
                Log.d("DEBUG","ChatConnection : Received message from :$contactJid broadcast sent.")
                ///ADDED
            }


        val reconnectionManager:ReconnectionManager = ReconnectionManager.getInstanceFor(mConnection)
        ReconnectionManager.setEnabledPerDefault(true)
//         reconnectionManager.setEnabledPerDefault(true)   // 없음
        reconnectionManager.enableAutomaticReconnection()
    }

    fun disconnect() {
        Log.d("DEBUG", "ChatConnection : Disconnection from Server : " + mServiceName)
        try {
            if(mConnection != null) {
                mConnection!!.disconnect()
            }
        } catch (e: SmackException.NotConnectedException) {
            ChatConnectionService.sConnectionState = ConnectionState.DISCONNECTED
            e.printStackTrace()
        }
        mConnection = null
    }

    override fun connected(connection: XMPPConnection?) {
        ChatConnectionService.sConnectionState = ConnectionState.CONNECTED
        Log.d("DEBUG", "ChatConnection : Connected Successfully")
    }

    override fun connectionClosed() {
        ChatConnectionService.sConnectionState = ConnectionState.DISCONNECTED
        Log.d("DEBUG", "ChatConnection : Connectionclosed()")
    }

    override fun connectionClosedOnError(e: Exception?) {
        ChatConnectionService.sConnectionState = ConnectionState.DISCONNECTED
        Log.d("DEBUG", "ChatConnection : ConnectionClosedOnError, error : " + e.toString())
    }

    override fun reconnectionSuccessful() {
        ChatConnectionService.sConnectionState = ConnectionState.CONNECTED
        Log.d("DEBUG", "ChatConnection : ReconnectionSuccessful()")
    }

    override fun authenticated(connection: XMPPConnection?, resumed: Boolean) {
        ChatConnectionService.sConnectionState = ConnectionState.CONNECTED
        Log.d("DEBUG", "ChatConnection : Authenticated Successfully")
        showUserListActivityWhenAuthenticated()
    }

    override fun reconnectionFailed(e: Exception?) {
        ChatConnectionService.sConnectionState = ConnectionState.DISCONNECTED
        Log.d("DEBUG", "ChatConnection : ReconnectionFailed()")
    }

    override fun reconnectingIn(seconds: Int) {
        ChatConnectionService.sConnectionState = ConnectionState.CONNECTING
        Log.d("DEBUG", "ChatConnection : ReconnectingIn()")
    }

}