package com.yinli.artist.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;

/**
 * Artist
 * Created by Yin Li on 18/04/15.
 * Copyright (c) 2015 Yin Li. All rights reserved.
 */
public class BitmapHelper {

    public static Bitmap resizeBitmapByView(int width, int height, InputStream inputStream) {
        // Get the size of the image
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(inputStream, null, bmOptions);

        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Figure out which way needs to be reduced less
        int scaleFactor = 1;
        if ((width > 0)) {
            scaleFactor = Math.min(photoW / width, photoH / width);
        }

        // Set bitmap options to scale the image decode target
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        // Decode the JPEG file into a Bitmap
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, bmOptions);
        return bitmap;
    }
}
