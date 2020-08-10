package com.example.openfiresmack.service

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log

class ChatConnectionService : Service() {
    private var mActive: Boolean = false
    private var mThread: Thread? = null
    private lateinit var mTHandler: Handler
    private var mConnection:ChatConnection? = null
    private lateinit var sLoggedInState: ChatConnection.LoggedInState

    companion object {

        val UI_AUTHENTICATED = "com.example.openfiresmack.uiauthenticated"
        val SEND_MESSAGE = "com.example.openfiresmack.sendmessage"
        val NEW_MESSAGE = "com.example.openfiresmack.newmessage"
        val BUNDLE_MESSAGE_BODY = "b_body"
        val BUNDLE_TO = "user"
        val BUNDLE_FROM = "user2"
        var sConnectionState: ChatConnection.ConnectionState? = ChatConnection.ConnectionState.CONNECTED

        fun getState(): ChatConnection.ConnectionState {
            return if (sConnectionState == null) {
                ChatConnection.ConnectionState.DISCONNECTED
            } else {
                sConnectionState!!
            }
        }
    }


    fun getLoggedInState(): ChatConnection.LoggedInState {
        return if(sLoggedInState == null) {
            ChatConnection.LoggedInState.LOGGED_OUT
        } else {
            sLoggedInState!!
        }
    }



    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
        return null
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("DEBUG", "ChatConnectionService : onCreate()")
    }

    private fun initConnection() {
        Log.d("DEBUG", "ChatConnectionService : initConnection()")
        if(mConnection == null) {
            mConnection = ChatConnection(this)
        }
        try {
            mConnection!!.connect()
        } catch(e: Exception) {
            Log.d("DEBUG", "Something went wrong while connection, make sure the creentials are right and try again")
            e.printStackTrace()

            stopSelf()
        }
    }

    fun start() {
        Log.d("DEBUG", "ChatConnectionService : Service Start() called.")
        if(!mActive) {
            mActive = true
            if(mThread == null || !mThread!!.isAlive) {
                mThread = Thread(Runnable {
                    @Override
                    fun run() {
                        Log.d("DEBUG", "ChatConnectionService : start.run()")
                        Looper.prepare()
                        mTHandler = Handler()
                        initConnection()
                        //background thread에서 할 코드
                        Looper.loop()
                    }
                })
                mThread!!.start()
            }
        }
    }

    fun stop() {
        Log.d("DEBUG", "ChatConnectionService : stop()")
        mActive = false
        mTHandler.post(Runnable {
            @Override
            fun run() {
                if(mConnection != null) {
                    mConnection!!.disconnect()
                }
            }
        })
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("DEBUG", "ChatConnectionService : onStartCommand()")
        start()
        return Service.START_STICKY
    }

    override fun onDestroy() {
        Log.d("DEBUG", "ChatConnectionService : onDestroy()")
        super.onDestroy()
        stop()
    }
}