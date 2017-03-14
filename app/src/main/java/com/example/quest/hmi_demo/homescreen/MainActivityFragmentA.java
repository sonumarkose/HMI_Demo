package com.example.quest.hmi_demo.homescreen;


import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.example.quest.hmi_demo.R;
import com.example.quest.hmi_demo.climate.ClimateMinimisedFragment;
import com.example.quest.hmi_demo.media.fragments.MediaFragment;
import com.example.quest.hmi_demo.message.Message_DefaultFragment;
import com.example.quest.hmi_demo.phone.fragments.DefaultViewFragment;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;


public class MainActivityFragmentA extends Fragment {

    final static int sh1 = getScreenHeight();
    final static int sh = sh1;
    final static int sw = getScreenWidth();
    final static int llh = ((sh * 4) / 5);
    final static int dhl = llh / 4;
    private static FrameLayout flNavigation = null;
    private static FrameLayout flMedia = null;
    private static FrameLayout flTelephone = null;
    private static FrameLayout flMessages = null;
    private static LinearLayout llmain = null;
    private static FragmentNavigator fragmentNavigator = null;
    private static MediaFragment fragmentMedia = null;
    private static Message_DefaultFragment fragmentMessages = null;
    private static DefaultViewFragment fragmentTelephone = null;
    final static int minh = llh / 10;
    final static int maxh = llh - (minh * 2);
    final static int maxh4m = llh - (minh * 3);
    final static Map<FrameLayout, Integer> frames = new LinkedHashMap<FrameLayout, Integer>();
    private NavigationSelectedListener nsl = null;
    private MediaSelectedListener mediasl = null;
    private TelephoneSelectedListener telephonesl = null;
    private MessagesSelectedListener message_sl = null;

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public static void defaultPage() {

        fragmentNavigator.navigationSelected("default");
        fragmentMedia.mediaSelected("default");
        fragmentMessages.messagesSelected("default");
        fragmentTelephone.telephoneSelected("default");

        final Map<FrameLayout, Integer> frames = new LinkedHashMap<FrameLayout, Integer>();
        frames.put(flMessages, dhl);
        frames.put(flTelephone, dhl);
        frames.put(flMedia, dhl);
        frames.put(flNavigation, dhl);
        AnimationProvider ap = new AnimationProvider();
        ap.beginAnimation(frames);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_activity_a, container, false);

        flNavigation = (FrameLayout) view.findViewById(R.id.fl_navigation);
        flMedia = (FrameLayout) view.findViewById(R.id.fl_media);
        flTelephone = (FrameLayout) view.findViewById(R.id.fl_telephone);
        flMessages = (FrameLayout) view.findViewById(R.id.fl_messages);

        llmain = (LinearLayout) view.findViewById(R.id.ll_main);

        llmain.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, llh));

        frames.put(flMessages, dhl);
        frames.put(flTelephone, dhl);
        frames.put(flMedia, dhl);
        frames.put(flNavigation, dhl);



