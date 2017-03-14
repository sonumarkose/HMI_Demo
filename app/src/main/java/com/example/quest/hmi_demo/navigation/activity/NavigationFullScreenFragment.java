package com.example.quest.hmi_demo.navigation.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.quest.hmi_demo.R;
import com.example.quest.hmi_demo.homescreen.FragmentMinNavigator;
import com.example.quest.hmi_demo.homescreen.FragmentNavigator;
import com.example.quest.hmi_demo.homescreen.MainActivityFragmentA;
import com.example.quest.hmi_demo.navigation.model.Step;
import com.example.quest.hmi_demo.navigation.rest.DataParser;
import com.example.quest.hmi_demo.navigation.util.MapUtil;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
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

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



/**
 * Created by test on 6/3/17.
 */
public class NavigationFullScreenFragment extends Fragment implements OnMapReadyCallback, LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener , MainActivityFragmentA.NavigationSelectedListener{
    private View view;
    private SupportMapFragment mapFragment;
    FrameLayout mapContainer;
    FragmentManager fm = null;

    private FloatingActionButton fab, fab1, fab2, fab3, showMyLocationFab, showDefaultFab;
    private boolean FAB_Status = false;
    Animation show_fab_1, hide_fab_1, show_fab_2, hide_fab_2, show_fab_3, hide_fab_3;
    public static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 98;
    List<Step> steps = null;
    public static final int RESULT_OK = -1;
    public static final int RESULT_CANCELED = 0;
    private LatLng origin, destination;
    Polyline line;
    MarkerOptions markerOptions;
    private Marker mCurrLocationMarker;
    private final static String API_KEY = "AIzaSyDawlwsvR4mthz68pSrJDD_62cymtnFHZQ";
    private final static String MODE = "driving";
    MapUtil mapUtil;
    private static final String TAG = "NavigationFullScreenFragment";
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private GoogleMap mMap;

    ifullScreenFragment communicator;

