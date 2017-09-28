package com.debut.ellipsis.freehit.Stats.Player;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.debut.ellipsis.freehit.R;


public class Batting_Fragment extends Fragment{
    String[] gridViewString = {
            "Alarm", "Android", "Mobile", "Website", "Profile", "WordPress",
            "Alarm", "Android", "Mobile", "Website", "Profile", "WordPress",
            "Alarm", "Android", "Mobile", "Website", "Profile", "WordPress",

    } ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_stats_batting, container, false);
        GridView androidGridView;
        Batting_Item_adapter adapterViewAndroid = new Batting_Item_adapter(getContext(), gridViewString );
       androidGridView=(GridView)rootView.findViewById(R.id.grid_view_image_text);
        androidGridView.setBackgroundColor(Color.WHITE);
        androidGridView.setVerticalSpacing(1);
        androidGridView.setHorizontalSpacing(1);

        androidGridView.setAdapter(adapterViewAndroid);




        return rootView;
    }




}
