package com.yinli.artist;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yinli.artist.adapter.AlbumGridAdapter;
import com.yinli.artist.data.Artist;
import com.yinli.artist.ui.CustomGridView;


/**
 * A simple {@link Fragment} subclass.
 */
public class AlbumsFragment extends Fragment {

    private final static String ARTISTNAME = "artist_name";
    private Artist mArtist;

    public static AlbumsFragment newInstance(Artist artist) {
        AlbumsFragment fragment = new AlbumsFragment();
        Bundle extras = new Bundle();
        extras.putParcelable(ARTISTNAME, artist);
        fragment.setArguments(extras);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mArtist = getArguments().getParcelable(ARTISTNAME);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.profile_content_albums_view, container, false);
        CustomGridView gridView = (CustomGridView) view.findViewById(R.id.iv_gridView);
        gridView.setEmptyView(view.findViewById(R.id.empty_grid_view));
        gridView.setExpanded(true);
        AlbumGridAdapter adapter = new AlbumGridAdapter(getActivity(), mArtist.getAlbums());
        gridView.setAdapter(adapter);
        return view;
    }


}
