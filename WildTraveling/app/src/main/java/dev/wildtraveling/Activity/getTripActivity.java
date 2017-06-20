package dev.wildtraveling.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.location.Location;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import dev.wildtraveling.Domain.Destination;
import dev.wildtraveling.Domain.Expense;
import dev.wildtraveling.Domain.Note;
import dev.wildtraveling.Domain.Person;
import dev.wildtraveling.Domain.RegisteredTraveler;
import dev.wildtraveling.Domain.Trip;
import dev.wildtraveling.Domain.dayMeteoPrevision;
import dev.wildtraveling.R;
import dev.wildtraveling.Service.ExpenseService;
import dev.wildtraveling.Service.LocationServiceAdapter;
import dev.wildtraveling.Domain.FoursquareVenue;
import dev.wildtraveling.Service.MeteoServiceAdapter;
import dev.wildtraveling.Service.NoteService;
import dev.wildtraveling.Service.TravelerService;
import dev.wildtraveling.Service.TripService;
import dev.wildtraveling.Service.emergencyPhoneAdapter;
import dev.wildtraveling.Util.RecyclerItemClickListener;
import dev.wildtraveling.Util.ServiceFactory;
import dev.wildtraveling.Util.Util;
import dev.wildtraveling.View.DestinationRecyclerView;
import dev.wildtraveling.View.ExpensesRecyclerView;
import dev.wildtraveling.View.MeteoRecyclerView;
import dev.wildtraveling.View.NotesRecyclerView;
import dev.wildtraveling.View.ParticipantsRecyclerView;

import static dev.wildtraveling.R.id.destination;
import static dev.wildtraveling.R.id.nav_imageView;

