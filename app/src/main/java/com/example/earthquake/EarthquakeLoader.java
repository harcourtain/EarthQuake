package com.example.earthquake;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.content.AsyncTaskLoader;
import android.util.Log;

import org.json.JSONException;

import java.util.ArrayList;

/**
 * Loads a list of earthquakes by using an AsyncTask to perform the
 * network request to the given URL.
 */
public class EarthquakeLoader extends AsyncTaskLoader<ArrayList<Word>> {
    /** Tag for log messages */
    private static final String LOG_TAG = EarthquakeLoader.class.getName();

    /** Query URL */
    private String mUrl;
    /**
     * Constructs a new {@link EarthquakeLoader}.
     *
     * @param context of the activity
     * @param url to load data from
     */
    public EarthquakeLoader(@NonNull Context context, String url) {
        super(context);

        Log.i(LOG_TAG,"Constructor of EarthquakeLoader is Called");
        // TODO: Finish implementing this constructor
        mUrl=url;
    }


    @Override
    protected void onStartLoading() {
        Log.i(LOG_TAG,"Force Loading in onStartLoading");
        forceLoad();
    }


    /**
     * This is on a background thread.
     */

    @Override
    public ArrayList<Word> loadInBackground() {
        Log.i(LOG_TAG,"BackGround Thread Started Fetching Information from URL");
        if(mUrl==null) {
        return null;}
        ArrayList<Word> result = null;

        // Perform the network request, parse the response, and extract a list of earthquakes.
        try {
            result = QueryUtils.fetchEarthquakeData(mUrl);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }
}