package com.example.openfiresmack.view.userList

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.openfiresmack.R
import com.example.openfiresmack.data.UserList
import com.example.openfiresmack.view.chat.ChatActivity
import kotlinx.android.synthetic.main.item.view.*

class UserListRvAdapter (val context: Context, val data: ArrayList<UserList>) :
    RecyclerView.Adapter<UserListRvAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int) = ItemViewHolder(viewGroup)

    override fun onBindViewHolder(holder: ItemViewHolder, i: Int) {
        data[i].let { item ->
            with(holder) {
                tv_UserListItem.text = item.name

                tv_UserListItem.setOnClickListener {
                    // 채팅시작
                    Log.d("DEBUG", "tv_UserListItem : " + tv_UserListItem.text.toString())
                    val intent = Intent(context, ChatActivity::class.java)
                    intent.putExtra("ID", tv_UserListItem.text.toString())
                    context.startActivity(intent)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return if (data.isNotEmpty()) {
            data.size
        } else {
            0
        }
    }

    inner class ItemViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item, parent, false)
    ) {
        val tv_UserListItem = itemView.tv_UserListItem
    }
}