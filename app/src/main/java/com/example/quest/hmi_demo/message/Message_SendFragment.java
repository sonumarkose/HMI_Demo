package com.example.quest.hmi_demo.message;


import android.Manifest;
import android.content.ContentUris;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

import com.example.quest.hmi_demo.R;
import com.example.quest.hmi_demo.homescreen.MainActivityFragmentA;

import java.io.IOException;
import java.io.InputStream;


public class Message_SendFragment extends Fragment {
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;
    Spinner contacts = null;
    EditText phoneNumber;
    EditText msg = null;
    String senderNo;
    Button send;
    ImageView back_Img;
    final int screenHeight=getScreenHeight();
    final int height=((screenHeight*4)/5);
    private RelativeLayout relativeLayout=null;
    AutoCompleteTextView acTextView;
    String messageNumber;
    SimpleCursorAdapter arr=null;
    Object selected;
    private static final int MY_PERMISSIONS_REQUEST_READ_SMS = 1;


    private Uri uriContact;
    private String contactID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.message_send_fragment, container, false);

        View view = inflater.inflate(R.layout.message_send_fragment, container, false);

        relativeLayout=(RelativeLayout)view.findViewById(R.id.send_messageLayout);
        relativeLayout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,height));

        checkPermissionsContact();
        checkPermissionsContactRead();

/*
        contacts=(Spinner)findViewById(R.id.spinner);

        contacts.setAdapter(ContactsAdapterBridge
                .INSTANCE
                .buildPhonesAdapter(this));

        means=(RadioGroup)findViewById(R.id.means);
        msg=(EditText)findViewById(R.id.msg);


        */




       acTextView = (AutoCompleteTextView)view.findViewById(R.id.phoneNumber);

        //Set the number of characters the user must type before the drop down list is shown
        acTextView.setThreshold(1);


         arr=ContactsAdapterBridge.INSTANCE.buildPhonesAdapter(getActivity());

        acTextView.setAdapter(arr);
        msg = (EditText)view. findViewById(R.id.msg);
        send = (Button)view. findViewById(R.id.send);
       // senderNo=acTextView

        acTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Cursor c=(Cursor)acTextView.getSelectedItem();
           //   selected = arr.getItem(position);
              //  acTextView.setText(position,true);

            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*
                 Cursor c=(Cursor)contacts.getSelectedItem();
                SmsManager
                    .getDefault()
                    .sendTextMessage(c.getString(2), null,
                            msg.getText().toString(),
                            null, null);*/

               // Cursor c=(Cursor)acTextView.getSelectedItem();
                SmsManager.getDefault().sendTextMessage(selected.toString(), null, msg.getText().toString(), null, null);

            }
        });

        back_Img=(ImageView)view.findViewById(R.id.back_img);

        back_Img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                Fragment fragmenta = new MainActivityFragmentA();
               // Message_ListFragment msgList = new Message_ListFragment();
                if (fm != null) {
                    fm.popBackStack();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.fl_main_a, fragmenta);
                    ft.commit();
                }
            }
        });







            //Log.d(TAG, "Response: " + data.toString());
           // uriContact = data.getData();

           retrieveContactName();
           retrieveContactNumber();
        retrieveContactPhoto();









































        return view;


    }

    private void retrieveContactPhoto() {

        Bitmap photo = null;

        try {
            InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(getActivity().getContentResolver(),
                    ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(contactID)));

            if (inputStream != null) {
                photo = BitmapFactory.decodeStream(inputStream);
            //    ImageView imageView = (ImageView)getActivity().findViewById(R.id.img_contact);
               // imageView.setImageBitmap(photo);
            }

            assert inputStream != null;
            inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }




    private void retrieveContactNumber() {

        String contactNumber = null;

        // getting contacts ID
        Cursor cursorID =getActivity().getContentResolver().query(uriContact,
                new String[]{ContactsContract.Contacts._ID},
                null, null, null);

        if (cursorID.moveToFirst()) {

            contactID = cursorID.getString(cursorID.getColumnIndex(ContactsContract.Contacts._ID));
        }

        cursorID.close();

      //  Log.d(TAG, "Contact ID: " + contactID);

        // Using the contact ID now we will get contact phone number
        Cursor cursorPhone = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},

                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " +
                        ContactsContract.CommonDataKinds.Phone.TYPE + " = " +
                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,

                new String[]{contactID},
                null);

        if (cursorPhone.moveToFirst()) {
            contactNumber = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        }

        cursorPhone.close();

       // Log.d(TAG, "Contact Phone Number: " + contactNumber);
    }

    private void retrieveContactName() {

        String contactName = null;

        // querying contact data store
        Cursor cursor = getActivity().getContentResolver().query(uriContact, null, null, null, null);

        if (cursor.moveToFirst()) {

            // DISPLAY_NAME = The display name for the contact.
            // HAS_PHONE_NUMBER =   An indicator of whether this contact has at least one phone number.

            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        }

        cursor.close();

       // Log.d(TAG, "Contact Name: " + contactName);

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







    public static int getScreenWidth(){
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight(){
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

}


