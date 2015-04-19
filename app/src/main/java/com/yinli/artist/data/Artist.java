package com.yinli.artist.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Artist
 * Created by Yin Li on 19/04/15.
 * Copyright (c) 2015 Yin Li. All rights reserved.
 */
public class Artist implements Parcelable {

    private long id;
    private String genres;
    private String picture;
    private String name;
    private String description;
    private ArrayList<Album> albums;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(ArrayList<Album> albums) {
        this.albums = albums;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(genres);
        dest.writeString(picture);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeList(albums);
    }

    public static final Parcelable.Creator<Artist> CREATOR = new Parcelable.Creator<Artist>() {
        public Artist createFromParcel(Parcel in) {
            Artist mArtist = new Artist();
            mArtist.id = in.readLong();
            mArtist.genres = in.readString();
            mArtist.picture = in.readString();
            mArtist.name = in.readString();
            mArtist.description = in.readString();
            mArtist.albums = in.readArrayList(null);
            return mArtist;
        }

        public Artist[] newArray(int size) {
            return new Artist[size];
        }
    };
}
