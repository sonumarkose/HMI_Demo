package com.example.quest.hmi_demo.phone.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.quest.hmi_demo.R;
import com.example.quest.hmi_demo.homescreen.MainActivityFragmentA;

/**
 * Created by quest on 27/2/17.
 */
public class DefaultViewFragment extends Fragment implements  MainActivityFragmentA.TelephoneSelectedListener {


    ImageView imageView;
    TextView phoneText;
    TextView deviceName;
    TextView providerName;
    ImageView batteryimage;
    private BroadcastReceiver mReceiver;
    /* FrameLayout phoneFrame;*/
    private TextView batteryTxt;
    FragmentManager fm =null;

    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context ctxt, Intent intent) {
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            batteryTxt.setText(String.valueOf(level) + "%");
        }
    };

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fm= getActivity().getSupportFragmentManager();
        View view=inflater.inflate(R.layout.maximized_fragment,container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TelephonyManager tManager = (TelephonyManager) getActivity().getBaseContext()
                .getSystemService(Context.TELEPHONY_SERVICE);
// Get carrier name (Network Operator Name)
        String carrierName = tManager.getNetworkOperatorName();

        imageView=(ImageView)view.findViewById(R.id.phoneimage);
        phoneText= (TextView)view.findViewById(R.id.phonetext);
       // phoneText.setTextColor(Color.BLACK);

        deviceName= (TextView)view.findViewById(R.id.device);
        //deviceName.setTextColor(Color.BLACK);
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        deviceName.setText(manufacturer+""+model);

        providerName= (TextView)view.findViewById(R.id.provider);
        providerName.setText(carrierName);
       // providerName.setTextColor(Color.BLACK);

        batteryimage= (ImageView)view.findViewById(R.id.battery);
        batteryTxt = (TextView)view.findViewById(R.id.batterytxt);
        getActivity().registerReceiver(mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        super.onStart();
       /* this.registerReceiver(this.mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));*/

    }


    @Override
    public void telephoneSelected(String state) {
        FragmentTransaction transaction = fm.beginTransaction();
        switch (state) {
            case "min":
                transaction.replace(R.id.fl_telephone, new MinimizedViewFragment());
                break;
            case "max":
                transaction.replace(R.id.fl_telephone, new MaximizedButtonsFragment());
                break;
            case "default":
                transaction.replace(R.id.fl_telephone, new DefaultViewFragment());
                break;
            default:
                transaction.replace(R.id.fl_telephone, new DefaultViewFragment());
                break;
        }
        transaction.commit();
    }

}
