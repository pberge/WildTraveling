package dev.wildtraveling.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Date;


import dev.wildtraveling.Domain.Person;
import dev.wildtraveling.Domain.RegisteredTraveler;
import dev.wildtraveling.Domain.NoUser;
import dev.wildtraveling.Domain.Trip;
import dev.wildtraveling.R;
import dev.wildtraveling.Service.TravelerService;
import dev.wildtraveling.Service.TripService;
import dev.wildtraveling.Util.ServiceFactory;
import dev.wildtraveling.View.DatePickerFragment;
import dev.wildtraveling.View.ParticipantsRecyclerView;
import dev.wildtraveling.View.onDateClickListener;

public class newTripActivity extends AppCompatActivity {

    private String name;
    private String destination;
    private List<Person> participants = new ArrayList<>();
    private List<String> participantsId = new ArrayList<>();
    private ImageButton newParticipant;
    private TextView initDateView, finalDateView;
    private View participantsRecyclerView;
    private TripService tripService;
    private TravelerService travelerService;
    private EditText nameI, destinationI;
    private Date finalDate, initDate;
    private RegisteredTraveler currentUser;
    private Double travelStyle = 0.0;
    private Trip trip;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_trip);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tripService = ServiceFactory.getTripService(getApplicationContext());
        travelerService = ServiceFactory.getTravelerService(getApplicationContext());

        currentUser = travelerService.getUserById(travelerService.getCurrentUser());

        nameI = (EditText)findViewById(R.id.newTripName);
        destinationI = (EditText) findViewById(R.id.newDestination);
        destination = destinationI.getText().toString();

        initDateView = (TextView) findViewById(R.id.newInitDate);
        finalDateView = (TextView) findViewById(R.id.newFinalDate);

        ImageButton newTrip = (ImageButton) findViewById(R.id.createTrip);
        newTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newTrip();
            }
        });

        participantsRecyclerView = findViewById(R.id.newParticipantsRecyclerView);
        ((RecyclerView) participantsRecyclerView).setLayoutManager(new LinearLayoutManager(this));

        participants.add(currentUser);
        participantsId.add(currentUser.getId());
        ParticipantsRecyclerView partRecyclerView = new ParticipantsRecyclerView(getApplicationContext(),participants);
        ((RecyclerView) participantsRecyclerView).setAdapter(partRecyclerView);
        partRecyclerView.notifyDataSetChanged();

        newParticipant = (ImageButton) findViewById(R.id.addParticipantButton);
        newParticipant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetRecyclerView();
            }
        });

        initDateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v,true);
            }
        });

        finalDateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v, false);
            }
        });
    }

    private void resetRecyclerView() {
        Boolean complete = true;
        NoUser traveler = new NoUser();
        EditText n = (EditText) findViewById(R.id.newparticipantName);
        EditText e = (EditText) findViewById(R.id.newparticipantEmail);
        Boolean newTraveler = false;

        EditText na = (EditText) findViewById(R.id.newparticipantEmail);
        if(!na.getText().toString().equals("") && travelerService.existingUser(na.getText().toString())){
            traveler = travelerService.getTravelerByEmail(na.getText().toString());
            complete = true;
        }
        else if(!na.getText().toString().equals("") && travelerService.existingUser(na.getText().toString())){
            RegisteredTraveler t = travelerService.getUserByEmail(na.getText().toString());
            traveler.setName(t.getName());
            traveler.setEmail(t.getEmail());
            complete = true;
        }
        else {
            newTraveler = true;
            if(!n.getText().toString().equals("")){
                traveler.setName(n.getText().toString());
            }
            else{
                complete = false;
            }
            if (!e.getText().toString().equals("")) {
                traveler.setEmail(e.getText().toString());
            } else {
                complete = false;
            }
        }
        if(complete){
            participants.add(traveler);
            ParticipantsRecyclerView partRecyclerView = new ParticipantsRecyclerView(getApplicationContext(),participants);
            ((RecyclerView) participantsRecyclerView).setAdapter(partRecyclerView);
            partRecyclerView.notifyDataSetChanged();

            n.setText("");
            e.setText("");
            if(newTraveler){
                travelerService.save(traveler);
            }
            participantsId.add(traveler.getId());
        }
        else{
            Toast.makeText(getApplicationContext(),"Incomplete",Toast.LENGTH_SHORT).show();
        }
    }


    public void showDatePickerDialog(View v, Boolean init) {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");

        if(init){
            newFragment.setOnDateClickListener(new onDateClickListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                    Calendar ini = Calendar.getInstance();
                    ini.set(Calendar.YEAR, datePicker.getYear());
                    ini.set(Calendar.MONTH, datePicker.getMonth());
                    ini.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
                    initDate = ini.getTime();
                    initDateView.setText(datePicker.getDayOfMonth() + "/" + datePicker.getMonth() + "/" + datePicker.getYear());
                }

            });
        }
        else{
            newFragment.setOnDateClickListener(new onDateClickListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                    Calendar fin = Calendar.getInstance();
                    fin.set(Calendar.YEAR, datePicker.getYear());
                    fin.set(Calendar.MONTH, datePicker.getMonth());
                    fin.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
                    finalDate = fin.getTime();
                    finalDateView.setText(datePicker.getDayOfMonth() + "/" + datePicker.getMonth() + "/" + datePicker.getYear());
                }

            });
        }
    }

    private void newTrip(){
        Boolean complete;
        trip = new Trip();
        name = nameI.getText().toString();
        if(!name.equals("")){
            trip.setName(name);
            complete = true;
        }
        else complete = false;
        destination = destinationI.getText().toString();
        if(!destination.equals("")&&complete){
            trip.setDestination(destination);
        }
        else complete = false;
        if(!finalDateView.getText().equals("")){
            trip.setFinalDate(finalDate);
        }
        else complete = false;
        if(!initDateView.getText().equals("")){
            trip.setInitDate(initDate);
        }
        else complete = false;

        trip.setParticipants(participantsId);
        trip.setUserId(travelerService.getCurrentUser());

        if (complete){
            setPreferencesDialog().show();
        }
        else Toast.makeText(getApplicationContext(),"Incomplete",Toast.LENGTH_SHORT).show();
    }

    private Dialog setPreferencesDialog(){
        final CharSequence[] items = {"Bag packer", "Back packing poser", "Intermediate", "Luxe"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select yout traveling style");
        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                travelStyle = (double) which;
            }
        });
        builder.setCancelable(false);
        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!travelStyle.equals("")) {
                    trip.setStyle(travelStyle);
                    tripService.save(trip);
                    Intent getTrip = new Intent(getApplicationContext(), getTripActivity.class);
                    getTrip.putExtra("FRAGMENT", "INFO");
                    tripService.setCurrentTrip(trip.getId());
                    startActivity(getTrip);
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        return builder.create();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, tripsListActivity.class);
        startActivity(intent);
    }
}
