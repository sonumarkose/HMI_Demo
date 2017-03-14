package com.example.quest.hmi_demo.media.bean_classes;

import android.media.MediaPlayer;

import java.util.ArrayList;

/**
 * Description: The class MediaPlayerSingleton is used for generate the Media player class object and some flags in Media Module
 * @author Sonu
 * @version 1.0
 * Created  on 1/3/17.
 */
public class MediaPlayerSingleton extends MediaPlayer {

    //----------------------------------------------------------------------------------------------
    // Member Variables
    //----------------------------------------------------------------------------------------------

    private static MediaPlayerSingleton mediaPlayerSingleton;
    private boolean libraryDisplayed,albumDisplayed,artistDisplayed,readArguments;
    private ArrayList SongList;
    private String title, artist, albumArtPath, songpath,playStatus;

    //----------------------------------------------------------------------------------------------
    // Member Methods
    //----------------------------------------------------------------------------------------------

    private MediaPlayerSingleton() {
        SongList = new ArrayList();
        libraryDisplayed = false;
        albumDisplayed = false;
        artistDisplayed = false;
        readArguments = false;
        playStatus = "";
    }

    public static MediaPlayerSingleton getInstance() {
        //synchronized (mediaPlayerSingleton)  { // if you'll be using it in moe then one thread
        if (mediaPlayerSingleton == null)
            mediaPlayerSingleton = new MediaPlayerSingleton();
        //}

        return mediaPlayerSingleton;
    }

    public ArrayList getSongList() {
        return SongList;
    }

    public void setSongList(ArrayList songList) {
        SongList = songList;
    }

    public String getSongName() {
        return title;
    }

    public void setSongName(String name) {
        title = name;
    }

    public String getArtistName() {
        return artist;
    }

    public void setArtistName(String name) {
        artist = name;
    }

    public void setAlbumPath(String name) {
        albumArtPath = name;
    }

    public String getAlbumPath() {
        return albumArtPath;
    }

    public void setSongPath(String str) {
        songpath = str;
    }

    public String getSongpath() {
        return songpath;
    }

    public boolean getLibraryDisplayed() {
        return libraryDisplayed;
    }

    public void setLibraryDisplayed(boolean value) {
        libraryDisplayed = value;
    }

    public boolean getAlbumDisplayed() {
        return albumDisplayed;
    }

    public void setAlbumDisplayed(boolean value) {
        albumDisplayed = value;
    }

    public boolean getArtistDisplayed() {
        return artistDisplayed;
    }

    public void setArtistDisplayed(boolean value) {
        artistDisplayed = value;
    }

    public String getPlayStatus() {
        return playStatus;
    }

    public void setPlayStatus(String value) {
        playStatus = value;
    }

    public boolean getArgumentStatus() {
        return readArguments;
    }

    public void setArguments(boolean value) {
        readArguments = value;
    }
}