package com.example.quest.hmi_demo.navigation.model;



import java.io.Serializable;

/**
 * Created by test on 20/2/17.
 */
public class Step implements Serializable {


    private Distance distance;


    private Duration duration;


    private String path;


    private Polyline polyline;

    public Distance getDistance() {
        return distance;
    }

    public void setDistance(Distance distance) {
        this.distance = distance;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Polyline getPolyline() {
        return polyline;
    }

    public void setPolyline(Polyline polyline) {
        this.polyline = polyline;
    }




}
