package com.example.quest.hmi_demo.phone.fragments;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.quest.hmi_demo.R;

/**
 * Created by quest on 22/2/17.
 */
public class KeypadFragment extends Fragment {

    EditText mEditor;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.keypad_fragment, container, false);

        return rootView;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mEditor = (EditText) view.findViewById(R.id.EditTextPhoneNumber);

        View.OnClickListener mDialPadListener = new View.OnClickListener() {
            public void onClick(View v) {
                if(mEditor.getText().toString()!=null) {
                    StringBuffer previousNumber = new StringBuffer(mEditor.getText().toString());
                    CharSequence phoneDigit = ((Button) v).getText();
                    mEditor.setText(previousNumber.append(phoneDigit));
                }
                else
                {
                    CharSequence phoneDigit = ((Button) v).getText();
                    mEditor.setText(phoneDigit);
                }
            }
        };
        /**
         * A call-back for when the user presses the call button.
         */

        View.OnClickListener mPhoneCallListener = new View.OnClickListener() {
            public void onClick(View v) {
                call(mEditor.getText().toString());
            }
        };

        View.OnClickListener mPhoneCloseListener= new View.OnClickListener() {
            public void onClick(View v) {
                // call(mEditor.getText().toString());
            }
        };
        View.OnClickListener mPhoneBackListener= new View.OnClickListener() {
            public void onClick(View v) {
                if(mEditor.getText().toString().length() > 0){
                    String input = mEditor.getText().toString();
                    input = input.substring(0, input.length()-1);
                    mEditor.setText(input);
                }
                // mEditor.setText(" ");
            }
        };

        // Hook up button presses to the appropriate event handler.
        ((Button) view.findViewById(R.id.Button00)).setOnClickListener(mDialPadListener);
        ((Button) view.findViewById(R.id.Button01)).setOnClickListener(mDialPadListener);
        ((Button) view.findViewById(R.id.Button02)).setOnClickListener(mDialPadListener);
        ((Button) view.findViewById(R.id.Button03)).setOnClickListener(mDialPadListener);
        ((Button) view.findViewById(R.id.Button04)).setOnClickListener(mDialPadListener);
        ((Button) view.findViewById(R.id.Button05)).setOnClickListener(mDialPadListener);
        ((Button) view.findViewById(R.id.Button06)).setOnClickListener(mDialPadListener);
        ((Button) view.findViewById(R.id.Button07)).setOnClickListener(mDialPadListener);
        ((Button) view.findViewById(R.id.Button08)).setOnClickListener(mDialPadListener);
        ((Button) view.findViewById(R.id.Button09)).setOnClickListener(mDialPadListener);
        ((Button) view.findViewById(R.id.Buttonstar)).setOnClickListener(mDialPadListener);
        ((Button) view.findViewById(R.id.Buttonhash)).setOnClickListener(mDialPadListener);

        ((ImageButton) view.findViewById(R.id.ImageButtonDial)).setOnClickListener(mPhoneCallListener);
        ((Button) view.findViewById(R.id.ButtonClose)).setOnClickListener(mPhoneCloseListener);
        ((ImageButton) view.findViewById(R.id.ImageButtonBack)).setOnClickListener(mPhoneBackListener);
    }
    private void call(String phoneNumber) {
        try {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + phoneNumber));
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            startActivity(callIntent);
        } catch (ActivityNotFoundException activityException) {
            Log.e("dialing-example", "Call failed", activityException);
        }
    }
}
