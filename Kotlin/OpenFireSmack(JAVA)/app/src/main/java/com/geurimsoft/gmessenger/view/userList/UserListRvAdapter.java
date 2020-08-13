/**
 * UserListRvAdapter.class
 * 기능 : RecyclerView와 데이터 바인딩
 */

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
    private ArrayList<UserList> mUserList;
    private Context context;

    /**
     * @func UserListRvAdapter 생성자
     * @param context, data
     * @return
     */
    public UserListRvAdapter(Context context, ArrayList<UserList> data) {
        mUserList = data;
        this.context = context;
    }

    /**
     * @func ItemViewHolder메소드로 ViewHolder 생성
     * @param viewGroup, i
     * @return ItemViewHolder(holderView)
     */
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View holderView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);
        return new ItemViewHolder(holderView);
    }

    /**
     * @func ViewHolder와 데이터 바인딩
     * @param holder, position
     * @return
     */
    @Override
    public void onBindViewHolder(@NonNull final ItemViewHolder holder, int position) {
        UserList item = mUserList.get(position);
        holder.tv_UserListItem.setText(item.getJid());

        /**
         * @func 클릭한 위젯 text 인텐트에 담아 ChatActivity.class로 전달
         * @param view
         * @return
         */
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

    /**
     * @func 데이터 아이템 수 반환
     * @param
     * @return mUserList.size()
     */
    @Override
    public int getItemCount() {
        return mUserList.size();
    }

    /**
     * ItemViewHOlder.class
     * 기능 : ViewHolder 상속받아 구현
     */
    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tv_UserListItem;

        /**
         * @func ItemViewHolder 생성자(레이아웃 위젯 연결)
         * @param view
         * @return
         */
        public ItemViewHolder(View view) {
            super(view);
            tv_UserListItem = itemView.findViewById(R.id.tv_UserListItem);
        }
    }
}
