package com.yinli.artist;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

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


public class MainListActivity extends ActionBarActivity {

    private final static String TAG = "Debugging";
    private final static String ERROR = "Error";

    private ArrayList<Artist> artists;
    private ArrayList<Album> albums;
    private ListView mainList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_list);

        mainList = (ListView) findViewById(R.id.artistList);

        init();
    }

    private void init() {
        artists = new ArrayList<>();
        albums = new ArrayList<>();
        new LoadHttpGet().execute("http://i.img.co/data/data.json");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
                artists = helper.artistsJSONParser(result);
                albums = helper.albumsJSONParser(result);
                for (int i=0; i<artists.size(); i++) {
                    Log.e("Artist", artists.get(i).getName());
                }
            }
        }
    }

}
