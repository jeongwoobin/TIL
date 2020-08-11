package com.example.openfiresmack.view.userList

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.openfiresmack.R
import com.example.openfiresmack.data.UserList
import com.example.openfiresmack.service.ChatConnectionService
import com.example.openfiresmack.view.MainActivity
import kotlinx.android.synthetic.main.activity_user_list.*

class UserListActivity : AppCompatActivity() {
    var item: ArrayList<UserList> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_list)

        setData()

        rv_UserList.layoutManager = LinearLayoutManager(this)
        rv_UserList.adapter = UserListRvAdapter(this, item)
    }

    override fun onDestroy() {
        super.onDestroy()
        val i1 = Intent(this, ChatConnectionService::class.java)
        stopService(i1)

        finish()
    }

    fun setData() {
        // db에서 유저list 받아와서 UserList dataclass에 저장
        for(i in 1..5) {
            item.add(UserList("user$i@localhost"))
        }
    }
}