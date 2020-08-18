/**
 * UserListRvAdapter.class
 * 기능 : RecyclerView 와 데이터 바인딩
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
import com.geurimsoft.gmessenger.data.App_Debug;
import com.geurimsoft.gmessenger.data.UserList;
import com.geurimsoft.gmessenger.view.chat.ChatActivity;

import java.util.ArrayList;

public class UserListRvAdapter extends RecyclerView.Adapter<UserListRvAdapter.ItemViewHolder>
{

    private ArrayList<UserList> mUserList;
    private Context context;

    /**
     * UserListRvAdapter 생성자
     * @param context 인텐트에 사용
     * @param data RecyclerView 에 item 으로 UserList 데이터 넣기위해 사용
     */
    public UserListRvAdapter(Context context, ArrayList<UserList> data)
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
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType)
    {

        View holderView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);
        return new ItemViewHolder(holderView);
    }

    /**
     * 생성된 ViewHolder 에 데이터 바인딩
     * position에 해당하는 데이터를 ViewHolder 의 itemView 에 표시
     * 클릭한 위젯 text 인텐트에 담아 ChatActivity.class 로 전달
     * @param holder 레이아웃 위젯 사용하기위해
     * @param position 클릭한 위젯 position 값으로 item 의 위젯 데이터 받아옴
     */
    @Override
    public void onBindViewHolder(@NonNull final ItemViewHolder holder, int position)
    {

        UserList item = mUserList.get(position);
        holder.tv_UserListItem.setText(item.getJid());

        holder.tv_UserListItem.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view)
            {

                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("ID", holder.tv_UserListItem.getText().toString());
                Log.d(App_Debug.APP_DEBUG, this.getClass().getName() + " : onBindViewHolder : context = " + context);
                Log.d(App_Debug.APP_DEBUG, this.getClass().getName() + " : onBindViewHolder : holder.tv_UserListItem.getText().toString() = " + holder.tv_UserListItem.getText().toString());

                context.startActivity(intent);
            }
        });
    }

    /**
     * 전체 item 수
     * @return Int 전체 item 수
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

        TextView tv_UserListItem;

        /**
         * ItemViewHolder 생성자
         * 레이아웃 위젯 연결
         * @param view ViewHolder 내의 View(위젯)
         */
        public ItemViewHolder(View view)
        {

            super(view);
            tv_UserListItem = itemView.findViewById(R.id.tv_UserListItem);
        }
    }
}
