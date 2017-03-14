package com.example.quest.hmi_demo.phone.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.quest.hmi_demo.R;
import com.example.quest.hmi_demo.phone.adapters.PhoneContactsApapter;
import com.example.quest.hmi_demo.phone.interfaces.mClickListener;
import com.example.quest.hmi_demo.phone.model_classes.ContactPojo;

import java.util.ArrayList;
import java.util.List;

public class ContactFragment extends Fragment implements mClickListener{
    RecyclerView rvContacts;
     RecyclerView.LayoutManager mLayoutManager;
    Context mContext;
    ContentResolver contentResolver;
    Cursor cursor;



    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;


    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.READ_CONTACTS)) {


            } else {

                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_CONTACTS},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermissions();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contact_fragment, container, false);
        mContext = container.getContext();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvContacts = (RecyclerView)view.findViewById(R.id.contacts);

        getAllContacts();

    }

    private void getAllContacts() {
        List<ContactPojo> contactVOList = new ArrayList();
        ContactPojo contactPojo;

        contentResolver = getActivity().getContentResolver();
        cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
       /* Uri queryUri = ContactsContract.Contacts.CONTENT_URI;

        String[] projection = new String[] {
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts.STARRED};

        String selection =ContactsContract.Contacts.STARRED + "='1'";

        Cursor cursor = contentResolver.query(queryUri, projection, selection, null, null);*/

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {

                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
                if (hasPhoneNumber > 0) {
                    String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                    contactPojo = new ContactPojo();
                    contactPojo.setContactName(name);

                    Cursor phoneCursor = contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id},
                            null);
                    if (phoneCursor.moveToNext()) {
                        String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        contactPojo.setContactNumber(phoneNumber);
                    }

                    phoneCursor.close();

                    Cursor emailCursor = contentResolver.query(
                            ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (emailCursor.moveToNext()) {
                        String emailId = emailCursor.getString(emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                    }
                    contactVOList.add(contactPojo);
                }
            }

            PhoneContactsApapter contactAdapter = new PhoneContactsApapter(contactVOList, getActivity());
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            rvContacts.setLayoutManager(layoutManager);
            contactAdapter.setClickListener((mClickListener) this);
            rvContacts.setAdapter(contactAdapter);



        }

    }

    @Override
    public void onClick(View view, int position) {
        Toast.makeText(getActivity(), "Tapped onnnnnnnnn"+ position, Toast.LENGTH_SHORT).show();
        cursor.moveToPosition(position);
        String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
        final String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        String phoneNumber = "";
        Cursor phoneCursor = contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                new String[]{id},
                null);
        if (phoneCursor.moveToNext()) {
            phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        }

        /*final Dialog dialog = new Dialog(mContext);
        dialog.setTitle("Contact");

        // set the custom dialog components - text, image and button
        TextView text = (TextView) dialog.findViewById(R.id.text);
        text.setText("Name : "+name +"\n Number : "+phoneNumber);
        System.out.println("....................................................................");
        System.out.println(name);
        ImageView image = (ImageView) dialog.findViewById(R.id.image);
        image.setImageResource(R.drawable.phone);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        dialog.show();*/

        final ImageView image = new ImageView(getActivity());
        image.setImageResource(R.drawable.call);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), "Tapped on Proggress", Toast.LENGTH_SHORT).show();
                if (v == image) {
                    Intent iCall = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
                            +name ));
                    startActivity(iCall);
                }

            }
        });

        AlertDialog.Builder builder =
                new AlertDialog.Builder(getActivity()).
                        setMessage("Name : "+name +"\n Number : "+phoneNumber).setView(image);

        builder.create().show();
    }



}