package com.yinli.artist.ui;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by yinli on 20/04/15.
 */

public class ExpandableHeightViewPager extends ViewPager {

    private static final String TAG = "ViewPager";

    public ExpandableHeightViewPager(Context context)
    {
        super(context);
    }

    public ExpandableHeightViewPager(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }


    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int height = 0;
        Log.i(TAG, "getChildCount() = " + getChildCount() + "");
        for(int i = 0; i < getChildCount(); i++) {

            View child = getChildAt(i);
            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int h = child.getMeasuredHeight();
            Log.i(TAG, "getMeasuredHeight() = " + h + "");
            height = Math.max(h, height);
        }

        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

}

