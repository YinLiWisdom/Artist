package com.yinli.artist.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yinli.artist.R;
import com.yinli.artist.data.Artist;
import com.yinli.artist.util.ImageLoader;

import java.util.ArrayList;

/**
 * Artist
 * Created by Yin Li on 18/04/15.
 * Copyright (c) 2015 Yin Li. All rights reserved.
 */
public class ArtistListAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Artist> mData;
    private int mLayoutResId;
    private final ImageLoader mImageLoader = new ImageLoader();

    public ArtistListAdapter(Context context, ArrayList data, int layoutResId) {
        this.mData = data;
        this.mContext = context;
        this.mLayoutResId = layoutResId;
    }

    private static class ViewHolder {
        TextView name;
        TextView genres;
        ImageView thumbnail;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(mLayoutResId, parent, false);

            holder = new ViewHolder();
            holder.name = (TextView) row.findViewById(R.id.name);
            holder.genres = (TextView) row.findViewById(R.id.genres);
            holder.thumbnail = (ImageView) row.findViewById(R.id.thumbnail);

            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }


        Artist artist = mData.get(position);
        if (artist != null) {
            holder.name.setText(artist.getName());
            holder.genres.setText(artist.getGenres());

            mImageLoader.download(artist.getPicture(), holder.thumbnail);
        }

        return row;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
