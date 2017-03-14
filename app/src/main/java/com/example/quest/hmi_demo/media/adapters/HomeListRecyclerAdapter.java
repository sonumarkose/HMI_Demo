package com.example.quest.hmi_demo.media.adapters;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.quest.hmi_demo.R;
import com.example.quest.hmi_demo.media.interfaces.mClickListener;

import java.util.ArrayList;

/**
 * Description: The class HomeListRecyclerAdapter is used for Home Screen Song List recycler view display in Media Module
 * @author Sonu
 * @version 1.0
 * Created  on 1/3/17.
 */
public class HomeListRecyclerAdapter extends RecyclerView.Adapter<HomeListRecyclerAdapter.SongHolder> {

    //----------------------------------------------------------------------------------------------
    // Member Variables
    //----------------------------------------------------------------------------------------------

    private Context mContext;
    private mClickListener clickListener;
    private ArrayList songList = new ArrayList();

    //----------------------------------------------------------------------------------------------
    // Member Methods
    //----------------------------------------------------------------------------------------------

    public HomeListRecyclerAdapter(Context context, ArrayList list) {
        mContext = context;
        songList = list;
    }

    @Override
    public SongHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.media_homesonglistadapter, parent, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        return new SongHolder(view);
    }


    @Override
    public int getItemCount() {
        return songList.size();
    }


    public void setClickListener(mClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    @Override
    public void onBindViewHolder(SongHolder holder, int position) {
        ArrayList currentSong = new ArrayList();
        currentSong = (ArrayList) songList.get(position);

        holder.songTitle.setText((String) currentSong.get(2));
        holder.songArtist.setText((String) currentSong.get(1));
        String albumID = (String) currentSong.get(6); // get string for album art.

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
        //animate(holder);


    }

    public void swapCursor(ArrayList data) {
        songList.clear();
        songList.addAll(data);
        notifyDataSetChanged();
    }

    public void animate(RecyclerView.ViewHolder viewHolder) {
        final Animation animAnticipateOvershoot = AnimationUtils.loadAnimation(mContext, R.anim.media_anticipate_overshoot_interpolator);
        viewHolder.itemView.setAnimation(animAnticipateOvershoot);
    }

    class SongHolder extends RecyclerView.ViewHolder implements OnClickListener {
        ImageView albumArt;
        TextView songTitle, songArtist;

        public SongHolder(View itemView) {
            super(itemView);
            albumArt = (ImageView) itemView.findViewById(R.id.albumart);
            songTitle = (TextView) itemView.findViewById(R.id.songtitle);
            songArtist = (TextView) itemView.findViewById(R.id.songtrack);
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
            if (clickListener != null)
                clickListener.onClick(v, getAdapterPosition(), songList.get(getAdapterPosition()));

        }
    }
}