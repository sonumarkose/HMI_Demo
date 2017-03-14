////////////////////////////////////////////////////////////////////////////////////////////////////
//
//Android Sample Project
//
////////////////////////////////////////////////////////////////////////////////////////////////////
package com.example.quest.hmi_demo.climate;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.quest.hmi_demo.R;
import com.example.quest.hmi_demo.homescreen.MainActivityFragmentA;

/**
 * Description: The class ClimateMinimisedFragment is used for the minimised view of Climate module
 * @author achala.nair
 * @version 1.0
 */
public class ClimateMinimisedFragment extends Fragment {

    //----------------------------------------------------------------------------------------------
    // Member Variables
    //----------------------------------------------------------------------------------------------

    private static ImageView defrost, floor, ventilation;
    private FrameLayout bottom_main =null;
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
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.content_climate_minimised, container, false);
        bottom_main = (FrameLayout)view.findViewById(R.id.bottom_main);
        int screenHeight = MainActivityFragmentA.getScreenHeight();
        bottom_main.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, screenHeight / 5));
        show();

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v != null) {
                    ClimateMaximisedFragment climate = new ClimateMaximisedFragment();
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.bottom_in, R.anim.top_out, R.anim.top_in, R.anim.bottom_out)
                            .replace(R.id.fl_main_a, climate)
                            .addToBackStack(ClimateConstants.NIL)
                            .commit();
                    ClimateExitFragment exit = new ClimateExitFragment();
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.right_in, R.anim.right_out)
                            .replace(R.id.fl_main_b, exit)
                            .commit();
                }
            }
        });
        return view;
    }

    /**
     * [Description] This method is used to set the visibility of air distribution controls in Minimised climate view
     */
    public void show(){
        defrost = (ImageView)view.findViewById(R.id.image_view_defrost);
        floor = (ImageView)view.findViewById(R.id.image_view_floor);
        ventilation = (ImageView)view.findViewById(R.id.image_view_ventilation);

        if(ClimateAirDistributionFragment.isVentilation){
            ventilation.setVisibility(View.VISIBLE);
        }else{
            ventilation.setVisibility(View.INVISIBLE);
        }

        if(ClimateAirDistributionFragment.isDefrost){
            defrost.setVisibility(View.VISIBLE);
        }else{
            defrost.setVisibility(View.INVISIBLE);
        }

        if(ClimateAirDistributionFragment.isFloor){
            floor.setVisibility(View.VISIBLE);
        }else{
            floor.setVisibility(View.INVISIBLE);
        }
    }
}
