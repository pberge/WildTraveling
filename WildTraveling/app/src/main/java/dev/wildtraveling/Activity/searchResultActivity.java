package dev.wildtraveling.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import dev.wildtraveling.Domain.Trip;
import dev.wildtraveling.R;
import dev.wildtraveling.Domain.FoursquareVenue;
import dev.wildtraveling.Service.TripService;
import dev.wildtraveling.Util.RecyclerItemClickListener;
import dev.wildtraveling.Util.ServiceFactory;
import dev.wildtraveling.Util.Util;
import dev.wildtraveling.View.SearchResultRecyclerView;

public class searchResultActivity extends AppCompatActivity {
    private View searchResultRecyclerView;
    private List<FoursquareVenue> venues;
    private TripService tripService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Exploration results");
        setSupportActionBar(toolbar);

        tripService = ServiceFactory.getTripService(this);
        searchResultRecyclerView = findViewById(R.id.searchResultRecyclerView);

        Trip trip = tripService.getTripById(tripService.getCurrentTrip());
        venues = Util.sortVenues(trip.getStyle());

        SearchResultRecyclerView recyclerView = new SearchResultRecyclerView(getApplicationContext(),venues);
        ((RecyclerView) searchResultRecyclerView).setLayoutManager(new LinearLayoutManager(this));
        ((RecyclerView) searchResultRecyclerView).setAdapter(recyclerView);
        ((RecyclerView) searchResultRecyclerView).addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        ((RecyclerView) searchResultRecyclerView).setItemAnimator(new DefaultItemAnimator());
        ((RecyclerView) searchResultRecyclerView).addOnItemTouchListener(new RecyclerItemClickListener(this, (RecyclerView) searchResultRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                FoursquareVenue venue = venues.get(position);
                if(!venue.getLocation().equals("")){
                    Util.setCurrentVenue(venue);
                    Intent intent = new Intent(getApplicationContext(), mapsActivity.class);
                    startActivity(intent);
                } else{
                    Toast.makeText(getApplicationContext(),"Not address found",Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onItemLongClick(View view, int position) { //Delete dialog
                // ...
            }
        }));
        recyclerView.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(),getTripActivity.class);
        intent.putExtra("FRAGMENT", "SEARCH");
        Util.setVenues(new ArrayList<FoursquareVenue>());
        startActivity(intent);
    }
}
