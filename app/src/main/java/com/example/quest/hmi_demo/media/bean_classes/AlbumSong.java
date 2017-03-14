package com.example.quest.hmi_demo.media.bean_classes;

/**
 * Description: The class AlbumSong is used for storing Album Songs in Media Module
 * @author Sonu
 * @version 1.0
 * Created  on 1/3/17.
 */

public class AlbumSong {

    //----------------------------------------------------------------------------------------------
    // Member Variables
    //----------------------------------------------------------------------------------------------

    private long id;
    private String title;
    private String artist;
    private String albumPath;
    private String album_id;
    private String duration;

    //----------------------------------------------------------------------------------------------
    // Member Methods
    //----------------------------------------------------------------------------------------------

    public AlbumSong(long songID, String songTitle, String songArtist, String path, String albumid, String dur) {
        id = songID;
        title = songTitle;
        artist = songArtist;
        albumPath = path;
        album_id = albumid;
        duration = dur;
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

    public String getDuration() {
        return duration;
    }
}
