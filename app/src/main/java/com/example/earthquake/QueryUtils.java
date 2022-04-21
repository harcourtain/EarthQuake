package com.example.earthquake;

import android.media.metrics.Event;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public class QueryUtils {
    /** Tag for the log messages */
   public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * Query the USGS dataset and return an {@link Event} object to represent a single earthquake.
     */
  public static ArrayList<Word> fetchEarthquakeData(String requestUrl) throws JSONException {

//      Force the background thread to sleep for 2 seconds
//      To force the background thread to sleep for 2 seconds,
//      we are temporarily simulating a very slow network response time. We are “pretending”
//      that it took a long time to fetch the response. That allows us to see the loading spinner on
//      the screen for a little longer than it normally would appear for.
//      In the QueryUtils.java file, within the fetchEarthquakeData() method,
//      add this snippet of code at the top of the method.
//      Leave the rest of the code in the method as-is. We are forcing the background thread
//      to pause execution and wait for 2 seconds (which is 2000 milliseconds), before proceeding to
//      execute the rest of lines of code in this method. If you try to add the Thread.sleep(2000);
//      method call by itself, Android Studio will complain that there is an uncaught exception,
//      so we need to surround that statement with a try/catch block.
      try {
          Thread.sleep(20); // Increasing time for Starting of Fetch
      } catch (InterruptedException e) {
          e.printStackTrace();
      }
      // Create URL object
         URL url = createURL(requestUrl);
      // Perform HTTP request to the URL and receive a JSON response back
      String jsonResponse = null;
      try {
          jsonResponse=makeHttpRequest(url);
      }catch (IOException e){
          Log.e(LOG_TAG,"Error closing input Stream",e);
      }
      // Extract relevant fields from the JSON response and create an {@link Event} object
     ArrayList<Word> earthquake = extractEarthquakesFromJSON(jsonResponse);
      return earthquake;
  }
    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createURL(String stringUrl){
        URL url= null;
        try{
            url= new URL(stringUrl); // Extract Url from string
        }catch (MalformedURLException e){
            Log.e(LOG_TAG,"Error with creating URL",e);
        }
        return url;
    }
    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException{
        String jsonResponse="";
        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse; // protecting errors
        }
        HttpURLConnection urlConnection = null;  // HttpURLConnection object
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if(urlConnection.getResponseCode()==200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }else{
                Log.e(LOG_TAG,"Error Response Code "  +urlConnection.getResponseCode());
            }
        } catch (IOException e){
            Log.e(LOG_TAG,"Problem retrieving the earthQuake JSON Result.",e);
        }finally {
            if(urlConnection!=null){
                urlConnection.disconnect();
            }
            if(inputStream!=null){
                inputStream.close();
            }
        }
        return jsonResponse;
    }
    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException{
    StringBuilder output =new StringBuilder();
    if(inputStream!=null){
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream,Charset.forName("UTF-8"));
        BufferedReader reader = new BufferedReader(inputStreamReader);
        String line = reader.readLine();
        while(line!=null){
            output.append(line);
            line =reader.readLine();
        }
    }
    return output.toString();

    }

    /**
     * Return a list of {@link Word} objects that has been built up from
     * parsing a JSON response.
     */
    public static ArrayList<Word> extractEarthquakesFromJSON(String earthquakeJSON) throws JSONException {
        ArrayList<Word> earthquakes = new ArrayList<>();
        // Create an empty ArrayList that we can start adding earthquakes to
        try{
            //    Extract “features” JSONArray
            JSONObject jsonObject = new JSONObject(earthquakeJSON);
            JSONArray jsonFeatures = jsonObject.getJSONArray("features");
            //    Loop through each feature in the array
            for (int i = 0; i < jsonFeatures.length(); i++) {

                //    Get earthquake(Word) JSONObject at position i
                JSONObject currObject = (JSONObject) jsonFeatures.get(i); // Accessing current object
                //    Get “properties” JSONObject
                JSONObject properties = currObject.getJSONObject("properties");  // Accessing properties this object
                //    Extract “mag” for magnitude
                //    Extract “place” for location
                //    Extract “time” for time
                double mag = properties.getDouble("mag"); // Extract the value for the key called "mag"
                // retriving URL
                String url = properties.getString("url");
                Log.i(LOG_TAG,"url Gotten "+url);
                // Styling Magnitude
                DecimalFormat formatter = new DecimalFormat("0.0");
                String magOneDeci = formatter.format(mag);

                String compGeoLoc = properties.getString("place");
                int split = compGeoLoc.indexOf("of ");
                long date = properties.getLong("time");  // int -> 4 byte better option ->long 8->byte
//            long timeInMilliseconds = 1454124312220L;
//            Date dateObject = new Date(timeInMilliseconds);
                Date dateObject = new Date(date); // Wed Dec 10 14:09:50 GMT+05:30 1068
//            SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM DD, yyyy");
//            String dateToDisplay = dateFormatter.format(dateObject);
                String dateToDisplay = formatDate(dateObject);
                String timeToDisplay = formatTime(dateObject);
                DecimalFormat magFormater = new DecimalFormat("0.00");
//           Create Earthquake java object from magnitude, location, and time

//          Add earthquake to list of earthquakes
                earthquakes.add(new Word(mag, compGeoLoc.substring(0, split + 3), compGeoLoc.substring(split + 3, compGeoLoc.length()), dateToDisplay, timeToDisplay,url));
            }
        }catch (JSONException e){
            Log.e(LOG_TAG,"Problem parsing the earthquake JSON results",e);
        }
        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.


        // Return the list of earthquakes
        return earthquakes;
    }
    /**
     * Return the formatted date string (i.e. "Mar 3, 1984") from a Date object.
     */
    private static String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }

    /**
     * Return the formatted date string (i.e. "4:30 PM") from a Date object.
     */
    private static String formatTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }


    //  To Do
//    Convert SAMPLE_JSON_RESPONSE String into a JSONObject
//    Extract “features” JSONArray
//    Loop through each feature in the array
//    Get earthquake JSONObject at position i
//    Get “properties” JSONObject
//    Extract “mag” for magnitude
//    Extract “place” for location
//    Extract “time” for time
//    Create Earthquake java object from magnitude, location, and time
//    Add earthquake to list of earthquakes
}
