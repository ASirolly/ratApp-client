package com.example.paramount.ratappandroid;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import com.example.paramount.ratappandroid.dao.GraphDateDAO;
import com.example.paramount.ratappandroid.model.GraphDate;
import com.example.paramount.ratappandroid.model.Model;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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

    private final DataPoint[] ten = new DataPoint[12];
    private final DataPoint[] eleven = new DataPoint[12];
    private final DataPoint[] twelve = new DataPoint[12];
    private final DataPoint[] thirteen = new DataPoint[12];
    private final DataPoint[] fourteen = new DataPoint[12];
    private final DataPoint[] fifteen = new DataPoint[12];
    private final DataPoint[] sixteen = new DataPoint[12];
    private final DataPoint[] seventeen = new DataPoint[12];
    private LineGraphSeries<DataPoint> series2010;
    private LineGraphSeries<DataPoint> series2011;
    private LineGraphSeries<DataPoint> series2012;
    private LineGraphSeries<DataPoint> series2013;
    private LineGraphSeries<DataPoint> series2014;
    private LineGraphSeries<DataPoint> series2015;
    private LineGraphSeries<DataPoint> series2016;
    private LineGraphSeries<DataPoint> series2017;
    private GraphView graphChart;

    @Override
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
        selectEndDateButton.setText(String.format(selectEndDateButtonTextTemplate,
                displayDateFormat.format(endDate)));

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

        graphChart = (GraphView) findViewById(R.id.chart);
    }

    private void handleThisData(JSONObject response) {
        Log.d(TAG, "handling data");
        Model.getInstance().resetGraphDates();
        JSONObject json;
        Log.w(TAG, "Wir sind successful!");
        try {
            Log.d(TAG, "DATA GETTING BACK: " + response);
            JSONArray innerArr = response.getJSONArray("data");
            int len = innerArr.length();
            Log.d(TAG, "THIS IS INNERARR: " + innerArr);
            Log.d(TAG, "THIS IS LEN: " + len);
            for (int i = 0; i < len; i++) {
                json = (JSONObject) innerArr.get(i);
                Log.d(TAG,"THIS IS json: " + json);

                //json = (JSONObject) response.get(i);
                Model.getInstance().getGraphDates().add(new GraphDate(json));
            }

        } catch (JSONException e) {
            Log.w(TAG, "EXCEPTION HAPPENING HERE");
            Log.w(TAG, e);
        }
        showAllFrequencies();
    }

    private void showAllFrequencies() {
        List<GraphDate> temp = Model.getInstance().getGraphDates();
//        LineGraphSeries<DataPoint> series;
//        DataPoint[] dataPoints = new DataPoint[temp.size()];
        for (GraphDate gD: temp) {
            if ((gD != null) && (gD.getYear() != null) && (gD.getMonth() != null)
                    && !("".equals(gD.getYear())) && !("".equals(gD.getMonth()))) {
                int year = Integer.parseInt(gD.getYear());
                int month = Integer.parseInt(gD.getMonth());
                int frequency = Integer.parseInt(gD.getFrequency());
                Log.d(TAG, String.format(
                        "found date with year %d, month %d, and frequency %d",
                        year, month, frequency));
                switch(year) {
                    case 2010:
                        ten[month - 1] = new DataPoint(month, frequency);
                        break;
                    case 2011:
                        eleven[month - 1] = new DataPoint(month, frequency);
                        break;
                    case 2012:
                        twelve[month - 1] = new DataPoint(month, frequency);
                        break;
                    case 2013:
                        thirteen[month - 1] = new DataPoint(month, frequency);
                        break;
                    case 2014:
                        fourteen[month - 1] = new DataPoint(month, frequency);
                        break;
                    case 2015:
                        fifteen[month - 1] = new DataPoint(month, frequency);
                        break;
                    case 2016:
                        sixteen[month - 1] = new DataPoint(month, frequency);
                        break;
                    case 2017:
                        seventeen[month - 1] = new DataPoint(month, frequency);
                        break;
                }
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

        populateColor();

        graphChart.removeAllSeries();
        graphChart.addSeries(series2017); // TODO: set 2017 button to checked

        Viewport viewport = graphChart.getViewport();
        viewport.setMinX(1);
        viewport.setMaxX(12);
        viewport.setXAxisBoundsManual(true);
    }

    /**
     * Set styling for all series.
     */
    private void populateColor() {
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

        Arrays.stream(allSeries).forEach(series -> {
            series.setTitle("This");
            series.setColor(Color.GREEN);
            series.setDrawDataPoints(true);
            series.setDataPointsRadius(10);
            series.setThickness(8);
        });
    }

    public void populate() {
        for (int i = 0; i < ten.length; i++) {
            if (ten[i] == null ) {
                ten[i] = new DataPoint(i+1, 0);
            } else if (ten[i].getY() == 0) {
                ten[i] = new DataPoint(i+1, 0);
            }
        }
        for (int i = 0; i < eleven.length; i++) {
            if (eleven[i] == null) {
                eleven[i] = new DataPoint(i+1, 0);
            } else if (eleven[i].getY() == 0) {
                eleven[i] = new DataPoint(i+1, 0);
            }
        }
        for (int i = 0; i < twelve.length; i++) {
            if (twelve[i] == null) {
                twelve[i] = new DataPoint(i+1, 0);
            } else if (twelve[i].getY() == 0) {
                twelve[i] = new DataPoint(i+1, 0);
            }
        }
        for (int i = 0; i < thirteen.length; i++) {
            if (thirteen[i] == null) {
                thirteen[i] = new DataPoint(i+1, 0);
            } else if (thirteen[i].getY() == 0) {
                thirteen[i] = new DataPoint(i+1, 0);
            }
        }
        for (int i = 0; i < fourteen.length; i++) {
            if (fourteen[i] == null) {
                fourteen[i] = new DataPoint(i+1, 0);
            } else if (fourteen[i].getY() == 0) {
                fourteen[i] = new DataPoint(i+1, 0);
            }
        }
        for (int i = 0; i < fifteen.length; i++) {
            if (fifteen[i] == null) {
                fifteen[i] = new DataPoint(i+1, 0);
            } else if (fifteen[i].getY() == 0) {
                fifteen[i] = new DataPoint(i+1, 0);
            }
        }
        for (int i = 0; i < sixteen.length; i++) {
            if (sixteen[i] == null) {
                sixteen[i] = new DataPoint(i+1, 0);
            } else if (sixteen[i].getY() == 0) {
                sixteen[i] = new DataPoint(i+1, 0);
            }
        }
        for (int i = 0; i < seventeen.length; i++) {
            if (seventeen[i] == null) {
                seventeen[i] = new DataPoint(i+1, 0);
            } else if (seventeen[i].getY() == 0) {
                seventeen[i] = new DataPoint(i+1, 0);
            }
        }
    }

    public void onSwitchYear(View view) {
        boolean check = ((RadioButton) view).isChecked();
        RadioButton button = (RadioButton) view;
        Log.d(TAG, String.format("button with year %s was clicked", button.getText()));

        if (check) {
            graphChart.removeAllSeries();
        }

        switch(Integer.parseInt((String) button.getText())) {
            case 2010:
                if(check) {
                    if (series2010 != null) {
                        graphChart.addSeries(series2010);
                    }
                }
                break;
            case 2011:
                if (check) {
                    if (series2011 != null) {
                        graphChart.addSeries(series2011);
                    }
                }
                break;
            case 2012:
                if (check) {
                    if (series2012 != null) {
                        graphChart.addSeries(series2012);
                    }
                }
                break;
            case 2013:
                if (check) {
                    if (series2013 != null) {
                        graphChart.addSeries(series2013);
                    }
                }
                break;
            case 2014:
                if (check) {
                    if (series2014 != null) {
                        graphChart.addSeries(series2014);
                    }
                }
                break;
            case 2015:
                if (check) {
                    if (series2015 != null) {
                        graphChart.addSeries(series2015);
                    }
                }
                break;
            case 2016:
                if (check) {
                    if (series2016 != null) {
                        graphChart.addSeries(series2016);
                    }
                }
                break;
            case 2017:
                if (check) {
                    if (series2017 != null) {
                        graphChart.addSeries(series2017);
                    }
                }
                break;
        }
    }
}
