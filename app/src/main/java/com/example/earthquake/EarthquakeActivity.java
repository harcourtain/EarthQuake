package com.example.earthquake;

import android.app.LoaderManager;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.content.Loader;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Word>> {// added implements LoaderManager.LoaderCallbacks  along with 3 methods
    private MediaPlayer mediaPlayer;
    private String LOG_TAG= EarthquakeActivity.class.getName();

    private WordAdapter mAdapter;

    private static String url="https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=4&limit=450";
    /** TextView that is displayed when the list is empty */
    private TextView mEmptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_list);

        ListView earthquakeListView = (ListView) findViewById(R.id.list);
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        earthquakeListView.setEmptyView(mEmptyStateTextView);

//        // Find a reference to the {@link ListView} in the layout
//        ListView earthquakeListView = (ListView) findViewById(R.id.list);
//        // Create a new adapter that takes an empty list of earthquakes as input
//        mAdapter = new WordAdapter(this, new ArrayList<Word>());
//        // Set the adapter on the {@link ListView}
//        // so the list can be populated in the user interface
//        earthquakeListView.setAdapter(mAdapter);
//        // Set an item click listener on the ListView, which sends an intent to a web browser
//        // to open a website with more information about the selected earthquake.
//        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                // Find the current earthquake that was clicked on
//                Word currentEarthquake = mAdapter.getItem(position);
//                // Convert the String URL into a URI object (to pass into the Intent constructor)
//                String url = "http://www.stackoverflow.com";
//                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.stackoverflow.com")));
////                openWebPage(url);
//                Uri earthquakeUri = Uri.parse(url);
////                // Create a new intent to view the earthquake URI
//                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);
////                // Send the intent to launch a new activity
//                startActivity(websiteIntent);
//            }
//        });

        // Start the AsyncTask to fetch the earthquake data
        Log.i("onCreate","onCreate is working fine.");

        LoaderManager loaderManager = getLoaderManager();
        EarthquakeLoader task = new EarthquakeLoader(this,url);
        Log.i(LOG_TAG,"Initiating initLoader For loader Manager");
        loaderManager.initLoader(0,null,this);



    }
//    Using Intent
//    public void openWebPage(String url) {
//        Uri webpage = Uri.parse(url);
//        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
//        if (intent.resolveActivity(getPackageManager()) != null) {
//            startActivity(intent);
//        }
//    }

    public void updateUI(ArrayList<Word> earthquakes){
        WordAdapter itemsAdapter = new WordAdapter(this,earthquakes);
//        listView.setAdapter(itemsAdapter);
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(itemsAdapter);
    }
//  3 methods of -> LoaderManager.LoaderCallbacks
    @NonNull
    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<Word>> loader, ArrayList<Word> data) {
        if(data.size()==0)  // Set empty state text to display "No earthquakes found."
            mEmptyStateTextView.setText(R.string.no_earthquakes);
        Log.i(LOG_TAG,"Updating all UI");
        // Adding progress bar -> sibling of listView and Empty_state_view
        ProgressBar progressBar= (ProgressBar)findViewById(R.id.loading_spinner) ;
        progressBar.setVisibility(View.INVISIBLE);
        if(!isInternetConnection()) {
            mEmptyStateTextView.setText("No Internet Connection");
            return;
        }
        updateUI(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<Word>> loader) {
        // Loader reset, so we can clear out our existing data.

    }
    @Override
    public Loader<ArrayList<Word>> onCreateLoader(int i, Bundle bundle) {

        Log.i(LOG_TAG,"onCreateLoader is Called , Initialising new EarthquakeLoader");
        // Create a new loader for the given URL
        return new EarthquakeLoader(this, url);
    }

//    public class EarthquakeAsyncTask extends AsyncTask<String ,Void, ArrayList<Word>>{
//        /**
//         * This method is invoked (or called) on a background thread, so we can perform
//         * long-running operations like making a network request.
//         *
//         * It is NOT okay to update the UI from a background thread, so we just return an
//         * {@link Event} object as the result. -> update it from that returned data ->postExecute i.e. mainThread
//         */
//        @Override
//        protected ArrayList<Word> doInBackground(String... urls) {
//            if(urls.length<1 || urls[0] == null){
//                return null;
//            }
//            ArrayList<Word> result = null;
//            try {
//                result = QueryUtils.fetchEarthquakeData(urls[0]);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            return result;
//        }
//        /**
//         * This method is invoked on the main UI thread after the background work has been
//         * completed.
//         *
//         * It IS okay to modify the UI within this method. We take the {@link Event} object
//         * (which was returned from the doInBackground() method) and update the views on the screen.
//         */
//        @Override
//        protected void onPostExecute(ArrayList<Word> result) {
//            if(result == null) {
//                return;
//            }
//            updateUI(result);
//        }
//    }
public  boolean isInternetConnection()
{

    ConnectivityManager connectivityManager =  (ConnectivityManager)this.getSystemService(this.CONNECTIVITY_SERVICE);
    if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
            connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED)
        return true;
    return false;
}
}

//  debugging
//    https://gist.github.com/udacityandroid/ee8fe10c04ec2d912c509ee3fb287149
//   https://github.com/udacity/ud843-QuakeReport/commit/6ac53a7c88e8eb2b2390555c7e550bfd4b398594?diff=unified#diff-2c4eab4cdc60051a79f61abc61fbfc9d9db0001e4b1873f9ee88269ce0364de6R50