/**
 * UserListActivity.class
 * 기능 : UserList 에 유저정보 저장 후 RecyclerView 세팅
 */

package com.geurimsoft.gmessenger.view.userList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.geurimsoft.gmessenger.R;
import com.geurimsoft.gmessenger.data.AppConfig;
import com.geurimsoft.gmessenger.data.UserList;
import com.geurimsoft.gmessenger.service.ChatConnectionService;

import java.util.ArrayList;

import static com.geurimsoft.gmessenger.data.AppConfig.CHAT_ADDR;

public class UserListActivity extends AppCompatActivity
{

    private ViewPager vp_UserList;
    private Button btn_Ul, btn_Cl;

    /**
     * 레이아웃 위젯 연결
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        Log.d(AppConfig.APP_DEBUG, this.getClass().getName() + " : onCreate()");
        setContentView(R.layout.activity_user_list);
        vp_UserList = findViewById(R.id.vp_UserList);
        btn_Cl = findViewById(R.id.btn_Cl);
        btn_Ul = findViewById(R.id.btn_Ul);

    }

    /**
     * 유저데이터 저장을 위한 setData 호출
     * RecyclerView 를 위한 LayoutManager, Adapter 세팅
     */
    @Override
    protected void onResume()
    {

        super.onResume();
        Log.d(AppConfig.APP_DEBUG, this.getClass().getName() + " : onResume()");
        setButton();
        setViewPager();

    }

    /**
     * 액티비티 Destroy 시 기존 실행되던 service 중단
     * 액티비티 종료
     */
    @Override
    protected void onDestroy()
    {

        super.onDestroy();
        Log.d(AppConfig.APP_DEBUG, this.getClass().getName() + " : onDestroy()");

        Intent i1 = new Intent(getApplicationContext(), ChatConnectionService.class);
        Log.d(AppConfig.APP_DEBUG, this.getClass().getName() + " : ChatConnectionService stop");
        stopService(i1);

        finish();

    }



    // UI
    private void setViewPager() {
        ViewPager.OnPageChangeListener listener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        onUserListClicked();
                        break;
                    case 1:
                        onChatListClicked();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };

        vp_UserList.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        vp_UserList.removeOnPageChangeListener(listener);
        vp_UserList.addOnPageChangeListener(listener);
    }

    private void setButton() {
        btn_Ul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vp_UserList.setCurrentItem(0);
            }
        });

        btn_Cl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vp_UserList.setCurrentItem(1);
            }
        });
    }

    private void onUserListClicked() {
        // UI 작업
    }
    private void onChatListClicked() {
        // UI 작업
    }

}