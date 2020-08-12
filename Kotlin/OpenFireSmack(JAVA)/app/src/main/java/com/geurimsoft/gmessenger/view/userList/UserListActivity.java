package com.geurimsoft.gmessenger.view.userList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.geurimsoft.gmessenger.R;
import com.geurimsoft.gmessenger.data.UserList;
import com.geurimsoft.gmessenger.service.ChatConnectionService;

import java.util.ArrayList;

public class UserListActivity extends AppCompatActivity {
    private ArrayList<UserList> item = new ArrayList<UserList>();
    private RecyclerView rv_UserList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        rv_UserList = findViewById(R.id.rv_UserList);

        setData();

        rv_UserList.setLayoutManager(new LinearLayoutManager(this));
        rv_UserList.setAdapter(new UserListRvAdapter(this, item));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent i1 = new Intent(this, ChatConnectionService.class);
        stopService(i1);

        finish();
    }

    private void setData() {
        for(int i = 1; i <6; i++) {
            UserList ul = new UserList("user" + i + "@localhost");
            item.add(ul);
        }
    }
}