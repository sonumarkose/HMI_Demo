package com.example.quest.hmi_demo.navigation.model;



import java.util.ArrayList;
import java.util.List;

public class Leg {


    private Distance distance;

    private Duration duration;



    private String startAddress;

    private String endAddress;

    private List<Step> steps = new ArrayList<>();




    /**
     *
     * @return
     * The distance
     */
    public Distance getDistance() {
        return distance;
    }

    /**
     *
     * @param distance
     * The distance
     */
    public void setDistance(Distance distance) {
        this.distance = distance;
    }

    /**
     *
     * @return
     * The duration
     */
    public Duration getDuration() {
        return duration;
    }

    /**
     *
     * @param duration
     * The duration
     */
    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public String getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
    }

    public String getEndAddress() {
        return endAddress;
    }

    public void setEndAddress(String endAddress) {
        this.endAddress = endAddress;
    }
    public List<Step> getSteps() {
        return steps;
    }

    public void setStep(List<Step> steps) {
        this.steps = steps;
    }


}
