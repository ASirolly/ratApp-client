package com.example.paramount.ratappandroid;

import android.widget.RadioButton;
import android.widget.RadioGroup;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertEquals;

/**
 * Created by greg on 11/14/17.
 *
 * Unit tests for MapsActivity.
 */

@RunWith(RobolectricTestRunner.class)
public class GraphActivityTest {

    /**
     * Makes sure that checking a RadioButton in one of the RadioGroups causes any checked
     * RadioButton in the other RadioGroup to become unchecked.
     */
    @Test
    public void testGraphActivityRadioGroups() {
        GraphActivity activity = Robolectric.setupActivity(GraphActivity.class);
        RadioGroup radioGroup1 = (RadioGroup) activity.findViewById(R.id.radioGroup1);
        RadioGroup radioGroup2 = (RadioGroup) activity.findViewById(R.id.radioGroup2);

        // belongs to radioGroup1
        RadioButton twentyTenButton = (RadioButton) activity.findViewById(R.id.twenty_ten);

        // belongs to radioGroup2
        RadioButton twentySeventeenButton = (RadioButton) activity.findViewById(
                R.id.twenty_seventeen);

        // both RadioGroups are initially unchecked
        assertEquals(radioGroup1.getCheckedRadioButtonId(), -1);
        assertEquals(radioGroup2.getCheckedRadioButtonId(), -1);

        // check a button in RadioGroup1
        twentyTenButton.performClick();

        // The 2010 button should be checked, and RadioGroup2 should still be unchecked
        assertEquals(radioGroup1.getCheckedRadioButtonId(), R.id.twenty_ten);
        assertEquals(radioGroup2.getCheckedRadioButtonId(), -1);

        // check a button in RadioGroup2
        twentySeventeenButton.performClick();

        // RadioGroup1 should now be unchecked, with the 2017 button in RadioGroup2 checked
        assertEquals(radioGroup1.getCheckedRadioButtonId(), -1);
        assertEquals(radioGroup2.getCheckedRadioButtonId(), R.id.twenty_seventeen);
    }
}
