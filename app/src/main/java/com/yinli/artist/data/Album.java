package com.yinli.artist.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Artist
 * Created by Yin Li on 19/04/15.
 * Copyright (c) 2015 Yin Li. All rights reserved.
 */
public class Album implements Parcelable{

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeLong(artistId);
        dest.writeString(title);
        dest.writeString(type);
        dest.writeString(picture);
    }

    public static final Parcelable.Creator<Album> CREATOR = new Parcelable.Creator<Album>() {
        public Album createFromParcel(Parcel in) {
            Album mAlbum = new Album();
            mAlbum.id = in.readLong();
            mAlbum.artistId = in.readLong();
            mAlbum.title = in.readString();
            mAlbum.type = in.readString();
            mAlbum.picture = in.readString();
            return mAlbum;
        }

        public Album[] newArray(int size) {
            return new Album[size];
        }
    };
}
