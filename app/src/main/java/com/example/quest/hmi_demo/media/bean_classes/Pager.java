package com.example.quest.hmi_demo.media.bean_classes;

/**
 * Description: The class Pager is used for playlist tab layout in Media module.
 * @author Sonu
 * @version 1.0
 * Created  on 1/3/17.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.quest.hmi_demo.media.list_classes.AlbumList;
import com.example.quest.hmi_demo.media.list_classes.ArtistList;
import com.example.quest.hmi_demo.media.list_classes.SongsList;

/**
 * Created by Belal on 2/3/2016.
 */
//Extending FragmentStatePagerAdapter
public class Pager extends FragmentStatePagerAdapter {

    //----------------------------------------------------------------------------------------------
    // Member Variables
    //----------------------------------------------------------------------------------------------

    //integer to count number of tabs
    int tabCount;

    //----------------------------------------------------------------------------------------------
    // Member Methods
    //----------------------------------------------------------------------------------------------

    //Constructor to the class
    public Pager(FragmentManager fm, int tabCount) {
        super(fm);
        //Initializing tab count
        this.tabCount = tabCount;
    }

    //Overriding method getItem
    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
            case 0:
                SongsList tab1 = new SongsList();
                return tab1;
            case 1:
                AlbumList tab2 = new AlbumList();
                return tab2;
            case 2:
                ArtistList tab3 = new ArtistList();
                return tab3;
            default:
                return null;
        }
    }

    //Overriden method getCount to get the number of tabs
    @Override
    public int getCount() {
        return tabCount;
    }


}
