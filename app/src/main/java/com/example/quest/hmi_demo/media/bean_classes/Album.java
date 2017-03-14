package com.example.quest.hmi_demo.media.bean_classes;

/**
 * Description: The class Album is used for storing the album songs in Media Module
 * @author Sonu
 * @version 1.0
 * Created  on 1/3/17.
 */
public class Album {

    //----------------------------------------------------------------------------------------------
    // Member Variables
    //----------------------------------------------------------------------------------------------

    private long id;
    private String title;
    private String artist;
    private String albumPath;
    private String album_id;

    //----------------------------------------------------------------------------------------------
    // Member Methods
    //----------------------------------------------------------------------------------------------

    public Album(long songID, String songTitle, String songArtist, String path, String albumid) {
        id = songID;
        title = songTitle;
        artist = songArtist;
        albumPath = path;
        album_id = albumid;
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

    public String getSongPath() {
        return album_id;
    }
}
