package dev.wildtraveling.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import dev.wildtraveling.Domain.Destination;
import dev.wildtraveling.Domain.Expense;
import dev.wildtraveling.Domain.Person;
import dev.wildtraveling.Domain.Trip;
import dev.wildtraveling.R;
import dev.wildtraveling.Service.ExpenseService;
import dev.wildtraveling.Service.TravelerService;
import dev.wildtraveling.Service.TripService;
import dev.wildtraveling.Util.RecyclerItemClickListener;
import dev.wildtraveling.Util.ServiceFactory;
import dev.wildtraveling.Util.Util;
import dev.wildtraveling.View.DestinationRecyclerView;
import dev.wildtraveling.View.ExpensesRecyclerView;
import dev.wildtraveling.View.ParticipantsRecyclerView;

public class getTripActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView initDate;
    private TextView finalDate;
    private TripService tripService;
    private TravelerService travelerService;
    private ExpenseService expenseService;

    private View destinationRecyclerView;
    private View participantsRecyclerView;
    private Trip trip;
    private TextView total;
    private View expenseRecyclerView;
    private FloatingActionButton fab;
    private Intent intent;
    private NavigationView navigationView;
    private Integer currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tripService = ServiceFactory.getTripService(getApplicationContext());
        travelerService = ServiceFactory.getTravelerService(getApplicationContext());
        expenseService = ServiceFactory.getExpenseService(getApplicationContext());
        trip = tripService.get(tripService.getCurrentTrip());

        intent = getIntent();

        fab = (FloatingActionButton) findViewById(R.id.fab);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if(intent.getStringExtra("FRAGMENT").equals("INFO")) {
            navigationView.getMenu().getItem(0).setChecked(true);
            initFragment(R.layout.content_get_trip);
            initializeGeneralInfo();
        }
        else if(intent.getStringExtra("FRAGMENT").equals("EXPENSE")){
            navigationView.getMenu().getItem(1).setChecked(true);
            initFragment(R.layout.content_list_expense);
            initializeExpenses();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if(currentFragment == 0) {
            Intent getTrip = new Intent(this, tripsListActivity.class);
            tripService.setCurrentTrip("");
            startActivity(getTrip);
        } else{
            navigationView.getMenu().getItem(0).setChecked(true);
            initFragment(R.layout.content_get_trip);
            initializeGeneralInfo();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.trip, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.info) {
            initFragment(R.layout.content_get_trip);
            initializeGeneralInfo();
        } else if (id == R.id.money) {
            initFragment(R.layout.content_list_expense);
            initializeExpenses();
        } else if (id == R.id.search) {

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initializeExpenses() {
        currentFragment = 1;
        fab.setVisibility(View.VISIBLE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(),newExpenseActivitiy.class);
                startActivity(intent1);
            }
        });
        setTitle("Expenses");
        total = (TextView) findViewById(R.id.getExpenseTotalAmount);
        total.setText(expenseService.getTotalAmountByTrip(trip.getId()).toString());

        expenseRecyclerView = findViewById(R.id.expensesRecyclerView);
        ((RecyclerView) expenseRecyclerView).setLayoutManager(new LinearLayoutManager(this));

        final List<Expense> expenses = expenseService.getExpenseByTripId(tripService.getCurrentTrip());

        ExpensesRecyclerView recyclerView = new ExpensesRecyclerView(getApplicationContext(),expenses);
        ((RecyclerView) expenseRecyclerView).setAdapter(recyclerView);
        recyclerView.notifyDataSetChanged();
        ((RecyclerView) expenseRecyclerView).addOnItemTouchListener(new RecyclerItemClickListener(this, (RecyclerView) expenseRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getApplicationContext(), getExpenseActivity.class);
                intent.putExtra("expenseId", expenses.get(position).getId());
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) { //Delete dialog
                // ...
            }
        }));
        fab.setImageResource(R.drawable.plus);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), newTripActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initializeGeneralInfo() {
        currentFragment = 0;
        fab.setVisibility(View.INVISIBLE);
        initDate = (TextView) findViewById(R.id.getTripInitDate);
        finalDate = (TextView) findViewById(R.id.getTripFinalDate);
        //Set dates
        initDate.setText(Util.obtainDateString(trip.getInitDate()));
        finalDate.setText(Util.obtainDateString(trip.getFinalDate()));

        //Crea llista de destins (provisional)
        List<Destination> destinations = new ArrayList<>();
        Destination d = new Destination();
        d.setName(trip.getDestination());
        destinations.add(d);

        destinationRecyclerView = findViewById(R.id.getTripDestinationRV);
        ((RecyclerView) destinationRecyclerView).setLayoutManager(new LinearLayoutManager(this));
        DestinationRecyclerView recyclerView = new DestinationRecyclerView(getApplicationContext(),destinations);
        recyclerView.notifyDataSetChanged();
        ((RecyclerView) destinationRecyclerView).setAdapter(recyclerView);

        List<Person> travelers = new ArrayList<>();
        for(String id: trip.getParticipants()){
            if(id.equals(travelerService.getCurrentUser())){
                travelers.add(travelerService.getUserById(id));
            }else {
                travelers.add(travelerService.getTravelerById(id));
            }
        }

        participantsRecyclerView = findViewById(R.id.getTripParticipantsRV);
        ((RecyclerView) participantsRecyclerView).setLayoutManager(new LinearLayoutManager(this));
        ParticipantsRecyclerView part = new ParticipantsRecyclerView(getApplicationContext(),travelers);
        part.notifyDataSetChanged();
        ((RecyclerView) participantsRecyclerView).setAdapter(part);
    }

    private void initFragment(int id) {
        LayoutInflater inflater = getLayoutInflater();
        View v;
        if (id != -1) v = inflater.inflate(id, null);
        else v = new View(getApplicationContext());
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.main_frame_layout);
        frameLayout.removeAllViews();
        frameLayout.addView(v);
    }
}
