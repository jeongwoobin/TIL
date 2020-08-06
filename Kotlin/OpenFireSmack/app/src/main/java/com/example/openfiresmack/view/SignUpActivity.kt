package com.example.openfiresmack.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.openfiresmack.R
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        btn_SignUpBack.setOnClickListener {
            finish()
        }

        btn_SignUpConfirm.setOnClickListener {
            finish()
        }
    }
}