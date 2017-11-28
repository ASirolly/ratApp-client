package com.example.paramount.ratappandroid;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.Checkable;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.paramount.ratappandroid.dao.GraphDateDAO;
import com.example.paramount.ratappandroid.model.GraphDate;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;
import com.jjoe64.graphview.series.Series;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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

    private GraphView graphChart;
    private RadioGroup yearsRadioGroup1;
    private RadioGroup yearsRadioGroup2;
    private RadioGroup graphTypeRadioGroup;

    private List<GraphDate> graphDates;
    private SparseArray<DataPoint[]> yearToDataPointArray;

    // maps year to a map from Series type to the Series of that type for that year
    private Map<Integer, Map<String, Series>> yearToEachSeriesMap;

    // keeps track of the current series type in use (this should probably be an enum)
    private String currentSeriesType = LINE_GRAPH_SERIES_KEY;

    private static final int NUMBER_OF_MONTHS_IN_A_YEAR = 12; // big if true
    private static final int NUMBER_2010 = 2010;
    private static final int NUMBER_2011 = 2011;
    private static final int NUMBER_2012 = 2012;
    private static final int NUMBER_2013 = 2013;
    private static final int NUMBER_2014 = 2014;
    private static final int NUMBER_2015 = 2015;
    private static final int NUMBER_2016 = 2016;
    private static final int NUMBER_2017 = 2017;
    private static final int[] ALL_NUMBERS = new int[]{
            NUMBER_2010,
            NUMBER_2011,
            NUMBER_2012,
            NUMBER_2013,
            NUMBER_2014,
            NUMBER_2015,
            NUMBER_2016,
            NUMBER_2017
    }; // any number not in this array is not actually a number

    private static final String LINE_GRAPH_SERIES_KEY = "lineGraphSeries";
    private static final String POINTS_GRAPH_SERIES_KEY = "pointsGraphSeries";
    private static final String BAR_GRAPH_SERIES_KEY = "barGraphSeries";


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
        yearToDataPointArray = new SparseArray<>();
        yearToDataPointArray.put(NUMBER_2010, ten);
        yearToDataPointArray.put(NUMBER_2011, eleven);
        yearToDataPointArray.put(NUMBER_2012, twelve);
        yearToDataPointArray.put(NUMBER_2013, thirteen);
        yearToDataPointArray.put(NUMBER_2014, fourteen);
        yearToDataPointArray.put(NUMBER_2015, fifteen);
        yearToDataPointArray.put(NUMBER_2016, sixteen);
        yearToDataPointArray.put(NUMBER_2017, seventeen);

        yearToEachSeriesMap = new HashMap<>();
        for (int year : ALL_NUMBERS) {
            Map<String, Series> stringToSeriesMap = new HashMap<>();
            stringToSeriesMap.put(LINE_GRAPH_SERIES_KEY, new LineGraphSeries<DataPoint>());
            stringToSeriesMap.put(POINTS_GRAPH_SERIES_KEY, new PointsGraphSeries());
            stringToSeriesMap.put(BAR_GRAPH_SERIES_KEY, new BarGraphSeries());

            yearToEachSeriesMap.put(year, stringToSeriesMap);
        }


        setRadioGroups();
        getData();
    }

    /**
     * Sets up RadioGroup onCheckedChangeListeners.
     */
    private void setRadioGroups() {
        yearsRadioGroup1 = findViewById(R.id.radioGroup1);
        yearsRadioGroup2 = findViewById(R.id.radioGroup2);
        yearsRadioGroup1.clearCheck(); // this is so we can start fresh, with no selection on both RadioGroups
        yearsRadioGroup2.clearCheck();

        findViewById(R.id.twenty_seventeen).performClick();

        yearsRadioGroup1.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId != -1) {
                resetRadioGroup2();
                updateDisplay();
            }
        });

        yearsRadioGroup2.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId != -1) {
                resetRadioGroup1();
                updateDisplay();
            }
        });

        graphTypeRadioGroup = findViewById(R.id.graphTypeRadioGroup);
        graphTypeRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            updateDisplay();
        });

        RadioButton lineGraphRadioButton = findViewById(R.id.lineGraphButton);
        RadioButton pointsGraphRadioButton = findViewById(R.id.pointGraphButton);
        RadioButton barGraphRadioButton = findViewById(R.id.barGraphButton);

        lineGraphRadioButton.setChecked(true);

        lineGraphRadioButton.setOnCheckedChangeListener((view, isChecked) -> {
            if (isChecked) {
                currentSeriesType = LINE_GRAPH_SERIES_KEY;
            }
            updateDisplay();
        });

        pointsGraphRadioButton.setOnCheckedChangeListener((view, isChecked) -> {
            if (isChecked) {
                currentSeriesType = POINTS_GRAPH_SERIES_KEY;
            }
            updateDisplay();
        });

        barGraphRadioButton.setOnCheckedChangeListener((view, isChecked) -> {
            if (isChecked) {
                currentSeriesType = BAR_GRAPH_SERIES_KEY;
            }
            updateDisplay();
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
        graphChart.addSeries(yearToEachSeriesMap.get(NUMBER_2017).get(currentSeriesType));

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
        yearToEachSeriesMap.values().forEach((stringToSeriesMap) -> {
            LineGraphSeries lineGraphSeries = (LineGraphSeries) stringToSeriesMap.get(LINE_GRAPH_SERIES_KEY);
            lineGraphSeries.setTitle("This");
            lineGraphSeries.setColor(Color.GREEN);
            lineGraphSeries.setDrawDataPoints(true);
            lineGraphSeries.setDataPointsRadius(10);
            lineGraphSeries.setThickness(8);

            PointsGraphSeries pointsGraphSeries = (PointsGraphSeries) stringToSeriesMap.get(POINTS_GRAPH_SERIES_KEY);
            pointsGraphSeries.setTitle("This");
            pointsGraphSeries.setColor(Color.GREEN);
            pointsGraphSeries.setSize(10);

            BarGraphSeries barGraphSeries = (BarGraphSeries) stringToSeriesMap.get(BAR_GRAPH_SERIES_KEY);
            barGraphSeries.setTitle("This");
            barGraphSeries.setColor(Color.GREEN);
            barGraphSeries.setDrawValuesOnTop(true);
            barGraphSeries.setSpacing(2);
            barGraphSeries.setValuesOnTopColor(Color.BLACK);
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

        yearToEachSeriesMap.forEach((year, seriesTypeToSeriesMap) -> {
            LineGraphSeries lineGraphSeries = (LineGraphSeries) seriesTypeToSeriesMap.get(LINE_GRAPH_SERIES_KEY);
            PointsGraphSeries pointsGraphSeries = (PointsGraphSeries) seriesTypeToSeriesMap.get(POINTS_GRAPH_SERIES_KEY);
            BarGraphSeries barGraphSeries = (BarGraphSeries) seriesTypeToSeriesMap.get(BAR_GRAPH_SERIES_KEY);

            lineGraphSeries.resetData(yearToDataPointArray.get(year));
            pointsGraphSeries.resetData(yearToDataPointArray.get(year));
            barGraphSeries.resetData(yearToDataPointArray.get(year));

        });
    }

    /**
     * Displays graph with correct style (line/points/bar graph) with data from correct year.
     */
    public void updateDisplay() {
        System.out.println(yearsRadioGroup1.getCheckedRadioButtonId());
        System.out.println(yearsRadioGroup2.getCheckedRadioButtonId());
        int yearId = yearsRadioGroup1.getCheckedRadioButtonId();
        if (yearId == -1) {
            yearId = yearsRadioGroup2.getCheckedRadioButtonId();
        }
        String yearString = (String) ((Button) findViewById(yearId)).getText();
        int year = Integer.parseInt(yearString);

        graphChart.removeAllSeries();
        graphChart.addSeries(yearToEachSeriesMap.get(year).get(currentSeriesType));
    }

    /**
     * Resets the first RadioGroup.
     */
    private void resetRadioGroup1() {
        yearsRadioGroup1.setOnCheckedChangeListener(null);
        yearsRadioGroup1.clearCheck();
        yearsRadioGroup1.setOnCheckedChangeListener((group, checkedId) -> {
            resetRadioGroup2();
            updateDisplay();
        });
    }

    /**
     * Resets the second RadioGroup.
     */
    private void resetRadioGroup2() {
        yearsRadioGroup2.setOnCheckedChangeListener(null);
        yearsRadioGroup2.clearCheck();
        yearsRadioGroup2.setOnCheckedChangeListener((group, checkedId) -> {
            resetRadioGroup1();
            updateDisplay();
        });
    }
}
