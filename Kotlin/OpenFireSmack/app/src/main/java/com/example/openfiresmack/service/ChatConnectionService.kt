package com.example.openfiresmack.service

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import java.lang.Exception

class ChatConnectionService : Service() {
    private var mActive: Boolean = false
    private var mThread: Thread? = null
    private lateinit var mTHandler: Handler
    private var mConnection: ChatConnection? = null

    companion object {

        val UI_AUTHENTICATED = "com.blikoon.rooster.uiauthenticated"
        val SEND_MESSAGE = "com.blikoon.rooster.sendmessage"
        val BUNDLE_MESSAGE_BODY = "b_body"
        val BUNDLE_TO = "b_to"
        var sConnectionState: ChatConnection.ConnectionState? = null
        lateinit var sLoggedInState: ChatConnection.LoggedInState

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
            sLoggedInState
        }
    }

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
        return null
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("DEBUG", "onCreate()")
    }

    private fun initConnection() {
        Log.d("DEBUG", "initConnection()")
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
        Log.d("DEBUG", "Service Start() called.")
        if(!mActive) {
            mActive = true
            if(mThread == null || !mThread!!.isAlive()) {
                mThread = Thread(Runnable {
                    @Override
                    fun run() {
                        Looper.prepare()
                        mTHandler = Handler()
                        //background thread에서 할 코드

                        Looper.loop()
                    }
                })
                mThread!!.start()
            }
        }
    }

    fun stop() {
        Log.d("DEBUG", "stop()")
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
        Log.d("DEBUG", "onStartCommand()")
        start()
        return Service.START_STICKY
    }

    override fun onDestroy() {
        Log.d("DEBUG", "onDestroy()")
        super.onDestroy()
        stop()
    }
}