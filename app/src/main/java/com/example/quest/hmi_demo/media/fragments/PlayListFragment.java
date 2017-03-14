package com.example.quest.hmi_demo.media.fragments;


import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.quest.hmi_demo.R;
import com.example.quest.hmi_demo.climate.ClimateMinimisedFragment;
import com.example.quest.hmi_demo.homescreen.MainActivityFragmentA;
import com.example.quest.hmi_demo.media.bean_classes.Pager;

import java.util.ArrayList;

/**
 * Description: The class SongService is used for saving song list from the device in Media module
 * @author Sonu
 * @version 1.0
 * Created  on 1/3/17.
 */
public class PlayListFragment extends Fragment implements TabLayout.OnTabSelectedListener, MainActivityFragmentA.MediaSelectedListener {

    //----------------------------------------------------------------------------------------------
    // Member Variables
    //----------------------------------------------------------------------------------------------

    private final int screenHeight = getScreenHeight();
    private final int height = ((screenHeight * 4) / 5);
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private LinearLayout playListFragment = null;

    //----------------------------------------------------------------------------------------------
    // Member Methods
    //----------------------------------------------------------------------------------------------

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.media_playlist_fragment, container, false);
        playListFragment = (LinearLayout) view.findViewById(R.id.listFragment);
        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        viewPager = (ViewPager) view.findViewById(R.id.pager);
        playListFragment.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height));
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();
        Fragment fragmenta = new MainActivityFragmentA();
        fragmenta.setArguments(null);

        //Initializing the tablayout


        //Adding the tabs using addTab() method
        tabLayout.addTab(tabLayout.newTab().setText("Songs"));
        tabLayout.addTab(tabLayout.newTab().setText("Albums"));
        tabLayout.addTab(tabLayout.newTab().setText("Artist"));

        // tabLayout.addTab(tabLayout.newTab().setText("Favourites"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        //Initializing viewPager


        //Creating our pager adapter
        Pager adapter = new Pager(getFragmentManager(), tabLayout.getTabCount());

        //Adding adapter to pager
        viewPager.setAdapter(adapter);

        //Adding onTabSelectedListener to swipe views
        tabLayout.setOnTabSelectedListener(this);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.getTabAt(0).setIcon(R.drawable.songs);
        tabLayout.getTabAt(1).setIcon(R.drawable.media_albums);
        tabLayout.getTabAt(2).setIcon(R.drawable.artist);

    }

    /**
     * Called when a tab enters the selected state.
     *
     * @param tab The tab that was selected
     */
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());

    }

    /**
     * Called when a tab exits the selected state.
     *
     * @param tab The tab that was unselected
     */
    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    /**
     * Called when a tab that is already selected is chosen again by the user. Some applications
     * may use this action to return to the top level of a category.
     *
     * @param tab The tab that was reselected.
     */
    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void mediaSelected(String state) {

       /* if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        }
        ClimateMinimisedFragment climateMinimisedFragment = new ClimateMinimisedFragment();
        getFragmentManager().beginTransaction()
                .replace(R.id.fl_main_b, climateMinimisedFragment).commit();*/
    }

    @Override
    public void onResume() {
        super.onResume();
        Pager adapter = new Pager(getFragmentManager(), tabLayout.getTabCount());
        //Adding adapter to pager
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
