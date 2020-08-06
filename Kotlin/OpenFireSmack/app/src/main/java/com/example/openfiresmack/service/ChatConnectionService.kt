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


    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
        return null
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("DEBUG", "onCreate()")
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
                //서버 닫는 코드
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