package com.yinli.artist.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Artist
 * Created by Yin Li on 18/04/15.
 * Copyright (c) 2015 Yin Li. All rights reserved.
 */
public class ImageLoader {
    private final static String TAG = "ImageDownloader";
    private final static String UNIQUENAME = "artist_bitmap";

    private Context context;
    // Disk cache default size 10M
    static final int DISK_CACHE_DEFAULT_SIZE = 10 * 1024 * 1024;
    private ImageCache memCache;
    private DiskLruCache diskCache;

    public ImageLoader(Context context) {
        this.context = context;
        initMemCache(context);
        initDiskLruCache();
    }

    /* Initialize memory cache */
    private void initMemCache(Context context) {
        // Calculate cache size based on available heap memory
        int memClass = ( (ActivityManager)context.getSystemService( Context.ACTIVITY_SERVICE ) ).getMemoryClass();
        int cacheSize = 1024 * 1024 * memClass / 8;
        memCache = new ImageCache( cacheSize );
    }

    /* Initialize disk cache */
    private void initDiskLruCache() {
        try {
            File cacheDir = getDiskCacheDir(context, UNIQUENAME);
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }
            diskCache = DiskLruCache.open(cacheDir, getAppVersion(context), 1, DISK_CACHE_DEFAULT_SIZE);
        } catch (IOException e) {
            Log.w(TAG, e.getMessage());
        }
    }

    /* Get bitmap from memory cache by url */
    public Bitmap getBitmapFromMem(String url) {
        return memCache.get(url);
    }

    /* Put bitmap into memory cache */
    public void putBitmapToMem(String url, Bitmap bitmap) {
        memCache.put(url, bitmap);
    }

    /* Get bitmap from disk cache by url */
    public Bitmap getBitmapFromDisk(String url) {
        try {
            String key = hashKeyForDisk(url);
            DiskLruCache.Snapshot snapShot = diskCache.get(key);
            if (snapShot != null) {
                InputStream is = snapShot.getInputStream(0);
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                return bitmap;
            }
        } catch (IOException e) {
            Log.w(TAG, e.getMessage());
        }

        return null;
    }

    public Bitmap loadImage(ImageView imageView, String imageUrl) {

        Bitmap bitmap = getBitmapFromMem(imageUrl);
        if (bitmap != null) {
            Log.i(TAG, "Image exists in memory");
            return bitmap;
        }

        bitmap = getBitmapFromDisk(imageUrl);
        if (bitmap != null) {
            Log.i(TAG, "Image exists in file");
            // Try to put the bitmap into memory cache
            putBitmapToMem(imageUrl, bitmap);
            return bitmap;
        }

        // Download image from internet if it has not been cached
        if (!TextUtils.isEmpty(imageUrl)) {
            new ImageDownloadTask(imageView).execute(imageUrl);
        }

        return null;
    }

    class ImageDownloadTask extends AsyncTask<String, Integer, Bitmap> {
        private String imageUrl;
        private final WeakReference<ImageView> mImageViewReference;

        public ImageDownloadTask(ImageView imageView) {
            this.mImageViewReference = new WeakReference<ImageView>(imageView);
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                imageUrl = params[0];
                String key = hashKeyForDisk(imageUrl);

                DiskLruCache.Editor editor = diskCache.edit(key);
                if (editor != null) {
                    OutputStream outputStream = editor.newOutputStream(0);
                    if (downloadUrlToStream(imageUrl, outputStream)) {
                        editor.commit();
                    } else {
                        editor.abort();
                    }
                }
                diskCache.flush();

                Bitmap bitmap = getBitmapFromDisk(imageUrl);
                if (bitmap != null) {
                    putBitmapToMem(imageUrl, bitmap);
                }

                return bitmap;
            } catch (IOException e) {
                Log.w(TAG, e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (isCancelled()) {
                bitmap = null;
            }

            if (mImageViewReference != null) {
                ImageView imageView = mImageViewReference.get();
                if (imageView != null) {
                    if (bitmap != null) {
                        if (imageView.getTag() != null && imageView.getTag().equals(imageUrl)) {
                            imageView.setImageBitmap(bitmap);
                        }
                    }
                }
            }
        }

        private boolean downloadUrlToStream(String urlString, OutputStream outputStream) {
            HttpURLConnection urlConnection = null;
            BufferedOutputStream out = null;
            BufferedInputStream in = null;
            try {
                final URL url = new URL(urlString);
                urlConnection = (HttpURLConnection) url.openConnection();
                in = new BufferedInputStream(urlConnection.getInputStream(), 8 * 1024);
                out = new BufferedOutputStream(outputStream, 8 * 1024);
                int b;
                while ((b = in.read()) != -1) {
                    out.write(b);
                }
                return true;
            } catch (final IOException e) {
                Log.w(TAG, e.getMessage());
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                try {
                    if (out != null) {
                        out.close();
                    }
                    if (in != null) {
                        in.close();
                    }
                } catch (final IOException e) {
                    Log.w(TAG, e.getMessage());
                }
            }
            return false;
        }
    }

    private File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }

    private int getAppVersion(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            Log.w(TAG, e.getMessage());
        }
        return 1;
    }

    private String hashKeyForDisk(String key) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
            Log.w(TAG, e.getMessage());
        }
        return cacheKey;
    }

    private String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }
}
