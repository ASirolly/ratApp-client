package com.example.paramount.ratappandroid;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Checkable;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.paramount.ratappandroid.dao.GraphDateDAO;
import com.example.paramount.ratappandroid.model.GraphDate;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;


/**
 * Created by Joshua Reno on 11/4/17.
 *
 * Displays the ratSightings graph.
 */

public class GraphActivity extends AppCompatActivity {

    private static final String TAG = "GRAPH_ACTIVITY";

    private final DataPoint[] ten = new DataPoint[12];
    private final DataPoint[] eleven = new DataPoint[12];
    private final DataPoint[] twelve = new DataPoint[12];
    private final DataPoint[] thirteen = new DataPoint[12];
    private final DataPoint[] fourteen = new DataPoint[12];
    private final DataPoint[] fifteen = new DataPoint[12];
    private final DataPoint[] sixteen = new DataPoint[12];
    private final DataPoint[] seventeen = new DataPoint[12];
    private final LineGraphSeries<DataPoint> series2010 = new LineGraphSeries<>();
    private final LineGraphSeries<DataPoint> series2011 = new LineGraphSeries<>();
    private final LineGraphSeries<DataPoint> series2012 = new LineGraphSeries<>();
    private final LineGraphSeries<DataPoint> series2013 = new LineGraphSeries<>();
    private final LineGraphSeries<DataPoint> series2014 = new LineGraphSeries<>();
    private final LineGraphSeries<DataPoint> series2015 = new LineGraphSeries<>();
    private final LineGraphSeries<DataPoint> series2016 = new LineGraphSeries<>();
    private final LineGraphSeries<DataPoint> series2017 = new LineGraphSeries<>();
    private GraphView graphChart;
    private RadioGroup rg1;
    private RadioGroup rg2;
    private List<GraphDate> graphDates;
    private Map<Integer, DataPoint[]> yearToDataPointArray;

    private static final int NUMBER_OF_MONTHS_IN_A_YEAR = 12; // big if true
    private static final int NUMBER_2010 = 2010;
    private static final int NUMBER_2011 = 2011;
    private static final int NUMBER_2012 = 2012;
    private static final int NUMBER_2013 = 2013;
    private static final int NUMBER_2014 = 2014;
    private static final int NUMBER_2015 = 2015;
    private static final int NUMBER_2016 = 2016;
    private static final int NUMBER_2017 = 2017;



