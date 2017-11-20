package com.example.paramount.ratappandroid;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.robolectric.RobolectricTestRunner;
import static org.junit.Assert.assertEquals;

/**
 * Created by joshuareno on 11/19/17.
 */

@RunWith(RobolectricTestRunner.class)
public class DashboardTest {
    /**
     * Checks's buttons in MapsActivity
     *
     */
//    public void testMapsActivityRadioGroups() {
//        SimpleDateFormat displayDateFormat = new SimpleDateFormat("yyyy-MM-dd",
//                Locale.US);
//        MapsActivity activity = Robolectric.setupActivity(MapsActivity.class);
//        DatePickerDialog startDatePickerDialog = new DatePickerDialog(activity);
//        DatePickerDialog endDatePickerDialog = new DatePickerDialog(activity);
//        Button findRatSightingsButton = (Button) activity.findViewById(R.id.findRatSightings);
//        String selectStartDateButtonTextTemplate =
//                "SELECT START DATE (selected date: %s)";
//        Date startDate = Calendar.getInstance().getTime();
//
//        assertEquals(startDatePickerDialog.getDatePicker(), -1);
//        assertEquals(endDatePickerDialog.getDatePicker(), -1);
//
//    }
    @Test
    public void testDashBoard() {
        //MapsActivity activity = Robolectric.setupActivity(MapsActivity.class);
        EndlessScrollListener scrollListener = new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                return;
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
