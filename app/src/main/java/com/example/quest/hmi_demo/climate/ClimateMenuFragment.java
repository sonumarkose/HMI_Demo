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

import com.example.quest.hmi_demo.R;

/**
 * Description: The class ClimateMenuFragment is used for menu panel view of Climate module
 * @author Resmi
 * @version 1.0
 */
public class ClimateMenuFragment extends Fragment {

    //----------------------------------------------------------------------------------------------
    // Member Variables
    //----------------------------------------------------------------------------------------------

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
        super.onCreateView(inflater, container, savedInstanceState);
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.content_climate_menu, container, false);
        } catch (InflateException e) {
            e.printStackTrace();
        }
        return view;
    }
}
