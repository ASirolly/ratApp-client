package com.example.paramount.ratappandroid.model;

/**
 * Created by joshuareno on 10/21/17.
 */

public class Location {
    private double latitude;
    private double longitude;

    /**
     * Sets the data for the location
     * @param latitude the latitude
     * @param longitude the longitude
     */
    public Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Returns the latitude
     * @return latitude
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Returns the longitude
     * @return longitude
     */
    public double getLongitude() {
        return longitude;
    }
}
