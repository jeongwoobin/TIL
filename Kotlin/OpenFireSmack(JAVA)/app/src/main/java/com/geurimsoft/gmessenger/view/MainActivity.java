/**
 * gMessenger
 * 작성자 : 정우빈
 * 20200812 (최초생성) Kotlin 완료
 * 20200813 (수정) Kotlin to Java 완료
 *
 * 버그 : 뒤로가기로 종료하면 정상적으로 서버에서 로그아웃 됨, BUT 강제종료하면 서버에서 로그아웃 안되고있다가 시간이 지나면 로그아웃 됨
 *
 * 기능 : 메신저기능(텍스트 송,수신 가능)
 */

/**
 * MainActivity.class
 * 기능 : 로그인 data 저장 및 ChatConnectionService 시작
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
import com.geurimsoft.gmessenger.service.ChatConnectionService;
import com.geurimsoft.gmessenger.view.userList.UserListActivity;

public class MainActivity extends AppCompatActivity {
    BroadcastReceiver mBroadcastReceiver;
    private Button btn_SignIn;
    private EditText et_Id;
    private EditText et_Pw;

    /**
     * @func 레이아웃 위젯 연결
     * @param savedInstanceState
     * @return
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("DEBUG", "MainActivity : onCreate()");

        et_Id = findViewById(R.id.et_Id);
        et_Pw = findViewById(R.id.et_Pw);
        btn_SignIn = findViewById(R.id.btn_SignIn);

        /**
         * @func btn_SignIn 클릭시 로그인정보 저장메소드(saveCredentialsAndLogin) 호출
         * @param view
         * @return
         */
        btn_SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveCredentialsAndLogin();
            }
        });
    }

    /**
     * @func Receiver 등록해제하는 unregisterReceiver 호출
     * @param
     * @return
     */
    @Override
    protected void onPause() {
        super.onPause();
        Log.d("DEBUG", "MainActivity : onPause()");
        this.unregisterReceiver(mBroadcastReceiver);
    }

    /**
     * @func BroadcastReceiver와 인텐트 필터를 이용하여 Receiver 등록
     * @param
     * @return
     */
    @Override
    protected void onResume() {
        super.onResume();
        Log.d("DEBUG", "MainActivity : onResume()");

        /**
         * @func ChatConnection에서 인증 완료되면 switch문에서 해당 case로 넘어감 -> UserListActivity.class 실행
         * @param context, intent
         * @return
         */
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                switch(action) {
                    case ChatConnectionService.UI_AUTHENTICATED:
                        Log.d("DEBUG", "MainActivity : Got a broadcast to show the main app window");
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
     * @func Preference에 로그인정보 저장
     *       ChatConnectionService 시작(로그인 성공하면 ChatConnectionService에서 MessageReceiver 세팅, 로그인 인증완료)
     * @param
     * @return
     */
    private void saveCredentialsAndLogin() {
        Log.d("DEBUG", "MainActivity : saveCredentialsAndLogin() called.");

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.edit()
                .putString("xmpp_mId", et_Id.getText().toString())
                .putString("xmpp_mPw", et_Pw.getText().toString())
                .putBoolean("xmpp_logged_in", true)
                .apply();

        Intent i1 = new Intent(getApplicationContext(), ChatConnectionService.class);
        Log.d("DEBUG", "MainActivity : ChatConnectionService start");
        startService(i1);
    }
}