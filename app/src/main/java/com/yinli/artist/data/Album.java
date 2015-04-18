package com.yinli.artist.data;

/**
 * Artist
 * Created by Yin Li on 19/04/15.
 * Copyright (c) 2015 Yin Li. All rights reserved.
 */
public class Album {

    private long id;
    private long artistId;
    private String title;
    private String type;
    private String picture;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getArtistId() {
        return artistId;
    }

    public void setArtistId(long artistId) {
        this.artistId = artistId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
