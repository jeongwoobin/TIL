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

import com.geurimsoft.gmessenger.data.AppConfig;

public class ChatConnectionService extends Service
{

    public static final String UI_AUTHENTICATED = "com.geurimsoft.gmessenger.uiauthenticated";
    public static final String SEND_MESSAGE = "com.geurimsoft.gmessenger.sendmessage";
    public static final String NEW_MESSAGE = "com.geurimsoft.gmessenger.newmessage";
    public static final String GROUP_NEW_MESSAGE = "com.geurimsoft.gmessenger.groupnewmessage";
    public static final String BUNDLE_MESSAGE_BODY = "b_body";
    public static final String BUNDLE_TO = "b_to";
    public static final String BUNDLE_FROM = "b_from";
    public static final String BUNDLE_GROUP_FROM = "b_gfrom";

    public static ChatConnection.ConnectionState sConnectionState;
//    public static ChatConnection.LoggedInState sLoggedInState;

    private boolean mActive = false;
    private Thread mThread = null;
    private Handler mTHandler = null;
    private ChatConnection cConnection;

    /**
     * ChatConnectionService 생성자
     */
    public ChatConnectionService()
    {
        Log.d(AppConfig.APP_DEBUG, this.getClass().getName() + " : ChatConnectionService()");
    }

    /**
     * 현재 연결상태 getter
     * @return ChatConnection.ConnectionState 현재 연결 상태
     */
    public static ChatConnection.ConnectionState getState()
    {

        Log.d(AppConfig.APP_DEBUG, ChatConnectionService.class.getName() + " : getState()");

        if(sConnectionState == null)
        {
            return ChatConnection.ConnectionState.DISCONNECTED;
        }
        else
        {
            return sConnectionState;
        }

    }

//    /**
//     *
//     * @return
//     */
//    public static ChatConnection.LoggedInState getLoggedInState() {
//        Log.d(App_Debug.APP_DEBUG, "ChatConnectionService : getLoggedInState()");
//        if(sLoggedInState == null) {
//            return ChatConnection.LoggedInState.LOGGED_OUT;
//        }
//        else {
//            return sLoggedInState;
//        }
//    }

    /**
     * (콜백메소드) IBinder 객체를 리턴하여 service 와 client 사이의 인터페이스 정의
     * @param intent 바인딩 할 액티비티
     * @return IBinder null
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {

        Log.d(AppConfig.APP_DEBUG, this.getClass().getName() + " : onBind()");
        return null;

    }

    /**
     * 액티비티 최초생성
     */
    @Override
    public void onCreate()
    {

        super.onCreate();
        Log.d(AppConfig.APP_DEBUG, this.getClass().getName() + " : onCreate()");

    }

    /**
     * ChatConnection 객체생성 후 서버연결
     */
    private void initConnection()
    {

        Log.d(AppConfig.APP_DEBUG, this.getClass().getName() + " : initConnection()");

        if(cConnection == null)
        {
            cConnection = new ChatConnection(this);
        }

        try
        {
            cConnection.connect();
        }
        catch (Exception e)
        {

            e.printStackTrace();
            Log.d(AppConfig.APP_DEBUG, this.getClass().getName() + " : initConnection() : catch = " + e.toString());
            stopSelf();

        }

    }

    /**
     * 스레드 생성 및 시작
     * initConnection 호출하여 서버연결
     */
    public void start()
    {

        Log.d(AppConfig.APP_DEBUG, this.getClass().getName() + " : start()");

        if(!mActive)
        {

            mActive = true;

            if(mThread == null || !mThread.isAlive())
            {

                mThread = new Thread(new Runnable()
                {

                    @Override
                    public void run()
                    {

                        Log.d(AppConfig.APP_DEBUG, this.getClass().getName() + " : start() : run()");
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
     * 스레드 중단 및 서버연결 해제
     */
    public void stop()
    {

        Log.d(AppConfig.APP_DEBUG, this.getClass().getName() + " : stop()");

        mActive = false;
        mTHandler.post(new Runnable()
        {

            @Override
            public void run()
            {

                Log.d(AppConfig.APP_DEBUG, this.getClass().getName() + " : stop() : run()");

                if(cConnection != null)
                {

                    Log.d(AppConfig.APP_DEBUG, this.getClass().getName() + " : stop() : run() : disconnect()");
                    cConnection.disconnect();

                }

            }

        });

    }

    /**
     * service 시작할 때 호출되는 함수
     * @param intent
     * @param flags 재실행 모드를 구분하기 위해 flags 사용
     * @param startId (서비스 구분자) 서비스를 종료할 때 사용
     * @return Service.START_STICKY (재실행 모드)명시적으로 중지될 때 까지 service 영원히 실행 알림
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {

        Log.d(AppConfig.APP_DEBUG, this.getClass().getName() + " : onStartCommand()");
        start();
        return Service.START_STICKY;

    }

    /**
     * 액티비티 종료시 호출
     * service 중단
     */
    @Override
    public void onDestroy()
    {

        super.onDestroy();
        Log.d(AppConfig.APP_DEBUG, this.getClass().getName() + " : onDestroy()");
        stop();

    }

}