    private final int screenHeight = getScreenHeight();
    private final int height = ((screenHeight * 4) / 5);

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }


    public void setCommunicator(ifullScreenFragment communicator) {
        this.communicator = communicator;
    }

    CoordinatorLayout layoutContainer ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_map, container, false);
        layoutContainer = (CoordinatorLayout)view.findViewById(R.id.mapFullScreenContainer);
        layoutContainer.setLayoutParams(new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT, height));
      /*  FragmentManager fm1 = getSupportFragmentManager();
        FragmentTransaction transaction1 = fm1.beginTransaction();*/
        showDefaultFab = (FloatingActionButton) view.findViewById(R.id.fab_back);
        showMyLocationFab = (FloatingActionButton) view.findViewById(R.id.fab_my_location);
 /*       showDefaultFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transaction1.replace(R.id.fl_main_a, fragmenta);
                transaction1.commit();
            }
        });*/

        showMyLocationFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCurrentLocation();
            }
        });

        System.out.println("NavigationFullScreenFragment.onCreateView");
        return view;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        System.out.println("NavigationFullScreenFragment.onActivityCreated");
        FragmentManager fm = getChildFragmentManager();
        mapUtil = MapUtil.getInstance();
        this.mMap = mapUtil.getmMap();
        mapFragment = (SupportMapFragment) fm.findFragmentById(R.id.map_container);
        mapContainer = (FrameLayout) view.findViewById(R.id.map_container);
        /*if (mapUtil.getmMap() == null) {
*/
        mapFragment = SupportMapFragment.newInstance();
        //fm.beginTransaction().replace(R.id.map_container, mapFragment).commit();

  /*      }*/
        fm.beginTransaction().replace(R.id.map_container, mapFragment).commit();

       /* fab = (FloatingActionButton) view.findViewById(R.id.fab);*/
        fab1 = (FloatingActionButton) view.findViewById(R.id.fab_1);
     /*   fab2 = (FloatingActionButton) view.findViewById(R.id.fab_2);
        fab3 = (FloatingActionButton) view.findViewById(R.id.fab_3);

        //Animations
        show_fab_1 = AnimationUtils.loadAnimation(getActivity(), R.anim.fab1_show);
        hide_fab_1 = AnimationUtils.loadAnimation(getActivity(), R.anim.fab1_hide);
        show_fab_2 = AnimationUtils.loadAnimation(getActivity(), R.anim.fab2_show);
        hide_fab_2 = AnimationUtils.loadAnimation(getActivity(), R.anim.fab2_hide);
        show_fab_3 = AnimationUtils.loadAnimation(getActivity(), R.anim.fab3_show);
        hide_fab_3 = AnimationUtils.loadAnimation(getActivity(), R.anim.fab3_hide);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (FAB_Status == false) {
                    //Display FAB menu
                    expandFAB();
                    mapContainer.setAlpha(0.4f);
                    FAB_Status = true;
                } else {
                    //Close FAB menu
                    hideFAB();
                    mapContainer.setAlpha(1f);
                    FAB_Status = false;
                }
            }
        });*/

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), "Floating Action Button 1", Toast.LENGTH_SHORT).show();
                callPlaceAutocompleteActivityIntent();
            }
        });

 /*       fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(getActivity(), "Floating Action Button 2", Toast.LENGTH_SHORT).show();
                showDirections();
            }
        });

        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Floating Action Button 3", Toast.LENGTH_SHORT).show();
            }
        });*/
        // setCurrentLocation();
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED && mapUtil.getmGoogleApiClient() != null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mapUtil.getmGoogleApiClient(), mapUtil.getmLocationRequest(), this);
        } else {
            mapUtil.buildGoogleApiClient();
        }

    }

    private void callPlaceAutocompleteActivityIntent() {
        try {
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY).build(getActivity());
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
            //PLACE_AUTOCOMPLETE_REQUEST_CODE is integer for request code

        } catch (GooglePlayServicesRepairableException |

                GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("requestCode = " + requestCode);
        System.out.println("resultCode = " + resultCode);
        System.out.println("data = " + data);
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(getActivity(), data);
/*                System.out.println("place.getLatLng() = " + place.getLatLng());
                System.out.println("place.getLocale() = " + place.getLocale());
                System.out.println("place.getWebsiteUri() = " + place.getWebsiteUri());
                System.out.println("place.getAttributions() = " + place.getAttributions());
                System.out.println("place.getAddress() = " + place.getAddress());
                System.out.println("place.getPhoneNumber() = " + place.getPhoneNumber());
                System.out.println("place.getPlaceTypes() = " + place.getPlaceTypes());*/
                mapUtil.setDestination(place.getLatLng());
                markerOptions = new MarkerOptions();
                markerOptions.position(mapUtil.getDestination());
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                mCurrLocationMarker = mapUtil.getmMap().addMarker(markerOptions);
                buildRoute();
               // build_retrofit_and_get_response(MODE);
                Toast.makeText(getActivity(), place.getAddress().toString(), Toast.LENGTH_SHORT).show();
                //txtlocation.setText(place.getAddress().toString());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getActivity(), data);
            } else if (requestCode == RESULT_CANCELED) {

            }
        }
    }

    /*public void setCurrentLocation() {
        markerOptions = new MarkerOptions();
        markerOptions.position(mapUtil.getCurPosition());
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(origin));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
        Toast.makeText(getActivity(), String.valueOf(mapUtil.getCurPosition().latitude) + " " + String.valueOf(mapUtil.getCurPosition().longitude), Toast.LENGTH_SHORT).show();
    }
*/
    public void buildRoute(){
        // Getting URL to the Google Directions API
        String url = getUrl(mapUtil.getCurPosition(), mapUtil.getDestination());
        Log.d("onMapClick", url.toString());
        FetchUrl FetchUrl = new FetchUrl();

        // Start downloading json data from Google Directions API
        FetchUrl.execute(url);
    }
    public void showDirections() {
        if (steps != null && steps.size() > 0) {
            Intent intent = new Intent(getActivity(), MapDirectionsActivity.class);
            intent.putExtra("Steps", (Serializable) steps);
            startActivity(intent);
        }

    }

    private void build_retrofit_and_get_response(String type) {
      /*  System.out.println("MapsFragment.build_retrofit_and_get_response");
        String url = "https://maps.googleapis.com/maps/";
        RetrofitMaps apiService = ApiClient.getClient().create(RetrofitMaps.class);
        Call<Example> call = apiService.getDistanceDuration(mapUtil.getCurPosition().latitude + "," + mapUtil.getCurPosition().longitude, mapUtil.getDestination().latitude + "," + mapUtil.getDestination().longitude, API_KEY);
        call.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {
                System.out.println("MapsFragment.onResponse");
                System.out.println("response message = " + response.message());
                System.out.println("response.body() = " + response.body());
                System.out.println("response.isSuccess() = " + response.isSuccessful());
                System.out.println("response.body().getRoutes() = " + response.body().getRoutes());
                System.out.println("response.code = " + response.code());
                System.out.println("response.body().getRoutes().size() = " + response.body().getRoutes().size());

                try {
                    //Remove previous line from map
                    if (line != null) {
                        line.remove();
                    }
                    // This loop will go through all the results and add marker on each location.
                    for (int i = 0; i < response.body().getRoutes().size(); i++) {
                        String distance = response.body().getRoutes().get(i).getLegs().get(i).getDistance().getText();
                        String time = response.body().getRoutes().get(i).getLegs().get(i).getDuration().getText();
                        steps = response.body().getRoutes().get(i).getLegs().get(i).getSteps();


                        //Toast.makeText(getActivity(), "Distance:" + distance + ", Duration:" + time, Toast.LENGTH_SHORT).show();
                        Snackbar.make(view, "Distance:" + distance + ", Duration:" + time, Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();

                        //modification starts
                        String encodedString = "";
                        List<LatLng> list = new ArrayList<>();

                        for (int j = 0; j < steps.size(); j++) {
                            //System.out.println("steps.size() j = " + steps.size()+" "+j);
                            *//*System.out.println("response.body().getRoutes().get(j).getLegs().get(j).getSteps().get(j) = " + response.body().getRoutes().get(j).getLegs().get(j).getSteps().get(j));
                            System.out.println("response.body().getRoutes().get(j).getLegs().get(j).getSteps().get(j).getPolyline() = " + response.body().getRoutes().get(j).getLegs().get(j).getSteps().get(j).getPolyline());
                            System.out.println("response.body().getRoutes().get(j).getLegs().get(j).getSteps().get(j).getPolyline().getPoints() = " + response.body().getRoutes().get(j).getLegs().get(j).getSteps().get(j).getPolyline().getPoints());*//*
                            encodedString = response.body().getRoutes().get(i).getLegs().get(i).getSteps().get(j).getPolyline().getPoints();
                            list.addAll(mapUtil.decodePoly(encodedString));

                        }
                        mapUtil.setLatLonglist(list);
                        System.out.println("steps.size() = " + steps.size());
                        System.out.println("list.size() = " + list.size());

                        line = mapUtil.getmMap().addPolyline(new PolylineOptions()
                                        .addAll(list)
                                        .width(15)
                                        .color(Color.MAGENTA)
                                        .geodesic(true)
                        );
                        //modification ends

                    }

                } catch (Exception e) {
                    Log.d("onResponse", "There is an error");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Example> call, Throwable t) {
                Log.d("onFailure", t.toString());
            }
        });
*/
    }


    @Override
    public void onResume() {
        super.onResume();
        System.out.println(TAG + ".onResume");
        mapUtil.setmMap(mapFragment.getMap());
        if (mapUtil.getmGoogleApiClient() == null) {
            mapUtil.buildGoogleApiClient();
        }
        if (mapUtil.getCurPosition() != null && mapUtil.getDestination() != null) {
            createPath();
        }

    }


    private void expandFAB() {

        //Floating Action Button 1
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) fab1.getLayoutParams();
        layoutParams.rightMargin += (int) (fab1.getWidth() * 1.7);
        layoutParams.bottomMargin += (int) (fab1.getHeight() * 0.25);
        fab1.setLayoutParams(layoutParams);
        System.out.println("fab1 layoutParams = " + fab1.getLayoutParams());
        System.out.println("fab layoutParams = " + fab.getLayoutParams());
        fab1.startAnimation(show_fab_1);
        fab1.setClickable(true);

        //Floating Action Button 2
        FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) fab2.getLayoutParams();
        layoutParams2.rightMargin += (int) (fab2.getWidth() * 1.5);
        layoutParams2.bottomMargin += (int) (fab2.getHeight() * 1.5);
        fab2.setLayoutParams(layoutParams2);
        fab2.startAnimation(show_fab_2);
        fab2.setClickable(true);

        //Floating Action Button 3
        FrameLayout.LayoutParams layoutParams3 = (FrameLayout.LayoutParams) fab3.getLayoutParams();
        layoutParams3.rightMargin += (int) (fab3.getWidth() * 0.25);
        layoutParams3.bottomMargin += (int) (fab3.getHeight() * 1.7);
        fab3.setLayoutParams(layoutParams3);
        fab3.startAnimation(show_fab_3);
        fab3.setClickable(true);
    }


    private void hideFAB() {

        //Floating Action Button 1
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) fab1.getLayoutParams();
        layoutParams.rightMargin -= (int) (fab1.getWidth() * 1.7);
        layoutParams.bottomMargin -= (int) (fab1.getHeight() * 0.25);
        fab1.setLayoutParams(layoutParams);
        fab1.startAnimation(hide_fab_1);
        fab1.setClickable(false);

        //Floating Action Button 2
        FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) fab2.getLayoutParams();
        layoutParams2.rightMargin -= (int) (fab2.getWidth() * 1.5);
        layoutParams2.bottomMargin -= (int) (fab2.getHeight() * 1.5);
        fab2.setLayoutParams(layoutParams2);
        fab2.startAnimation(hide_fab_2);
        fab2.setClickable(false);

        //Floating Action Button 3
        FrameLayout.LayoutParams layoutParams3 = (FrameLayout.LayoutParams) fab3.getLayoutParams();
        layoutParams3.rightMargin -= (int) (fab3.getWidth() * 0.25);
        layoutParams3.bottomMargin -= (int) (fab3.getHeight() * 1.7);
        fab3.setLayoutParams(layoutParams3);
        fab3.startAnimation(hide_fab_3);
        fab3.setClickable(false);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        System.out.println(TAG + ".onMapReady");
        mapUtil.setmMap(googleMap);
        mapUtil.getmMap().setMapType(GoogleMap.MAP_TYPE_HYBRID);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                mapUtil.buildGoogleApiClient();
                mapUtil.getmMap().setMyLocationEnabled(true);
                mapUtil.getmMap().getUiSettings().setCompassEnabled(true);
                mapUtil.getmMap().getUiSettings().setMyLocationButtonEnabled(true);
            }
        } else {
            mapUtil.buildGoogleApiClient();
            mapUtil.getmMap().setMyLocationEnabled(true);
            mapUtil.getmMap().getUiSettings().setCompassEnabled(true);
            mapUtil.getmMap().getUiSettings().setMyLocationButtonEnabled(true);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        System.out.println("NavigationFullScreenFragment.onLocationChanged");
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        markerOptions = new MarkerOptions();
        markerOptions.position(mapUtil.getCurPosition());
        markerOptions.title("Current Position");
        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_action_location_found);
        markerOptions.icon(icon);
        mCurrLocationMarker = mapUtil.getmMap().addMarker(markerOptions);

        //move map camera
        showCurrentLocation();

        // Toast.makeText(getActivity(), String.valueOf(mapUtil.getCurPosition().latitude) + " " + String.valueOf(mapUtil.getCurPosition().longitude), Toast.LENGTH_SHORT).show();

        //stop location updates
        if (mapUtil.getmGoogleApiClient() != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mapUtil.getmGoogleApiClient(), this);
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        System.out.println("NavigationFullScreenFragment.onConnected");
/*        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);*/
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mapUtil.getmGoogleApiClient(), mapUtil.getmLocationRequest(), this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        System.out.println("NavigationFullScreenFragment.onConnectionSuspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public void createPath() {
        System.out.println("NavigationFullScreenFragment.createPath");
        markerOptions = new MarkerOptions();
        markerOptions.position(mapUtil.getDestination());
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
        mCurrLocationMarker = mapUtil.getmMap().addMarker(markerOptions);

        List<LatLng> list = mapUtil.getLatLonglist();
        if(list != null){
            System.out.println("list.size() = " + list.size());
            //Remove previous line from map
            if (line != null) {
                line.remove();
            }
            PolylineOptions polylineOptions = new PolylineOptions();
            polylineOptions.addAll(list);
            polylineOptions.width(15);
            polylineOptions.color(Color.MAGENTA);
            polylineOptions.geodesic(true);
            line = mapUtil.getmMap().addPolyline(polylineOptions);
            System.out.println("NavigationFullScreenFragment line created");
            /*line = mapUtil.getmMap().addPolyline(new PolylineOptions()
                            .addAll(list)
                            .width(15)
                            .color(Color.MAGENTA)
                            .geodesic(true)
            );*/
        }

    }

    @Override
    public void navigationSelected(String state) {
        FragmentTransaction transaction = fm.beginTransaction();
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

    public interface ifullScreenFragment {
        public void showDefaultView();
    }

    public void showCurrentLocation() {
        mapUtil.getmMap().moveCamera(CameraUpdateFactory.newLatLng(mapUtil.getCurPosition()));
        mapUtil.getmMap().animateCamera(CameraUpdateFactory.zoomTo(11));
    }
    private String getUrl(LatLng origin, LatLng dest) {
        System.out.println("NavigationFullScreenFragment.getUrl");

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;


        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;


        return url;
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        System.out.println("NavigationFullScreenFragment.downloadUrl");
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            Log.d("downloadUrl", data.toString());
            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Fetches data from url passed
    private class FetchUrl extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... url) {
            System.out.println("FetchUrl.doInBackground");
            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
                Log.d("Background Task data", data.toString());
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            System.out.println("FetchUrl.onPostExecute");
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);

        }
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
            System.out.println("ParserTask.doInBackground");

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                Log.d("ParserTask",jsonData[0].toString());
                DataParser parser = new DataParser();
                Log.d("ParserTask", parser.toString());

                // Starts parsing data
                routes = parser.parse(jObject);
                Log.d("ParserTask","Executing routes");
                Log.d("ParserTask",routes.toString());

            } catch (Exception e) {
                Log.d("ParserTask",e.toString());
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            System.out.println("ParserTask.onPostExecute");
            ArrayList<LatLng> points;
          /*  PolylineOptions lineOptions = null;*/

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<>();
            /*    lineOptions = new PolylineOptions();*/

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);

                }
                System.out.println("points.size() = " + points.size());
                mapUtil.setLatLonglist(points);

                // Adding all the points in the route to LineOptions
       /*         lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.color(Color.RED);*/

                Log.d("onPostExecute","onPostExecute lineoptions decoded");

            }

            // Drawing polyline in the Google Map for the i-th route
        /*    System.out.println("lineOptions = " + lineOptions);*/
           if(mapUtil.getLatLonglist().size() > 0){
               createPath();
           }

       /*     if(lineOptions != null) {*/
                //if(mMap == null){
/*                mMap = mapUtil.getmMap();
                //}
                mMap.addPolyline(lineOptions);*/
                createPath();
            /*}
            else {
                Log.d("onPostExecute","without Polylines drawn");
            }*/
          /*  createPath();*/
        }
    }
}
