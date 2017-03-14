package com.example.quest.hmi_demo.homescreen;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.quest.hmi_demo.R;

public class FragmentMinMedia extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.minimize_fragment_media, container, false);
        return view;
    }
}
