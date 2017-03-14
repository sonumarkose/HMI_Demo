package com.example.quest.hmi_demo.homescreen;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.quest.hmi_demo.R;
import com.example.quest.hmi_demo.climate.ClimateMinimisedFragment;
import com.example.quest.hmi_demo.navigation.util.MapUtil;


public class MainActivity extends AppCompatActivity implements View.OnTouchListener{

    private static String cstatus="default";
    private FrameLayout fl = null;
    float dX =0;
    float dY =0;
    int lastAction =0;
    MapUtil mapUtil;
    private int PERMISSION_ALL = 1;



    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // The request code used in ActivityCompat.requestPermissions()
        // and returned in the Activity's onRequestPermissionsResult()

        String[] PERMISSIONS = {Manifest.permission.READ_CONTACTS,
                Manifest.permission.WRITE_CONTACTS,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_SMS,
                Manifest.permission.SEND_SMS,
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_FINE_LOCATION
        };

        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }



        mapUtil = MapUtil.getInstance();
        mapUtil.setContext(this);

        Fragment fragmenta = new MainActivityFragmentA();
        Fragment fragmentb = new ClimateMinimisedFragment();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnTouchListener(this);
/*        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportFragmentManager().popBackStack();
                }
                else {
                    MainActivityFragmentA.setAnimation(fl,"max");
                }
                ClimateMinimisedFragment climateMinimisedFragment = new ClimateMinimisedFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fl_main_b, climateMinimisedFragment).commit();

            }
        });*/

      /*  FragmentManager fm1 = getSupportFragmentManager();
        FragmentTransaction transaction1 = fm1.beginTransaction();


        transaction1.replace(R.id.fl_main_a, fragmenta);
        transaction1.replace(R.id.fl_main_b, fragmentb);
        transaction1.commit();

*/

        if (useNavigationAllowed()){
            initializeModules();
        } else
        {
            mapUtil.checkNavigationPermissions(this);
        }



    }
    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        }else{
            super.onBackPressed();
        }
        ClimateMinimisedFragment climateMinimisedFragment = new ClimateMinimisedFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fl_main_b, climateMinimisedFragment).commit();
    }
    public static void setStatus(String state){cstatus=state;}


    public static String getStatus(){return cstatus;}
    @Override
    public boolean onTouch( View view, MotionEvent event ) {
        switch (event.getActionMasked()) {



            case MotionEvent.ACTION_DOWN:
                dX = view.getX() - event.getRawX();
                dY = view.getY() - event.getRawY();
                lastAction = MotionEvent.ACTION_DOWN;
                break;

            case MotionEvent.ACTION_MOVE:
                view.setY(event.getRawY() + dY);
                view.setX(event.getRawX() + dX);
                lastAction = MotionEvent.ACTION_MOVE;
                break;

            case MotionEvent.ACTION_UP:
                if (lastAction == MotionEvent.ACTION_DOWN){
                    if (getSupportFragmentManager().getBackStackEntryCount() > 0) {

                        for(int i =0; i< getSupportFragmentManager().getBackStackEntryCount(); i++)
                        {
                            getSupportFragmentManager().popBackStack();
                        }
                    }
                    else {
                        MainActivityFragmentA.setAnimation(fl,"max");
                    }
                    ClimateMinimisedFragment climateMinimisedFragment = new ClimateMinimisedFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fl_main_b, climateMinimisedFragment).commit();
                }
                break;

            default:
                return false;
        }
        return true;
    }



    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        System.out.println("MainActivity.onRequestPermissionsResult");
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // Permission was granted.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        MapUtil.getInstance().buildGoogleApiClient();
                        initializeModules();

                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show();

                }
                return;
            }

            // other 'case' lines to check for other permissions this app might request.
            //You can add here other case statements according to your requirement.
        }
    }
    public void initializeModules(){
        Fragment fragmenta = new MainActivityFragmentA();
        Fragment fragmentb = new ClimateMinimisedFragment();


        FragmentManager fm1 = getSupportFragmentManager();
        FragmentTransaction transaction1 = fm1.beginTransaction();


        transaction1.replace(R.id.fl_main_a, fragmenta);
        transaction1.replace(R.id.fl_main_b, fragmentb);
        transaction1.commit();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //No call for super(). Bug on API Level > 11.
    }
    private boolean useNavigationAllowed() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION);

        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;

        //If permission is not granted returning false
        return false;
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }


}
