package com.example.quest.hmi_demo.phone.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.quest.hmi_demo.R;
import com.example.quest.hmi_demo.homescreen.MainActivity;
import com.example.quest.hmi_demo.homescreen.MainActivityFragmentA;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by quest on 8/3/17.
 */
public class MaximizedButtonsFragment extends Fragment implements MainActivityFragmentA.TelephoneSelectedListener {
    View v;
    private SharedPreferences sharedPreferences;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    String mlog = " FragmentAllPhone ";
    MainActivityFragmentA mFragment;

    Map<FrameLayout,Integer> framesDetails=new LinkedHashMap<FrameLayout,Integer>();

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.maximized_buttons_fragment, container,false);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();


        Button contacts = (Button) v.findViewById(R.id.btn_contacts);
        Button recents = (Button) v.findViewById(R.id.btn_recents);
        Button keypad = (Button) v.findViewById(R.id.btn_keypad);




        contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.setStatus("MaxTelephone");
                mFragment = new MainActivityFragmentA();
                PhoneContactsFragment contactsFragment = new PhoneContactsFragment();
                fragmentTransaction.replace(R.id.fl_main_a,contactsFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });


        recents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.setStatus("MaxTelephone");
                Log.i(mlog, "uuuuuuuuuuuuuuu ");
                mFragment = new MainActivityFragmentA();
                RecentFragment recentFragment = new RecentFragment();
                fragmentTransaction.replace(R.id.fl_main_a,recentFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        keypad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.setStatus("MaxTelephone");
                Log.i(mlog , "uuuuuuuuuuuuuuu ");
                mFragment = new MainActivityFragmentA();
                KeypadFragment keypad = new KeypadFragment();
                fragmentTransaction.replace(R.id.fl_main_a,keypad);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        return v;

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