public class getTripActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    private TextView initDate;
    private TextView finalDate;
    private TripService tripService;
    private TravelerService travelerService;
    private ExpenseService expenseService;
    private Handler handler;
    private View v;

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
    private TextView maxTemp;
    private TextView minTemp;
    private TextView wind;
    private TextView rain;
    private TextView humidity;
    private TextView meteo_desc;
    private ImageView meteo_icon;
    private TextView meteo_day;
    private List<Note> notes;

    private List<dayMeteoPrevision> meteo;
    private MeteoServiceAdapter meteoAdapter;
    private emergencyPhoneAdapter emergencyAdapter;
    private LocationServiceAdapter foursquareAPI;
    private NoteService noteService;
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

        handler = new Handler();
        meteoAdapter = new MeteoServiceAdapter();
        emergencyAdapter = new emergencyPhoneAdapter();

        tripService = ServiceFactory.getTripService(getApplicationContext());
        travelerService = ServiceFactory.getTravelerService(getApplicationContext());
        expenseService = ServiceFactory.getExpenseService(getApplicationContext());
        foursquareAPI = ServiceFactory.getFoursquareAPI();
        noteService = ServiceFactory.getNoteService(getApplicationContext());

        trip = tripService.get(tripService.getCurrentTrip());
        RegisteredTraveler user = travelerService.getUserById(travelerService.getCurrentUser());

        intent = getIntent();

        fab = (FloatingActionButton) findViewById(R.id.fabNav);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View hView =  navigationView.getHeaderView(0);

        TextView nav_name = (TextView)hView.findViewById(R.id.nav_header_title);
        nav_name.setText(user.getName());
        TextView nav_email = (TextView)hView.findViewById(R.id.nav_header_subtitle);
        nav_email.setText(user.getEmail());
        ImageView nav_photo = (ImageView) hView.findViewById(R.id.nav_imageView);

        System.out.println("user photo url: "+user.getPhotoUrl());
        Picasso.with(this).load(user.getPhotoUrl()).into(nav_photo);



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
        } else if (intent.getStringExtra("FRAGMENT").equals("METEO")) {
            navigationView.getMenu().getItem(3).setChecked(true);
            initFragment(R.layout.meteo_layout);
            initializeMeteo();
        } else if (intent.getStringExtra("FRAGMENT").equals("EMERGENCY")) {
            navigationView.getMenu().getItem(4).setChecked(true);
            initFragment(R.layout.emergency_layout);
            initializeEmergency();
        } else if (intent.getStringExtra("FRAGMENT").equals("NOTE")) {
            navigationView.getMenu().getItem(5).setChecked(true);
            initFragment(R.layout.note_list_layout);
            initializeNote();
        }
    }

    private void initializeNote() {
        currentFragment = 5;
        fab.setVisibility(View.VISIBLE);
        setTitle("Notes");

        notes = noteService.getNotesByTripId(trip.getId());

        View notesRecyclerView = findViewById(R.id.note_recyclerview);
        ((RecyclerView) notesRecyclerView).setLayoutManager(new LinearLayoutManager(this));
        NotesRecyclerView recyclerView = new NotesRecyclerView(getApplicationContext(), notes);
        ((RecyclerView) notesRecyclerView).setAdapter(recyclerView);
        recyclerView.notifyDataSetChanged();
        ((RecyclerView) notesRecyclerView).addOnItemTouchListener(new RecyclerItemClickListener(this, (RecyclerView) notesRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getApplicationContext(), getNoteActivity.class);
                intent.putExtra("noteId", notes.get(position).getId());
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
                Intent intent = new Intent(getApplicationContext(), newNoteActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getMeteo() {
        currentFragment = 3;
        fab.setVisibility(View.INVISIBLE);
        setTitle("Meteo");
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
        if (!GPS) {
            showGPSAlertDialog().show();
        } else {
            progressDialog = new ProgressDialog(getTripActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
            new getMeteo().execute();
        }
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
        getMenuInflater().inflate(R.menu.menu_get_trip, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.deleteTrip) {
            deleteDialog().show();
            return true;        }

        return super.onOptionsItemSelected(item);
    }

    private AlertDialog deleteDialog() {
        AlertDialog dialog =new AlertDialog.Builder(this)
                .setTitle("Delete Trip")
                .setMessage("Do you want to delete this trip?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        noteService.deleteNotesByTripId(tripService.getCurrentTrip());
                        expenseService.deleteExpensesByTrip(tripService.getCurrentTrip());
                        tripService.deleteTrip(tripService.getCurrentTrip());
                        Intent intent = new Intent(getTripActivity.this, tripsListActivity.class);
                        tripService.setCurrentTrip("");
                        startActivity(intent);
                        getTripActivity.this.finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        return dialog;
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
        } else if (id == R.id.meteo) {
            getMeteo();
        } else if (id == R.id.emergency) {
            initFragment(R.layout.emergency_layout);
            initializeEmergency();
        } else if (id == R.id.note){
            initFragment(R.layout.note_list_layout);
            initializeNote();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initializeEmergency() {
        currentFragment = 4;
        fab.setVisibility(View.INVISIBLE);
        setTitle("Emergencies");

        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        int halfScreen = Util.getHalfScreenWidht(size);
        LinearLayout a = (LinearLayout) findViewById(R.id.emergency_call_layout);
        a.setMinimumWidth(halfScreen);

        ImageButton hospital = (ImageButton) findViewById(R.id.hospital_route_button);
        ImageButton emergencyCall = (ImageButton) findViewById(R.id.emergency_call_button);
        ImageButton contactCall = (ImageButton) findViewById(R.id.contact_call_button);

        hospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hospitalRoute();
            }
        });
        emergencyCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emergCall();
            }
        });
        contactCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contctCall();
            }
        });
    }

    private void contctCall() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        Person person = travelerService.getPersonById(trip.getContactPerson());
        callIntent.setData(Uri.parse("tel:"+person.getPhone()));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {}
        startActivity(callIntent);
    }

    private void emergCall() { //contactar amb la api
        new getEmergencyPhone().execute();
    }

    private void call(String num) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        System.out.println("telefon a trucar: " + num);
        callIntent.setData(Uri.parse("tel:"+num));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {}
        //startActivity(callIntent);
    }

    private void hospitalRoute() { //api i iniciar activity route
        /*  1. fer la cerca de l'hospital
            2. obtenir la ruta
            3. anar a l'activitat de mapa
         */
        progressDialog = new ProgressDialog(getTripActivity.this);
        progressDialog.setMessage("Loading...");

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
        progressDialog.show();
        Util.setCurrentRouteStart(currentCoord);

        foursquareAPI.getHospital(currentCoord);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    venues = new ArrayList<>();
                    Util.setNewVenues();
                    while (Util.getVenues().size() == 0 && !Util.getFinishSearch()) {
                        Thread.sleep(1000);
                        venues = Util.getVenues();
                    }
                } catch (InterruptedException ex) {
                    // Catching exception
                } finally {
                    if(Util.getVenues().size()>0){
                        FoursquareVenue v = getNearestHospital();
                        Util.setCurrentVenue(v);
                        activityMaps();
                        progressDialog.dismiss();
                    } else {
                        progressDialog.dismiss();
                        showToast();
                    }
                }
            }
        }).start();
    }

    private FoursquareVenue getNearestHospital() {
        FoursquareVenue res = new FoursquareVenue();
        res.setDistance(99999999);
        for(FoursquareVenue v : Util.getVenues()){
            if(v.getDistance()<res.getDistance()){
                res = v;
            }
        }
        return res;
    }


    private void initializeSearch() {
        currentFragment = 2;
        fab.setVisibility(View.INVISIBLE);
        setTitle("Explore");

        final String query = "";

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
                        System.out.println("current coord: "+currentCoord.toString());
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
                                        venues = new ArrayList<>();
                                        Util.setNewVenues();
                                        Util.setFinishSearch(false);
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
                                        venues = new ArrayList<>();
                                        Util.setNewVenues();
                                        while (Util.getVenues().size() == 0 && Util.getFinishSearch()) {
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

    private void activityMaps() {
        Intent intent = new Intent(getApplicationContext(), mapsActivity.class);
        startActivity(intent);
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
        setTitle("Trip");
        currentFragment = 0;
        fab.setVisibility(View.INVISIBLE);
        initDate = (TextView) findViewById(R.id.getTripInitDate);
        finalDate = (TextView) findViewById(R.id.getTripFinalDate);
        //Set dates
        initDate.setText(Util.obtainDateString(trip.getInitDate()));
        finalDate.setText(Util.obtainDateString(trip.getFinalDate()));

        TextView desti = (TextView) findViewById(destination);

        //Crea llista de destins (provisional)
        /*List<Destination> destinations = new ArrayList<>();
        Destination d = new Destination();
        d.setName(trip.getDestination());
        destinations.add(d);

        destinationRecyclerView = findViewById(R.id.getTripDestinationRV);
        ((RecyclerView) destinationRecyclerView).setLayoutManager(new LinearLayoutManager(this));
        DestinationRecyclerView recyclerView = new DestinationRecyclerView(getApplicationContext(), destinations);
        recyclerView.notifyDataSetChanged();
        ((RecyclerView) destinationRecyclerView).setAdapter(recyclerView);*/

        desti.setText(trip.getDestination());

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

    private void initializeMeteo() {
        currentFragment = 3;
        fab.setVisibility(View.INVISIBLE);
        setTitle("Meteo");

        meteo_day = (TextView) findViewById(R.id.meteo_day);
        maxTemp = (TextView) findViewById(R.id.meteo_maxTemp);
        minTemp = (TextView) findViewById(R.id.meteo_minTemp);
        wind = (TextView) findViewById(R.id.meteo_wind);
        humidity = (TextView) findViewById(R.id.meteo_humidity);
        meteo_desc = (TextView) findViewById(R.id.meteo_weather);
        meteo_icon = (ImageView) findViewById(R.id.meteo_icon);

        Calendar c = Calendar.getInstance();
        Integer day = c.get(Calendar.DAY_OF_MONTH);
        Integer month = c.get(Calendar.MONTH);

        meteo_day.setText(day + " " + Util.getMonthName("" + month));
        minTemp.setText(meteo.get(0).getTemp_min().toString());
        maxTemp.setText(meteo.get(0).getTemp_max().toString());
        wind.setText(meteo.get(0).getWind_speed().toString());
        humidity.setText(meteo.get(0).getHumidity().toString());
        meteo_desc.setText(meteo.get(0).getWeather_type());
        meteo_icon.setImageResource(meteoAdapter.setMeteoIcon(meteo.get(0).getWeather_type()));

        List<dayMeteoPrevision> rview = meteo;
        dayMeteoPrevision a = rview.remove(0); //remove current day

        View meteoRecyclerView = findViewById(R.id.meteo_recyclerview);
        ((RecyclerView) meteoRecyclerView).setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        MeteoRecyclerView part = new MeteoRecyclerView(getApplicationContext(), rview);
        part.notifyDataSetChanged();
        ((RecyclerView) meteoRecyclerView).setAdapter(part);

    }

    private void initFragment(int id) {
        LayoutInflater inflater = getLayoutInflater();
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

    private class getMeteo extends AsyncTask<View, Void, String> {


        List<dayMeteoPrevision> m;

        @Override
        protected String doInBackground(View... urls) {
            // make Call to the url
            m = meteoAdapter.getForecast(currentCoord);
            return "";
        }

        @Override
        protected void onPreExecute() {
            // we can start a progress bar here
        }

        @Override
        protected void onPostExecute(String result) {
            if (m == null) {
                // we have an error to the call
                // we can also stop the progress bar
                System.out.println("METEO ES NULL a getTRipActivity");
            } else {
                // all things went right
                // parseFoursquare venues search result
                navigationView.getMenu().getItem(3).setChecked(true);
                initFragment(R.layout.meteo_layout);
                meteo = m;
                System.out.println("El temps que fa avui es: "+meteo.get(0).getWeather_description());
                initializeMeteo();
                progressDialog.dismiss();

            }
        }
    }

    private class getEmergencyPhone extends AsyncTask<View, Void, String> {


        String phone = "";

        @Override
        protected String doInBackground(View... urls) {
            // make Call to the url
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            }
            currentLocation = new Location("");
            currentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            String code = Util.getCountryCode(currentLocation.getLatitude(),currentLocation.getLongitude(),getApplicationContext());
            phone = emergencyAdapter.getEmergencyPhone(currentCoord, code);
            return "";
        }

        @Override
        protected void onPreExecute() {
            // we can start a progress bar here
        }

        @Override
        protected void onPostExecute(String result) {
            if (phone.equals("")) {
                // we have an error to the call
                // we can also stop the progress bar
                System.out.println("phone ES NULL a getTRipActivity");
            } else {
                // INIT CALL ACTIVITY
                System.out.println("phone NO ES NULL a getTRipActivity");
                call(phone);
            }
        }
    }
}
