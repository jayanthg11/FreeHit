package com.debut.ellipsis.freehit.Stats.Player;


import android.text.TextUtils;
import android.util.Log;

import com.debut.ellipsis.freehit.Matches.LiveMatches.LiveMatchCardItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static com.debut.ellipsis.freehit.Matches.LiveMatches.QueryUtilLiveMatchCard.extractFeatureFromJson;

public class QueryUtilStats {
    public static final String LOG_TAG = com.debut.ellipsis.freehit.Stats.Player.QueryUtilStats.class.getSimpleName();

    private QueryUtilStats() {

    }

    public static List<LiveMatchCardItem> fetchPlayerInfo(String requestUrl) {


        Log.i(LOG_TAG, "TEST: fetchLiveMatchData() called");
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }


        return extractFeatureFromJson(jsonResponse);

    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(60000 /* milliseconds */);
            urlConnection.setConnectTimeout(60000  /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the news JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    public static List<LiveMatchCardItem> extractFeatureFromJson(String UpcomingMatchesJSON) {

        //if the JSON string is empty or null then return early
        if (TextUtils.isEmpty(UpcomingMatchesJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding LiveMatches to
        List<LiveMatchCardItem> LiveMatches = new ArrayList<>();

        // Try to parse the JSON response string If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // TODO: Parse the response given by the JSON response string and
            // build up a list of News Articles objects with the corresponding data.

            //create a JSONObject from  the JSON response string
            JSONObject basJsonResponse = new JSONObject(UpcomingMatchesJSON);
            JSONArray result = basJsonResponse.getJSONArray("result");

            for (int i = 0; i < result.length(); i++) {

            }
            /*LiveMatches.add(new LiveMatchCardItem("Click to view more"));*/
//                Log.e(LOG_TAG, String.valueOf(j));
            return LiveMatches;
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the UpcomingMatches JSON results", e);
        }
        return LiveMatches;
    }
}
