////////////////////////////////////////////////////////////////////////////////////////////////////
//
//Android Sample Project
//
////////////////////////////////////////////////////////////////////////////////////////////////////
package com.example.quest.hmi_demo.climate;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.example.quest.hmi_demo.R;
import com.example.quest.hmi_demo.homescreen.MainActivityFragmentA;

/**
 * Description: The class ClimateMaximisedFragment is used for the maximised view of Climate module
 * @author achala.nair
 * @version 1.0
 */
public class ClimateMaximisedFragment extends Fragment {

    //----------------------------------------------------------------------------------------------
    // Member Variables
    //----------------------------------------------------------------------------------------------

    private FrameLayout topClimate = null;
    private View view;

    //----------------------------------------------------------------------------------------------
    // Member Methods
    //----------------------------------------------------------------------------------------------

    /**
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.content_climate_maximised, container, false);
            topClimate = (FrameLayout)view.findViewById(R.id.top_climate);
            int screenHeight = MainActivityFragmentA.getScreenHeight();
            topClimate.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,screenHeight * 4 / 5));

        } catch (InflateException e) {
            e.printStackTrace();
        }
        return view;
    }
}
