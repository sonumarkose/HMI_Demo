package com.example.quest.hmi_demo.media.list_classes;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.quest.hmi_demo.R;
import com.example.quest.hmi_demo.homescreen.MainActivityFragmentA;
import com.example.quest.hmi_demo.media.adapters.SongListAdapter;
import com.example.quest.hmi_demo.media.supporting_classes.DividerItemDecoration;
import com.example.quest.hmi_demo.media.bean_classes.MediaPlayerSingleton;
import com.example.quest.hmi_demo.media.interfaces.mClickListener;

import java.util.ArrayList;

/**
 * Created by quest on 22/2/17.
 */
public class SongsList extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, mClickListener {

    private ArrayList songList = new ArrayList();
    private ListView songView;
    private Context context;
    private RecyclerView recyclerView;
    private SongListAdapter recycleAdapter;
    private int tappedIndex;
    private MediaPlayerSingleton obj;
    private int scrollPosition;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.media_songslist, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.playList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecoration(null);
        recyclerView.addItemDecoration(dividerItemDecoration);
        obj = MediaPlayerSingleton.getInstance();
        context = container.getContext();
        if (isReadPhoneStateAllowed()) {
            startActivity();

        } else {
            requestPhoneStatePermission();
        }
        recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return view;
    }

    public void startActivity() {
        getLoaderManager().initLoader(1, null, this);
        //ringProgressDialog = ProgressDialog.show(context, "Please wait ...", "Downloading Contact ...", true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == 1) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(getActivity().getApplicationContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
                startActivity();
            } else {
                //Displaying another toast if permission is not granted

            }
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        CursorLoader cursorLoader = new CursorLoader(getActivity().getApplicationContext(), musicUri, null, null, null, android.provider.MediaStore.Audio.Media.TITLE);
        return cursorLoader;
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        data.moveToFirst();
        recycleAdapter = new SongListAdapter(getActivity(), data);
        recyclerView.setAdapter(recycleAdapter);
        recycleAdapter.setClickListener(this);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private boolean isReadPhoneStateAllowed() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);

        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;

        //If permission is not granted returning false
        return false;
    }

    private void requestPhoneStatePermission() {
        //And finally ask for the permission
        this.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

    }


    @Override
    public void onClick(View view, int position, Object object) {

        tappedIndex = position;
        //playSong(position);
        FragmentManager fm = getFragmentManager();
        Fragment fragmenta = new MainActivityFragmentA();
        Bundle bundle = new Bundle();
        // Song song = (Song) object;
        Bundle values = new Bundle();
        values = (Bundle) object;
        bundle.putInt("index", tappedIndex);
        ArrayList details = new ArrayList();
        /*
        details.add(values.getString("title"));
        details.add(values.getString("artist"));
        details.add(values.getString("songPath"));
        details.add(values.getString("albumURI"));*/

        System.out.println("SongsList.onClick -> index =" + values.getInt("index"));
        System.out.println("SongsList.onClick() -> title =" + values.getString("title"));
        System.out.println("SongsList.onClick() -> artist =" + values.getString("artist"));
        System.out.println("SongsList.onClick() -> songPath =" + values.getString("songPath"));
        System.out.println("SongsList.onClick() -> albumURI =" + values.getString("albumURI"));

        /*details.add(0, tappedIndex);
        details.add(1, values.getString("title"));
        details.add(2, values.getString("artist"));
        details.add(3, values.getString("songPath"));
        details.add(4, values.getString("albumURI"));*/

       // bundle.putStringArrayList("songDetails", details);
        bundle.putString("title", values.getString("title"));
        bundle.putString("artist", values.getString("artist"));
        bundle.putString("songPath",values.getString("songPath"));
        bundle.putString("albumURI", values.getString("albumURI"));

        MediaPlayerSingleton mediaSong = MediaPlayerSingleton.getInstance();
        if(mediaSong!= null)
        {
            mediaSong.setSongName(values.getString("title"));
            mediaSong.setArtistName(values.getString("artist"));
            mediaSong.setSongPath(values.getString("songPath"));
            mediaSong.setAlbumPath(values.getString("albumURI"));
        }

        //fragmenta.getArguments().clear();
        fragmenta.setArguments(bundle);
        MediaPlayerSingleton mp = MediaPlayerSingleton.getInstance();
        if(mp != null)
        {
            mp.setArguments(true);
        }
        if (fm != null) {
            fm.popBackStack();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fl_main_a, fragmenta);
            ft.commit();
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        scrollPosition = recyclerView.getScrollY();

    }

    @Override
    public void onResume() {
        super.onResume();
        recyclerView.setScrollY(scrollPosition);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
