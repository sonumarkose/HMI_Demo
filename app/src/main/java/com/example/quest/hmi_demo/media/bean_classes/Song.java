package com.example.quest.hmi_demo.media.bean_classes;

/**
 * Description: The class Song is used for saving song list from the device in Media module
 * @author Sonu
 * @version 1.0
 * Created  on 1/3/17.
 */
public class Song {

    //----------------------------------------------------------------------------------------------
    // Member Variables
    //----------------------------------------------------------------------------------------------

    private long id;
    private String title;
    private String artist;
    private String albumPath;
    private String songPath;


    //----------------------------------------------------------------------------------------------
    // Member Methods
    //----------------------------------------------------------------------------------------------

    public Song(long songID, String songTitle, String songArtist, String path, String songpath) {
        id = songID;
        title = songTitle;
        artist = songArtist;
        albumPath = path;
        songPath = songpath;
    }

    public long getID() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbumpath() {
        return albumPath;
    }

    public String getSongpath() {
        return songPath;
    }
}
