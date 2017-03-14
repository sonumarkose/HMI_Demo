package com.example.quest.hmi_demo.phone.fragments;

import android.Manifest;
import android.content.ContentResolver;
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

import com.example.quest.hmi_demo.R;
import com.example.quest.hmi_demo.phone.adapters.PhoneContactsApapter;
import com.example.quest.hmi_demo.phone.model_classes.ContactPojo;

import java.util.ArrayList;
import java.util.List;

public class FavoriteFragment extends Fragment {
    RecyclerView rvFavorites;

    private static final int MY_PERMISSIONS_REQUEST_FAVOURITE_CONTACTS = 1;


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
                        MY_PERMISSIONS_REQUEST_FAVOURITE_CONTACTS);

            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_FAVOURITE_CONTACTS: {
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
        View view = inflater.inflate(R.layout.favourite_fragment, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvFavorites = (RecyclerView) view.findViewById(R.id.rvFavorites);

        getAllContacts();
    }

    private void getAllContacts() {
        List<ContactPojo> contactPojoList = new ArrayList();
        ContactPojo contactPojo;

        ContentResolver contentResolver = getActivity().getContentResolver();
        // Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
        Uri queryUri = ContactsContract.Contacts.CONTENT_URI;

        String[] projection = new String[]{
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts.STARRED};

        String selection = ContactsContract.Contacts.STARRED + "='1'";

        Cursor cursor = contentResolver.query(queryUri, projection, selection, null, null);

        if (cursor.getCount() > 0) {


            while (cursor.moveToNext()) {
               // int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
              //  if (hasPhoneNumber > 0)
              //  {
                    String contactID = cursor.getString(cursor
                            .getColumnIndex(ContactsContract.Contacts._ID));
                    String title = (cursor.getString(
                            cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));


                    contactPojo = new ContactPojo();
                    contactPojo.setContactName(title);

                    Cursor phoneCursor = contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{contactID},
                            null);
                    if (phoneCursor.moveToNext())
                    {
                        String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        contactPojo.setContactNumber(phoneNumber);
                    }

                    phoneCursor.close();

                    Cursor emailCursor = contentResolver.query(
                            ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                            new String[]{contactID}, null);
                    while (emailCursor.moveToNext())
                    {
                        String emailId = emailCursor.getString(emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                       // contactVO.setContactNumber(emailId);
                    }
                    contactPojoList.add(contactPojo);

                        //      }
            }




           PhoneContactsApapter contactAdapter = new PhoneContactsApapter(contactPojoList, getActivity());
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            rvFavorites.setLayoutManager(layoutManager);
            rvFavorites.setAdapter(contactAdapter);

        }
        else
        {
            contactPojo = new ContactPojo();
            contactPojo.setContactName("No Favorites Available");
            contactPojoList.add(contactPojo);

            PhoneContactsApapter contactAdapter = new PhoneContactsApapter(contactPojoList, getActivity());
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            rvFavorites.setLayoutManager(layoutManager);
            rvFavorites.setAdapter(contactAdapter);
        }
       // cursor.close();
    }



}