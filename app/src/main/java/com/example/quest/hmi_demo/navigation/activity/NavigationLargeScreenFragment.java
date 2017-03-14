package com.example.quest.hmi_demo.navigation.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.quest.hmi_demo.R;
import com.example.quest.hmi_demo.homescreen.FragmentMinNavigator;
import com.example.quest.hmi_demo.homescreen.FragmentNavigator;
import com.example.quest.hmi_demo.homescreen.MainActivityFragmentA;
import com.example.quest.hmi_demo.navigation.util.MapUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by test on 6/3/17.
 */
public class NavigationLargeScreenFragment extends Fragment implements MainActivityFragmentA.NavigationSelectedListener, OnMapReadyCallback, LocationListener,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    private View view;
    private SupportMapFragment mapFragment;
    MapUtil mapUtil;
    private static final String TAG = "NavigationLargeScreenFragment";
    Polyline line;
    MarkerOptions markerOptions;
    private Marker mCurrLocationMarker;
    FloatingActionButton fabMax, fabMin;
    ilargeFragment communicator;
    FragmentManager fm = null;
    NavigationFullScreenFragment navFullScrFragment;
    TextView currentLocation;
    public void setCommunicator(ilargeFragment communicator){
        this.communicator = communicator;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        System.out.println("NavigationLargeScreenFragment.onCreateView");
        fm = getActivity().getSupportFragmentManager();

        view = inflater.inflate(R.layout.map_large_layout, container, false);
        currentLocation = (TextView) view.findViewById(R.id.current_location_large);
        fabMax = (FloatingActionButton)view.findViewById(R.id.fab_max);
        fabMax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //communicator.showFullScreen();

                FragmentManager fm = getActivity().getSupportFragmentManager();
                if (fm != null) {
                    if(navFullScrFragment == null)
                        navFullScrFragment = new NavigationFullScreenFragment();

                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.fl_main_a, navFullScrFragment);
                    ft.addToBackStack(null);
                    ft.commit();
                }
            }
        });
        fabMin = (FloatingActionButton)view.findViewById(R.id.fab_back);
        fabMin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDefaultView();
            }
        });
        return view;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        System.out.println("NavigationLargeScreenFragment.onActivityCreated");
        FragmentManager fmc = getChildFragmentManager();
        mapUtil = MapUtil.getInstance();

        mapFragment = (SupportMapFragment) fmc.findFragmentById(R.id.map_large_container);
        mapFragment = SupportMapFragment.newInstance();
        //mapFragment.getMapAsync(this);
        System.out.println("mapFragment = " + mapFragment);
        fmc.beginTransaction().replace(R.id.map_large_container, mapFragment).commit();
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED && mapUtil.getmGoogleApiClient()!=null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mapUtil.getmGoogleApiClient(), mapUtil.getmLocationRequest(), this);
        }else
        {
            mapUtil.buildGoogleApiClient();
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        System.out.println(TAG+".onResume");
        mapUtil.setmMap(mapFragment.getMap());
        if (mapUtil.getmGoogleApiClient() == null) {
            mapUtil.buildGoogleApiClient();
        }
        if(mapUtil.getCurPosition()!=null){
            setCurrentPosition();
        }
        if(mapUtil.getCurPosition() != null && mapUtil.getDestination() != null)
        {
            createPath();
        }

    }

    @Override
    public void onConnected(Bundle bundle) {
        System.out.println("NavigationLargeScreenFragment.onConnected");

        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mapUtil.getmGoogleApiClient(), mapUtil.getmLocationRequest(), this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        System.out.println("NavigationLargeScreenFragment.onConnectionSuspended");
    }

    public void setCurrentPosition(){
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        markerOptions = new MarkerOptions();
        markerOptions.position(mapUtil.getCurPosition());
        markerOptions.title("Current Position");
        if(mapUtil.getmMap() != null){
            BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_action_location_found);
            markerOptions.icon(icon);
            mCurrLocationMarker =  mapUtil.getmMap().addMarker(markerOptions);

            //move map camera
            mapUtil.getmMap().moveCamera(CameraUpdateFactory.newLatLng(mapUtil.getCurPosition()));
            mapUtil.getmMap().animateCamera(CameraUpdateFactory.zoomTo(11));
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        System.out.println("NavigationLargeScreenFragment.onLocationChanged");
        showCurrentLocation();
        setCurrentPosition();



        // Toast.makeText(getActivity(), String.valueOf(mapUtil.getCurPosition().latitude) + " " + String.valueOf(mapUtil.getCurPosition().longitude), Toast.LENGTH_SHORT).show();

        //stop location updates
        if (mapUtil.getmGoogleApiClient() != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mapUtil.getmGoogleApiClient(), this);
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        System.out.println("NavigationLargeScreenFragment.onConnectionFailed");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        System.out.println(TAG + ".onMapReady");
        System.out.println("googleMap = " + googleMap);
        mapUtil.setmMap(googleMap);
        mapUtil.getmMap().setMapType(GoogleMap.MAP_TYPE_HYBRID);
       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                System.out.println("onmapready if = " );
                mapUtil.buildGoogleApiClient();
                mapUtil.getmMap().setMyLocationEnabled(true);
                mapUtil.getmMap().getUiSettings().setCompassEnabled(true);
                mapUtil.getmMap().getUiSettings().setMyLocationButtonEnabled(true);
            }
        } else {
            System.out.println("onmapready else = ");
            mapUtil.buildGoogleApiClient();
            mapUtil.getmMap().setMyLocationEnabled(true);
            mapUtil.getmMap().getUiSettings().setCompassEnabled(true);
            mapUtil.getmMap().getUiSettings().setMyLocationButtonEnabled(true);
        }*/

    }
    public void createPath(){
        markerOptions = new MarkerOptions();
        markerOptions.position(mapUtil.getDestination());
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
        mCurrLocationMarker = mapUtil.getmMap().addMarker(markerOptions);

        List<LatLng> list = mapUtil.getLatLonglist();
        if(list != null) {
            //Remove previous line from map
            if (line != null) {
                line.remove();
            }
            line = mapUtil.getmMap().addPolyline(new PolylineOptions()
                            .addAll(list)
                            .width(15)
                            .color(Color.MAGENTA)
                            .geodesic(true)
            );
        }
    }

    @Override
    public void navigationSelected(String state) {
        FragmentTransaction transaction = fm.beginTransaction();
        System.out.println("state = " + state);
        switch (state) {
            case "min":
                transaction.replace(R.id.fl_navigation, new FragmentMinNavigator());
                break;
            case "max":
                transaction.replace(R.id.fl_navigation, new NavigationLargeScreenFragment());
                break;
            case "default":
                transaction.replace(R.id.fl_navigation, new FragmentNavigator());
                break;
            default:
                transaction.replace(R.id.fl_navigation, new FragmentNavigator());
                break;
        }
        transaction.commit();
    }

    public interface ilargeFragment{
        public void showFullScreen();
    }
    public void showCurrentLocation(){
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(mapUtil.getCurPosition().latitude, mapUtil.getCurPosition().longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String streetName =  addresses.get(0).getAddressLine(0) + " "+addresses.get(0).getAddressLine(1);
        currentLocation.setText(streetName);
    }

    public void showDefaultView(){
        System.out.println("NavigationLargeScreenFragment.showDefaultView");
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fl_navigation, new FragmentNavigator());
        transaction.commit();
    }
}
