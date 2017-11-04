package com.example.paramount.ratappandroid;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.paramount.ratappandroid.dao.RatSightingDAO;
import com.example.paramount.ratappandroid.model.Model;
import com.example.paramount.ratappandroid.model.RatSighting;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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
    private List<Entry> ratSightingsList = new ArrayList<Entry>();
    private HashMap<Date, Integer> frequency = new HashMap<>();


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graph_activity);

        lineChart = (LineChart) findViewById(R.id.chart);

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
                RatSightingDAO.getInstance().getRatSightingsByDate(startDate, endDate, this::handleThisData)
        );
    }

    private void handleThisData(JSONArray response) {
        Log.d("this1", "tag6");
        Model.getInstance().resetMapRatSightings();
        JSONObject json;
        int len = response.length();
        for (int i = 0; i < len; i++) {
            try {
                json = (JSONObject) response.get(i);
                RatSighting ratSighting = new RatSighting(json);
                Model.getInstance().getMapRatSightings().put(ratSighting.getUniqueKey(), ratSighting);
            } catch (JSONException | ParseException e) {
                Log.w(TAG, e);
            }
        }
        showAllRatSightings();
    }

    private void showAllRatSightings() {
        Log.d("this", "tag7");
        Model.getInstance().getMapRatSightings().values()
                .forEach(ratSighting -> {
                    Log.i(TAG, String.format("Placing marker at lat %f and long %f", ratSighting.getLatitude(), ratSighting.getLongitude()));
                    Date date = ratSighting.getCreateDate();
                    Integer num = frequency.get(date);
                    int count = num != null ? num.intValue() : 0;
                    frequency.put(date, count + 1);
                });
        Set<Date> dateSet = frequency.keySet();
        Date[] dateArray = dateSet.toArray(new Date[dateSet.size()]);
        for (Date key : dateArray) {
            ratSightingsList.add(new Entry(key.getMonth(), (float) frequency.get(key)));
        }
        Log.d("myThing", "tag1");
        LineDataSet set = new LineDataSet(ratSightingsList, "entries");
        Log.d("myThing", "tag2");
        LineData lineData = new LineData(set);
        Log.d("myThing", "tag3");
        lineChart.setData(lineData);
        Log.d("myThing", "tag4");
        lineChart.invalidate();
        Log.d("myThing", "tag5");
    }
}
