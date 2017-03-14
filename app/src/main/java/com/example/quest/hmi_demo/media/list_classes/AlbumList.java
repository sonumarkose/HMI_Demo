package com.example.quest.hmi_demo.media.list_classes;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.quest.hmi_demo.R;
import com.example.quest.hmi_demo.media.adapters.AlbumListAdapter;
import com.example.quest.hmi_demo.media.adapters.AlbumSongAdapter;
import com.example.quest.hmi_demo.media.bean_classes.AlbumSong;
import com.example.quest.hmi_demo.media.supporting_classes.DividerItemDecoration;
import com.example.quest.hmi_demo.media.supporting_classes.Utilities;
import com.example.quest.hmi_demo.media.interfaces.mClickListener;

import java.util.ArrayList;

/**
 * Created by quest on 24/2/17.
 */
public class AlbumList extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, mClickListener {

    private RecyclerView recyclerView;
    private AlbumListAdapter recycleAdapter;
    private AlbumSongAdapter albumSongAdapter;
    private Context context;
    private CursorLoader cursorLoader;
    private Utilities utilities;
    private int scrollPosition;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.media_songslist, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.playList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecoration(null);
        recyclerView.addItemDecoration(dividerItemDecoration);

        context = container.getContext();
        if (isReadPhoneStateAllowed()) {

            getLoaderManager().initLoader(1, null, this);

        } else {
            requestPhoneStatePermission();
        }

        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == 1) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(getActivity().getApplicationContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
                getLoaderManager().initLoader(1, null, this);
            } else {
                //Displaying another toast if permission is not granted

            }
        }
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

    /**
     * Instantiate and return a new Loader for the given ID.
     *
     * @param id   The ID whose loader is to be created.
     * @param args Any arguments supplied by the caller.
     * @return Return a new Loader instance that is ready to start loading.
     */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri musicUri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
        cursorLoader = new CursorLoader(getActivity().getApplicationContext(), musicUri, null, null, null, MediaStore.Audio.Albums.ALBUM);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        data.moveToFirst();
        recycleAdapter = new AlbumListAdapter(getActivity(), data);
        recyclerView.setAdapter(recycleAdapter);
        recycleAdapter.setClickListener(this);
    }

    /**
     * Called when a previously created loader is being reset, and thus
     * making its data unavailable.  The application should at this point
     * remove any references it has to the Loader's data.
     *
     * @param loader The Loader that is being reset.
     */
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onClick(View view, int position, Object object) {
        utilities = new Utilities();
        ArrayList<AlbumSong> mSongList = new ArrayList<AlbumSong>();
        //Album album = (Album) object;
        Bundle values = new Bundle();
        values = (Bundle) object;
        Long albumID = values.getLong("id");
        String albumName = values.getString("title");
        String[] column = {MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media._ID, MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.COMPOSER,
                MediaStore.Audio.Media.TRACK,};

        String where = MediaStore.Audio.Media.ALBUM + "=?";

        String whereVal[] = {albumName};

        String orderBy = MediaStore.Audio.Media.TITLE;

        Cursor cursor = getActivity().managedQuery(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                column, where, whereVal, orderBy);
        cursor.moveToFirst();
        String albumUri = "";

        do {
            final long id = cursor.getLong(1);
            final String songPath = cursor.getString(0);
            final String songName = cursor.getString(2);
            final String artist = cursor.getString(3) + "|" + cursor.getString(6);
            final String albumartid = cursor.getString(4);
            final String duration = utilities.milliSecondsToTimer(cursor.getLong(5));
            ContentResolver musicResolver = context.getContentResolver();
            Cursor musicCursor = musicResolver.query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART},
                    MediaStore.Audio.Albums._ID + "=?",
                    new String[]{albumartid},
                    null);

            if (musicCursor.moveToFirst()) {
                albumUri = musicCursor.getString(musicCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
                musicCursor.close();
            }
            final AlbumSong albumSong = new AlbumSong(id, songName, artist, albumUri, songPath, duration);
            mSongList.add(albumSong);
        } while (cursor.moveToNext());

        albumSongAdapter = new AlbumSongAdapter(getActivity(), mSongList);
        recyclerView.setAdapter(albumSongAdapter);
        albumSongAdapter.setClickListener(this);

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

}
