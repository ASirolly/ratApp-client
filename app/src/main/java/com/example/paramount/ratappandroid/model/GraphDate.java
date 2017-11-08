package com.example.paramount.ratappandroid.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Paramount on 2017/11/4.
 *
 * Represents a single data point in rat sighting frequency graph.
 */

public class GraphDate {
    private final String month;
    private final String year;
    private final String frequency;

    /**
     * Creates a GraphDate from the provided JSON
     * @param obj json object
     * @throws JSONException if a field is missing
     */
    public GraphDate (JSONObject obj) throws JSONException {
        year = obj.getJSONObject("_id").getString("year");
        month = obj.getJSONObject("_id").getString("month");
        frequency = obj.getString("rat_sightings");
    }

    /**
     * Get the GraphDate's month
     * @return the GraphDate's month
     */
    public String getMonth() {
        return month;
    }

    /**
     * Get the GraphDate's year
     * @return the GraphDate's year
     */
    public String getYear() {
        return year;
    }

    /**
     * Get the GraphDate's frequency
     * @return the GraphDate's frequency
     */
    public String getFrequency() {
        return frequency;
    }

    public String toString() {
        return month + " " + year;
    }
}
