package com.example.me.neexoapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by m.elshaeir on 2/25/2018.
 */

class SectionPagerAdapter extends FragmentPagerAdapter {
    public SectionPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                RequsetsFragment requsetsFragment = new RequsetsFragment();
                return requsetsFragment;
            case 1:
                ChatFragment chatFragment = new ChatFragment();
                return chatFragment;
            case 2:
                FriendsFragment friendsFragment = new FriendsFragment();
                return friendsFragment;
             default:
                 return null;

        }


    }

    @Override
    public int getCount() {
        return 3;
    }
    public CharSequence getPageTitle(int position)
    {
        switch (position)
        {
            case 0:
                return "Requests";
            case 1:
                return "Chats";
            case 2:
                return "Friends";
            default:
                return null;

        }
    }
}
