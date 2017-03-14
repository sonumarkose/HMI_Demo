package com.example.quest.hmi_demo.phone.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.quest.hmi_demo.R;

/**
 * Created by quest on 8/3/17.
 */
public class CallingFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.calling_fragment, container, false);
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
