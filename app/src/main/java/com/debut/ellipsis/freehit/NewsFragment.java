package com.debut.ellipsis.freehit;



import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<NewsItem>> {


  public  View loadingIndicator;
    //the webiste url of the api
    private static final String URL =
            "https://newsapi.org/v1/articles?source=espn-cric-info&sortBy=latest&apiKey=df0d40507d0548308c737ded0b6f8a82";

    private static final int NEWS_LOADER_ID = 1;

    private NewsItemAdapter mAdapter;
    public TextView mEmptyStateTextView;
    public String status;

    public NewsFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.item, container, false);
        View fragView = inflater.inflate(R.layout.fragment_news, container, false);
        getLoaderManager().initLoader(0, null, this);
        getLoaderManager().getLoader(0).startLoading();
        loadingIndicator=fragView.findViewById(R.id.progress_bar);
        // Find a reference to the {@link ListView} in the layout
        ListView NewsListView = (ListView) rootView.findViewById(R.id.list);

       mEmptyStateTextView = (TextView) (fragView.findViewById(R.id.empty_view));
       NewsListView.setEmptyView(mEmptyStateTextView);

        // Create a new adapter that takes an empty list of subjects as input
        mAdapter = new NewsItemAdapter(getActivity(), new ArrayList<NewsItem>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        NewsListView.setAdapter(mAdapter);


        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
//            loadingIndicator = rootView.findViewById(R.id.progress_bar);
            loadingIndicator.setVisibility(View.GONE);
            System.out.println("NOCONN");
            // Update empty state with no connection error message
            mEmptyStateTextView.setText("NO CONNECTION");
            return fragView;

        }

        return rootView;
    }


    @Override
    public Loader<List<NewsItem>> onCreateLoader(int i, Bundle bundle) {
        // Create a new loader for the given URL
        return new NewsLoader(getActivity(), URL);

    }

    @Override
    public void onLoadFinished(Loader<List<NewsItem>> loader, List<NewsItem> News) {

            loadingIndicator.setVisibility(View.GONE);
//         Set empty state text to display "No News found."
        if(mEmptyStateTextView.getText()==null) {
            mEmptyStateTextView.setText("NEWS NOT FOUND");
        }
        //ABOVE LINES IF UNCOMMENTED GIVE NULLPOINTER EXCEPTION ERROR  . PLEASE CHECK

        // If there is a valid list of {@link News}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (News != null && !News.isEmpty()) {

            mAdapter.addAll(News);
        }

    }



    @Override
    public void onLoaderReset(Loader<List<NewsItem>> loader) {
        loader=null;
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }

}