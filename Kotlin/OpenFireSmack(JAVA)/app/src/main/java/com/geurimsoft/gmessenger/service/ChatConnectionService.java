/**
 * ChatConnectionService.class
 * 기능 : service 기능
 *        백그라운드 서버실행을 위한 스레드 관리
 */

package com.geurimsoft.gmessenger.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;

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

    /**
     * @func ChatConnectionService 생성자
     * @param 
     * @return 
     */
    public ChatConnectionService() {
        Log.d("DEBUG", "ChatConnectionService : ChatConnectionService()");
    }

    /**
     * @func 현재 연결상태 getter
     * @param 
     * @return sConnectionState
     */
    public static ChatConnection.ConnectionState getState() {
        Log.d("DEBUG", "ChatConnectionService : getState()");
        if(sConnectionState == null) {
            return ChatConnection.ConnectionState.DISCONNECTED;
        } else {
            return sConnectionState;
        }
    }

//    /**
//     * @func 
//     * @param 
//     * @return 
//     */
//    public static ChatConnection.LoggedInState getLoggedInState() {
//        Log.d("DEBUG", "ChatConnectionService : getLoggedInState()");
//        if(sLoggedInState == null) {
//            return ChatConnection.LoggedInState.LOGGED_OUT;
//        }
//        else {
//            return sLoggedInState;
//        }
//    }

    /**
     * @func  service와 client 사이의 인터페이스 기능
     * @param intent
     * @return null
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("DEBUG", "ChatConnectionService : onBind()");
        return null;
    }

    /**
     * @func 액티비티 최초생성
     * @param 
     * @return 
     */
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("DEBUG", "ChatConnectionService : onCreate()");
    }

    /**
     * @func ChatConnection 객체생성 후 서버연결
     * @param
     * @return
     */
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

    /**
     * @func 스레드 생성 및 시작
     *       initConnection 호출하여 서버연결
     * @param 
     * @return 
     */
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

    /**
     * @func 스레드 중단 및 서버연결 해제
     * @param 
     * @return 
     */
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

    /**
     * @func service 시작할 때 호출되는 함수
     *       return START_STICKY : 명시적으로 중지될 때 까지 service 영원히 실행 알림
     * @param intent, flags, startId
     * @return Service.START_STICKY
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("DEBUG", "ChatConnectionService : onStartCommand()");
        start();
        return Service.START_STICKY;
    }

    /**
     * @func 액티비티 종료시 호출
     *       service 중단
     * @param
     * @return
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("DEBUG", "ChatConnectionService : onDestroy()");
        stop();
    }
}
