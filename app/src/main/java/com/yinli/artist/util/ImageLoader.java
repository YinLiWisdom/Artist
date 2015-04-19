package com.yinli.artist.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.yinli.artist.R;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;

/**
 * Artist
 * Created by Yin Li on 18/04/15.
 * Copyright (c) 2015 Yin Li. All rights reserved.
 */
public class ImageLoader {
    private final static String TAG = "ImageDownloader";

    static Bitmap downloadBitmap(String src) {
/*        final AndroidHttpClient client = AndroidHttpClient.newInstance("Android");
        final HttpGet getRequest = new HttpGet(url);

        try {
            HttpResponse response = client.execute(getRequest);
            final int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                Log.w(TAG, "Error " + statusCode + " while retrieving bitmap from " + url);
                return null;
            }

            final HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream inputStream = null;
                try {
                    inputStream = entity.getContent();
//                    final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    final Bitmap bitmap = BitmapHelper.resizeBitmapByView(100, 100, inputStream);
                    return bitmap;
                } finally {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    entity.consumeContent();
                }
            }
        } catch (IOException e) {
            getRequest.abort();
            Log.w(TAG, "I/O error while retrieving bitmap from " + url, e);
        } catch (IllegalStateException e) {
            getRequest.abort();
            Log.w(TAG, "Incorrect URL: " + url);
        } catch (Exception e) {
            getRequest.abort();
            Log.w(TAG, "Error while retrieving bitmap from " + url, e);
        } finally {
            if (client != null) {
                client.close();
            }
        }
        return null;*/

        try {
            java.net.URL url = new java.net.URL(src);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            return bitmap;
        } catch (IOException e) {
            Log.w(TAG, "Error while retrieving bitmap from " + src, e);
        }
        return null;
    }

    class LoaderTask extends AsyncTask<String, Void, Bitmap> {
        private final WeakReference<ImageView> mImageViewReference;

        public LoaderTask(ImageView view) {
            this.mImageViewReference = new WeakReference<ImageView>(view);
        }


        @Override
        protected Bitmap doInBackground(String... params) {
            return downloadBitmap(params[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (isCancelled()) {
                bitmap = null;
            }

            if (mImageViewReference != null) {
                ImageView imageView = mImageViewReference.get();
                if (imageView != null) {
                    if (bitmap != null) imageView.setImageBitmap(bitmap);
                    else imageView.setImageResource(R.drawable.default_thumb);
                }
            }
        }

    }

/*    private static LoaderTask getBitmapDownloaderTask(ImageView imageView) {
        if (imageView != null) {
            Drawable drawable = imageView.getDrawable();
            if (drawable instanceof DownloadedDrawable) {
                DownloadedDrawable downloadedDrawable = (DownloadedDrawable) drawable;
                return downloadedDrawable.getBitmapDownloaderTask();
            }
        }
        return null;
    }

    static class DownloadedDrawable extends ColorDrawable {
        private final WeakReference<LoaderTask> bitmapDownloaderTaskReference;

        public DownloadedDrawable(LoaderTask bitmapDownloaderTask) {
            super(Color.BLACK);
            bitmapDownloaderTaskReference =
                    new WeakReference<LoaderTask>(bitmapDownloaderTask);
        }

        public LoaderTask getBitmapDownloaderTask() {
            return bitmapDownloaderTaskReference.get();
        }
    }*/

    public void download(String url, ImageView imageView) {
        new LoaderTask(imageView).execute(url);
    }
}
