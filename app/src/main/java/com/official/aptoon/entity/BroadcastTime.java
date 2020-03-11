package com.official.aptoon.entity;

public class BroadcastTime {
    private String time;
    private String title;

    public BroadcastTime(String time, String title){
        this.time = time;
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public String getTitle() {
        return title;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
