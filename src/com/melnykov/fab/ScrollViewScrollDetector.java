package com.melnykov.fab;

import android.widget.ScrollView;

abstract class ScrollViewScrollDetector implements ObservableScrollView.OnScrollChangedListener {
    private int mLastScrollY;
    private int mLastScrollX;
    private int mScrollThreshold;

    abstract void onScrollUp();

    abstract void onScrollDown();

    @Override
    public void onScrollChanged(int l, int t, int oldl, int oldt) {
        boolean isSignificantDelta = Math.abs(t - mLastScrollY) > mScrollThreshold;
        if (isSignificantDelta) {
            if (t > mLastScrollY) {
                onScrollUp();
            } else {
                onScrollDown();
            }
        }
        mLastScrollY = t;
        
        isSignificantDelta = Math.abs(l - mLastScrollX) > mScrollThreshold;
        if (isSignificantDelta) {
            if (l > mLastScrollX) {
                onScrollUp();
            } else {
                onScrollDown();
            }
        }
        mLastScrollX = l;
    }

    public void setScrollThreshold(int scrollThreshold) {
        mScrollThreshold = scrollThreshold;
    }
}