package com.example.paramount.ratappandroid;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * The first screen that users see after they log in.
 * Created by Greg on 9/22/17.
 * Modified by William on 10/10/17
 */

public class Dashboard extends LoggedInBaseActivity {

    /**
     * Creates the dashboard page.
     * @param savedInstanceState Bundle object containing the activity's previously saved state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);

        //Keeping the array as Object for now
        //The below sets up the listview
        Object[] ratSightings = new Object[10];
        ListAdapter listAdapter = new ArrayAdapter<Object>(this, android.R.layout.simple_list_item_1, ratSightings);
        ListView sightingListView = (ListView)findViewById(R.id.sightingListView);
        sightingListView.setAdapter(listAdapter);

        //Sets the actions that happen when you click on an item in the listview
        sightingListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //For funsies just gets the toString of the clicked item and makes it a toast
                        String funsy = String.valueOf(parent.getItemAtPosition(position));

                        //Makes the toast. Can be taken out
                        Toast.makeText(this, ratSightings, Toast.LENGTH_LONG.show());
                    }
                }
        );
    }
}
