package com.rjokela.networkiplocation;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * A placeholder fragment containing a simple view.
 */
public class GetIpLocationActivityFragment extends Fragment {
    public static final String TAG = "GetIpLocation";
    private static final int QUERY_GET_IP = 1;
    private static final int QUERY_GET_IP_LOC_INFO = 2;

    private int caller = 0;
    private GetNetworkInfoTask niTask = null;
    Button getIpLocationInfoButton;

    public GetIpLocationActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_get_ip_location, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Button getIpButton = (Button) getActivity().findViewById(R.id.button_getIp);
        getIpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                caller = QUERY_GET_IP;
                String queryBase = "http://www.telize.com/jsonip";
                String queryString = Uri.parse(queryBase).buildUpon().toString();
                startNetworkInfoTask(queryString);
            }
        });

        getIpLocationInfoButton = (Button) getActivity().findViewById(R.id.button_locationInfo);
        getIpLocationInfoButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                caller = QUERY_GET_IP_LOC_INFO;
                TextView ipInfoTV = (TextView) getActivity().findViewById(R.id.tv_ipAddress);
                String ipInfo = ipInfoTV.getText().toString();
                if (TextUtils.isEmpty(ipInfo)){
                    ipInfo = "8.8.8.8";
                }
                String queryBase = "http://www.telize.com/geoip/";
                String queryString =
                        Uri.parse(queryBase).buildUpon().appendPath(ipInfo).toString();
                startNetworkInfoTask(queryString);
            }
        });
    }

    public class GetNetworkInfoTask extends AsyncTask<String, Integer, String> {
        public static final String TAG = "GetNetworkInfo";

        private Context context = null;

        GetNetworkInfoTask(Context context) { this.context = context; }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(TAG, "onPreExecute() called");
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d(TAG, "onPostExecute() called, string = " + s);
            handleResults(s);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            Log.d(TAG, "onProgressUpdate() called");
        }

        @Override
        protected String doInBackground(String... params) {
            Log.d(TAG, "doInBackground() called, param[0] = " + params[0]);
            String networkInfo = getDataFromWeb(params[0]);
            return networkInfo;
        }
    }

    private void handleResults(String results) {
        Log.d(TAG, "handleResults: " + results);
        IpLocationInfo ipLocationInfo = null;
        if (results != null) {
            ParseJsonInfo parseJsonInfo = new ParseJsonInfo();
            ipLocationInfo = parseJsonInfo.decodeMessage(results);

            if (caller == QUERY_GET_IP) {
                displayResultsIP(ipLocationInfo);
            } else if (caller == QUERY_GET_IP_LOC_INFO) {
                displayResultsIPLocInfo(ipLocationInfo);
            }
        }
    }

    private void displayResultsIPLocInfo(IpLocationInfo ipLocationInfo) {
        TextView tv = null;

        tv = (TextView) getActivity().findViewById(R.id.tv_latitude);
        tv.setText(ipLocationInfo.getLatitude());
        tv = (TextView) getActivity().findViewById(R.id.tv_longitude);
        tv.setText(ipLocationInfo.getLongitude());
        tv = (TextView) getActivity().findViewById(R.id.tv_country);
        tv.setText(ipLocationInfo.getCountry());
        tv = (TextView) getActivity().findViewById(R.id.tv_city);
        tv.setText(ipLocationInfo.getCity());
        tv = (TextView) getActivity().findViewById(R.id.tv_region);
        tv.setText(ipLocationInfo.getRegion());
        tv = (TextView) getActivity().findViewById(R.id.tv_timezone);
        tv.setText(ipLocationInfo.getTimezone());
    }

    private void displayResultsIP(IpLocationInfo ipLocationInfo) {
        TextView textView = (TextView) getActivity().findViewById(R.id.tv_ipAddress);
        textView.setText(ipLocationInfo.getIp());
    }

    private String getDataFromWeb(String queryString) {
        String sb = null;
        try {
            // Open the connection and get the response from the url. Response should be in json
            URL url = new URL(queryString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            int response = urlConnection.getResponseCode();
            if (response != HttpURLConnection.HTTP_OK) {
                Log.e(TAG, "getDataFromWeb HttpURLConnection not ok: " + response);
            }else {
                Log.d(TAG, "getDataFromWeb HttpURLConnection is ok: " + response);
                sb = getInputString(urlConnection);
                Log.d(TAG, sb);
                urlConnection.disconnect();
            }
        }
        catch (Exception e){
            Log.e(TAG, this.getClass().getSimpleName() + e + " for url: " + queryString);
            e.printStackTrace();
        }
        return sb;
    }

    private String getInputString(HttpURLConnection urlConnection) {
        StringBuffer sb = null;
        BufferedReader in;
        try {
            // urlConnection.getInputStream reads the response body.
            // InputStreamReader is a java class that turns a byte stream into a character stream
            // BufferedReader is a java class that wraps an existing reader and buffers the input, to minimize
            // interaction with the underlying reader as most requests are satisfied from accessing the buffer
            in = new BufferedReader(
                    new InputStreamReader(urlConnection.getInputStream()));
            sb = new StringBuffer("");
            String line = "";
            String NL = System.getProperty("line.separator");
            while ((line = in.readLine()) != null) {
                sb.append(line + NL);
            }
            in.close();
        } catch (IOException e) {
            Log.d(TAG, "");
            e.printStackTrace();
        }

        return sb.toString();
    }

    public void startNetworkInfoTask(String queryString) {
    // Add code to kill any existing asyncTask if the button is pressed multiple times
        if(niTask != null) {
            AsyncTask.Status aiStatus = niTask.getStatus();
            Log.d(TAG, " startHttpURLTask :doClick: aiTask status is " + aiStatus);
            if(aiStatus != AsyncTask.Status.FINISHED) {
                Log.v(TAG, "startHttpURLTask doClick cancel existing task if not done.");
                niTask.cancel(true);
            }
        }
        // Start the asyncTask
        niTask = new GetNetworkInfoTask(getActivity());
        niTask.execute(queryString);
    }
}
