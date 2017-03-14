package com.example.quest.hmi_demo.message;

import android.app.Activity;
import android.database.Cursor;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.widget.SimpleCursorAdapter;

class NewContactsAdapterBridge extends ContactsAdapterBridge {
    SimpleCursorAdapter buildPhonesAdapter(Activity a) {
        String[] PROJECTION=new String[] { Contacts._ID,
                Contacts.DISPLAY_NAME,
                Phone.NUMBER
        };
        String[] ARGS={String.valueOf(Phone.TYPE_MOBILE)};
        Cursor c=a.managedQuery(Phone.CONTENT_URI,
                PROJECTION, Phone.TYPE+"=?",
                ARGS, Contacts.DISPLAY_NAME);

        SimpleCursorAdapter adapter=new SimpleCursorAdapter(a,
                android.R.layout.simple_spinner_item,
                c,
                new String[] {
                        Contacts.DISPLAY_NAME
                },
                new int[] {
                        android.R.id.text1
                });

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        return(adapter);
    }
}