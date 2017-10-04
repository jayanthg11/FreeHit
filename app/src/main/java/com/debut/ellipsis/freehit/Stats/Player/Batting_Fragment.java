package com.debut.ellipsis.freehit.Stats.Player;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;

import com.debut.ellipsis.freehit.APIInterface;
import com.debut.ellipsis.freehit.ApiClient;
import com.debut.ellipsis.freehit.R;


public class Batting_Fragment extends Fragment {
    String[] gridViewString = {
            "Batting", "Test", "Odi", "T20", "IPL",
            "Matches", "99,999", "10", "10", "10",
            "Innings", "10", "10", "10", "10",
            "N/0", "10", "10", "10", "10",
            "Runs", "10", "10", "10", "10",
            "Highest", "10", "10", "10", "10",
            "100s", "10", "10", "10", "10",
            "50s", "10", "10", "10", "10",
            "4s", "10", "10", "10", "10",
            "6s", "10", "10", "10", "10",
            "Avg", "10", "10", "10", "10",
            "Strike\nRate", "10", "10", "10", "10",


    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_stats_player_batting_bowling_gridview, container, false);
        APIInterface apiInterface = ApiClient.getClient().create(APIInterface.class);
        GridView androidGridView;
        Batting_Item_adapter adapterViewAndroid = new Batting_Item_adapter(getContext(), gridViewString);
        androidGridView = (GridView) rootView.findViewById(R.id.grid_view_batting_bowling);
        androidGridView.setAdapter(adapterViewAndroid);
        setGridViewHeightBasedOnChildren(androidGridView, 5);

        return rootView;
    }

    public void setGridViewHeightBasedOnChildren(GridView gridView, int columns) {
        ListAdapter listAdapter = gridView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int items = listAdapter.getCount();
        int rows = 0;

        View listItem = listAdapter.getView(0, null, gridView);
        listItem.measure(0, 0);
        totalHeight = listItem.getMeasuredHeight();

        float x = 1;
        if (items > columns) {
            x = items / columns;
            rows = (int) (x + 1);
            totalHeight *= rows;
        }

        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight;
        gridView.setLayoutParams(params);

    }
}
