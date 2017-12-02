package com.example.paramount.ratappandroid;

import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.robolectric.RobolectricTestRunner;
import static org.junit.Assert.assertEquals;

/**
 * Tests the dashboard to ensure UI is correct
 */
@RunWith(RobolectricTestRunner.class)
public class DashboardTest {

    /**
     * Test that ensures the dashboard runs correctly
     */
    @Test
    public void testDashBoard() {
        //MapsActivity activity = Robolectric.setupActivity(MapsActivity.class);
        EndlessScrollListener scrollListener = new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                Log.d("msg", "" + page);
            }
        };
        assertEquals(scrollListener.getPreviousTotalItemCount(), 0);
        scrollListener.onScroll(null, 1, 1, 3);

        boolean bool = (scrollListener.getPreviousTotalItemCount()>0);
        assertEquals(bool, true);
        assertEquals(scrollListener.getPreviousTotalItemCount(), 3);

        // assert equals with the current page
        assertEquals(scrollListener.getCurrentPage(), 1);

        scrollListener.onScroll(null, 1, 1, -3);
        assertEquals(scrollListener.getPreviousTotalItemCount(), -3);
        assertEquals(scrollListener.getCurrentPage(), 0);
    }
}
