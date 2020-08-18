/**
 * UserListActivity.class
 * 기능 : UserList 에 유저정보 저장 후 RecyclerView 세팅
 */

package com.geurimsoft.gmessenger.view.userList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.geurimsoft.gmessenger.R;
import com.geurimsoft.gmessenger.data.App_Debug;
import com.geurimsoft.gmessenger.data.UserList;
import com.geurimsoft.gmessenger.service.ChatConnection;
import com.geurimsoft.gmessenger.service.ChatConnectionService;

import java.util.ArrayList;

public class UserListActivity extends AppCompatActivity
{

    private ArrayList<UserList> item = new ArrayList<>();
    private RecyclerView rv_UserList;

    /**
     * 레이아웃 위젯 연결
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        Log.d(App_Debug.APP_DEBUG, this.getClass().getName() + " : onCreate()");
        setContentView(R.layout.activity_user_list);

        rv_UserList = findViewById(R.id.rv_UserList);
    }

    /**
     * 유저데이터 저장을 위한 setData 호출
     * RecyclerView 를 위한 LayoutManager, Adapter 세팅
     */
    @Override
    protected void onResume()
    {

        super.onResume();
        Log.d(App_Debug.APP_DEBUG, this.getClass().getName() + " : onResume()");

        setData();

        rv_UserList.setLayoutManager(new LinearLayoutManager(this));
        rv_UserList.setAdapter(new UserListRvAdapter(this, item));
    }

    /**
     * 액티비티 Destroy 시 기존 실행되던 service 중단
     * 액티비티 종료
     */
    @Override
    protected void onDestroy()
    {

        super.onDestroy();
        Log.d(App_Debug.APP_DEBUG, this.getClass().getName() + " : onDestroy()");

        Intent i1 = new Intent(getApplicationContext(), ChatConnectionService.class);
        Log.d(App_Debug.APP_DEBUG, this.getClass().getName() + " : ChatConnectionService stop");
        stopService(i1);

        finish();
    }

    /**
     * UserList 에 유저데이터 저장
     */
    private void setData()
    {

        Log.d(App_Debug.APP_DEBUG, this.getClass().getName() + " : setData()");

        for(int i = 1; i <6; i++)
        {

            UserList ul = new UserList("user" + i + "@localhost");
            item.add(ul);
        }
    }
}