    /**
     * Set RadioGroup and set Calendar information
     * @param savedInstanceState the state
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graph_activity);
        graphChart = findViewById(R.id.chart);

        graphDates = new ArrayList<>();
        yearToDataPointArray = new HashMap<>();
        yearToDataPointArray.put(NUMBER_2010, ten);
        yearToDataPointArray.put(NUMBER_2011, eleven);
        yearToDataPointArray.put(NUMBER_2012, twelve);
        yearToDataPointArray.put(NUMBER_2013, thirteen);
        yearToDataPointArray.put(NUMBER_2014, fourteen);
        yearToDataPointArray.put(NUMBER_2015, fifteen);
        yearToDataPointArray.put(NUMBER_2016, sixteen);
        yearToDataPointArray.put(NUMBER_2017, seventeen);
        setRadioGroups();
        getData();
    }

    /**
     * Sets up RadioGroup onCheckedChangeListeners.
     */
    private void setRadioGroups() {
        rg1 = findViewById(R.id.radioGroup1);
        rg2 = findViewById(R.id.radioGroup2);
        rg1.clearCheck(); // this is so we can start fresh, with no selection on both RadioGroups
        rg2.clearCheck();
        rg1.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId != -1) {
                resetRadioGroup2();
            }
        });

        rg2.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId != -1) {
                resetRadioGroup1();
            }
        });
    }

    /**
     * Uses GraphDateDAO to retrieve graph data.
     */
    private void getData() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Date endOfTime = calendar.getTime();

        calendar.add(Calendar.DAY_OF_MONTH, -1);
        calendar.add(Calendar.YEAR, -10);
        Date beginningOfTime = calendar.getTime();

        GraphDateDAO graphDateDAO = GraphDateDAO.getInstance(getApplicationContext());
        graphDateDAO.getDates(beginningOfTime, endOfTime, this::handleThisData);
    }

    /**
     * Stores graph data returned in JSON response.
     * @param response the JSON response
     */
    private void handleThisData(JSONObject response) {
        Log.d(TAG, "handling data");
        graphDates.clear();
        JSONObject json;
        Log.w(TAG, "successful!");
        try {
            Log.d(TAG, "DATA GETTING BACK: " + response);
            JSONArray innerArr = response.getJSONArray("data");
            int len = innerArr.length();
            Log.d(TAG, "THIS IS ARRAY: " + innerArr);
            Log.d(TAG, "THIS IS LEN: " + len);
            for (int i = 0; i < len; i++) {
                json = (JSONObject) innerArr.get(i);
                Log.d(TAG,"THIS IS json: " + json);

                graphDates.add(new GraphDate(json));
            }

        } catch (JSONException e) {
            Log.w(TAG, "EXCEPTION HAPPENING HERE");
            Log.w(TAG, e);
        }
        showAllFrequencies();
    }

    /**
     * Sets the data with the appropriate year.
     */
    private void showAllFrequencies() {
        for (GraphDate gD: graphDates) {
            if (!StringUtils.isEmpty(gD.getYear()) &&
                !StringUtils.isEmpty(gD.getMonth()) &&
                !StringUtils.isEmpty(gD.getFrequency())) {

                int year = Integer.parseInt(gD.getYear());
                int month = Integer.parseInt(gD.getMonth());
                int frequency = Integer.parseInt(gD.getFrequency());
                Log.d(TAG, String.format(
                        "found date with year %d, month %d, and frequency %d",
                        year, month, frequency));
                DataPoint[] dataPoints = yearToDataPointArray.get(year);
                dataPoints[month - 1]  = new DataPoint(month, frequency);
            }
        }

        populate();
        setSeriesStyling();

        graphChart.removeAllSeries();
        graphChart.addSeries(series2017);
        Checkable button2017 = (RadioButton) findViewById(R.id.twenty_seventeen);
        button2017.setChecked(true);

        setChartStyling();
    }

    /**
     * Sets chart styling.
     */
    private void setChartStyling() {
        Viewport viewport = graphChart.getViewport();
        viewport.setMinX(1);
        viewport.setMaxX(NUMBER_OF_MONTHS_IN_A_YEAR);
        viewport.setXAxisBoundsManual(true);

        GridLabelRenderer gridLabelRenderer = graphChart.getGridLabelRenderer();
        gridLabelRenderer.setGridStyle(GridLabelRenderer.GridStyle.BOTH);
        gridLabelRenderer.setNumHorizontalLabels(NUMBER_OF_MONTHS_IN_A_YEAR);
        gridLabelRenderer.setHorizontalAxisTitle("month");
    }

    /**
     * Set styling for all series.
     */
    private void setSeriesStyling() {
        LineGraphSeries[] allSeries = new LineGraphSeries[] {
                series2010,
                series2011,
                series2012,
                series2013,
                series2014,
                series2015,
                series2016,
                series2017,
        };

        Stream<LineGraphSeries> stream = Arrays.stream(allSeries);
        stream.forEach(series -> {
            series.setTitle("This");
            series.setColor(Color.GREEN);
            series.setDrawDataPoints(true);
            series.setDataPointsRadius(10);
            series.setThickness(8);
        });
    }

    /**
     * Fill in missing data points, and reset data in all series.
     */
    private void populate() {
        for (int i = 0; i < NUMBER_OF_MONTHS_IN_A_YEAR; i++) {
            if (ten[i] == null ) {
                ten[i] = new DataPoint(i+1, 0);
            }
            if (eleven[i] == null) {
                eleven[i] = new DataPoint(i+1, 0);
            }
            if (twelve[i] == null) {
                twelve[i] = new DataPoint(i+1, 0);
            }
            if (thirteen[i] == null) {
                thirteen[i] = new DataPoint(i+1, 0);
            }
            if (fourteen[i] == null) {
                fourteen[i] = new DataPoint(i+1, 0);
            }
            if (fifteen[i] == null) {
                fifteen[i] = new DataPoint(i+1, 0);
            }
            if (sixteen[i] == null) {
                sixteen[i] = new DataPoint(i+1, 0);
            }
            if (seventeen[i] == null) {
                seventeen[i] = new DataPoint(i+1, 0);
            }
        }

        series2010.resetData(ten);
        series2011.resetData(eleven);
        series2012.resetData(twelve);
        series2013.resetData(thirteen);
        series2014.resetData(fourteen);
        series2015.resetData(fifteen);
        series2016.resetData(sixteen);
        series2017.resetData(seventeen);
    }

    /**
     * Method called when a year radio button is clicked.
     * @param view View on which method was called
     */
    public void onSwitchYear(View view) {
        boolean check = ((RadioButton) view).isChecked();
        RadioButton button = (RadioButton) view;
        Log.d(TAG, String.format("button with year %s was clicked", button.getText()));

        if (check) {
            graphChart.removeAllSeries();
            switch(Integer.parseInt((String) button.getText())) {
                case NUMBER_2010:
                    graphChart.addSeries(series2010);
                    break;
                case NUMBER_2011:
                    graphChart.addSeries(series2011);
                    break;
                case NUMBER_2012:
                    graphChart.addSeries(series2012);
                    break;
                case NUMBER_2013:
                    graphChart.addSeries(series2013);
                    break;
                case NUMBER_2014:
                    graphChart.addSeries(series2014);
                    break;
                case NUMBER_2015:
                    graphChart.addSeries(series2015);
                    break;
                case NUMBER_2016:
                    graphChart.addSeries(series2016);
                    break;
                case NUMBER_2017:
                    graphChart.addSeries(series2017);
                    break;
            }
        }
    }

    /**
     * Resets the first RadioGroup.
     */
    private void resetRadioGroup1() {
        rg1.setOnCheckedChangeListener(null);
        rg1.clearCheck();
        rg1.setOnCheckedChangeListener((group, checkedId) -> resetRadioGroup2());
    }

    /**
     * Resets the second RadioGroup.
     */
    private void resetRadioGroup2() {
        rg2.setOnCheckedChangeListener(null);
        rg2.clearCheck();
        rg2.setOnCheckedChangeListener((group, checkedId) -> resetRadioGroup1());
    }
}
