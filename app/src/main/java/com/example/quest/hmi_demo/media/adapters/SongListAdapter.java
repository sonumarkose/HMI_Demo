package com.example.quest.hmi_demo.media.adapters;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.quest.hmi_demo.R;
import com.example.quest.hmi_demo.media.supporting_classes.Utilities;
import com.example.quest.hmi_demo.media.interfaces.mClickListener;

/**
 * Description: The class SongListAdapter is used for song list recycler view display in Media Module
 * @author Sonu
 * @version 1.0
 * Created  on 1/3/17.
 */
public class SongListAdapter extends RecyclerView.Adapter<SongListAdapter.ContactHolder> {

    //----------------------------------------------------------------------------------------------
    // Member Variables
    //----------------------------------------------------------------------------------------------

    private Cursor cursor;
    private Context mContext;
    private mClickListener clickListener;
    private Utilities utils;

    //----------------------------------------------------------------------------------------------
    // Member Methods
    //----------------------------------------------------------------------------------------------

    public SongListAdapter(Context context, Cursor cursor) {
        mContext = context;
        this.cursor = cursor;
        utils = new Utilities();
    }

    @Override
    public ContactHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.media_songlist_adapter, parent, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        return new ContactHolder(view);
    }


    @Override
    public int getItemCount() {
        return cursor.getCount();
    }


    public void setClickListener(mClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    @Override
    public void onBindViewHolder(ContactHolder holder, int position) {
        cursor.moveToPosition(position);
        holder.songTitle.setText(cursor.getString(cursor.getColumnIndex
                (MediaStore.Audio.Media.TITLE)));
        holder.songArtist.setText(cursor.getString(cursor.getColumnIndex
                (MediaStore.Audio.Media.ARTIST)));
        holder.songDuration.setText(utils.milliSecondsToTimer(cursor.getLong(cursor.getColumnIndex
                (MediaStore.Audio.Media.DURATION))));
        String albumID = cursor.getString(cursor.getColumnIndex
                (MediaStore.Audio.Media.ALBUM_ID)); // get string for album art.

        ContentResolver musicResolver = mContext.getContentResolver();
        Cursor musicCursor = musicResolver.query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART},
                MediaStore.Audio.Albums._ID + "=?",
                new String[]{albumID},
                null);

        if (musicCursor.moveToFirst()) {
            String albumUri = musicCursor.getString(musicCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
            musicCursor.close();
            if (albumUri != null) {
                holder.albumArt.setImageURI(Uri.parse(albumUri));
            } else {
                holder.albumArt.setImageResource(R.drawable.media_headphone);
            }
        }
    }

    public Bundle getDetailsByPosition(int pos) {
        System.out.println("SongListAdapter.getDetailsByPosition() = "+pos);
        cursor.moveToPosition(pos);
        Long id = cursor.getLong(cursor.getColumnIndex
                (MediaStore.Audio.Media._ID));
        String title = cursor.getString(cursor.getColumnIndex
                (MediaStore.Audio.Media.TITLE));
        String artist = cursor.getString(cursor.getColumnIndex
                (MediaStore.Audio.Media.ARTIST));
        String songduration = cursor.getString(cursor.getColumnIndex
                (MediaStore.Audio.Media.DURATION));
        String albumID = cursor.getString(cursor.getColumnIndex
                (MediaStore.Audio.Media.ALBUM_ID)); // get string for album art.
        String songPath = cursor.getString(cursor.getColumnIndex
                (MediaStore.Audio.Media.DATA));

        ContentResolver musicResolver = mContext.getContentResolver();
        Cursor musicCursor = musicResolver.query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART},
                MediaStore.Audio.Albums._ID + "=?",
                new String[]{albumID},
                null);
        String albumUri = "";
        if (musicCursor.moveToFirst()) {
            albumUri = musicCursor.getString(musicCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
            musicCursor.close();
            if (albumUri != null) {
                // holder.ivContactPhoto.setImageURI(Uri.parse(albumUri));
            } else {
                //holder.ivContactPhoto.setImageResource(R.drawable.media_headphone);
            }
        }
        Bundle values = new Bundle();
        values.putLong("id", id);
        values.putString("title", title);
        values.putString("artist", artist);
        values.putString("albumURI", albumUri);
        values.putString("songPath", songPath);
        return values;
        // return new Song(id,title,artist,albumUri,songPath);

    }

    class ContactHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView albumArt;
        TextView songTitle, songArtist, songDuration;
        int selectedItem;

        public ContactHolder(View itemView) {
            super(itemView);
            albumArt = (ImageView) itemView.findViewById(R.id.albumart);
            songTitle = (TextView) itemView.findViewById(R.id.songtitle);
            songArtist = (TextView) itemView.findViewById(R.id.songtrack);
            songDuration = (TextView) itemView.findViewById(R.id.songDuration);
            itemView.setClickable(true);
            itemView.setOnClickListener(this);

        }

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            if (clickListener != null) {
                Bundle bundle = getDetailsByPosition(getAdapterPosition());
                clickListener.onClick(v, getAdapterPosition(), bundle);
            }

        }
    }
}