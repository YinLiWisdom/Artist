package com.yinli.artist.adapter;

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
import com.yinli.artist.data.Album;
import com.yinli.artist.util.BitmapHelper;
import com.yinli.artist.util.ImageLoader;

import java.util.ArrayList;

/**
 * Artist
 * Created by Yin Li on 19/04/15.
 * Copyright (c) 2015 Yin Li. All rights reserved.
 */
public class AlbumGridAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Album> mData;
    private final ImageLoader mImageLoader;

    public AlbumGridAdapter(Context context, ArrayList<Album> mData) {
        this.mContext = context;
        this.mData = mData;
        mImageLoader = new ImageLoader(context);
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
            final String thumbUrl = album.getPicture();

            holder.img.setTag(thumbUrl);
            holder.img.setImageResource(R.drawable.default_album);
            if (!TextUtils.isEmpty(thumbUrl)) {
                Bitmap bitmap = mImageLoader.loadImage(holder.img, thumbUrl);
                if (bitmap != null) {
                    float width = mContext.getResources().getDimension(R.dimen.main_list_picture_width);
                    Bitmap bmp = BitmapHelper.resizeBitmapByView(bitmap, (int) width, (int) width);
                    holder.img.setImageBitmap(bmp);
                }
            }

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
