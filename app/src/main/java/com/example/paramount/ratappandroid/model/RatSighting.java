package com.example.paramount.ratappandroid.model;

/**
 * Created by Paramount on 2017/10/14.
 */

public class RatSighting {
    private String uniqueKey;
    private String createDate;
    private String locType;
    private String incidentZip;
    private String incidentAddr;
    private String city;
    private String Borough;
    private String latitude;
    private String longitude;


    public RatSighting() {

    }

    public String getUniqueKey() {
        return uniqueKey;
    }

    public void setUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
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
        return Borough;
    }

    public void setBorough(String borough) {
        Borough = borough;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
