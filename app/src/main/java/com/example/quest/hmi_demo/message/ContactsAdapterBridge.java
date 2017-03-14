package com.example.quest.hmi_demo.message;

import android.app.Activity;
import android.os.Build;
import android.widget.SimpleCursorAdapter;

abstract class ContactsAdapterBridge {
    abstract SimpleCursorAdapter buildPhonesAdapter(Activity a);

    public static final ContactsAdapterBridge INSTANCE=buildBridge();

    private static ContactsAdapterBridge buildBridge() {
        int sdk=new Integer(Build.VERSION.SDK).intValue();

        return(new NewContactsAdapterBridge());
    }
}