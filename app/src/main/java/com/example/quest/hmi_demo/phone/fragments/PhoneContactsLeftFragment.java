package com.example.quest.hmi_demo.phone.fragments;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.quest.hmi_demo.R;

import java.util.ArrayList;

/**
 * Created by quest on 23/2/17.
 */
public class PhoneContactsLeftFragment extends ListFragment {

    ArrayList leftItems = new ArrayList();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.phone_contacts_left_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        leftItems.add("");
        leftItems.add("Library");
        leftItems.add("Contacts");
        leftItems.add("Keypad");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ArrayAdapter adapter = new ArrayAdapter(getActivity().getApplicationContext(), R.layout.support_simple_spinner_dropdown_item,leftItems);
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
      /*  MiddleRightFragment obj = (MiddleRightFragment)getFragmentManager().findFragmentById(R.id.fragment2);
       if(position==1) {
           if (obj != null ) {
               obj.createLibrary();
           }
       }
        if(position==2) {
            if (obj != null ) {

            }
        }
        if(position==3) {
            if (obj != null ) {

            }
        }*/
           /* if(middleRightFragment!=null)
            {
                middleRightFragment.update(position, (String) leftItems.get(position));

                // obj.update(array[position]);
            }
            else
            {
                Toast.makeText(getActivity().getApplicationContext(), "3rd Frgament is not visible", Toast.LENGTH_SHORT).show();
                Intent launchingIntent = new Intent(getActivity(),NewActivity.class);
                launchingIntent.putExtra("id", position);
                launchingIntent.putExtra("name", (String) array.get(position));
                launchingIntent.putExtra("details", (String) detailslist.get(position));
                launchingIntent.putExtra("updates", (String) updates.get(position));
                launchingIntent.putExtra("description", (String) descriptionlist.get(position));
                startActivity(launchingIntent);
            }*/
    }
}
