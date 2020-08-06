package com.example.openfiresmack.service

import android.content.Context
import android.preference.PreferenceManager
import android.util.Log
import org.jivesoftware.smack.ConnectionListener
import org.jivesoftware.smack.XMPPConnection
import org.jivesoftware.smack.tcp.XMPPTCPConnection
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration
import java.io.IOException

class ChatConnection: ConnectionListener {
    lateinit var mApplicationContext: Context
    lateinit var mUserName: String
    lateinit var mPassWord: String
    lateinit var mServiceName: String
    lateinit var mConnection: XMPPTCPConnection

    enum class ConnectionState {
        CONNECTED, AUTHENTICATED, CONNECTING, DISCONNECTING, DISCONNECTED
    }

    enum class LoggedInState {
        LOGGED_IN, LOGGED_OUT
    }

    fun ChatConnection(context: Context) {
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

    fun connect() {
        Log.d("DEBUG", "Connecting to Server : " + mServiceName)
        val builder: XMPPTCPConnectionConfiguration.Builder = XMPPTCPConnectionConfiguration.builder()

        // ############################################## 2020/08/06 #################################################
        builder.setServiceName(mServiceName)
    }























    override fun connected(connection: XMPPConnection?) {
        TODO("Not yet implemented")
    }

    override fun connectionClosed() {
        TODO("Not yet implemented")
    }

    override fun connectionClosedOnError(e: Exception?) {
        TODO("Not yet implemented")
    }

    override fun reconnectionSuccessful() {
        TODO("Not yet implemented")
    }

    override fun authenticated(connection: XMPPConnection?, resumed: Boolean) {
        TODO("Not yet implemented")
    }

    override fun reconnectionFailed(e: Exception?) {
        TODO("Not yet implemented")
    }

    override fun reconnectingIn(seconds: Int) {
        TODO("Not yet implemented")
    }

}