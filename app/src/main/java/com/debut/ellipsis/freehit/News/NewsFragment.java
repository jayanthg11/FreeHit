package com.debut.ellipsis.freehit.News;


import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.debut.ellipsis.freehit.R;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<NewsItem>> {

    public static final String LOG_TAG = NewsFragment.class.getSimpleName();

    public View loadingIndicator;
    //the website url of the api
    private static final String URL =
            "http://freehit-api.herokuapp.com/news";

    private static final int NEWS_LOADER_ID = 1;

    private NewsItemAdapter mAdapter;
    public TextView mEmptyStateTextView;
    private ProgressBar mProgressBar;

    public NewsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.list_common, container, false);
        View fragView = inflater.inflate(R.layout.fragment_news_list_item, container, false);


        //Finding a reference to the AVLoading bar
        AVLoadingIndicatorView loader = (AVLoadingIndicatorView) fragView.findViewById(R.id.avi);

        // Find a reference to the {@link ListView} in the layout
        ListView NewsListView = (ListView) rootView.findViewById(R.id.list);

        mEmptyStateTextView = (TextView) (fragView.findViewById(R.id.empty_view));
        NewsListView.setEmptyView(mEmptyStateTextView);
        NewsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current earthquake that was clicked on
                NewsItem currentNewsArticle = mAdapter.getItem(position);

                //will be replaced with id later on
                int news_article_id = currentNewsArticle.getMnewsID();

                Intent newsArticleIntent = new Intent(getContext(), NewsArticle.class);
                newsArticleIntent.putExtra("news_article_id", news_article_id);
                // Send the intent to launch a new activity
                startActivity(newsArticleIntent);

            }
        });


        // Create a new adapter that takes an empty list of subjects as input
        mAdapter = new NewsItemAdapter(getActivity(), new ArrayList<NewsItem>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        NewsListView.setAdapter(mAdapter);

        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar);
        int colorCodeDark = Color.parseColor("#F44336");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mProgressBar.setIndeterminateTintList(ColorStateList.valueOf(colorCodeDark));
        }

        // Get a reference to the ConnectivityManager to check state of network connectivity
        final ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        final NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

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
            mAdapter.add(new NewsItem("No connection", "Looks like you have no connection, switch on your internet connection and try refreshing to see the latest news."));
            return rootView;

        }

        // Finding a reference to the refresh layout
        final SwipeRefreshLayout refLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);
        refLayout.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);
        refLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                                           @Override
                                           public void onRefresh() {
                                               // Checking if connected or not on refresh
                                               refLayout.setRefreshing(true);
                                               NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                                               if (networkInfo != null && networkInfo.isConnected()) {
//                LoaderManager loaderManager = getLoaderManager();
                                                   mAdapter.clear();
//                mAdapter.clear();
                                                   getLoaderManager().restartLoader(NEWS_LOADER_ID, null, NewsFragment.this);
                                                   mAdapter.notifyDataSetChanged();
                                                   new Handler().postDelayed(new Runnable() {
                                                       @Override
                                                       public void run() {
                                                           refLayout.setRefreshing(false);
                                                       }
                                                   }, 1000);

                                               } else {
                                                   // Otherwise, display error
                                                   // First, hide loading indicator so error message will be visible
                                                   mAdapter.clear();
                                                   mAdapter.add(new NewsItem("No connection", "Looks like you have no connection, switch on your internet connection and try refreshing to see the latest news."));
                                                   mAdapter.notifyDataSetChanged();

                                               }
                                               refLayout.setRefreshing(false);
                                           }
                                       }
        );

        return rootView;
    }

    @Override
    public Loader<List<NewsItem>> onCreateLoader(int i, Bundle bundle) {
        // Create a new loader for the given URL
        return new NewsLoader(getActivity(), URL);

    }

    @Override
    public void onLoadFinished(Loader<List<NewsItem>> loader, List<NewsItem> News) {

        mProgressBar.setVisibility(View.GONE);
        if (mEmptyStateTextView.getText() == null) {
            mEmptyStateTextView.setText(R.string.EmptyNews);
        }

        // If there is a valid list of {@link News}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (News != null && !News.isEmpty() && mAdapter.getCount() < 1) {

            mAdapter.addAll(News);
        }

    }

    @Override
    public void onLoaderReset(Loader<List<NewsItem>> loader) {
        loader = null;
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }

}