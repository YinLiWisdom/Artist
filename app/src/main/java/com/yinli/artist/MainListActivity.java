package com.yinli.artist;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.romainpiel.titanic.library.Titanic;
import com.romainpiel.titanic.library.TitanicTextView;
import com.yinli.artist.adapter.ArtistListAdapter;
import com.yinli.artist.data.Album;
import com.yinli.artist.data.Artist;
import com.yinli.artist.util.JSONHelper;
import com.yinli.artist.util.NetworkHelper;
import com.yinli.artist.util.Typefaces;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;


public class MainListActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {

    public final static String ARTISTNAME = "current_artist";
    private final static String JSONURL = "http://i.img.co/data/data.json";

    private final static String TAG = "Debugging";
    private final static String ERROR = "Error";

    private ArrayList<Artist> mArtists;
    private ArrayList<Album> mAlbums;
    private ListView mainList;
    private ArtistListAdapter mAdapter;
    private TitanicTextView titanicTextView;
    private Titanic titanic;
    private LinearLayout errorContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_list);

        init();
        mainList = (ListView) findViewById(R.id.artistList);
        errorContainer = (LinearLayout) findViewById(R.id.connectionErrorContainer);
        mainList.setOnItemClickListener(this);

        if (NetworkHelper.isNetworkConnected(this)) {
            errorContainer.setVisibility(View.INVISIBLE);
        } else {
            errorContainer.setVisibility(View.VISIBLE);
        }
    }

    private void init() {
        mArtists = new ArrayList<>();
        mAlbums = new ArrayList<>();
        new LoadHttpGet().execute(JSONURL);
    }

    private void loadList() {
        mAdapter = new ArtistListAdapter(MainListActivity.this, mArtists, R.layout.list_item);
        mainList.setAdapter(mAdapter);
    }

    public void btnClick(View view) {
        if (view.getId() == R.id.retryBtn) {
            // Refresh the activity
            recreate();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Artist artist = mArtists.get(position);
        ArrayList<Album> albums = new ArrayList<>();
        for (int i = 0; i < mAlbums.size(); i++) {
            if (mAlbums.get(i).getArtistId() == artist.getId()) {
                albums.add(mAlbums.get(i));
            }
        }
        artist.setAlbums(albums);
        Intent intent = new Intent(MainListActivity.this, DetailActivity.class);
        intent.putExtra(ARTISTNAME, artist);
        startActivity(intent);
    }

    private void startLoading() {
        titanicTextView = (TitanicTextView) findViewById(R.id.titanicLoading);
        titanicTextView.setTypeface(Typefaces.get(this, "Satisfy-Regular.ttf"));
        titanicTextView.setVisibility(View.VISIBLE);
        titanic = new Titanic();
        titanic.start(titanicTextView);
    }

    private void endLoading() {
        titanic.cancel();
        titanicTextView.setVisibility(View.INVISIBLE);
    }

    class LoadHttpGet extends AsyncTask<String, String, Boolean> {
        @Override
        protected Boolean doInBackground(String... urls) {
            HttpGet httpGet = new HttpGet(urls[0]);
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpResponse httpResponse = httpClient.execute(httpGet);
                if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    String strResult = EntityUtils.toString(httpResponse.getEntity());
                    if(!TextUtils.isEmpty(strResult) && strResult != null) {
                        JSONHelper helper = new JSONHelper();
                        mArtists = helper.artistsJSONParser(strResult);
                        mAlbums = helper.albumsJSONParser(strResult);
                        return true;
                    }
                } else return false;
            } catch (ClientProtocolException e) {
                Log.e(TAG, e.getMessage());
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
            return false;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            startLoading();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result) {
                loadList();
            }
            endLoading();
        }
    }

}
