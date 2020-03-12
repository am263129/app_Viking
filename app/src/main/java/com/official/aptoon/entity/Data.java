package com.official.aptoon.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data {


    @SerializedName("slides")
    @Expose
    private List<Slide> slides = null;

    @SerializedName("channels")
    @Expose
    private List<Channel> channels = null;

    @SerializedName("actors")
    @Expose
    private List<Actor> actors = null;


    @SerializedName("posters")
    @Expose
    private List<Poster> posters = null;

    @SerializedName("genres")
    @Expose
    private List<Genre> genres = null;

    @SerializedName("genre")
    @Expose
    private Genre genre;

    @SerializedName("streaming")
    @Expose
    private String streaming_URL;

    private int viewType = 1;

    public List<Slide> getSlides() {
        return slides;
    }

    public void setSlides(List<Slide> slides) {
        this.slides = slides;
    }


    public void setChannels(List<Channel> channels) {
        this.channels = channels;
    }

    public void setActors(List<Actor> actors) {
        this.actors = actors;
    }

    public void setStreaming_URL(String streaming_URL) {
        this.streaming_URL = streaming_URL;
    }

    public List<Channel> getChannels() {
        return channels;
    }

    public List<Actor> getActors() {
        return actors;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public Genre getGenre() {
        return genre;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public String getStreaming_URL() {
        return streaming_URL;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public Data setViewType(int viewType) {
        this.viewType = viewType;
        return this;
    }
    public int getViewType() {
        return viewType;
    }

    public List<Poster> getPosters() {
        return posters;
    }

    public void setPosters(List<Poster> posters) {
        this.posters = posters;
    }
}
