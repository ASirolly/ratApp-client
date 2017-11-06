package com.example.paramount.ratappandroid;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SyncStatusObserver;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.paramount.ratappandroid.dao.GraphDateDAO;
import com.example.paramount.ratappandroid.dao.RatSightingDAO;
import com.example.paramount.ratappandroid.model.GraphDate;
import com.example.paramount.ratappandroid.model.Model;
import com.example.paramount.ratappandroid.model.RatSighting;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.example.paramount.ratappandroid.DayAxisValueFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Created by joshuareno on 11/4/17.
 *
 * Displays the ratSightings graph.
 */

public class GraphActivity extends AppCompatActivity {

    private static final String TAG = "GRAPH_ACTIVITY";
    private static final SimpleDateFormat displayDateFormat = new SimpleDateFormat("yyyy-MM-dd",
            Locale.US);
    private static final String selectStartDateButtonTextTemplate =
            "SELECT START DATE (selected date: %s)";
    private static final String selectEndDateButtonTextTemplate =
            "SELECT END DATE (selected date: %s)";
    private Button selectStartDateButton;
    private Button selectEndDateButton;
    private DatePickerDialog startDatePickerDialog;
    private DatePickerDialog endDatePickerDialog;
    private Date startDate;
    private Date endDate;

    private LineChart lineChart;
    private RelativeLayout rl; // need to change
    private List<Entry> entriesList = new ArrayList<Entry>();
    private HashMap<Date, Integer> frequency = new HashMap<>();
    private BarChart barChart;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graph_activity);

//        lineChart = (LineChart) findViewById(R.id.chart);
//        barChart = (BarChart) findViewById(R.id.chart);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        endDate = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        calendar.add(Calendar.YEAR, -1);
        startDate = calendar.getTime();

        startDatePickerDialog = new DatePickerDialog(GraphActivity.this);
        endDatePickerDialog = new DatePickerDialog(GraphActivity.this);
        selectStartDateButton = (Button) findViewById(R.id.selectStartDateButtonGraph);
        selectEndDateButton = (Button) findViewById(R.id.selectEndDateButtonGraph);
        Button findRatSightingsButton = (Button) findViewById(R.id.findRatSightingsGraph);

        selectStartDateButton.setOnClickListener(click -> startDatePickerDialog.show());
        selectEndDateButton.setOnClickListener(click -> endDatePickerDialog.show());

        selectStartDateButton.setText(String.format(selectStartDateButtonTextTemplate,
                displayDateFormat.format(startDate)));
        selectEndDateButton.setText(String.format(selectEndDateButtonTextTemplate, displayDateFormat.format(endDate)));

        startDatePickerDialog.setOnDateSetListener((view, year, month, day) -> {
            calendar.set(year, month, day);
            startDate = calendar.getTime();
            selectStartDateButton.setText(String.format(selectStartDateButtonTextTemplate,
                    displayDateFormat.format(startDate)));
        });

        endDatePickerDialog.setOnDateSetListener((view, year, month, day) -> {
            calendar.set(year, month, day);
            endDate = calendar.getTime();
            selectEndDateButton.setText(String.format(selectEndDateButtonTextTemplate,
                    displayDateFormat.format(endDate)));
        });

        findRatSightingsButton.setOnClickListener(view ->
                GraphDateDAO.getInstance().getDates(startDate, endDate, this::handleThisData)
        );

        GraphView graph = (GraphView) findViewById(R.id.chart);
    }

    private void handleThisData(JSONObject response) {
        Log.d("this1", "tag6");
        Model.getInstance().resetGraphDates();
        JSONObject json;
        Log.w(TAG, "Wir sind successful!");
        try {
            System.out.println("DATA GETTING BACK: " + response);
            JSONArray innerArr = response.getJSONArray("data");
            int len = innerArr.length();
            System.out.println("THIS IS INNERARR: " + innerArr);
            System.out.println("THIS IS LEN: " + len);
            for (int i = 0; i < len; i++) {
                json = (JSONObject) innerArr.get(i);
                System.out.println("THIS IS json: " + json);

                //json = (JSONObject) response.get(i);
                Model.getInstance().getGraphDates().add(new GraphDate(json));
            }

        } catch (JSONException e) {
            Log.w(TAG, "HAPPENING HERE");
            Log.w(TAG, e);
        }
        showAllFrequencies();
//        testing();
    }

    private void testing() {
        ArrayList<GraphDate> temp = Model.getInstance().getGraphDates();
        System.out.println(temp.get(0));
    }

//    private void showAllFrequencies() {
//        Log.d("this", "tag7");
//        ArrayList<GraphDate> temp = Model.getInstance().getGraphDates();
//        List<BarEntry> barData = new LinkedList<>();
//        List<String> xValuesDates = new LinkedList<>();
//        System.out.println("THIS IS SIZE OF temp: " + temp.size());
//        for (GraphDate gD : temp) {
//            String dat = gD.getMonth() + "-" + gD.getYear();
//            int month = Integer.parseInt(gD.getMonth());
//            int frequency = Integer.parseInt(gD.getFrequency());
//            xValuesDates.add(dat);
//            barData.add(new BarEntry(month, frequency));
//        }
//        System.out.println("PRINGINT OUT STUFFFFF");
//        for (BarEntry bE: barData) {
//            System.out.println(bE);
//        }
//
//        System.out.println("All fine");
//
//        XAxis xAxis = barChart.getXAxis();
//        xAxis.setValueFormatter(new DayAxisValueFormatter(barChart));
//        BarDataSet set = new BarDataSet(barData, "entries");
//        BarData barDat = new BarData(set);
//        barChart.setData(barDat);
//        barChart.invalidate();
//    }
    private void showAllFrequencies() {
        ArrayList<GraphDate> temp = Model.getInstance().getGraphDates();
        LineGraphSeries<DataPoint> series;
        DataPoint[] dataPoints = new DataPoint[temp.size()];
        int count = 0;
        for (GraphDate gD: temp) {
            if (gD != null && gD.getYear() != null && gD.getMonth() != null
                    && gD.getYear() != "" && gD.getMonth() != "") {
                Log.d("asdf", gD.getYear());
                Log.d("asdf", gD.getMonth());
                int year = Integer.parseInt(gD.getYear());
                int month = Integer.parseInt(gD.getMonth());
                Date date = new Date();
                date.setYear(year);
                date.setMonth(month);
                int frequency = Integer.parseInt(gD.getFrequency());
                dataPoints[count] = new DataPoint(date.getMonth(), frequency);
                count++;
            }
        }
        series = new LineGraphSeries<DataPoint>(dataPoints);
        GraphView graph = (GraphView) findViewById(R.id.chart);

//        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
//                new DataPoint(0, 1),
//                new DataPoint(1, 5),
//                new DataPoint(2, 3),
//                new DataPoint(3, 2),
//                new DataPoint(4, 6)
//        });
        series.setTitle("This");
        series.setColor(Color.GREEN);
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(10);
        series.setThickness(8);

        graph.addSeries(series);
    }
}
