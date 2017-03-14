////////////////////////////////////////////////////////////////////////////////////////////////////
//
//Android Sample Project
//
////////////////////////////////////////////////////////////////////////////////////////////////////
package com.example.quest.hmi_demo.climate;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.example.quest.hmi_demo.R;

/**
 * Description: The class ClimateFanFragment is used to control the fan speed in Climate Module
 * @author Resmi
 * @version 1.0
 */
public class ClimateFanFragment extends Fragment implements View.OnClickListener {

    //----------------------------------------------------------------------------------------------
    // Member Variables
    //----------------------------------------------------------------------------------------------

    private View view;
    private Button buttonLevel1, buttonLevel2, buttonLevel3, buttonLevel4, buttonLevel5;
    private Button fanOff, fanMax;
    private SharedPreferences sharedpreferences;
    private SharedPreferences.Editor editor;
    private int speedLevel;

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
            if (parent != null){
                parent.removeView(view);
            }
        }
        try {
            view = inflater.inflate(R.layout.content_climate_fan, container, false);

            buttonLevel1= (Button)view.findViewById(R.id.btn_level1);
            buttonLevel2= (Button)view.findViewById(R.id.btn_level2);
            buttonLevel3= (Button)view.findViewById(R.id.btn_level3);
            buttonLevel4= (Button)view.findViewById(R.id.btn_level4);
            buttonLevel5= (Button)view.findViewById(R.id.btn_level5);
            fanOff = (Button)view.findViewById(R.id.btnOff);
            fanMax = (Button)view.findViewById(R.id.btnMax);

            buttonLevel1.setOnClickListener(this);
            buttonLevel2.setOnClickListener(this);
            buttonLevel3.setOnClickListener(this);
            buttonLevel4.setOnClickListener(this);
            buttonLevel5.setOnClickListener(this);
            fanOff.setOnClickListener(this);
            fanOff.setTextColor(Color.GREEN);
            fanMax.setTextColor(Color.GREEN);
            fanMax.setOnClickListener(this);

            sharedpreferences = getActivity().getSharedPreferences(ClimateConstants.MY_PREFERENCES, Context.MODE_PRIVATE);
            editor = sharedpreferences.edit();
            speedLevel=sharedpreferences.getInt(ClimateConstants.LEVEL, 0);
            if(speedLevel==R.id.btnOff){
                switchOffFan();
            }
            else {
            speedSelector(speedLevel);
            }
        } catch (InflateException e) {
            e.printStackTrace();
        }
        return view;
    }

    /**
     *[Description] This method is used to set maximum fan speed
     */
    public void maxFanSpeed(){
        blink();
        buttonLevel5.setBackgroundResource(R.drawable.climate_fan_button_selected);
        buttonLevel4.setBackgroundResource(R.drawable.climate_fan_button_selected);
        buttonLevel3.setBackgroundResource(R.drawable.climate_fan_button_selected);
        buttonLevel2.setBackgroundResource(R.drawable.climate_fan_button_selected);
        buttonLevel1.setBackgroundResource(R.drawable.climate_fan_button_selected);
        fanOff.setEnabled(true);
        fanOff.setTextColor(Color.GREEN);
    }

    /**
     *[Description] This method is used to set switch off fan
     */
    public void switchOffFan(){
        buttonLevel5.setBackgroundResource(R.drawable.climate_fan_button_deselected);
        buttonLevel4.setBackgroundResource(R.drawable.climate_fan_button_deselected);
        buttonLevel3.setBackgroundResource(R.drawable.climate_fan_button_deselected);
        buttonLevel2.setBackgroundResource(R.drawable.climate_fan_button_deselected);
        buttonLevel1.setBackgroundResource(R.drawable.climate_fan_button_deselected);
        fanOff.setEnabled(false);
        fanOff.setTextColor(Color.GRAY);
        fanMax.clearAnimation();
    }

    /**
     *[Description] This method is used to set anti_clockwise animation
     */
    public void antiClockwise(){
        /* Create Animation */
        Animation rotation = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.anti_clockwise);
        /* start Animation */
        fanOff.startAnimation(rotation);
    }

    /**
     *[Description] This method is used to set blink animation
     */
    public void blink(){
        Animation animation1 =
                AnimationUtils.loadAnimation(getActivity().getApplicationContext(),
                        R.anim.blink);
        fanMax.startAnimation(animation1);
    }

    /**
     *[Description] This method is used to set fan speed levels.
     * @param level - The fan speed level
     */
     public void speedSelector(int level){

        switch(level) {

            case R.id.btn_level5:
                maxFanSpeed();
                break;

            case R.id.btn_level4:
                buttonLevel5.setBackgroundResource(R.drawable.climate_fan_button_deselected);
                buttonLevel4.setBackgroundResource(R.drawable.climate_fan_button_selected);
                buttonLevel3.setBackgroundResource(R.drawable.climate_fan_button_selected);
                buttonLevel2.setBackgroundResource(R.drawable.climate_fan_button_selected);
                buttonLevel1.setBackgroundResource(R.drawable.climate_fan_button_selected);
                fanOff.setEnabled(true);
                fanOff.setTextColor(Color.GREEN);
                fanMax.clearAnimation();
                break;

            case R.id.btn_level3:
                buttonLevel5.setBackgroundResource(R.drawable.climate_fan_button_deselected);
                buttonLevel4.setBackgroundResource(R.drawable.climate_fan_button_deselected);
                buttonLevel3.setBackgroundResource(R.drawable.climate_fan_button_selected);
                buttonLevel2.setBackgroundResource(R.drawable.climate_fan_button_selected);
                buttonLevel1.setBackgroundResource(R.drawable.climate_fan_button_selected);
                fanOff.setEnabled(true);
                fanOff.setTextColor(Color.GREEN);
                fanMax.clearAnimation();
                break;

            case R.id.btn_level2:
                buttonLevel5.setBackgroundResource(R.drawable.climate_fan_button_deselected);
                buttonLevel4.setBackgroundResource(R.drawable.climate_fan_button_deselected);
                buttonLevel3.setBackgroundResource(R.drawable.climate_fan_button_deselected);
                buttonLevel2.setBackgroundResource(R.drawable.climate_fan_button_selected);
                buttonLevel1.setBackgroundResource(R.drawable.climate_fan_button_selected);
                fanOff.setEnabled(true);
                fanOff.setTextColor(Color.GREEN);
                fanMax.clearAnimation();
                break;

            case R.id.btn_level1:
                buttonLevel5.setBackgroundResource(R.drawable.climate_fan_button_deselected);
                buttonLevel4.setBackgroundResource(R.drawable.climate_fan_button_deselected);
                buttonLevel3.setBackgroundResource(R.drawable.climate_fan_button_deselected);
                buttonLevel2.setBackgroundResource(R.drawable.climate_fan_button_deselected);
                buttonLevel1.setBackgroundResource(R.drawable.climate_fan_button_selected);
                fanOff.setEnabled(true);
                fanOff.setTextColor(Color.GREEN);
                fanMax.clearAnimation();
                break;

            case R.id.btnOff:
                antiClockwise();
                switchOffFan();
                break;

            case R.id.btnMax:
                maxFanSpeed();
            default:
        }
    }

    /**
     * [Description] This method is used to set fan speed levels on click
     * @param v - layout view
     */
    @Override
    public void onClick(View v) {
        editor.putInt(ClimateConstants.LEVEL, v.getId());
        editor.commit();
        speedSelector(v.getId());
    }
}
