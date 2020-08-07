package com.example.openfiresmack.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.preference.PreferenceManager
import android.util.Log
import org.jivesoftware.smack.ConnectionListener
import org.jivesoftware.smack.ReconnectionManager
import org.jivesoftware.smack.SmackException
import org.jivesoftware.smack.SmackException.NotConnectedException
import org.jivesoftware.smack.XMPPConnection
import org.jivesoftware.smack.chat2.Chat
import org.jivesoftware.smack.chat2.ChatManager
import org.jivesoftware.smack.packet.Message
import org.jivesoftware.smack.tcp.XMPPTCPConnection
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration
import org.jxmpp.jid.EntityBareJid
import org.jxmpp.jid.impl.JidCreate
import org.jxmpp.stringprep.XmppStringprepException





class ChatConnection: ConnectionListener {

    lateinit var uiThreadMessageReceiver: BroadcastReceiver
    var mApplicationContext: Context
    var mUserName: String
    var mPassWord: String
    var mServiceName: String

    enum class ConnectionState {
        CONNECTED, AUTHENTICATED, CONNECTING, DISCONNECTING, DISCONNECTED
    }

    enum class LoggedInState {
        LOGGED_IN, LOGGED_OUT
    }

    // 생성자
    constructor(context: Context) {
        Log.d("DEBUG", "chatConnection Constructor called.")
        mApplicationContext = context.applicationContext
        val mId: String? = PreferenceManager.getDefaultSharedPreferences(mApplicationContext).getString("xmpp_mId", null)
        mPassWord = PreferenceManager.getDefaultSharedPreferences(mApplicationContext).getString("xmpp_mPw", null).toString()

        if(mId != null) {
            mUserName = mId.split("@")[0]
            mServiceName = mId.split("@")[1]
        }
        else {
            mUserName = ""
            mServiceName = ""
        }
    }

    fun setupUiThreadBroadCastMessageReceiver() {
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
        mApplicationContext.registerReceiver(uiThreadMessageReceiver, filter)
    }

    companion object {
        var mConnection: XMPPTCPConnection? = null

        fun sendMessage(body: String, mId: String) {
            Log.d("DEBUG", "Sending Message to : " + mId)
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
    }

    fun connect() {
        Log.d("DEBUG", "Connecting to Server : " + mServiceName)
        val builder = XMPPTCPConnectionConfiguration.builder()

        // ############################################## 2020/08/06 #################################################
        val jId = JidCreate.domainBareFrom(mServiceName)
        builder.setServiceName(jId)
        builder.setUsernameAndPassword(mUserName, mPassWord)
//        builder.setRosterLoadedAtLogin(true)  // 없음
        builder.setResource("Chat")

        mConnection = XMPPTCPConnection(builder.build())
        mConnection!!.addConnectionListener(this)
        mConnection!!.connect()
        mConnection!!.login()

        val reconnectionManager:ReconnectionManager = ReconnectionManager.getInstanceFor(mConnection)
        ReconnectionManager.setEnabledPerDefault(true)
//         reconnectionManager.setEnabledPerDefault(true)   // 없음
        reconnectionManager.enableAutomaticReconnection()
    }

    fun disconnect() {
        Log.d("DEBUG", "Disconnection from Server : " + mServiceName)
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
        Log.d("DEBUG", "Connected Successfully")
    }

    override fun connectionClosed() {
        ChatConnectionService.sConnectionState = ConnectionState.DISCONNECTED
        Log.d("DEBUG", "Connectionclosed()")
    }

    override fun connectionClosedOnError(e: Exception?) {
        ChatConnectionService.sConnectionState = ConnectionState.DISCONNECTED
        Log.d("DEBUG", "ConnectionClosedOnError, error : " + e.toString())
    }

    override fun reconnectionSuccessful() {
        ChatConnectionService.sConnectionState = ConnectionState.CONNECTED
        Log.d("DEBUG", "ReconnectionSuccessful()")
    }

    override fun authenticated(connection: XMPPConnection?, resumed: Boolean) {
        ChatConnectionService.sConnectionState = ConnectionState.CONNECTED
        Log.d("DEBUG", "Authenticated Successfully")
    }

    override fun reconnectionFailed(e: Exception?) {
        ChatConnectionService.sConnectionState = ConnectionState.DISCONNECTED
        Log.d("DEBUG", "ReconnectionFailed()")
    }

    override fun reconnectingIn(seconds: Int) {
        ChatConnectionService.sConnectionState = ConnectionState.CONNECTING
        Log.d("DEBUG", "ReconnectingIn()")
    }

}