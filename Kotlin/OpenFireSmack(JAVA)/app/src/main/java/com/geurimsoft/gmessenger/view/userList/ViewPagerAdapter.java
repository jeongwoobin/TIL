package com.geurimsoft.gmessenger.view.userList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.geurimsoft.gmessenger.view.chatList.ChatListFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private final int NUM = 2;

    public ViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                return new UserListFragment();
            case 1:
                return new ChatListFragment();
            default:
                return new ChatListFragment();
        }
    }

    @Override
    public int getCount() {
        return NUM;
    }
}
