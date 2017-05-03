package dev.wildtraveling.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import dev.wildtraveling.Domain.Trip;
import dev.wildtraveling.R;
import dev.wildtraveling.Service.TravelerService;
import dev.wildtraveling.Service.TripService;
import dev.wildtraveling.Util.RecyclerItemClickListener;
import dev.wildtraveling.Util.ServiceFactory;
import dev.wildtraveling.View.TripsListRecyclerView;

public class tripsListActivity extends AppCompatActivity {

    private Context context ;
    private TripService tripService;
    private TravelerService travelerService;
    private List<Trip> trips = new ArrayList<>();
    private View tripsListRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trips_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = this;

        tripService = ServiceFactory.getTripService(this);
        travelerService = ServiceFactory.getTravelerService(this);

        trips = tripService.getTripsByUser(travelerService.getCurrentUser());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, newTripActivity.class);
                startActivity(intent);
            }
        });

        tripsListRecyclerView = findViewById(R.id.tripsListRecyclerView);
        ((RecyclerView) tripsListRecyclerView).setLayoutManager(new LinearLayoutManager(this));

        TripsListRecyclerView recyclerView = new TripsListRecyclerView(getApplicationContext(),trips);
        ((RecyclerView) tripsListRecyclerView).setAdapter(recyclerView);

        ((RecyclerView) tripsListRecyclerView).addOnItemTouchListener(new RecyclerItemClickListener(context, (RecyclerView) tripsListRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(context, getTripActivity.class);
                intent.putExtra("FRAGMENT","INFO");
                tripService.setCurrentTrip(trips.get(position).getId());
                startActivity(intent);
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

    }
}
