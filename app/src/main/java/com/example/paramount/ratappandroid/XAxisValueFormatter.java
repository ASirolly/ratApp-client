package com.example.paramount.ratappandroid;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Paramount on 2017/11/5.
 */

public class XAxisValueFormatter implements IAxisValueFormatter {
    private List<String> mValues;

    public XAxisValueFormatter(List<String> values) {
        this.mValues = values;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        // "value" represents the position of the label on the axis (x or y)
        return mValues.get((int) value);
    }

}
