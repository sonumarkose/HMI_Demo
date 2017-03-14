package com.example.quest.hmi_demo.media.adapters;

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
import com.example.quest.hmi_demo.media.interfaces.mClickListener;

/**
 * Description: The class AlbumListAdapter is used for albumList recycler view display in Media Module
 * @author Sonu
 * @version 1.0
 * Created  on 1/3/17.
 */

public class AlbumListAdapter extends RecyclerView.Adapter<AlbumListAdapter.ContactHolder> {

    //----------------------------------------------------------------------------------------------
    // Member Variables
    //----------------------------------------------------------------------------------------------

    private Cursor cursor;
    private Context mContext;
    private mClickListener clickListener;

    //----------------------------------------------------------------------------------------------
    // Member Methods
    //----------------------------------------------------------------------------------------------
    public AlbumListAdapter(Context context, Cursor cursor) {
        mContext = context;
        this.cursor = cursor;
    }

    /**
     * Called when a view has been binded to List.
     *
     * @param v The view that was clicked.
     */
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
                (MediaStore.Audio.Albums.ALBUM)));
        holder.songArtist.setText(cursor.getString(cursor.getColumnIndex
                (MediaStore.Audio.Albums.ARTIST)));// get string for album art.
        holder.songDuration.setText(cursor.getString(cursor.getColumnIndex
                (MediaStore.Audio.Albums.NUMBER_OF_SONGS)) + " Song");
        String albumUri = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));


        if (albumUri != null) {
            holder.albumArt.setImageURI(Uri.parse(albumUri));
        } else {
            holder.albumArt.setImageResource(R.drawable.media_headphone);
        }

    }

    public Bundle getDetailsByPosition(int pos) {
        cursor.moveToPosition(pos);
        Long id = cursor.getLong(cursor.getColumnIndex
                (MediaStore.Audio.Albums._ID));
        String title = cursor.getString(cursor.getColumnIndex
                (MediaStore.Audio.Albums.ALBUM));
        String artist = cursor.getString(cursor.getColumnIndex
                (MediaStore.Audio.Albums.ARTIST));
        String songPath = "";
        String albumUri = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
        // return new Album(id,title,artist,albumUri,songPath);
        Bundle values = new Bundle();
        values.putLong("id", id);
        values.putString("title", title);
        values.putString("artist", artist);
        values.putString("albumURI", albumUri);
        values.putString("songPath", songPath);
        return values;

    }

    class ContactHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView albumArt;
        TextView songTitle, songArtist, songDuration;

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
