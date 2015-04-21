package com.yinli.artist.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
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
    private final ImageLoader mImageLoader;

    public ArtistListAdapter(Context context, ArrayList data, int layoutResId) {
        this.mData = data;
        this.mContext = context;
        this.mLayoutResId = layoutResId;
        mImageLoader = new ImageLoader(context);
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
            final String thumbUrl = artist.getPicture();
            holder.name.setText(artist.getName());
            holder.genres.setText(artist.getGenres());

            holder.thumbnail.setTag(thumbUrl);
            holder.thumbnail.setImageResource(R.drawable.default_avatar);
            if (!TextUtils.isEmpty(thumbUrl)) {
                float width = mContext.getResources().getDimension(R.dimen.main_list_picture_width);
                Bitmap bitmap = mImageLoader.loadImage(holder.thumbnail, thumbUrl, (int)width, 0);
                if (bitmap != null) {
                    holder.thumbnail.setImageBitmap(bitmap);
                }
            }
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
