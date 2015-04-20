package com.yinli.artist;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yinli.artist.data.Artist;

import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends Fragment {

    private final static String ARTISTNAME = "artist_name";
    private Artist mArtist;

    public static DetailsFragment newInstance(Artist artist) {
        DetailsFragment fragment = new DetailsFragment();
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

        View view = inflater.inflate(R.layout.profile_content_details_view, container, false);
        TextView name = (TextView) view.findViewById(R.id.iv_name);
        TextView genres = (TextView) view.findViewById(R.id.iv_genres);
        TextView description = (TextView) view.findViewById(R.id.iv_description);

        name.setText(mArtist.getName());
        genres.setText(mArtist.getGenres());
        description.setText(Html.fromHtml(mArtist.getDescription()));
        return view;
    }

}
