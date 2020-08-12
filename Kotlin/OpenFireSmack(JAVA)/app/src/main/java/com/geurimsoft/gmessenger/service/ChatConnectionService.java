// 20200812 내일 검토해야함
// 수신 안됨


package com.geurimsoft.gmessenger.service;


import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;

import androidx.annotation.Nullable;

public class ChatConnectionService extends Service {
    public static final String UI_AUTHENTICATED = "com.example.openfiresmack.uiauthenticated";
    public static final String SEND_MESSAGE = "com.example.openfiresmack.sendmessage";
    public static final String NEW_MESSAGE = "com.example.openfiresmack.newmessage";
    public static final String BUNDLE_MESSAGE_BODY = "b_body";
    public static final String BUNDLE_TO = "b_to";
    public static final String BUNDLE_FROM = "b_from";
    public static ChatConnection.ConnectionState sConnectionState;
    public static ChatConnection.LoggedInState sLoggedInState;

    private boolean mActive;
    private Thread mThread;
    private Handler mTHandler;
    private ChatConnection mConnection;

    public ChatConnectionService() {

    }

    public static ChatConnection.ConnectionState getState() {
        if(sConnectionState == null) {
            return ChatConnection.ConnectionState.DISCONNECTED;
        } else {
            return sConnectionState;
        }
    }

    public static ChatConnection.LoggedInState getLoggedInState() {
        if(sLoggedInState == null) {
            return ChatConnection.LoggedInState.LOGGED_OUT;
        }
        else {
            return sLoggedInState;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    private void initConnection() {
        if(mConnection == null) {
            mConnection = new ChatConnection(this);
        }
        try {
            mConnection.connect();
        } catch (Exception e) {
            e.printStackTrace();
            stopSelf();
        }
    }

    public void start() {
        if(!mActive) {
            mActive = true;
            if(mThread == null || !mThread.isAlive()) {
                mThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Looper.prepare();
                        mTHandler = new Handler();
                        initConnection();
                        Looper.loop();
                    }
                });
                mThread.start();
            }
        }
    }

    public void stop() {
        mActive = false;
        mTHandler.post(new Runnable() {
            @Override
            public void run() {
                if(mConnection != null) {
                    mConnection.disconnect();
                }
            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        start();
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stop();
    }
}
