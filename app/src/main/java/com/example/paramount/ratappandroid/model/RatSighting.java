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
    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.US);
    private static final DateFormat displayDateFormat = new SimpleDateFormat("MMMM dd, yyyy HH:mm", Locale.US);

    private String uniqueKey;
    private Date createDate;
    private String locType;
    private String incidentZip;
    private String incidentAddr;
    private String city;
    private String borough;
    private Double latitude;
    private Double longitude;


    public RatSighting() {

    }

    /**
     * Creates a new RatSighting from a JSON object.
     */
    public RatSighting(JSONObject json) throws JSONException, ParseException {
        uniqueKey = json.getJSONObject("_id").getString("$oid");
        createDate = dateFormat.parse(json.getString("created_at")); // TODO: handle ParseException?
        locType = json.getString("location_type"); // TODO: use enum?

        JSONObject locationJson = json.getJSONObject("location");
        incidentZip = locationJson.getString("zip");
        incidentAddr = locationJson.getString("address");

        // Throws JSONException if latitude or longitude cannot be parsed to doubles.
        latitude = locationJson.getDouble("latitude");
        longitude = locationJson.getDouble("longitude");

        city = locationJson.getJSONObject("city").getString("name");
        borough = locationJson.getJSONObject("borough").getString("name");

        // Address, zip, city, borough can be missing as long as latitude and longitude are present.
        incidentAddr = checkForMissingValue(incidentAddr, "missing address");
        incidentZip = checkForMissingValue(incidentZip, "missing zip code");
        city = checkForMissingValue(city, "missing city");
        borough = checkForMissingValue(borough, "missing borough");
    }

    /**
     * If "value" parameter is empty or equal to "null", returns "missing" parameter. Otherwise,
     * returns "value" parameter.
     */
    private String checkForMissingValue(String value, String missing) {
        return StringUtils.isEmpty(value) || value.equals("null") ? missing : value;
    }

    public String getUniqueKey() {
        return uniqueKey;
    }

    public void setUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getLocType() {
        return locType;
    }

    public void setLocType(String locType) {
        this.locType = locType;
    }

    public String getIncidentZip() {
        return incidentZip;
    }

    public void setIncidentZip(String incidentZip) {
        this.incidentZip = incidentZip;
    }

    public String getIncidentAddr() {
        return incidentAddr;
    }

    public void setIncidentAddr(String incidentAddr) {
        this.incidentAddr = incidentAddr;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getBorough() {
        return borough;
    }

    public void setBorough(String borough) {
        this.borough = borough;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String toString() {
        String formattedDate = displayDateFormat.format(createDate);
        return String.format("%s\n%s", incidentAddr, formattedDate);
    }
}
