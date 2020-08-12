package com.geurimsoft.gmessenger.view.userList;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.geurimsoft.gmessenger.R;
import com.geurimsoft.gmessenger.data.UserList;
import com.geurimsoft.gmessenger.view.chat.ChatActivity;

import java.util.ArrayList;

public class UserListRvAdapter extends RecyclerView.Adapter<UserListRvAdapter.ItemViewHolder> {
    private ArrayList<UserList> mUserLlist;
    private Context context;

    public UserListRvAdapter(Context context, ArrayList<UserList> data) {
        mUserLlist = data;
        this.context = context;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View holderView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);
        return new ItemViewHolder(holderView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemViewHolder holder, int position) {
        UserList item = mUserLlist.get(position);
        holder.tv_UserListItem.setText(item.getJid());

        holder.tv_UserListItem.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("ID", holder.tv_UserListItem.getText().toString());
                Log.d("DEBUG", "context : " + context);
                Log.d("DEBUG", "holder.tv_UserListItem.getText().toString() : " + holder.tv_UserListItem.getText().toString());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUserLlist.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tv_UserListItem;

        public ItemViewHolder(View view) {
            super(view);
            tv_UserListItem = itemView.findViewById(R.id.tv_UserListItem);
        }
    }
}
