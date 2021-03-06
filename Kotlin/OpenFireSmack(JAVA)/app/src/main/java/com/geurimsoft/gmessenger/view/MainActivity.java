/**
 * gMessenger
 * 작성자 : 정우빈
 * 20200812 (최초생성) Kotlin 완료(1:1 채팅 메시지 송수신 구현 완료)
 * 20200813 (수정) Kotlin to Java 완료
 * 20200814 (추가) 주석추가 완료
 * 20200818 (수정) 주석수정 완료
 * 20200819 (수정) UserList, ChatList 를 Fragment 로 변경 후 ViewPaging 완료
 * 20200820 (추가) GroupChat 추가중
 * 20200821 (추가) GroupChat PC 에서 송신한 메시지 Mobile 에서 수신 가능
 *
 * 기능 : 메신저기능(텍스트 송,수신 가능)
 *
 * 버그 : 뒤로가기로 종료하면 정상적으로 서버에서 로그아웃 됨, BUT ChatView 에서 강제종료하면 서버에서 로그아웃 안되고있다가 시간이 오래 지나면 로그아웃 됨
 */

// Todo:
//  1) 서버와 DB 연결이 되어있으므로 회원가입시 테이블 업데이트 및 ValidCheck(이메일형식 등)
//  2) 로그인시 ValidCheck
//  3) DB 에서 UserList 받아오기
//  4) (할 수 있다면) UserList 에서 ONLINE, OFFLINE 표시하고 탭 나누기
//  5) (할 수 있다면) 앱 다시시작하더라도 기존 채팅 유지
//          서버 : monitoring 플러그인 -> 채팅 저장
//          단말 : 플러그인 사용불가
//  6) 채팅 삭제 기능
//  7) 이미지 송수신 기능
//  8) 그룹채팅 기능
//  9) pc와 동시 로그인되어있을 시 모바일에서 채팅 안나옴
//  10) 메인서버(스키마 15개)와 openfire서버(스키마 7개) 동기화
//          1 - 사용자정의 DB 통합모드 : 외부 DB 읽기가능
//                  -> 메인서버 DB 읽어서 openfire DB에 저장(동일 스키마만) : 메인서버 DB 탐색용, 로그인용 ( 메인서버 DB로 openfire 동기화)
//                  -> 어차피 메인서버 DB 읽어서 openfire에서 사용하면 되지않을까..
//          2 - 메인, openfire 둘다 존재하는 스키마 테이블끼리는 동기화 후 나머지 테이블 각각 만들어서 동기화?

/**
 * MainActivity.class
 * 기능 : 로그인 data 저장 및 ChatConnectionService  service 시작
 */

package com.geurimsoft.gmessenger.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.geurimsoft.gmessenger.R;
import com.geurimsoft.gmessenger.data.AppConfig;
import com.geurimsoft.gmessenger.service.ChatConnectionService;
import com.geurimsoft.gmessenger.view.userList.UserListActivity;

public class MainActivity extends AppCompatActivity
{

    private BroadcastReceiver mBroadcastReceiver;
    private Button btn_SignIn;
    private EditText et_Id;
    private EditText et_Pw;

    /**
     * 레이아웃 위젯 연결
     * btn_SignIn 클릭시 로그인정보 저장메소드(saveCredentialsAndLogin) 호출
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(AppConfig.APP_DEBUG, this.getClass().getName() + " : onCreate()");

        et_Id = findViewById(R.id.et_Id);
        et_Pw = findViewById(R.id.et_Pw);
        btn_SignIn = findViewById(R.id.btn_SignIn);

        btn_SignIn.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view)
            {
                saveCredentialsAndLogin();
            }
        });

    }

    /**
     * Receiver 등록해제하는 unregisterReceiver 호출
     */
    @Override
    protected void onPause()
    {

        super.onPause();
        Log.d(AppConfig.APP_DEBUG, this.getClass().getName() + " : onPause()");

        this.unregisterReceiver(mBroadcastReceiver);

    }

    /**
     * BroadcastReceiver와 인텐트 필터를 이용하여 Receiver 등록
     * ChatConnection에서 인증 완료되면 switch문에서 해당 case로 넘어감 -> UserListActivity.class 실행
     */
    @Override
    protected void onResume()
    {

        super.onResume();
        Log.d(AppConfig.APP_DEBUG, this.getClass().getName() + " : onResume()");

        mBroadcastReceiver = new BroadcastReceiver()
        {

            @Override
            public void onReceive(Context context, Intent intent)
            {

                String action = intent.getAction();

                switch(action)
                {
                    case ChatConnectionService.UI_AUTHENTICATED:
                        Log.d(AppConfig.APP_DEBUG, this.getClass().getName() + " : Got a broadcast to show the main app window");

                        //Show the main app window
                        Intent i2 = new Intent(getApplicationContext(), UserListActivity.class);
                        startActivity(i2);
                        finish();
                        break;
                }

            }

        };

        IntentFilter filter = new IntentFilter(ChatConnectionService.UI_AUTHENTICATED);
        this.registerReceiver(mBroadcastReceiver, filter);

    }

    /**
     * Preference에 로그인정보 저장
     * ChatConnectionService 시작(로그인 성공하면 ChatConnectionService에서 MessageReceiver 세팅, 로그인 인증완료)
     */
    private void saveCredentialsAndLogin()
    {

        Log.d(AppConfig.APP_DEBUG, this.getClass().getName() + " : saveCredentialsAndLogin() called.");

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.edit()
                .putString("xmpp_mId", et_Id.getText().toString())
                .putString("xmpp_mPw", et_Pw.getText().toString())
                .putBoolean("xmpp_logged_in", true)
                .apply();

        Intent i1 = new Intent(getApplicationContext(), ChatConnectionService.class);
        Log.d(AppConfig.APP_DEBUG, this.getClass().getName() + " : ChatConnectionService start");
        startService(i1);

    }
}