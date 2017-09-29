package com.debut.ellipsis.freehit.Matches.UpcomingMatches;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.debut.ellipsis.freehit.R;

import java.util.ArrayList;
import java.util.List;


public class UpcomingMatchesActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<UpcomingMatchCardItem>> {


    private Toolbar toolbar;
    ArrayList<UpcomingMatchCardItem> upcomingMatches;
    public UpcomingMatchesListAdapter MatchListAdapter;
    private final static int UPCOMING_LOADER_ID = 5;
    private RecyclerView rv;
    private static final String URL =
            "https://freehit-api.herokuapp.com/upcoming?max=100";

    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_matches_complete_match_list);

        setTitle(R.string.upcoming_list);

        toolbar = (Toolbar) findViewById(R.id.toolbar_for_match_list);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Lookup the recyclerview in activity layout
        rv = (RecyclerView) findViewById(R.id.match_list);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);

       // Set layout manager to position the items
        rv.setLayoutManager(new LinearLayoutManager(this));

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getSupportLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(UPCOMING_LOADER_ID, null, this);


        }
    }
    @Override

    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                overridePendingTransition(0,R.anim.exit_to_right);
                return true;


        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        UpcomingMatchesActivity.super.onBackPressed();
        overridePendingTransition(0,R.anim.exit_to_right);
    }

    @Override
    public Loader<List<UpcomingMatchCardItem>> onCreateLoader(int i, Bundle bundle) {
        // Create a new loader for the given URL
        return new UpcomingMatchCardLoader(getApplicationContext(), URL);

    }

    @Override
    public void onLoadFinished(Loader<List<UpcomingMatchCardItem>> loader, List<UpcomingMatchCardItem> data) {

        mProgressBar.setVisibility(View.GONE);

        // If there is a valid list of {@link UpcomingMatches}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (data != null && !data.isEmpty()) {

            MatchListAdapter = new UpcomingMatchesListAdapter(getApplicationContext(), data);
            // This is the inner viewPager so commenting it out for now
            rv.setAdapter(MatchListAdapter);
        }

    }


    @Override
    public void onLoaderReset(Loader<List<UpcomingMatchCardItem>> loader) {

        // Loader reset, so we can clear out our existing data.

    }

}
