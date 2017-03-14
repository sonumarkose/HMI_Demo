package com.example.quest.hmi_demo.phone.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.quest.hmi_demo.R;
import com.example.quest.hmi_demo.phone.utile.CallDetectService;

/**
 * Created by quest on 23/2/17.
 */
public class ActivityFragment extends Fragment {
    private boolean detectEnabled;
    Button ButtonEnd;
    private static final int MY_PERMISSIONS_REQUEST_READ_CALL_LOG = 1;




    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_CALL_LOG)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.READ_CALL_LOG)) {


            } else {

                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_CALL_LOG},
                        MY_PERMISSIONS_REQUEST_READ_CALL_LOG);

            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CALL_LOG: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.active_call_fragment, container, false);
        ButtonEnd= (Button) view.findViewById(R.id.ButtonEnd);
        ButtonEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDetectEnabled(false);

                getActivity().finish();
            }
        });
        return view;
    }

    public void setDetectEnabled(boolean enable) {
        detectEnabled = enable;

        Intent intent = new Intent(getContext(),CallDetectService.class);
        if (enable) {
            // start detect service
             getActivity().startService(intent);
            Toast.makeText(getContext(), "Detecting", Toast.LENGTH_SHORT).show();
           /* buttonToggleDetect.setText("Turn off");
            textViewDetectState.setText("Detecting");*/
        }
        else {
            // stop detect service
            getActivity().stopService(intent);
            Toast.makeText(getContext(),"NotDetecting",Toast.LENGTH_SHORT).show();
           /* buttonToggleDetect.setText("Turn on");
            textViewDetectState.setText("Not detecting");*/
        }

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        checkPermissions();
    }

}
