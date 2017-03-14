////////////////////////////////////////////////////////////////////////////////////////////////////
//
//Android Sample Project
//
////////////////////////////////////////////////////////////////////////////////////////////////////
package com.example.quest.hmi_demo.climate;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.quest.hmi_demo.R;
import com.example.quest.hmi_demo.homescreen.MainActivityFragmentA;

/**
 * Description: The class ClimateExitFragment is used to return to the previous screen from Climate Module
 * @author achala.nair
 * @version 1.0
 */

public class ClimateExitFragment extends Fragment {

    //----------------------------------------------------------------------------------------------
    // Member Variables
    //----------------------------------------------------------------------------------------------

    private View view;
    private Button back;
    private FrameLayout bottomClimate=null;

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
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.content_climate_exit, container, false);

            bottomClimate = (FrameLayout)view.findViewById(R.id.bottom_climate);
            int sh= MainActivityFragmentA.getScreenHeight();
            bottomClimate.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,sh / 5));

            back = (Button)view.findViewById(R.id.back);
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 0) {
                        getActivity().getSupportFragmentManager().popBackStack();
                    }
                    ClimateMinimisedFragment af = new ClimateMinimisedFragment();
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.left_in, R.anim.left_out)
                            .replace(R.id.fl_main_b, af).commit();
                }

            });

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(), ClimateConstants.TOAST, Toast.LENGTH_SHORT).show();
                }
            });
        } catch (InflateException e) {
        e.printStackTrace();
        }
        return view;
    }

   /* public void onDestroyView() {
        super.onDestroyView();
        getActivity().getFragmentManager().beginTransaction().remove(getActivity().getFragmentManager().findFragmentById(R.id.air_distribution_fragment)).commit();
        getActivity().getFragmentManager().beginTransaction().remove(getActivity().getFragmentManager().findFragmentById(R.id.menu_fragment)).commit();
        getActivity().getFragmentManager().beginTransaction().remove(getActivity().getFragmentManager().findFragmentById(R.id.fan_fragment)).commit();
    }*/
}
