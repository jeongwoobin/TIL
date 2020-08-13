/**
 * UserListActivity.class
 * 기능 : UserList에 유저정보 저장 후 RecyclerView 세팅
 */

package com.geurimsoft.gmessenger.view.userList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.geurimsoft.gmessenger.R;
import com.geurimsoft.gmessenger.data.UserList;
import com.geurimsoft.gmessenger.service.ChatConnection;
import com.geurimsoft.gmessenger.service.ChatConnectionService;

import java.util.ArrayList;

public class UserListActivity extends AppCompatActivity {
    private ArrayList<UserList> item = new ArrayList<UserList>();
    private RecyclerView rv_UserList;

    /**
     * @func 레이아웃 위젯 연결
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("DEBUG", "UserListActivity : onCreate()");
        setContentView(R.layout.activity_user_list);
        rv_UserList = findViewById(R.id.rv_UserList);
    }

    /**
     * @func 유저데이터 저장을 위한 setData 호출
     *       RecyclerView를 위한 layoutmanager, adapter 세팅
     * @param
     * @return
     */
    @Override
    protected void onResume() {
        super.onResume();
        Log.d("DEBUG", "UserListActivity : onResume()");

        setData();

        rv_UserList.setLayoutManager(new LinearLayoutManager(this));
        rv_UserList.setAdapter(new UserListRvAdapter(this, item));
    }

    /**
     * @func 액티비티 destroy시 기존 실행되던 service 중단
     *       액티비티 종료
     * @param
     * @return
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("DEBUG", "UserListActivity : onDestroy()");
        Intent i1 = new Intent(getApplicationContext(), ChatConnectionService.class);
        Log.d("DEBUG", "UserListActivity : ChatConnectionService stop");
        stopService(i1);

        finish();
    }

    /**
     * @func UserList에 유저데이터 저장
     * @param
     * @return
     */
    private void setData() {
        Log.d("DEBUG", "UserListActivity : setData()");
        for(int i = 1; i <6; i++) {
            UserList ul = new UserList("user" + i + "@localhost");
            item.add(ul);
        }
    }
}