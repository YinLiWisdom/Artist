package com.yinli.artist;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecloud.pulltozoomview.PullToZoomScrollViewEx;
import com.yinli.artist.adapter.DetailPagerAdapter;
import com.yinli.artist.data.Artist;
import com.yinli.artist.ui.CustomGridView;
import com.yinli.artist.ui.CustomViewPager;
import com.yinli.artist.util.ImageLoader;


public class DetailActivity extends ActionBarActivity {

    private Artist mArtist;
    private ImageView picture;
    private CustomGridView gridView;
    private TextView name, description;
    private PullToZoomScrollViewEx scrollView;
    private View headView, zoomView, contentView;
    private CustomViewPager viewPager;
    private FragmentPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Bundle bundle = getIntent().getExtras();
        if (bundle == null) return;
        mArtist = bundle.getParcelable(MainListActivity.ARTISTNAME);
        if (mArtist == null) return;

        getSupportActionBar().setTitle(mArtist.getName());

        scrollView = (PullToZoomScrollViewEx) findViewById(R.id.scrollView);
        loadViewForCode();

        picture = (ImageView) zoomView.findViewById(R.id.iv_zoom);
        ViewTreeObserver observer = picture.getViewTreeObserver();
        observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                picture.getViewTreeObserver().removeOnPreDrawListener(this);
                if (!TextUtils.isEmpty(mArtist.getPicture())) {
                    Bitmap bitmap = new ImageLoader(DetailActivity.this).loadImage(picture, mArtist.getPicture(), (int) picture.getWidth(), 0);
                    if (bitmap != null) {
                        picture.setImageBitmap(bitmap);
                    }
                }
                return true;
            }
        });


        viewPager = (CustomViewPager) contentView.findViewById(R.id.viewPager);
        pagerAdapter = new DetailPagerAdapter(getSupportFragmentManager(), mArtist);
        viewPager.setAdapter(pagerAdapter);

        scrollView.getPullRootView().findViewById(R.id.iv_leftTab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0);
            }
        });

        scrollView.getPullRootView().findViewById(R.id.iv_rightTab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1);
            }
        });

        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
        int mScreenWidth = localDisplayMetrics.widthPixels;
        LinearLayout.LayoutParams localObject = new LinearLayout.LayoutParams(mScreenWidth, (int) (9.0F * (mScreenWidth / 16.0F)));
        scrollView.setHeaderLayoutParams(localObject);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadViewForCode() {
        headView = LayoutInflater.from(this).inflate(R.layout.profile_head_view, null, false);
        zoomView = LayoutInflater.from(this).inflate(R.layout.profile_zoom_view, null, false);
        contentView = LayoutInflater.from(this).inflate(R.layout.profile_content_view, null, false);
        scrollView.setHeaderView(headView);
        scrollView.setZoomView(zoomView);
        scrollView.setScrollContentView(contentView);
    }
}
