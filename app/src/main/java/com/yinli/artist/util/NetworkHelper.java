package com.yinli.artist.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Artist
 * Created by Yin Li on 17/04/15.
 * Copyright (c) 2015 Yin Li. All rights reserved.
 */
public class NetworkHelper {

    public static boolean isNetworkConnected(Context context) {
        final ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
        return (activeNetwork != null && activeNetwork.isConnected());
    }
}
