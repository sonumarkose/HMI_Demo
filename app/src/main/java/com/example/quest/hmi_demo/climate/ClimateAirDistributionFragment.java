////////////////////////////////////////////////////////////////////////////////////////////////////
//
//Android Sample Project
//
////////////////////////////////////////////////////////////////////////////////////////////////////

package com.example.quest.hmi_demo.climate;


import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.SeriesSelection;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.quest.hmi_demo.R;

/**
 * Description: The class ClimateAirDistributionFragment is used to control the Air distribution in Climate Module
 * @author achala.nair
 * @version 1.0
 */

public class ClimateAirDistributionFragment extends Fragment {

    //----------------------------------------------------------------------------------------------
    // Member Variables
    //----------------------------------------------------------------------------------------------

    public static boolean isVentilation =false, isDefrost = false, isFloor = false;
    private View view;
    private static GraphicalView mChart;
    public static ImageView fan;
    public static TextView ventilation, defrost, floor;

    // Pie Chart Slice Names
    private static final String[] code = new String[] {ClimateConstants.VENT1, ClimateConstants.FLOOR,
            ClimateConstants.NIL, ClimateConstants.DEFROST, ClimateConstants.VENT2};
    // Pie Chart Slice Values
    private static final double[] distribution = {4.2, 8.3, 75, 8.3, 4.2};
    private static DefaultRenderer defaultRenderer;
    private static CategorySeries distributionSeries;
    // Color of each Pie Chart Slices
    private static int[] colors = {Color.LTGRAY, Color.LTGRAY, Color.WHITE, Color.LTGRAY, Color.LTGRAY};

    //----------------------------------------------------------------------------------------------
    // Member Methods
    //----------------------------------------------------------------------------------------------

    /**
     * [Description]
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
            if (parent != null){
                parent.removeView(view);
            }
        }
        try {
            view = inflater.inflate(R.layout.content_climate_air_distribution, container, false);
            view.setBackgroundColor(Color.WHITE);

            fan = (ImageView)view.findViewById(R.id.image_view_fan);
            ventilation = (TextView)view.findViewById(R.id.txt_ventilation);
            floor = (TextView)view.findViewById(R.id.txt_floor);
            defrost = (TextView)view.findViewById(R.id.txt_defrost);

             if(isDefrost || isVentilation || isFloor){
                 clockwise();
             }
            // Ploting the chart
            openChart();
        } catch (InflateException e) {
            e.printStackTrace();
        }
        return view;
    }


    /**
     *[Description] This method is used to plot the Piechart
     */

    private void openChart(){
        // Instantiating CategorySeries to plot Pie Chart
        distributionSeries = new CategorySeries(ClimateConstants.NIL);
        for(int i=0 ;i < distribution.length;i++){
            // Adding a slice with its values and name to the Pie Chart
            distributionSeries.add(code[i], distribution[i]);
        }
        renderPieChart();
    }


    /**
     * [Description] This method is used to change colours of the clicked portion
     */

    private void renderPieChart(){
        defaultRenderer  = new DefaultRenderer();
        for(int i = 0 ;i<distribution.length;i++){
            // Instantiating a render for the slice
            SimpleSeriesRenderer seriesRenderer = new SimpleSeriesRenderer();
            seriesRenderer.setColor(colors[i]);
            seriesRenderer.setDisplayChartValues(false);
            // Adding the renderer of a slice to the renderer of the pie chart
            defaultRenderer.addSeriesRenderer(seriesRenderer);

        }

        // Getting PieChartView to add to the custom layout
        mChart = ChartFactory.getPieChartView(getActivity().getBaseContext(), distributionSeries, defaultRenderer);
        defaultRenderer.setClickEnabled(true);
        defaultRenderer.setSelectableBuffer(10);
        defaultRenderer.setZoomEnabled(false);
        defaultRenderer.setPanEnabled(false);

        mChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SeriesSelection seriesSelection = mChart.getCurrentSeriesAndPoint();

                if (seriesSelection != null) {
                    // Getting the name of the clicked slice
                    int seriesIndex = seriesSelection.getPointIndex();
                    String selectedSeries = "";
                    selectedSeries = code[seriesIndex];
                    // Getting the value of the clicked slice

                    switch (selectedSeries.toString()) {
                        case ClimateConstants.VENT1:
                        case ClimateConstants.VENT2:
                            if (isVentilation) {
                                isVentilation = false;
                                if(!isDefrost && !isVentilation && !isFloor){
                                    anti_clockwise();
                                }
                                colors[0] = colors[4] = Color.LTGRAY;
                            } else {
                                isVentilation = true;
                                clockwise();
                                colors[0] = colors[4] = Color.YELLOW;
                            }
                            break;
                        case ClimateConstants.DEFROST:
                            if (isDefrost) {
                                isDefrost = false;
                                if(!isDefrost && !isVentilation && !isFloor){
                                    anti_clockwise();
                                }
                                colors[3] = Color.LTGRAY;
                            } else {
                                isDefrost = true;
                                clockwise();
                                colors[3] = Color.YELLOW;
                            }
                            break;
                        case ClimateConstants.FLOOR:
                            if (isFloor) {
                                isFloor = false;
                                if(!isDefrost && !isVentilation && !isFloor){
                                    anti_clockwise();
                                }
                               colors[1] = Color.LTGRAY;
                            } else {
                                isFloor = true;
                                clockwise();
                                colors[1] = Color.YELLOW;
                            }
                            break;
                    }
                    renderPieChart();
                }
            }
        });

        // Getting a reference to view group linear layout chart_container
        LinearLayout chartContainer = (LinearLayout)view.findViewById(R.id.chart_container);
        // Adding the pie chart to the custom layout
        chartContainer.removeAllViews();
        chartContainer.addView(mChart);
    }


    public void clockwise(){
        /* Create Animation */
        Animation rotation = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.clockwise_on);
        /* start Animation */
        fan.startAnimation(rotation);
    }


    public void anti_clockwise(){
        /* Create Animation */
        Animation rotation = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.clockwise_off);
        /* start Animation */
        fan.startAnimation(rotation);
    }
}


