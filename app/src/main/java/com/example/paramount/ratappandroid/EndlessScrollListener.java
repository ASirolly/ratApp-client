package com.example.paramount.ratappandroid;

import android.util.Log;
import android.widget.AbsListView;

/**
 * Created by Paramount on 2017/10/12.
 *
 * Provides a way for endless scrolling in a ListView
 */

abstract class EndlessScrollListener implements AbsListView.OnScrollListener {
    // The current offset index of data you have loaded
    private int currentPage = 0;
    // The total number of items in the data set after the last load
    private int previousTotalItemCount = 0;
    // True if we are still waiting for the last set of data to load.
    private boolean loading = true;

    private static final String TAG = "ENDLESS SCROLL LISTENER";

    EndlessScrollListener() {
    }

        /**
     * Defines procedures for scrolling.
     * @param view current view
     * @param firstVisibleItem index of the first visible item
     * @param visibleItemCount number of visible items
     * @param totalItemCount number of total items
     */
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                         int totalItemCount)
    {
        int visibleThreshold = 5;
        // If the total item count is zero and the previous isn't, assume the
        // list is invalidated and should be reset back to initial state
        if (totalItemCount < previousTotalItemCount) {
            Log.w(TAG, String.format(
                    "invalid state; totalItemCount is %d and previousTotalItemCount is %d",
                    totalItemCount, previousTotalItemCount));
            this.currentPage = 0;
            this.previousTotalItemCount = totalItemCount;
            if (totalItemCount == 0) { this.loading = true; }
        }
        // If it's still loading, we check to see if the data set count has
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
        if (!loading &&
                ((firstVisibleItem + visibleItemCount + visibleThreshold) >= totalItemCount)) {
            onLoadMore(currentPage + 1, totalItemCount);
            loading = true;
        }
    }

    /**
     * Defines the process for actually loading more data based on page.
     * @param page page to loud
     * @param totalItemsCount number of total items
     * @return true if more data is being loaded; false if there is no more data to load.
     */
    public abstract void onLoadMore(int page, int totalItemsCount);

    /**
     * abstract method for detailing if scroll state changed
     * @param view  current view
     * @param scrollState current scrollState
     */
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // Don't take any action on changed
    }
}
