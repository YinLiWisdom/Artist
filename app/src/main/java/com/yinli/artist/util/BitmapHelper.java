package com.yinli.artist.util;

import android.graphics.Bitmap;
import android.graphics.Matrix;

/**
 * Artist
 * Created by Yin Li on 18/04/15.
 * Copyright (c) 2015 Yin Li. All rights reserved.
 */
public class BitmapHelper {

    public static Bitmap resizeBitmapByView(Bitmap bm, int newWidth, int newHeight) {

        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // A matrix to manipulate
        Matrix matrix = new Matrix();

        if (scaleWidth == 0 || scaleHeight == 0) {
            // Resize the bitmap by given width or height only
            float scale = Math.max(scaleWidth, scaleHeight);
            matrix.postScale(scale, scale);
        } else {
            // Resize the bitmap by both given width and height
            matrix.postScale(scaleWidth, scaleHeight);
        }

        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }
}
