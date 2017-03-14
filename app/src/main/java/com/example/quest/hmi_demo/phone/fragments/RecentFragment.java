package com.example.quest.hmi_demo.phone.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ListFragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quest.hmi_demo.R;
import com.example.quest.hmi_demo.phone.utile.CallDetectService;
import com.example.quest.hmi_demo.phone.utile.CallLogHelper;
import com.example.quest.hmi_demo.phone.utile.OutgoingCallReceiver;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RecentFragment extends ListFragment {
    private ArrayList<String> conNames;
    private ArrayList<String> conNumbers;
    private ArrayList<String> conTime;
    private ArrayList<String> conDate;
    private ArrayList<String> conType;
    ListView listview;
   OutgoingCallReceiver obj;
    private boolean detectEnabled;
    Context _context;



    private static final int MY_PERMISSIONS_REQUEST_RECENT_CONTACTS = 1;


    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.READ_CONTACTS)) {


            } else {

                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_CONTACTS},
                        MY_PERMISSIONS_REQUEST_RECENT_CONTACTS);

            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_RECENT_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermissions();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recent_fragment, container, false);

        conNames = new ArrayList<String>();
        conNumbers = new ArrayList<String>();
        conTime = new ArrayList<String>();
        conDate = new ArrayList<String>();
        conType = new ArrayList<String>();

        Cursor curLog = CallLogHelper.getAllCallLogs(getActivity().getContentResolver());

        setCallLogs(curLog);
        curLog.close();




        listview = (ListView) rootView.findViewById(android.R.id.list);
        listview.setAdapter(new MyAdapter(getActivity(), android.R.layout.simple_list_item_1,
                R.id.tvNameMain, conNames));
        return rootView;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

      //  obj= new OutgoingCallReceiver();

    //    _context.registerReceiver(obj, new IntentFilter(Intent.ACTION_NEW_OUTGOING_CALL));
      //  setDetectEnabled(!detectEnabled);
        //startActivity(intentActiveCall);
       /* try {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + conNumbers.get(position)));
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            startActivity(callIntent);
        } catch (ActivityNotFoundException activityException) {
            Log.e("dialing-example", "Call failed", activityException);
        }*/

    }
  /*  @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        _context = context;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        _context.unregisterReceiver(obj);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        _context.unregisterReceiver(obj);
    }
*/
    private void setDetectEnabled(boolean enable) {
        detectEnabled = enable;

        Intent intent = new Intent(getActivity(), CallDetectService.class);
        if (enable) {
            // start detect service
            getActivity().startService(intent);
            Toast.makeText(getActivity(),"Detecting",Toast.LENGTH_SHORT).show();
           /* buttonToggleDetect.setText("Turn off");
            textViewDetectState.setText("Detecting");*/
        }
        else {
            // stop detect service
            getActivity().stopService(intent);
            Toast.makeText(getActivity(), "Not Detecting", Toast.LENGTH_SHORT).show();
            /*buttonToggleDetect.setText("Turn on");
            textViewDetectState.setText("Not detecting");*/
        }
    }
    private class MyAdapter extends ArrayAdapter<String> {

        public MyAdapter(Context context, int resource, int textViewResourceId,
                         ArrayList<String> conNames) {
            super(context, resource, textViewResourceId, conNames);

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View row = setList(position, parent);
            return row;
        }

        private View setList(int position, ViewGroup parent) {
            LayoutInflater inf = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View row = inf.inflate(R.layout.liststyle, parent, false);

            TextView tvName = (TextView) row.findViewById(R.id.tvNameMain);
            TextView tvNumber = (TextView) row.findViewById(R.id.tvNumberMain);
            TextView tvTime = (TextView) row.findViewById(R.id.tvTime);
            TextView tvDate = (TextView) row.findViewById(R.id.tvDate);
            TextView tvType = (TextView) row.findViewById(R.id.tvType);

            tvName.setText(conNames.get(position));
            tvNumber.setText(conNumbers.get(position));
            tvTime.setText("( " + conTime.get(position) + "sec )");
            tvDate.setText(conDate.get(position));
            tvType.setText("( " + conType.get(position) + " )");

            return row;
        }
    }

    private void setCallLogs(Cursor curLog) {
        while (curLog.moveToNext()) {
            String callNumber = curLog.getString(curLog
                    .getColumnIndex(android.provider.CallLog.Calls.NUMBER));
            conNumbers.add(callNumber);

            String callName = curLog
                    .getString(curLog
                            .getColumnIndex(android.provider.CallLog.Calls.CACHED_NAME));
            if (callName == null) {
                conNames.add("Unknown");
            } else
                conNames.add(callName);

            String callDate = curLog.getString(curLog
                    .getColumnIndex(android.provider.CallLog.Calls.DATE));
            SimpleDateFormat formatter = new SimpleDateFormat(
                    "dd-MMM-yyyy HH:mm");
            String dateString = formatter.format(new Date(Long
                    .parseLong(callDate)));
            conDate.add(dateString);

            String callType = curLog.getString(curLog
                    .getColumnIndex(android.provider.CallLog.Calls.TYPE));
            if (callType.equals("1")) {
                conType.add("Incoming");
            } else
                conType.add("Outgoing");

            String duration = curLog.getString(curLog
                    .getColumnIndex(android.provider.CallLog.Calls.DURATION));
            conTime.add(duration);

        }
    }


}
