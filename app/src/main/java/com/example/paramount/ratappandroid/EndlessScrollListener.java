package com.example.paramount.ratappandroid;

import android.util.Log;
import android.widget.AbsListView;

/**
 * Created by Paramount on 2017/10/12.
 */

public abstract class EndlessScrollListener implements AbsListView.OnScrollListener {
    // The minimum number of items to have below your current scroll position
    // before loading more.
    private int visibleThreshold = 5;
    // The current offset index of data you have loaded
    private int currentPage = 0;
    // The total number of items in the dataset after the last load
    private int previousTotalItemCount = 0;
    // True if we are still waiting for the last set of data to load.
    private boolean loading = true;
    // Sets the starting page index
    private int startingPageIndex = 0;

    private static final String TAG = "ENDLESS SCROLL LISTENER";

    public EndlessScrollListener() {
    }

    /**
     * Sets the threshold
     * @param visibleThreshold
     */
    public EndlessScrollListener(int visibleThreshold) {
        this.visibleThreshold = visibleThreshold;
    }

    /**
     * Sets appropriate variables.
     * @param visibleThreshold
     * @param startPage
     */
    public EndlessScrollListener(int visibleThreshold, int startPage) {
        this.visibleThreshold = visibleThreshold;
        this.startingPageIndex = startPage;
        this.currentPage = startPage;
    }


    /**
     * Defines procedures for scrolling.
     * @param view
     * @param firstVisibleItem
     * @param visibleItemCount
     * @param totalItemCount
     */
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
    {
        // If the total item count is zero and the previous isn't, assume the
        // list is invalidated and should be reset back to initial state
        if (totalItemCount < previousTotalItemCount) {
            Log.w(TAG, String.format("invalid state; totalItemCount is %d and previousTotalItemCount is %d", totalItemCount, previousTotalItemCount));
            this.currentPage = this.startingPageIndex;
            this.previousTotalItemCount = totalItemCount;
            if (totalItemCount == 0) { this.loading = true; }
        }
        // If it's still loading, we check to see if the dataset count has
        // changed, if so we conclude it has finished loading and update the current page
        // number and total item count.
        if (loading && (totalItemCount > previousTotalItemCount)) {
            loading = false;
            previousTotalItemCount = totalItemCount;
            currentPage++;
        }

        // If it isn't currently loading, we check to see if we have breached
        // the visibleThreshold and need to reload more data.
        // If we do need to reload some more data, we execute onLoadMore to fetch the data.
        if (!loading && (firstVisibleItem + visibleItemCount + visibleThreshold) >= totalItemCount ) {
            loading = onLoadMore(currentPage + 1, totalItemCount);
        }
    }

    // Defines the process for actually loading more data based on page
    // Returns true if more data is being loaded; returns false if there is no more data to load.
    public abstract boolean onLoadMore(int page, int totalItemsCount);

    /**
     * abstract method for detailing if scroll state changed
     * @param view
     * @param scrollState
     */
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // Don't take any action on changed
    }
}
