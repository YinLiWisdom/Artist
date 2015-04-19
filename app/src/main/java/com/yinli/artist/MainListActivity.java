package com.yinli.artist;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.yinli.artist.adapter.ArtistListAdapter;
import com.yinli.artist.data.Album;
import com.yinli.artist.data.Artist;
import com.yinli.artist.util.JSONHelper;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;


public class MainListActivity extends ActionBarActivity implements AdapterView.OnItemClickListener{

    public final static String ARTISTNAME = "current_artist";

    private final static String TAG = "Debugging";
    private final static String ERROR = "Error";

    private ArrayList<Artist> mArtists;
    private ArrayList<Album> mAlbums;
    private ListView mainList;
    private ArtistListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_list);

        init();
        mainList = (ListView) findViewById(R.id.artistList);
        mainList.setOnItemClickListener(this);
    }

    private void init() {
        mArtists = new ArrayList<>();
        mAlbums = new ArrayList<>();
        new LoadHttpGet().execute("http://i.img.co/data/data.json");
    }

    private void loadList() {
        mAdapter = new ArtistListAdapter(MainListActivity.this, mArtists, R.layout.list_item);
        mainList.setAdapter(mAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Artist artist = mArtists.get(position);
        ArrayList<Album> albums = new ArrayList<>();
        for(int i = 0; i < mAlbums.size(); i++) {
            if (mAlbums.get(i).getArtistId() == artist.getId()) {
                albums.add(mAlbums.get(i));
            }
        }
        artist.setAlbums(albums);
        Intent intent = new Intent(MainListActivity.this, DetailActivity.class);
        intent.putExtra(ARTISTNAME, artist);
        startActivity(intent);
    }

    class LoadHttpGet extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... urls) {
            HttpGet httpGet = new HttpGet(urls[0]);
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpResponse httpResponse = httpClient.execute(httpGet);
                if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    String strResult = EntityUtils.toString(httpResponse.getEntity());
                    return strResult;
                } else return ERROR;
            } catch (ClientProtocolException e) {
                Log.e(TAG, e.getMessage());
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (!TextUtils.equals(result, ERROR) && result != null) {
                JSONHelper helper = new JSONHelper();
                mArtists = helper.artistsJSONParser(result);
                mAlbums = helper.albumsJSONParser(result);
                loadList();
            }
        }
    }

}
