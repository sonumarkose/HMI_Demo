package com.example.quest.hmi_demo.homescreen;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.quest.hmi_demo.R;
import com.example.quest.hmi_demo.phone.fragments.DefaultViewFragment;
import com.example.quest.hmi_demo.phone.fragments.MaximizedFragment;
import com.example.quest.hmi_demo.phone.fragments.MinimizedViewFragment;

public class FragmentTelephone extends Fragment implements MainActivityFragmentA.TelephoneSelectedListener {
    FragmentManager fm = null;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fm = getActivity().getSupportFragmentManager();
        View view = inflater.inflate(R.layout.default_fragment_telephone, container, false);
        return view;
    }

    @Override
    public void telephoneSelected(String state) {
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
                transaction.replace(R.id.fl_telephone, new FragmentTelephone());
                break;
        }
        transaction.commit();
    }
}
