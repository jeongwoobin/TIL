package com.example.openfiresmack.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.openfiresmack.R
import com.example.openfiresmack.view.userList.UserListActivity
import kotlinx.android.synthetic.main.activity_main.*

lateinit var intent: Intent

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_SignIn.setOnClickListener {
            intent = Intent(this, UserListActivity::class.java)
            startActivity(intent)
        }

        btn_SignUp.setOnClickListener {
            intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }
}