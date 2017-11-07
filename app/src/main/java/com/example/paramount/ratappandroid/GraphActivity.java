package com.example.paramount.ratappandroid;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SyncStatusObserver;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
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
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.BarGraphSeries;
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

//    private LineChart lineChart;
//    private RelativeLayout rl; // need to change
//    private List<Entry> entriesList = new ArrayList<Entry>();
//    private HashMap<Date, Integer> frequency = new HashMap<>();
//    private BarChart barChart;

    private DataPoint[] ten = new DataPoint[12];
    private DataPoint[] eleven = new DataPoint[12];
    private DataPoint[] twelve = new DataPoint[12];
    private DataPoint[] thirteen = new DataPoint[12];
    private DataPoint[] fourteen = new DataPoint[12];
    private DataPoint[] fifteen = new DataPoint[12];
    private DataPoint[] sixteen = new DataPoint[12];
    private DataPoint[] seventeen = new DataPoint[12];
    private LineGraphSeries<DataPoint> series2010;
    private LineGraphSeries<DataPoint> series2011;
    private LineGraphSeries<DataPoint> series2012;
    private LineGraphSeries<DataPoint> series2013;
    private LineGraphSeries<DataPoint> series2014;
    private LineGraphSeries<DataPoint> series2015;
    private LineGraphSeries<DataPoint> series2016;
    private LineGraphSeries<DataPoint> series2017;
    private GraphView graphChart;

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
                Log.d("thing", "a" + year + "c");
                switch(year) {
                    case 2010:
                        Log.d("ten", "");
                        Log.d("ten", "" + month);
                        try {
                            Log.d("ten", "");
                            ten[month] = new DataPoint(date.getMonth(), frequency);
                        } catch (ArrayIndexOutOfBoundsException e) {
                            // do alert
                        }
                        break;
                    case 2011:
                        try {
                            Log.d("eleven", "");
                            eleven[month] = new DataPoint(date.getMonth(), frequency);
                        } catch (ArrayIndexOutOfBoundsException e) {
                            // do alert
                        }
                        break;
                    case 2012:
                        try {
                            Log.d("twelve", "");
                            twelve[month] = new DataPoint(date.getMonth(), frequency);
                        } catch (ArrayIndexOutOfBoundsException e) {
                            // do alert
                        }
                        break;
                    case 2013:
                        try {
                            Log.d("thirteen", "");
                            thirteen[month] = new DataPoint(date.getMonth(), frequency);
                        } catch (ArrayIndexOutOfBoundsException e) {
                            // do alert
                        }
                        break;
                    case 2014:
                        try {
                            Log.d("fourteen", "");
                            fourteen[month] = new DataPoint(date.getMonth(), frequency);
                        } catch (ArrayIndexOutOfBoundsException e) {
                            // do alert
                        }
                        break;
                    case 2015:
                        try {
                            Log.d("fifteen", "");
                            fifteen[month] = new DataPoint(date.getMonth(), frequency);
                        } catch (ArrayIndexOutOfBoundsException e) {
                            // do alert
                        }
                        break;
                    case 2016:
                        Log.d("six", "");
                        Log.d("six", "" + month);
                        try {
                            Log.d("sixteen", "");
                            sixteen[month] = new DataPoint(date.getMonth(), frequency);
                        } catch (ArrayIndexOutOfBoundsException e) {
                            // do alert
                        }
                        break;
                    case 2017:
                        Log.d("seven", "abc");
                        Log.d("seven", "" + month);
                        try {
                            Log.d("seventeen", "hi");
                            seventeen[month] = new DataPoint(date.getMonth(), frequency);
                        } catch (ArrayIndexOutOfBoundsException e) {
                            Log.d("seventeen", "asdfsadfasdfasdf");
                        }
                        break;
                }
                count++;
            }
        }
        populate();
        series2010 = new LineGraphSeries<>(ten);
        series2011 = new LineGraphSeries<>(eleven);
        series2012 = new LineGraphSeries<>(twelve);
        series2013 = new LineGraphSeries<>(thirteen);
        series2014 = new LineGraphSeries<>(fourteen);
        series2015 = new LineGraphSeries<>(fifteen);
        series2016 = new LineGraphSeries<>(sixteen);
        series2017 = new LineGraphSeries<>(seventeen);

        //series = new LineGraphSeries<>(dataPoints);

        populateColor();
        graphChart = (GraphView) findViewById(R.id.chart);

        //GraphView graph = (GraphView) findViewById(R.id.chart);
        //graph.removeAllSeries();
        //graph.addSeries(series);
        graphChart.removeAllSeries();
        graphChart.addSeries(series2010);
    }

    public void populateColor() {
        series2010.setTitle("This");
        series2010.setColor(Color.GREEN);
        series2010.setDrawDataPoints(true);
        series2010.setDataPointsRadius(10);
        series2010.setThickness(8);

        series2011.setTitle("This");
        series2011.setColor(Color.GREEN);
        series2011.setDrawDataPoints(true);
        series2011.setDataPointsRadius(10);
        series2011.setThickness(8);

        series2012.setTitle("This");
        series2012.setColor(Color.GREEN);
        series2012.setDrawDataPoints(true);
        series2012.setDataPointsRadius(10);
        series2012.setThickness(8);

        series2013.setTitle("This");
        series2013.setColor(Color.GREEN);
        series2013.setDrawDataPoints(true);
        series2013.setDataPointsRadius(10);
        series2013.setThickness(8);

        series2014.setTitle("This");
        series2014.setColor(Color.GREEN);
        series2014.setDrawDataPoints(true);
        series2014.setDataPointsRadius(10);
        series2014.setThickness(8);

        series2015.setTitle("This");
        series2015.setColor(Color.GREEN);
        series2015.setDrawDataPoints(true);
        series2015.setDataPointsRadius(10);
        series2015.setThickness(8);

        series2016.setTitle("This");
        series2016.setColor(Color.GREEN);
        series2016.setDrawDataPoints(true);
        series2016.setDataPointsRadius(10);
        series2016.setThickness(8);

        series2017.setTitle("This");
        series2017.setColor(Color.GREEN);
        series2017.setDrawDataPoints(true);
        series2017.setDataPointsRadius(10);
        series2017.setThickness(8);
    }

    public void populate() {
        for (int i = 0; i < ten.length; i++) {
            if (ten[i] == null ) {
                ten[i] = new DataPoint(i, 0);
            } else if (ten[i].getY() != 0) {
                ten[i] = new DataPoint(i, 0);
            }
        }
        for (int i = 0; i < eleven.length; i++) {
            if (eleven[i] == null) {
                eleven[i] = new DataPoint(i, 0);
            } else if (eleven[i].getY() == 0) {
                eleven[i] = new DataPoint(i, 0);
            }
        }
        for (int i = 0; i < twelve.length; i++) {
            if (twelve[i] == null) {
                twelve[i] = new DataPoint(i, 0);
            } else if (twelve[i].getY() == 0) {
                twelve[i] = new DataPoint(i, 0);
            }
        }
        for (int i = 0; i < thirteen.length; i++) {
            if (thirteen[i] == null) {
                thirteen[i] = new DataPoint(i, 0);
            } else if (thirteen[i].getY() == 0) {
                thirteen[i] = new DataPoint(i, 0);
            }
        }
        for (int i = 0; i < fourteen.length; i++) {
            if (fourteen[i] == null) {
                fourteen[i] = new DataPoint(i, 0);
            } else if (fourteen[i].getY() == 0) {
                fourteen[i] = new DataPoint(i, 0);
            }
        }
        for (int i = 0; i < fifteen.length; i++) {
            if (fifteen[i] == null) {
                fifteen[i] = new DataPoint(i, 0);
            } else if (fifteen[i].getY() == 0) {
                fifteen[i] = new DataPoint(i, 0);
            }
        }
        for (int i = 0; i < sixteen.length; i++) {
            if (sixteen[i] == null) {
                sixteen[i] = new DataPoint(i, 0);
            } else if (sixteen[i].getY() == 0) {
                sixteen[i] = new DataPoint(i, 0);
            }
        }
        for (int i = 0; i < seventeen.length; i++) {
            if (seventeen[i] == null) {
                seventeen[i] = new DataPoint(i, 0);
            } else if (seventeen[i].getY() == 0) {
                seventeen[i] = new DataPoint(i, 0);
            }
        }
    }

    public void onSwitchYear(View view) {
        boolean check = ((RadioButton) view).isChecked();
        Log.d("" + view.getId(), "thing");
        RadioButton button = (RadioButton) view;

        switch(Integer.parseInt((String) button.getText())) {
            case 2010:
                if(check) {
                    graphChart.removeAllSeries();
                    if (series2010 != null) {
                        graphChart.addSeries(series2010);
                    }
                }
                break;
            case 2011:
                if (check) {
                    graphChart.removeAllSeries();
                    if (series2011 != null) {
                        graphChart.addSeries(series2011);
                    }
                }
                break;
            case 2012:
                if (check) {
                    graphChart.removeAllSeries();
                    if (series2012 != null) {
                        graphChart.addSeries(series2012);
                    }
                }
                break;
            case 2013:
                if (check) {
                    graphChart.removeAllSeries();
                    if (series2013 != null) {
                        graphChart.addSeries(series2013);
                    }
                }
                break;
            case 2014:
                if (check) {
                    graphChart.removeAllSeries();
                    if (series2014 != null) {
                        graphChart.addSeries(series2014);
                    }
                }
                break;
            case 2015:
                if (check) {
                    graphChart.removeAllSeries();
                    if (series2015 != null) {
                        graphChart.addSeries(series2015);
                    }
                }
                break;
            case 2016:
                if (check) {
                    graphChart.removeAllSeries();
                    if (series2016 != null) {
                        graphChart.addSeries(series2016);
                    }
                }
                break;
            case 2017:
                if (check) {
                    graphChart.removeAllSeries();
                    if (series2017 != null) {
                        graphChart.addSeries(series2017);
                    }
                }
                break;
        }
    }
}
