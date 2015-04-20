package com.yinli.artist.util;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * Artist
 * Created by Yin Li on 20/04/15.
 * Copyright (c) 2015 Yin Li. All rights reserved.
 */
public class ImageCache extends LruCache<String, Bitmap> {

    public ImageCache( int maxSize ) {
        super( maxSize );
    }

    @Override
    protected int sizeOf( String key, Bitmap value ) {
        return value.getByteCount();
    }

    @Override
    protected void entryRemoved( boolean evicted, String key, Bitmap oldValue, Bitmap newValue ) {
        oldValue.recycle();
    }

}
