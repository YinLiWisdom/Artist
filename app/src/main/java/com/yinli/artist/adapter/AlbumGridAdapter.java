package com.yinli.artist.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yinli.artist.R;
import com.yinli.artist.data.Album;

import java.util.ArrayList;

/**
 * Artist
 * Created by Yin Li on 19/04/15.
 * Copyright (c) 2015 Yin Li. All rights reserved.
 */
public class AlbumGridAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Album> mData;

    public AlbumGridAdapter(Context mContext, ArrayList<Album> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public Album getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ItemHolder holder = null;

        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.grid_item, parent, false);

            holder = new ItemHolder();
            holder.layout = (ViewGroup) view.findViewById(R.id.topLayout);
            holder.img = (ImageView) view.findViewById(R.id.image);
            holder.txt = (TextView) view.findViewById(R.id.textBtn);

            view.setTag(holder);
        } else {
            holder = (ItemHolder) view.getTag();
        }

        Album album = mData.get(position);
        if (album != null) {
//            holder.img.setImageResource(item.getResourceId());
            holder.txt.setText(album.getTitle());
        }
        return view;
    }

    private static class ItemHolder {
        ViewGroup layout;
        ImageView img;
        TextView txt;
    }
}
