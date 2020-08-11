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
    private var mConnection:ChatConnection? = null
    private lateinit var mTHandler: Handler
    private lateinit var sLoggedInState: ChatConnection.LoggedInState

    companion object {

        const val UI_AUTHENTICATED = "com.example.openfiresmack.uiauthenticated"
        const val SEND_MESSAGE = "com.example.openfiresmack.sendmessage"
        const val NEW_MESSAGE = "com.example.openfiresmack.newmessage"
        const val BUNDLE_MESSAGE_BODY = "b_body"
        const val BUNDLE_TO = "b_to"
        const val BUNDLE_FROM = "b_from"
        var sConnectionState: ChatConnection.ConnectionState = ChatConnection.ConnectionState.DISCONNECTED

        fun getState(): ChatConnection.ConnectionState {
            return if (sConnectionState == null) {
                Log.d("DEBUG", "ChatConnectionService : sConnectionState == null")
                ChatConnection.ConnectionState.DISCONNECTED
            } else {
                Log.d("DEBUG", "ChatConnectionService : sConnectionState != null")
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
                Log.d("DEBUG", "if(mThread == null || !mThread!!.isAllive)")
                mThread = Thread(Runnable {
                        Log.d("DEBUG", "ChatConnectionService : start.run()")
                        Looper.prepare()
                        mTHandler = Handler()
                        initConnection()
                        //background thread에서 할 코드
                        Looper.loop()
                })
                mThread!!.start()
            }
            else {
                Log.d("DEBUG", "if(mThread != null)")
            }
        }
    }

    fun stop() {
        Log.d("DEBUG", "ChatConnectionService : stop()")

        mActive = false
        mTHandler.post(Runnable {
            if(mConnection != null) {
                mConnection!!.disconnect()
            }
        })
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("DEBUG", "ChatConnectionService : onStartCommand()")
        start()
        return START_STICKY
    }

    override fun onDestroy() {
        Log.d("DEBUG", "ChatConnectionService : onDestroy()")
        super.onDestroy()
        stop()
    }
}