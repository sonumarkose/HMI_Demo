package com.example.quest.hmi_demo.media.adapters;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.quest.hmi_demo.R;
import com.example.quest.hmi_demo.homescreen.MainActivityFragmentA;
import com.example.quest.hmi_demo.media.bean_classes.AlbumSong;
import com.example.quest.hmi_demo.media.bean_classes.MediaPlayerSingleton;
import com.example.quest.hmi_demo.media.interfaces.mClickListener;

import java.util.ArrayList;

/**
 * Description: The class AlbumSongAdapter is used for Album-Songs recycler view display in Media Module
 * @author Sonu
 * @version 1.0
 * Created  on 1/3/17.
 */
public class AlbumSongAdapter extends RecyclerView.Adapter<AlbumSongAdapter.ContactHolder> {

    //----------------------------------------------------------------------------------------------
    // Member Variables
    //----------------------------------------------------------------------------------------------

    private  Cursor cursor;
    private  Context mContext;
    private  mClickListener clickListener;
    private  ArrayList songList = new ArrayList();

    //----------------------------------------------------------------------------------------------
    // Member Methods
    //----------------------------------------------------------------------------------------------

    public AlbumSongAdapter(Context context, ArrayList list) {
        mContext = context;
        songList = list;
    }

    @Override
    public ContactHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.media_albumsonglist_adapter, parent, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        return new ContactHolder(view);
    }


    @Override
    public int getItemCount() {
        return songList.size();
    }


    public void setClickListener(mClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    @Override
    public void onBindViewHolder(ContactHolder holder, int position) {
        AlbumSong albumSong = (AlbumSong) songList.get(position);
        String title = albumSong.getTitle();
        String artist = albumSong.getArtist();
        String duration = albumSong.getDuration();
        String albumUri = albumSong.getAlbumpath();
        holder.songTitle.setText(title);
        holder.songArtist.setText(artist);
        holder.songDuration.setText(duration);
        if (albumUri != null) {
            holder.albumArt.setImageURI(Uri.parse(albumUri));
        } else {
            holder.albumArt.setImageResource(R.drawable.media_headphone);
        }

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

            Bundle bundle = new Bundle();
            AlbumSong albumSong = (AlbumSong) songList.get(getAdapterPosition());
            Fragment fragmenta = new MainActivityFragmentA();
            FragmentManager fm = ((AppCompatActivity) mContext).getSupportFragmentManager();
            bundle.putInt("index", getAdapterPosition());

            System.out.println("SongsList.onClick -> index =" + getAdapterPosition());
            System.out.println("SongsList.onClick() -> title =" + albumSong.getTitle());
            System.out.println("SongsList.onClick() -> artist =" + albumSong.getArtist());
            System.out.println("SongsList.onClick() -> songPath =" + albumSong.getSongPath());
            System.out.println("SongsList.onClick() -> albumURI =" + albumSong.getAlbumpath());



            MediaPlayerSingleton mediaSong = MediaPlayerSingleton.getInstance();
            if(mediaSong!= null)
            {
                mediaSong.setSongName(albumSong.getTitle());
                mediaSong.setArtistName(albumSong.getArtist());
                mediaSong.setSongPath(albumSong.getSongPath());
                mediaSong.setAlbumPath(albumSong.getAlbumpath());
            }

            //bundle.putStringArrayList("songDetails", details);
            MediaPlayerSingleton mp = MediaPlayerSingleton.getInstance();
            if(mp != null)
            {
                mp.setArguments(true);
            }
            //fragmenta.getArguments().clear();
            fragmenta.setArguments(bundle);
            if (fm != null) {
                fm.popBackStack();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.fl_main_a, fragmenta);
                ft.commit();
            }
        }
    }

}
