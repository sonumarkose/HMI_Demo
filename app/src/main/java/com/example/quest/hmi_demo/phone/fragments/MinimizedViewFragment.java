package com.example.quest.hmi_demo.phone.fragments;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
public class MinimizedViewFragment extends Fragment implements MainActivityFragmentA.TelephoneSelectedListener{

    ImageView imageView;
    TextView phoneText;
    TextView deviceName;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.minimized_view_fragment,container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imageView=(ImageView)view.findViewById(R.id.phoneimage);
        phoneText= (TextView)view.findViewById(R.id.phonetext);
        //phoneText.setTextColor(Color.BLACK);

        deviceName= (TextView)view.findViewById(R.id.device);
       // deviceName.setTextColor(Color.BLACK);
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        deviceName.setText(manufacturer + "" + model);
        super.onStart();
    }

    @Override
    public void telephoneSelected(String state) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        switch (state) {
            case "min":
                transaction.replace(R.id.fl_telephone, new MinimizedViewFragment());
                break;
            case "max":
                transaction.replace(R.id.fl_telephone, new MaximizedFragment());
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

