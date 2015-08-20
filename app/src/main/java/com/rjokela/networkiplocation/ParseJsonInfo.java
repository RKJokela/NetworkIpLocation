package com.rjokela.networkiplocation;

/*
    http://www.telize.com This service offers a REST API allowing to get a
    visitor IP address and to query location information from any IP address.
    It outputs JSON-encoded IP geolocation data.
    There is no rate limit of any sort at the moment, and the service is free for everyone to use.
    http://www.telize.com/jsonip
        {"ip":"76.88.27.143"}
    http://www.telize.com/geoip/46.19.37.108
        {"longitude":4.9,"latitude":52.3667,"asn":"AS196752","offset":"2",
            "ip":"46.19.37.108","area_code":"0","continent_code":"EU","dma_code":"0",
            "timezone":"Europe\/Amsterdam","country_code":"NL","isp":"Tilaa B.V.",
            "country":"Netherlands","country_code3":"NLD"}
    http://www.telize.com/geoip/8.8.8.8
        {"longitude":-122.0838,"latitude":37.386,"asn":"AS15169","offset":"-7",
            "ip":"8.8.8.8","area_code":"0","continent_code":"NA","dma_code":"0",
            "city":"Mountain View","timezone":"America\/Los_Angeles","region":"California",
            "country_code":"US", "isp":"Google Inc.","postal_code":"94040",
            "country":"United States", "country_code3":"USA","region_code":"CA"}
*/

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Randon K. Jokela on 8/19/2015.
 */
public class ParseJsonInfo {
    String TAG = "GetIpLocation_Json";
    IpLocationInfo ipLocationInfo = null;
    String ipAddr = null;

    public String getIpAddr() {
        return ipAddr;
    }

    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
    }

    public IpLocationInfo getIpLocationInfo() {

        return ipLocationInfo;
    }

    public ParseJsonInfo(String ipAddr) {

        this.ipAddr = ipAddr;
    }

    public ParseJsonInfo() {

    }

    public IpLocationInfo decodeMessage(String message) {
        try {
            Log.d(TAG, "Parsing: " + message);
            ipLocationInfo = new IpLocationInfo();

            JSONObject jObject;
            jObject = new JSONObject(message);

            // extract location fields from JSON
            String value = getJsonValue(jObject, IpLocationInfo.KEY_IP);
            ipLocationInfo.setIp(value);

            if (jObject.length() > 1) {
                // only do these when querying location info
                value = getJsonValue(jObject, IpLocationInfo.KEY_LATITUDE);
                ipLocationInfo.setLatitude(value);
                value = getJsonValue(jObject, IpLocationInfo.KEY_LONGITUDE);
                ipLocationInfo.setLongitude(value);
                value = getJsonValue(jObject, IpLocationInfo.KEY_COUNTRY);
                ipLocationInfo.setCountry(value);
                value = getJsonValue(jObject, IpLocationInfo.KEY_CITY);
                ipLocationInfo.setCity(value);
                value = getJsonValue(jObject, IpLocationInfo.KEY_REGION);
                ipLocationInfo.setRegion(value);
                value = getJsonValue(jObject, IpLocationInfo.KEY_TIMEZONE);
                ipLocationInfo.setTimezone(value);
            }

        } catch (Exception e) {
            Log.e(TAG, "decodeMessage: exception during parsing - ", e);
            e.printStackTrace();
            return null;
        }
        return  ipLocationInfo;
    }

    @Override
    public String toString() {
        return super.toString() + ipLocationInfo.toString();
    }

    // Need to catch the exception if the json string does not have the key
    // and default value to null in that case.
    public String getJsonValue(JSONObject jObject, String KEY) throws JSONException {
        String value = "";
        try {
            value = jObject.getString(KEY);
            Log.d(TAG, "getJsonValue: value = " + value);
        } catch (Exception e) {
            throw e;
        }
        return  value;
    }
}
