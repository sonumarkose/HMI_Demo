package com.example.quest.hmi_demo.homescreen;


import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.quest.hmi_demo.R;
import com.example.quest.hmi_demo.navigation.activity.NavigationFullScreenFragment;
import com.example.quest.hmi_demo.navigation.activity.NavigationLargeScreenFragment;
import com.example.quest.hmi_demo.navigation.util.MapUtil;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class FragmentNavigator extends Fragment implements MainActivityFragmentA.NavigationSelectedListener, MapUtil.inotifyLocation {
    FragmentManager fm = null;
    MapUtil mapUtil;
    private View view;
    TextView route, currentLocation, module, currentCity, currentState, currentCountry ;
    NavigationLargeScreenFragment navLargeScrFrag;
/*    FloatingActionButton fabMax;
    NavigationFullScreenFragment navFullScrFragment;*/

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        System.out.println("FragmentNavigator.onCreateView");
        fm = getActivity().getSupportFragmentManager();
        mapUtil = MapUtil.getInstance();
        mapUtil.setCommunicator(this);
        /*fabMax = (FloatingActionButton)view.findViewById(R.id.fab_max);
        fabMax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //communicator.showFullScreen();

                FragmentManager fm = getActivity().getSupportFragmentManager();
                if (fm != null) {
                    if (navFullScrFragment == null)
                        navFullScrFragment = new NavigationFullScreenFragment();

                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.fl_main_a, navFullScrFragment);
                    ft.commit();
                }
            }
        });*/



        view = inflater.inflate(R.layout.map_default_layout, container, false);
        /*route = (TextView) view.findViewById(R.id.route);*/
        module = (TextView) view.findViewById(R.id.module_name_default);
        currentLocation = (TextView) view.findViewById(R.id.current_location_default);
        currentCity = (TextView) view.findViewById(R.id.current_city_default);
        currentState = (TextView) view.findViewById(R.id.current_state_default);
        currentCountry = (TextView) view.findViewById(R.id.current_country_default);
        //navLargeScrFrag = new NavigationLargeScreenFragment();
        //navLargeScrFrag.setCommunicator(this);




        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        System.out.println("FragmentNavigator.onActivityCreated");
        mapUtil = MapUtil.getInstance();
        mapUtil.setCommunicator(this);
        //setDefaultView();
        if(mapUtil.getCurPosition() != null ){
            setDefaultView();
        }

    }

    public void setDefaultView(){
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(mapUtil.getCurPosition().latitude, mapUtil.getCurPosition().longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String streetName =  addresses.get(0).getAddressLine(0) + " "+addresses.get(0).getAddressLine(1);
        String cityName = addresses.get(0).getLocality();
        String stateName = addresses.get(0).getAddressLine(2);
        String countryName = addresses.get(0).getCountryName();
        currentLocation.setText(streetName);
        currentCity.setText(cityName);
        currentState.setText(stateName);
        currentCountry.setText(countryName);
    }

    @Override
    public void navigationSelected(String state) {
        System.out.println("state = " + state);
        FragmentTransaction transaction = fm.beginTransaction();
        switch (state) {
            case "min":
                transaction.replace(R.id.fl_navigation, new FragmentMinNavigator());
                break;
            case "max":
                transaction.replace(R.id.fl_navigation, new NavigationLargeScreenFragment());
                break;
            case "default":
                System.out.println("R.id.fl_navigation = " + R.id.fl_navigation);
                transaction.replace(R.id.fl_navigation, new FragmentNavigator());
                break;
            default:
                transaction.replace(R.id.fl_navigation, new FragmentNavigator());
                break;
        }
       // transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void notifyLocation() {
        if(isVisible() && isResumed()){
            setDefaultView();
        }


    }

}
