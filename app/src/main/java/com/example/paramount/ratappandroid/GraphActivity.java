package com.example.paramount.ratappandroid;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import java.util.Date;

/**
 * Created by joshuareno on 11/4/17.
 */

public class GraphActivity extends AppCompatActivity {
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
}
