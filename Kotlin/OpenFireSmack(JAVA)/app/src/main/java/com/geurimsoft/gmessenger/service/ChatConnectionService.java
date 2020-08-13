// 20200812 내일 검토해야함
// 수신 안됨


package com.geurimsoft.gmessenger.service;


import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;

import org.jivesoftware.smack.chat2.Chat;

public class ChatConnectionService extends Service {
    public static final String UI_AUTHENTICATED = "com.geurimsoft.gmessenger.uiauthenticated";
    public static final String SEND_MESSAGE = "com.geurimsoft.gmessenger.sendmessage";
    public static final String NEW_MESSAGE = "com.geurimsoft.gmessenger.newmessage";
    public static final String BUNDLE_MESSAGE_BODY = "b_body";
    public static final String BUNDLE_TO = "b_to";
    public static final String BUNDLE_FROM = "b_from";

    public static ChatConnection.ConnectionState sConnectionState;
    public static ChatConnection.LoggedInState sLoggedInState;

    private boolean mActive = false;
    private Thread mThread = null;
    private Handler mTHandler = null;
    private ChatConnection cConnection;

    public ChatConnectionService() {
        Log.d("DEBUG", "ChatConnectionService : ChatConnectionService()");

    }

    public static ChatConnection.ConnectionState getState() {
        Log.d("DEBUG", "ChatConnectionService : getState()");
        if(sConnectionState == null) {
            return ChatConnection.ConnectionState.DISCONNECTED;
        } else {
            return sConnectionState;
        }
    }

    public static ChatConnection.LoggedInState getLoggedInState() {
        Log.d("DEBUG", "ChatConnectionService : getLoggedInState()");
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
        Log.d("DEBUG", "ChatConnectionService : onBind()");
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("DEBUG", "ChatConnectionService : onCreate()");
    }

    private void initConnection() {
        Log.d("DEBUG", "ChatConnectionService : initConnection()");
        if(cConnection == null) {
            cConnection = new ChatConnection(this);
        }
        try {
            cConnection.connect();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("DEBUG", "ChatConnectionService : initConnection() - catch : " + e.toString());
            stopSelf();
        }
    }

    public void start() {
        Log.d("DEBUG", "ChatConnectionService : start()");
        if(!mActive) {
            mActive = true;
            if(mThread == null || !mThread.isAlive()) {
                mThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("DEBUG", "ChatConnectionService : start() - run()");
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
        Log.d("DEBUG", "ChatConnectionService : stop()");
        mActive = false;
        mTHandler.post(new Runnable() {
            @Override
            public void run() {
                Log.d("DEBUG", "ChatConnectionService : stop() - run()");
                if(cConnection != null) {
                    Log.d("DEBUG", "ChatConnectionService : stop() - run() - disconnect()");
                    cConnection.disconnect();
                }
            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("DEBUG", "ChatConnectionService : onStartCommand()");
        start();
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("DEBUG", "ChatConnectionService : onDestroy()");
        stop();
    }
}
