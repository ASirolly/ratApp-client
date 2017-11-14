package com.example.paramount.ratappandroid.model;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Paramount on 2017/10/14.
 * Represents a single rat sighting.
 */

public class RatSighting implements Serializable {
    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ",
            Locale.US);
    private static final DateFormat displayDateFormat = new SimpleDateFormat("MMMM dd, yyyy HH:mm",
            Locale.US);

    private final String uniqueKey;
    private final Date createDate;
    private final String locType;
    private String incidentZip;
    private String incidentAddress;
    private String city;
    private String borough;
    private final Double latitude;
    private final Double longitude;

    /**
     * Creates a new RatSighting from a JSON object.
     * @param json json with values for each RatSighting field
     * @throws JSONException if there is an error when parsing JSON fields
     * @throws ParseException if createDate cannot be parsed
     */
    public RatSighting(JSONObject json) throws JSONException, ParseException {
        JSONObject keyJson = json.getJSONObject("_id");
        uniqueKey = keyJson.getString("$oid");

        createDate = dateFormat.parse(json.getString("created_at"));
        locType = json.getString("location_type");

        JSONObject locationJson = json.getJSONObject("location");
        incidentZip = locationJson.getString("zip");
        incidentAddress = locationJson.getString("address");

        // Throws JSONException if latitude or longitude cannot be parsed to doubles.
        latitude = locationJson.getDouble("latitude");
        longitude = locationJson.getDouble("longitude");

        JSONObject cityJson = locationJson.getJSONObject("city");
        city = cityJson.getString("name");

        JSONObject boroughJson = locationJson.getJSONObject("borough");
        borough = boroughJson.getString("name");

        // Address, zip, city, borough can be missing as long as latitude and longitude are present.
        incidentAddress = checkForMissingValue(incidentAddress, "missing address");
        incidentZip = checkForMissingValue(incidentZip, "missing zip code");
        city = checkForMissingValue(city, "missing city");
        borough = checkForMissingValue(borough, "missing borough");
    }

    /**
     * If "value" parameter is empty or equal to "null", returns "missing" parameter. Otherwise,
     * returns "value" parameter.
     */
    private String checkForMissingValue(String value, String missing) {
        return (StringUtils.isEmpty(value) || "null".equals(value)) ? missing : value;
    }

    /**
     * Returns the rat sighting's unique key
     * @return unique key
     */
    public String getUniqueKey() {
        return uniqueKey;
    }

    /**
     * Returns the rat sighting's creation date
     * @return creation date
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * Returns the rat sighting's location type
     * @return location type
     */
    public CharSequence getLocType() {
        return locType;
    }

    /**
     * Returns the rat sighting's incident zip
     * @return incident zip
     */
    public CharSequence getIncidentZip() {
        return incidentZip;
    }

    /**
     * Returns the rat sighting's incident address
     * @return incident address
     */
    public CharSequence getIncidentAddress() {
        return incidentAddress;
    }

    /**
     * Returns the rat sighting's city
     * @return city
     */
    public CharSequence getCity() {
        return city;
    }

    /**
     * Returns the rat sighting's borough
     * @return borough
     */
    public CharSequence getBorough() {
        return borough;
    }

    /**
     * Returns the rat sighting's latitude
     * @return latitude
     */
    public Double getLatitude() {
        return latitude;
    }

    /**
     * Returns the rat sighting's longitude
     * @return longitude
     */
    public Double getLongitude() {
        return longitude;
    }

    public String toString() {
        String formattedDate = displayDateFormat.format(createDate);
        return String.format("%s\n%s", incidentAddress, formattedDate);
    }
}
