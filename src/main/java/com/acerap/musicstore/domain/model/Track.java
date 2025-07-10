package com.acerap.musicstore.domain.model;

import com.acerap.musicstore.domain.valueobjects.Duration;

public class Track {
    private int position;
    private String title;
    private Duration duration;

    public Track() {
    }

    public Track(int position, String title, Duration duration) {
        this.position = position;
        this.title = title;
        this.duration = duration;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }
}
