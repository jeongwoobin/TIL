package com.geurimsoft.gmessenger.view.chatList;

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
import com.geurimsoft.gmessenger.data.AppConfig;
import com.geurimsoft.gmessenger.data.UserList;
import com.geurimsoft.gmessenger.view.chat.ChatActivity;

import java.util.ArrayList;

public class ChatListRvAdapter extends RecyclerView.Adapter<ChatListRvAdapter.ItemViewHolder>
{

    private ArrayList<UserList> mUserList;
    private Context context;

    /**
     * UserListRvAdapter 생성자
     * @param context 인텐트에 사용
     * @param data RecyclerView 에 userListItem 으로 UserList 데이터 넣기위해 사용
     */
    public ChatListRvAdapter(Context context, ArrayList<UserList> data)
    {

        mUserList = data;
        this.context = context;

    }

    /**
     * ItemViewHolder 메소드로 ViewHolder 생성
     * @param viewGroup 부모 뷰, Inflate 하는데 사용
     * @param viewType 뷰 타입
     * @return ItemViewHolder ItemViewHolder 객체 생성하여 리턴
     */
    @NonNull
    @Override
    public ChatListRvAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType)
    {

        View holderView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.chatlistitem, viewGroup, false);
        return new ChatListRvAdapter.ItemViewHolder(holderView);

    }

    /**
     * 생성된 ViewHolder 에 데이터 바인딩
     * position에 해당하는 데이터를 ViewHolder 의 itemView 에 표시
     * 클릭한 위젯 text 인텐트에 담아 ChatActivity.class 로 전달
     * @param holder 레이아웃 위젯 사용하기위해
     * @param position 클릭한 위젯 position 값으로 userListItem 의 위젯 데이터 받아옴
     */
    @Override
    public void onBindViewHolder(@NonNull final ChatListRvAdapter.ItemViewHolder holder, int position)
    {

        UserList item = mUserList.get(position);
        holder.tv_ChatListItem.setText(item.getJid());

        holder.tv_ChatListItem.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view)
            {

                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("ID", holder.tv_ChatListItem.getText().toString());
                Log.d(AppConfig.APP_DEBUG, this.getClass().getName() + " : onBindViewHolder : context = " + context);
                Log.d(AppConfig.APP_DEBUG, this.getClass().getName() + " : onBindViewHolder : holder.tv_UserListItem.getText().toString() = " + holder.tv_ChatListItem.getText().toString());

                context.startActivity(intent);
            }
        });

    }

    /**
     * 전체 userListItem 수
     * @return Int 전체 userListItem 수
     */
    @Override
    public int getItemCount()
    {
        return mUserList.size();
    }

    /**
     * ItemViewHOlder.class
     * 기능 : ViewHolder 상속받아 구현
     */
    public static class ItemViewHolder extends RecyclerView.ViewHolder
    {

        TextView tv_ChatListItem;

        /**
         * ItemViewHolder 생성자
         * 레이아웃 위젯 연결
         * @param view ViewHolder 내의 View(위젯)
         */
        public ItemViewHolder(View view)
        {

            super(view);
            tv_ChatListItem = itemView.findViewById(R.id.tv_ChatListItem);

        }

    }

}