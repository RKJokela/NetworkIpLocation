package com.rjokela.networkiplocation;

/**
 * Holds IP location strings
 * Created by Randon K. Jokela on 8/19/2015.
 */
public class IpLocationInfo {
    // location fields
    private String ip;
    private String country;
    private String region;
    private String city;
    private String latitude;

    public IpLocationInfo() {
    }

    private String longitude;
    private String timezone;

    // JSON Keys
    public static final String KEY_IP = "ip";
    public static final String KEY_LONGITUDE = "longitude";
    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_COUNTRY = "country";
    public static final String KEY_TIMEZONE = "timezone";
    public static final String KEY_CITY = "city";
    public static final String KEY_REGION = "region";

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public IpLocationInfo(String ip, String country, String region, String city, String latitude, String longitude, String timezone) {

        this.ip = ip;
        this.country = country;
        this.region = region;
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timezone = timezone;
    }
}
