package dev.wildtraveling.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import dev.wildtraveling.R;
import dev.wildtraveling.Service.FoursquareVenue;
import dev.wildtraveling.Util.Util;
import dev.wildtraveling.View.SearchResultRecyclerView;

public class searchResultActivity extends AppCompatActivity {

    private View searchResultRecyclerView;
    private List<FoursquareVenue> venues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        searchResultRecyclerView = findViewById(R.id.searchResultRecyclerView);

        venues = Util.getVenues();

        SearchResultRecyclerView recyclerView = new SearchResultRecyclerView(getApplicationContext(),venues);
        ((RecyclerView) searchResultRecyclerView).setLayoutManager(new LinearLayoutManager(this));
        ((RecyclerView) searchResultRecyclerView).setAdapter(recyclerView);
        recyclerView.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(),getTripActivity.class);
        intent.putExtra("FRAGMENT","SEARCH");
        startActivity(intent);
    }
}
