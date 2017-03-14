package com.example.quest.hmi_demo.phone.utile;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.quest.hmi_demo.phone.fragments.ContactFragment;
import com.example.quest.hmi_demo.phone.fragments.FavoriteFragment;
import com.example.quest.hmi_demo.phone.fragments.KeypadFragment;
import com.example.quest.hmi_demo.phone.fragments.RecentFragment;

/**
 * Created by quest on 23/2/17.
 */
public class ContactsPagerAdapter extends FragmentPagerAdapter {
    int mNumOfTabs;

    public ContactsPagerAdapter(FragmentManager fm, int NumOfTabs){
        super(fm);
        this.mNumOfTabs = NumOfTabs;

    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                RecentFragment recentFragment = new RecentFragment();
                return recentFragment;
            case 1:
                ContactFragment contactFragment = new ContactFragment();
                return contactFragment;
            case 2:
                FavoriteFragment favoriteFragment = new FavoriteFragment();
                return favoriteFragment;
            case 3:
                KeypadFragment keypadFragment = new KeypadFragment();
                return keypadFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
