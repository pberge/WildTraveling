package dev.wildtraveling.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import dev.wildtraveling.Domain.Destination;
import dev.wildtraveling.Domain.Expense;
import dev.wildtraveling.Domain.Person;
import dev.wildtraveling.Domain.Trip;
import dev.wildtraveling.R;
import dev.wildtraveling.Service.ExpenseService;
import dev.wildtraveling.Service.FourSquareAPIImpl;
import dev.wildtraveling.Service.FoursquareVenue;
import dev.wildtraveling.Service.TravelerService;
import dev.wildtraveling.Service.TripService;
import dev.wildtraveling.Util.MyLocListener;
import dev.wildtraveling.Util.RecyclerItemClickListener;
import dev.wildtraveling.Util.ServiceFactory;
import dev.wildtraveling.Util.Util;
import dev.wildtraveling.View.DestinationRecyclerView;
import dev.wildtraveling.View.ExpensesRecyclerView;
import dev.wildtraveling.View.ParticipantsRecyclerView;

public class getTripActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    private TextView initDate;
    private TextView finalDate;
    private TripService tripService;
    private TravelerService travelerService;
    private ExpenseService expenseService;
    private Handler handler;

    private View destinationRecyclerView;
    private View participantsRecyclerView;
    private Trip trip;
    private TextView total;
    private View expenseRecyclerView;
    private FloatingActionButton fab;
    private Intent intent;
    private NavigationView navigationView;
    private Integer currentFragment;
    private EditText searchLocation;
    private TextView searchQuery;
    private ImageButton food;
    private ImageButton arts;
    private ImageButton drinks;
    private ImageButton shops;
    private ImageButton outdoors;


    private FourSquareAPIImpl foursquareAPI;
    private View searchResultRecyclerView;
    private List<FoursquareVenue> venues = new ArrayList<>();
    private ProgressDialog progressDialog;
    private LatLng currentCoord = new LatLng(0.0, 0.0);
    protected GoogleApiClient mGoogleApiClient;
    private Location currentLocation;

    private String currentSearch;
    private Boolean GPS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();

        tripService = ServiceFactory.getTripService(getApplicationContext());
        travelerService = ServiceFactory.getTravelerService(getApplicationContext());
        expenseService = ServiceFactory.getExpenseService(getApplicationContext());
        foursquareAPI = ServiceFactory.getFoursquareAPI();

        trip = tripService.get(tripService.getCurrentTrip());

        intent = getIntent();

        fab = (FloatingActionButton) findViewById(R.id.fabNav);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (intent.getStringExtra("FRAGMENT").equals("INFO")) {
            navigationView.getMenu().getItem(0).setChecked(true);
            initFragment(R.layout.content_get_trip);
            initializeGeneralInfo();
        } else if (intent.getStringExtra("FRAGMENT").equals("EXPENSE")) {
            navigationView.getMenu().getItem(1).setChecked(true);
            initFragment(R.layout.content_list_expense);
            initializeExpenses();
        } else if (intent.getStringExtra("FRAGMENT").equals("SEARCH")) {
            navigationView.getMenu().getItem(2).setChecked(true);
            initFragment(R.layout.new_search_layout);
            initializeSearch();
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        //client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (currentFragment == 0) {
            Intent getTrip = new Intent(this, tripsListActivity.class);
            tripService.setCurrentTrip("");
            startActivity(getTrip);
        } else {
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
            initFragment(R.layout.new_search_layout);
            initializeSearch();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initializeSearch() {
        currentFragment = 2;
        fab.setVisibility(View.INVISIBLE);
        setTitle("Explore");

        final String query = "";
        handler = new Handler();

        progressDialog = new ProgressDialog(getTripActivity.this);
        progressDialog.setMessage("Loading...");

        Util.setNewVenues();
        venues = Util.getVenues();

        searchLocation = (EditText) findViewById(R.id.searchLocation);
        searchQuery = (TextView) findViewById(R.id.searchQuery);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        currentLocation = new Location("");
        currentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (currentLocation != null) {
            GPS = true;
            System.out.println("current location no null");
            currentCoord = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        } else { //forçar GPS
            GPS = false;
            System.out.println("current location null");
        }

        Button exploreButton = (Button) findViewById(R.id.exploreButton);
        food = (ImageButton) findViewById(R.id.foodSearchButton);
        arts = (ImageButton) findViewById(R.id.artsSearchButton);
        drinks = (ImageButton) findViewById(R.id.drinksSearchButton);
        shops = (ImageButton) findViewById(R.id.shopsSearchButton);
        outdoors = (ImageButton) findViewById(R.id.outdoorsSearchButton);

        food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentSearch = "food";
                searchQuery.setText("Food");
                setImageColor();
            }
        });
        arts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentSearch = "arts";
                searchQuery.setText("Arts");
                setImageColor();
            }
        });
        drinks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentSearch = "drinks";
                searchQuery.setText("Drinks");
                setImageColor();
            }
        });
        shops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentSearch = "shops";
                searchQuery.setText("Shops");
                setImageColor();
            }
        });
        outdoors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentSearch = "outdoors";
                searchQuery.setText("Outdoors");
                setImageColor();
            }
        });

        currentSearch = "food";
        searchQuery.setText("Food");
        setImageColor();

        exploreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                currentLocation = new Location("");
                currentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                if (currentLocation != null) {
                    GPS = true;
                    System.out.println("current location no null");
                    currentCoord = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                } else { //forçar GPS
                    GPS = false;
                    System.out.println("current location null");
                }
                if(!GPS){
                    showGPSAlertDialog().show();
                } else {
                    progressDialog.show();
                    if (searchLocation.getText().toString().equals("")) {
                        if (!currentCoord.equals(null)) {
                            Util.setCurrentRouteStart(currentCoord);
                        }
                        if (searchQuery.getText().toString().equals("")) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Select one icon", Toast.LENGTH_SHORT).show();
                        } else {
                            foursquareAPI.getVenuesCategory(currentCoord, searchQuery.getText().toString());
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        System.out.println("ESPEREM A QUE venues tingui algo");
                                        venues = new ArrayList<>();
                                        Util.setNewVenues();
                                        System.out.println("mida venues abans bucle: " + venues.size());
                                        while (Util.getVenues().size() == 0 && !Util.getFinishSearch()) {
                                            Thread.sleep(1000);
                                            venues = Util.getVenues();
                                        }
                                    } catch (InterruptedException ex) {
                                        // Catching exception
                                    } finally {
                                        if(Util.getVenues().size()>0){
                                            activitySearch();
                                        } else {
                                            //dialogNotFound().show();
                                            progressDialog.dismiss();
                                            showToast();
                                        }
                                    }
                                }
                            }).start();
                        }

                    } else {
                        LatLng coord = Util.getLocationFromAddress(searchLocation.getText().toString(), getApplicationContext());
                        Util.setCurrentRouteStart(coord);
                        if (searchQuery.getText().toString().equals("")) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Select one icon", Toast.LENGTH_SHORT).show();
                        } else {
                            foursquareAPI.getVenuesCategory(coord, searchQuery.getText().toString());
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        System.out.println("ESPEREM A QUE venues tingui algo");
                                        venues = new ArrayList<>();
                                        Util.setNewVenues();
                                        System.out.println("mida venues abans bucle: " + venues.size());
                                        while (Util.getVenues().size() == 0 && Util.getFinishSearch()) {
                                            System.out.println("bucle: " + Util.getVenues().size());
                                            Thread.sleep(1000);
                                            venues = Util.getVenues();
                                        }
                                    } catch (InterruptedException ex) {
                                        // Catching exception
                                    } finally {
                                        if(Util.getVenues().size()>0){
                                            activitySearch();
                                        } else {
                                            //dialogNotFound().show();
                                            progressDialog.dismiss();
                                            showToast();
                                        }
                                    }
                                }
                            }).start();
                        }
                    }
                }
            }
        });
    }

    private void setImageColor() {
        if(currentSearch.equals("food")){
            food.setColorFilter(getResources().getColor(R.color.colorAccent));
            drinks.setColorFilter(getResources().getColor(R.color.colorPrimary));
            arts.setColorFilter(getResources().getColor(R.color.colorPrimary));
            shops.setColorFilter(getResources().getColor(R.color.colorPrimary));
            outdoors.setColorFilter(getResources().getColor(R.color.colorPrimary));
        } else if(currentSearch.equals("drinks")){
            food.setColorFilter(getResources().getColor(R.color.colorPrimary));
            drinks.setColorFilter(getResources().getColor(R.color.colorAccent));
            arts.setColorFilter(getResources().getColor(R.color.colorPrimary));
            shops.setColorFilter(getResources().getColor(R.color.colorPrimary));
            outdoors.setColorFilter(getResources().getColor(R.color.colorPrimary));
        } else if(currentSearch.equals("arts")){
            food.setColorFilter(getResources().getColor(R.color.colorPrimary));
            drinks.setColorFilter(getResources().getColor(R.color.colorPrimary));
            arts.setColorFilter(getResources().getColor(R.color.colorAccent));
            shops.setColorFilter(getResources().getColor(R.color.colorPrimary));
            outdoors.setColorFilter(getResources().getColor(R.color.colorPrimary));
        } else if(currentSearch.equals("shops")){
            food.setColorFilter(getResources().getColor(R.color.colorPrimary));
            drinks.setColorFilter(getResources().getColor(R.color.colorPrimary));
            arts.setColorFilter(getResources().getColor(R.color.colorPrimary));
            shops.setColorFilter(getResources().getColor(R.color.colorAccent));
            outdoors.setColorFilter(getResources().getColor(R.color.colorPrimary));
        } else if(currentSearch.equals("outdoors")){
            food.setColorFilter(getResources().getColor(R.color.colorPrimary));
            drinks.setColorFilter(getResources().getColor(R.color.colorPrimary));
            arts.setColorFilter(getResources().getColor(R.color.colorPrimary));
            shops.setColorFilter(getResources().getColor(R.color.colorPrimary));
            outdoors.setColorFilter(getResources().getColor(R.color.colorAccent));
        }
    }

    private AlertDialog dialogNotFound() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("No Results");
        builder.setMessage("Try with other parameters!")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {

                    }
                });
        AlertDialog alert = builder.create();
        return alert;
    }

    public void showToast() {
        handler.post(new Runnable() {
            public void run() {
                //Toast.makeText(getApplicationContext(),
                      //  "No results found, try with other parameters.", Toast.LENGTH_SHORT).show();
                dialogNotFound().show();
            }
        });
    }

    private AlertDialog showGPSDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("GPS");
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        turnGPSOn();
                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        }
                        currentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                        currentCoord = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        return alert;
    }

    private AlertDialog showGPSAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("GPS");
        builder.setMessage("Your GPS seems to be disabled, you should enable it to continue")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {

                    }
                });
        final AlertDialog alert = builder.create();
        return alert;
    }

    private void turnGPSOn() {
        Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
        intent.putExtra("enabled", true);
        sendBroadcast(intent);
    }

    private void activitySearch() {
        Intent intent = new Intent(getApplicationContext(), searchResultActivity.class);
        startActivity(intent);
    }

    public LatLng getLocationFromAddress(String strAddress) {
        return Util.getLocationFromAddress(strAddress, getApplicationContext());
    }

    private void initializeExpenses() {
        currentFragment = 1;
        fab.setVisibility(View.VISIBLE);
        setTitle("Expenses");
        total = (TextView) findViewById(R.id.getExpenseTotalAmount);
        total.setText(expenseService.getTotalAmountByTrip(trip.getId()).toString());

        expenseRecyclerView = findViewById(R.id.expensesRecyclerView);
        ((RecyclerView) expenseRecyclerView).setLayoutManager(new LinearLayoutManager(this));

        final List<Expense> expenses = expenseService.getExpenseByTripId(tripService.getCurrentTrip());

        ExpensesRecyclerView recyclerView = new ExpensesRecyclerView(getApplicationContext(), expenses);
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
                Intent intent = new Intent(getApplicationContext(), newExpenseActivitiy.class);
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
        DestinationRecyclerView recyclerView = new DestinationRecyclerView(getApplicationContext(), destinations);
        recyclerView.notifyDataSetChanged();
        ((RecyclerView) destinationRecyclerView).setAdapter(recyclerView);

        List<Person> travelers = new ArrayList<>();
        for (String id : trip.getParticipants()) {
            if (id.equals(travelerService.getCurrentUser())) {
                travelers.add(travelerService.getUserById(id));
            } else {
                travelers.add(travelerService.getTravelerById(id));
            }
        }

        participantsRecyclerView = findViewById(R.id.getTripParticipantsRV);
        ((RecyclerView) participantsRecyclerView).setLayoutManager(new LinearLayoutManager(this));
        ParticipantsRecyclerView part = new ParticipantsRecyclerView(getApplicationContext(), travelers);
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



    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
