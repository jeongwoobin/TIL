package com.example.openfiresmack.view

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.openfiresmack.R
import com.example.openfiresmack.service.ChatConnectionService
import com.example.openfiresmack.view.chat.ChatActivity
import com.example.openfiresmack.view.userList.UserListActivity
import kotlinx.android.synthetic.main.activity_main.*


lateinit var intent: Intent

class MainActivity : AppCompatActivity() {
    companion object {
//        lateinit var cid = et_Id.text.toString()
    }
    private var mBroadcastReceiver: BroadcastReceiver? = null
    private val mContext: Context? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_SignIn.setOnClickListener {
            saveCredentialsAndLogin()
//            Log.d("DEBUG", "prefId : " + PreferenceManager.getDefaultSharedPreferences(ChatConnection.mApplicationContext).getString("xmpp_mId", null))
//            Log.d("DEBUG", "prefPw : " + PreferenceManager.getDefaultSharedPreferences(ChatConnection.mApplicationContext).getString("xmpp_mPw", null))
            intent = Intent(this, UserListActivity::class.java)

//            intent = Intent(this, ChatActivity::class.java)
//            intent.putExtra("mId", et_Id.text.toString())
            startActivity(intent)
        }

//        btn_SignUp.setOnClickListener {
//            intent = Intent(this, SignUpActivity::class.java)
//            startActivity(intent)
//        }
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(mBroadcastReceiver)
    }

    override fun onResume() {
        super.onResume()
        mBroadcastReceiver = object: BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val action = intent?.action
                when (action) {
                    ChatConnectionService.UI_AUTHENTICATED -> {
                        Log.d("DEBUG", "MainActivity : Got a broadcast to show the main app window")
                        //Show the main app window
                        val i2 = Intent(mContext, UserListActivity::class.java)
                        startActivity(i2)
                        finish()
                    }
                }
            }
        }
        val filter = IntentFilter(ChatConnectionService.UI_AUTHENTICATED)
        this.registerReceiver(mBroadcastReceiver, filter)
    }

    private fun saveCredentialsAndLogin() {
        Log.d("DEBUG", "MainActivity : saveCredentialsAndLogin() called.")
//        val prefs = getSharedPreferences("User", Context.MODE_PRIVATE)
//        prefs.edit().putString("xmpp_mId", et_Id.text.toString())
//        prefs.edit().putString("xmpp_mPw", et_Pw.text.toString())
//        prefs.edit().putBoolean("xmpp_logged_in", true)
//        prefs.edit().apply()

        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        prefs.edit()
            .putString("xmpp_mId", et_Id.text.toString())
            .putString("xmpp_mPw", et_Pw.text.toString())
            .putBoolean("xmpp_logged_in", true)
            .apply()

        //Start the service
        val i1 = Intent(this, ChatConnectionService::class.java)
        Log.d("DEBUG", "MainActivity : ChatConnectionService start")
        startService(i1)
    }
}