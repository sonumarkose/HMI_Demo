package com.example.quest.hmi_demo.message;


import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.quest.hmi_demo.R;
import com.example.quest.hmi_demo.homescreen.MainActivityFragmentA;


public class Message_ListFragment extends Fragment implements mClickListener, MainActivityFragmentA.MessagesSelectedListener{

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    final int screenHeight=getScreenHeight();
    final int height=((screenHeight*4)/5);
    private RelativeLayout relativeLayout=null;

    ImageView newImageView;
    Activity mActivity;
    FragmentManager fm = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.message_list_fragment, container, false);

        relativeLayout=(RelativeLayout)view.findViewById(R.id.msgList_layout);


      relativeLayout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,height));


        fm = getActivity().getSupportFragmentManager();


        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mActivity=getActivity();
        mLayoutManager = new LinearLayoutManager(getActivity());
        //mLayoutManager.setReverseLayout(true);
        mRecyclerView.setLayoutManager(mLayoutManager);

       // mAdapter = new MyAdapter(getActivity(),mActivity );
       // mRecyclerView.setAdapter(mAdapter);


        MyAdapter mAdapter = new MyAdapter(getActivity(),mActivity);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setClickListener(this);


        newImageView=(ImageView)view.findViewById(R.id.imageView1);

        newImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                Message_SendFragment sendMessage = new Message_SendFragment();

                if (fm != null) {
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.addToBackStack(null);
                    ft.replace(R.id.fl_main_a, sendMessage);
                    ft.commit();
                }
            }
        });
        

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity=activity;
    }

    @Override
    public void onClick(View view, int position, String address) {

        FragmentManager fm = getFragmentManager();
   /*     Message_ChatFragment chatMessage = new Message_ChatFragment();

        Bundle args = new Bundle();
        args.putString("myNumber", address);
        chatMessage.setArguments(args);
        if (fm != null) {
            FragmentTransaction ft = fm.beginTransaction();
            ft.addToBackStack(null);
            ft.replace(R.id.fl_main_a,chatMessage);
            ft.commit();
        }*/

    }

    public static int getScreenWidth(){
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight(){
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    @Override
    public void messagesSelected(String state) {
        FragmentTransaction transaction = fm.beginTransaction();
        switch (state){
            case "max":transaction.replace(R.id.fl_messages, new Message_ListFragment());break;
            case "default":transaction.replace(R.id.fl_messages, new Message_DefaultFragment());break;
            default:transaction.replace(R.id.fl_messages, new Message_DefaultFragment());break;
        }
        transaction.commit();
    }
}
