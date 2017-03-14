package com.example.quest.hmi_demo.homescreen;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.quest.hmi_demo.R;

public class FragmentMessages extends Fragment implements MainActivityFragmentA.MessagesSelectedListener {
    FragmentManager fm = null;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fm = getActivity().getSupportFragmentManager();
        View view = inflater.inflate(R.layout.default_fragment_messages, container, false);
        return view;
    }

    @Override
    public void messagesSelected(String state) {
        FragmentTransaction transaction = fm.beginTransaction();
        switch (state) {
            case "max":
                transaction.replace(R.id.fl_messages, new FragmentMaxMessages());
                break;
            case "default":
                transaction.replace(R.id.fl_messages, new FragmentMessages());
                break;
            default:
                transaction.replace(R.id.fl_messages, new FragmentMessages());
                break;
        }
        transaction.commit();
    }
}
