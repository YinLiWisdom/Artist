package com.yinli.artist.util;

import android.util.Log;

import com.yinli.artist.data.Album;
import com.yinli.artist.data.Artist;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Artist
 * Created by Yin Li on 17/04/15.
 * Copyright (c) 2015 Yin Li. All rights reserved.
 */
public class JSONHelper {

    private static String TAG = "Debugging";

    public ArrayList<Artist> artistsJSONParser(String strResult) {
        try {
            JSONArray jsonArr = new JSONObject(strResult).getJSONArray("artists");
            ArrayList<Artist> list = new ArrayList<>();
            for (int i = 0; i < jsonArr.length(); i++) {
                Artist artist = new Artist();
                JSONObject jsonObj = jsonArr.getJSONObject(i);
                artist.setId(jsonObj.getLong("id"));
                artist.setGenres(jsonObj.getString("genres"));
                artist.setPicture(jsonObj.getString("picture"));
                artist.setName(jsonObj.getString("name"));
                artist.setDescription(jsonObj.getString("description"));
                list.add(artist);
            }
            return list;
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return null;
    }

    public ArrayList<Album> albumsJSONParser(String strResult) {
        try {
            JSONArray jsonArr = new JSONObject(strResult).getJSONArray("albums");
            ArrayList<Album> list = new ArrayList<>();
            for (int i = 0; i < jsonArr.length(); i++) {
                Album album = new Album();
                JSONObject jsonObj = jsonArr.getJSONObject(i);
                album.setId(jsonObj.getLong("id"));
                album.setArtistId(jsonObj.getLong("artistId"));
                album.setPicture(jsonObj.getString("picture"));
                album.setTitle(jsonObj.getString("title"));
                album.setType(jsonObj.getString("type"));
                list.add(album);
            }
            return list;
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return null;
    }
}
