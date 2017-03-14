package com.example.quest.hmi_demo.message;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.quest.hmi_demo.R;
import com.example.quest.hmi_demo.homescreen.MainActivityFragmentA;


public class Message_DefaultFragment extends Fragment implements MainActivityFragmentA.MessagesSelectedListener{
    private static final int MY_PERMISSIONS_REQUEST_READ_SMS = 1;
    TextView count_txt;
     FragmentManager fm = null;

    TextView deviceName;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fm = getActivity().getSupportFragmentManager();


        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.message_default_fragment,container,false);
        checkPermissions();
        checkPermissionsContact();
        checkPermissionsContactRead();


        final Uri SMS_INBOX = Uri.parse("content://sms/inbox");
        Cursor c = getActivity().getContentResolver().query(SMS_INBOX, null, "read = 0", null, null);
        int unreadMessagesCount = c.getCount();
        count_txt=(TextView)view.findViewById(R.id.msgCount_txt);
        String count= String.valueOf(unreadMessagesCount);
        count_txt.setText(count);
        c.deactivate();



        count_txt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

               /* Message_ListFragment fragmentMessages = new Message_ListFragment();
                FragmentManager fm1 = getFragmentManager();
                FragmentTransaction transaction1 = fm1.beginTransaction();
                transaction1.replace(R.id.fl_navigation, fragmentMessages);*/

              //  Intent idnt= new Intent(getContext(),MessageActivity.class);
               //startActivity(idnt);


             /*   android.app.FragmentManager fm = getActivity().getFragmentManager();//.getSupportFragmentManager();
                Message_ListFragment msgList = new Message_ListFragment();
                if (fm != null) {
                    android.app.FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.fl_main_a, msgList);
                    ft.commit();
                }*/
                return true;
            }
        });


        deviceName= (TextView)view.findViewById(R.id.model_name);
       // deviceName.setTextColor(Color.BLACK);
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        deviceName.setText(manufacturer + "" + model);
        super.onStart();

        return view;
    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_SMS)!= PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_SMS)) {


            } else {

                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_SMS}, MY_PERMISSIONS_REQUEST_READ_SMS);

            }
        }
    }


    private void checkPermissionsContact() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_CONTACTS)!= PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_CONTACTS)) {


            } else {

                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_CONTACTS}, MY_PERMISSIONS_REQUEST_READ_SMS);

            }
        }
    }
    private void checkPermissionsContactRead() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_CONTACTS)) {


            } else {

                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_CONTACTS}, MY_PERMISSIONS_REQUEST_READ_SMS);

            }
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_SMS: {
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

    @Override
    public void messagesSelected(String state) {

        FragmentTransaction transaction = fm.beginTransaction();
        switch (state){
            case "max":transaction.replace(R.id.fl_messages, new Message_ListFragment());break;
            case "default":transaction.replace(R.id.fl_messages, new Message_DefaultFragment());break;
            default:transaction.replace(R.id.fl_messages, new Message_DefaultFragment());break;
        }
        transaction.commit();

    }
}
