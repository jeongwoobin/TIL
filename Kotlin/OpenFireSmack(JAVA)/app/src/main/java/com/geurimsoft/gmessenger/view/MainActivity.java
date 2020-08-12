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
    private Context mContext;
    private Button btn_SignIn;
    private EditText et_Id;
    private EditText et_Pw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_Id = findViewById(R.id.et_Id);
        et_Pw = findViewById(R.id.et_Pw);
        btn_SignIn = findViewById(R.id.btn_SignIn);

        btn_SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveCredentialsAndLogin();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                switch(action) {
                    case ChatConnectionService.UI_AUTHENTICATED:
                        Log.d("DEBUG", "MainActivity : Got a broadcast to show the main app window");
                        //Show the main app window
                        Intent i2 = new Intent(mContext, UserListActivity.class);
                        startActivity(i2);
                        finish();
                        break;
                }
            }
        };
        IntentFilter filter = new IntentFilter(ChatConnectionService.UI_AUTHENTICATED);
        this.registerReceiver(mBroadcastReceiver, filter);
    }

    private void saveCredentialsAndLogin() {
        Log.d("DEBUG", "MainActivity : saveCredentialsAndLogin() called.");

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.edit()
                .putString("xmpp_mId", et_Id.getText().toString())
                .putString("xmpp_mPw", et_Pw.getText().toString())
                .putBoolean("xmpp_logged_in", true)
                .apply();

        Intent i1 = new Intent(this, ChatConnectionService.class);
        Log.d("DEBUG", "MainActivity : ChatConnectionService start");
        startService(i1);
    }
}