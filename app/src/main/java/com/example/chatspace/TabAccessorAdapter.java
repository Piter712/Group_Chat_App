package com.example.chatspace;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

// Adapter class for Access fragment
public class TabAccessorAdapter extends FragmentPagerAdapter {
    private final Context mContext;

    public TabAccessorAdapter(@NonNull FragmentManager fm, Context context) {
        super(fm );
        mContext = context;
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch(position) {

            case 0:
                GroupsFragment groupsFragment = new GroupsFragment();
                return groupsFragment;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch(position) {
            case 0:
                return "Groups";
            default:
                return null;
        }
    }
}
