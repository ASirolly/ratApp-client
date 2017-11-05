package com.example.paramount.ratappandroid.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;

/**
 * Created by Paramount on 2017/11/4.
 */

public class GraphDate {
    private String month;
    private String year;
    private String frequency;

    public GraphDate (JSONObject obj) throws JSONException {
        //TODO: Make the constructor actually create the graphDate
        year = obj.getJSONObject("_id").getString("year");
        month = obj.getJSONObject("_id").getString("month");
        frequency = obj.getString("rat_sightings");
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String toString() {
        return month + " " + year;
    }
}