/*        ResizeHeightAnimation anim = new ResizeHeightAnimation(frames);
        anim.setDuration(1000);
        flMessages.startAnimation(anim);*/

        AnimationProvider ap = new AnimationProvider();
        ap.beginAnimation(frames);
        flNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int nheight = flNavigation.getLayoutParams().height;
                if (nheight == maxh) {
                    MainActivity.setStatus("default");
                    setAnimation(flNavigation, "max");

                } else {
                    MainActivity.setStatus("MaxNavigation");
                    setAnimation(flNavigation, "min");

                    fragmentNavigator.navigationSelected("max");
                    fragmentMedia.mediaSelected("min");
                    // fragmentMessages.messagesSelected("min");
                    fragmentTelephone.telephoneSelected("min");
/*
                    DisplayMetrics metrics = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(metrics);

                    int height = metrics.heightPixels;
                    int width = metrics.widthPixels;
*/
//  ScrollView sv = new ScrollView(getApplicationContext());
                    System.out.println("---------------------------------------" + getScreenWidth() + "------------------------" + getScreenHeight() + "--------------------------------");

                }
            }
        });
        flMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int height = flMedia.getLayoutParams().height;
                if (height == maxh) {
                    MainActivity.setStatus("default");
                    setAnimation(flMedia, "max");
                } else {
                    MainActivity.setStatus("MaxMedia");
                    setAnimation(flMedia, "min");

                    fragmentNavigator.navigationSelected("min");
                    fragmentMedia.mediaSelected("max");
                    // fragmentMessages.messagesSelected("min");
                    fragmentTelephone.telephoneSelected("min");
                }
            }
        });
        flTelephone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int height = flTelephone.getLayoutParams().height;
                if (height == maxh) {
                    MainActivity.setStatus("default");
                    setAnimation(flTelephone, "max");
                } else {
                    MainActivity.setStatus("MaxTelephone");
                    setAnimation(flTelephone, "min");

                    fragmentNavigator.navigationSelected("min");
                    fragmentMedia.mediaSelected("min");
                    // fragmentMessages.messagesSelected("min");
                    fragmentTelephone.telephoneSelected("max");
                }
            }
        });
        flMessages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int height = flMessages.getLayoutParams().height;
                if (height == maxh) {
                    MainActivity.setStatus("default");
                    setAnimation(flMessages, "max");
                } else {
                    MainActivity.setStatus("MaxMessage");
                    setAnimation(flMessages, "min");

                    fragmentNavigator.navigationSelected("min");
                    fragmentMedia.mediaSelected("min");
                    fragmentMessages.messagesSelected("max");
                    fragmentTelephone.telephoneSelected("min");

                }


            }
        });


        fragmentNavigator = new FragmentNavigator();
        fragmentMedia = new MediaFragment();
        fragmentMessages = new Message_DefaultFragment();
        fragmentTelephone = new DefaultViewFragment();


        ClimateMinimisedFragment fragmentClimate = new ClimateMinimisedFragment();
        FragmentManager fm1 = getFragmentManager();


        /*Bundle value = getArguments();
        Bundle bundle = new Bundle();
        if (value != null) {

            bundle.putString("index", value.getString("index"));
            bundle.putString("title", value.getString("title"));
            bundle.putString("artist", value.getString("artist"));
            bundle.putString("songPath",value.getString("songPath"));
            bundle.putString("albumURI", value.getString("albumURI"));

            System.out.println("MainActivityFragmentA.onCreateView title= " + value.getString("title"));
            System.out.println("MainActivityFragmentA.onCreateView artist= "+value.getString("artist"));
            System.out.println("MainActivityFragmentA.onCreateView songPath= "+value.getString("songPath"));
            System.out.println("MainActivityFragmentA.onCreateView albumURI= "+value.getString("albumURI"));

            fragmentMedia.setArguments(bundle);
        }*/

        FragmentTransaction transaction1 = fm1.beginTransaction();
        transaction1.replace(R.id.fl_navigation, fragmentNavigator);
        transaction1.replace(R.id.fl_media, fragmentMedia);
        transaction1.replace(R.id.fl_telephone, fragmentTelephone);
        transaction1.replace(R.id.fl_messages, fragmentMessages);
        transaction1.replace(R.id.bottom_main, fragmentClimate);
        transaction1.commit();

        //fragmentNavigator.navigationSelected("default");
        //fragmentMedia.mediaSelected("default");
        //fragmentMessages.messagesSelected("default");
        //fragmentTelephone.telephoneSelected("default");
        changeScreenByStatus();
        return view;
    }

    public static void setAnimation(FrameLayout fl, String state) {
        if ("max".equalsIgnoreCase(state)) {

            fragmentNavigator.navigationSelected("default");
            fragmentMedia.mediaSelected("default");
            fragmentMessages.messagesSelected("default");
            fragmentTelephone.telephoneSelected("default");
            state = "default";
            frames.put(flNavigation, dhl);
            frames.put(flMedia, dhl);
            frames.put(flTelephone, dhl);
            frames.put(flMessages, dhl);
        } else {
            frames.put(flNavigation, minh);
            frames.put(flMedia, minh);
            frames.put(flTelephone, minh);
            frames.put(flMessages, 0);
            frames.put(fl, maxh);
        }

/*        ResizeHeightAnimation anim = new ResizeHeightAnimation(frames);
        anim.setDuration(1000);
        fl.startAnimation(anim);*/
        AnimationProvider ap = new AnimationProvider();
        ap.beginAnimation(frames);
    }

    public interface NavigationSelectedListener {
        public void navigationSelected(String state);
    }

    public interface MediaSelectedListener {
        public void mediaSelected(String state);
    }

    public interface TelephoneSelectedListener {
        public void telephoneSelected(String state);
    }

    public interface MessagesSelectedListener {
        public void messagesSelected(String state);
    }
    public static void changeScreenByStatus(){
        String status=MainActivity.getStatus();
        System.out.println("-----------------"+status+"-------------------------------------");
        switch(status){
            case "MaxNavigation":{
                frames.put(flNavigation, maxh);
                frames.put(flMedia, minh);
                frames.put(flTelephone, minh);
                frames.put(flMessages, 0);
                AnimationProvider ap = new AnimationProvider();
                ap.beginAnimation(frames);
                break;
            }
            case "MaxMedia":{
                frames.put(flNavigation, minh);
                frames.put(flMedia, maxh);
                frames.put(flTelephone, minh);
                frames.put(flMessages, 0);
                AnimationProvider ap = new AnimationProvider();
                ap.beginAnimation(frames);
                break;
            }
            case "MaxTelephone":{
                System.out.println("-----------------"+status+" in switch-------------------------------------");
                frames.put(flNavigation, minh);
                frames.put(flMedia, minh);
                frames.put(flTelephone, maxh);
                frames.put(flMessages, 0);
/*                fragmentNavigator.navigationSelected("min");
                fragmentMedia.mediaSelected("min");
                // fragmentMessages.messagesSelected("min");
                fragmentTelephone.telephoneSelected("max");*/
                AnimationProvider ap = new AnimationProvider();
                ap.beginAnimation(frames);
                break;
            }
            case "MaxMessage":{
                frames.put(flNavigation, minh);
                frames.put(flMedia, minh);
                frames.put(flTelephone, minh);
                frames.put(flMessages, maxh);
                AnimationProvider ap = new AnimationProvider();
                ap.beginAnimation(frames);
                break;
            }
            case "default":{
                frames.put(flNavigation, dhl);
                frames.put(flMedia, dhl);
                frames.put(flTelephone, dhl);
                frames.put(flMessages, dhl);
                break;
            }
        }
    }


}
