package com.geurimsoft.gmessenger.view.userList;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.geurimsoft.gmessenger.R;
import com.geurimsoft.gmessenger.data.AppConfig;
import com.geurimsoft.gmessenger.data.UserList;

import java.util.ArrayList;

import static com.geurimsoft.gmessenger.data.AppConfig.CHAT_ADDR;


public class UserListFragment extends Fragment {

    private ArrayList<UserList> item = new ArrayList();

    private RecyclerView rv_UserList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setData();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup userView =  (ViewGroup) inflater.inflate(R.layout.fragment_userlist, container, false);
        rv_UserList = userView.findViewById(R.id.rv_UserList);
        return userView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setRecycler();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void setRecycler() {
        rv_UserList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_UserList.setAdapter(new UserListRvAdapter(getActivity(), item));
    }

    /**
     * UserList 에 유저데이터 저장
     */
    private void setData()
    {

        Log.d(AppConfig.APP_DEBUG, this.getClass().getName() + " : setData()");

        for(int i = 1; i <6; i++)
        {

            UserList ul = new UserList("user" + i + "@" + CHAT_ADDR);
            item.add(ul);
        }

    }
}
