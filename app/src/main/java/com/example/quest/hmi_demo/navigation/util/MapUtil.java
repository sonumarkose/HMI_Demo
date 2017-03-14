package com.example.quest.hmi_demo.navigation.util;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by test on 3/3/17.
 */
public class MapUtil implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static MapUtil mapUtil;
    inotifyLocation communicator = null;

    public GoogleMap getmMap() {
        return mMap;
    }

    public void setmMap(GoogleMap mMap) {
        this.mMap = mMap;
    }

    private GoogleMap mMap;
    private final static String API_KEY = "AIzaSyB8Im4qQb8p858srgTYuGRoyZJHJgKeG1c";
    private final static String MODE = "driving";
 //   List<Step> steps = null;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 98;
    private Context context;

    private LatLng curPosition;
    private LatLng origin;

    private LatLng destination;

    public void setCommunicator(inotifyLocation communicator){
        this.communicator = communicator;
    }



    public List<LatLng> getLatLonglist() {
        return latLonglist;
    }

    public void setLatLonglist(List<LatLng> latLonglist) {
        this.latLonglist = latLonglist;
    }

    private  List<LatLng> latLonglist;

    public GoogleApiClient getmGoogleApiClient() {
        return mGoogleApiClient;
    }

    public void setmGoogleApiClient(GoogleApiClient mGoogleApiClient) {
        this.mGoogleApiClient = mGoogleApiClient;
    }

    private GoogleApiClient mGoogleApiClient;

    public LocationRequest getmLocationRequest() {
        return mLocationRequest;
    }

    public void setmLocationRequest(LocationRequest mLocationRequest) {
        this.mLocationRequest = mLocationRequest;
    }

    private LocationRequest mLocationRequest;


    public LatLng getDestination() {
        return destination;
    }

    public void setDestination(LatLng destination) {
        this.destination = destination;
    }

    public LatLng getOrigin() {
        return origin;
    }

    public void setOrigin(LatLng origin) {
        this.origin = origin;
    }

    public LatLng getCurPosition() {
        return curPosition;
    }

    public void setCurPosition(LatLng curPosition) {
        this.curPosition = curPosition;
    }



    private MapUtil(){}

    public static MapUtil getInstance(){
        if(mapUtil==null){
            mapUtil = new MapUtil();
            return mapUtil;
        }
        return mapUtil;
    }

    @Override
    public void onConnected(Bundle bundle) {
        System.out.println("MapUtil.onConnected");
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        System.out.println("MapUtil.onConnectionSuspended");

    }

    @Override
    public void onLocationChanged(Location location) {
        System.out.println("MapUtil.onLocationChanged");
        setCurPosition(new LatLng(location.getLatitude(), location.getLongitude()));
        setOrigin(new LatLng(location.getLatitude(), location.getLongitude()));
        System.out.println("location.getLatitude() = " + location.getLatitude());
        System.out.println("location.getLongitude() = " + location.getLongitude());
        if (getmGoogleApiClient() != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(getmGoogleApiClient(), this);
        }
        if(communicator != null){
            System.out.println("MapUtil.onLocationChanged  notifylocation called");
            communicator.notifyLocation();
        }



    }



    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        System.out.println("MapUtil.onConnectionFailed");
    }


    public void buildGoogleApiClient() {
        System.out.println("MapUtil.buildGoogleApiClient");
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(context)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        mGoogleApiClient.connect();
    }



    public void checkNavigationPermissions(AppCompatActivity context){
        System.out.println("MapUtil.checkNavigationPermissions");
        this.context = context;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
            }else{
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(context,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);

            }
        } else {
            buildGoogleApiClient();
        }
    }

    public void setContext(Context context){
        this.context = context;
    }
    public List<LatLng> decodePoly(String encoded) {
        List<LatLng> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }

    public interface inotifyLocation{
        public void notifyLocation();
    }

}
