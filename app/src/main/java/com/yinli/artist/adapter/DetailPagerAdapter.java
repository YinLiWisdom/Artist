package com.yinli.artist.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.yinli.artist.AlbumsFragment;
import com.yinli.artist.DetailsFragment;
import com.yinli.artist.data.Artist;

public class DetailPagerAdapter extends FragmentPagerAdapter {

    private final static int PAGENUM = 2;
    private Artist mArtist;

    public DetailPagerAdapter(FragmentManager fragmentManager, Artist artist) {
        super(fragmentManager);
        this.mArtist = artist;
    }

    @Override
    public int getCount() {
        return PAGENUM;
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return DetailsFragment.newInstance(mArtist);
            case 1:
                return AlbumsFragment.newInstance(mArtist);
            default:
                return null;
        }
    }
}
