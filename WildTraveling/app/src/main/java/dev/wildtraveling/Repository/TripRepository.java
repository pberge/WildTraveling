package dev.wildtraveling.Repository;

import android.content.Context;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;


import dev.wildtraveling.Domain.Trip;
import dev.wildtraveling.Util.FirebaseRepository;

/**
 * Created by pere on 3/31/17.
 */
public class TripRepository extends FirebaseRepository<Trip> {

    public TripRepository(Context context) {
        super(context);
    }

    @Override
    protected Trip convert(DataSnapshot data) {
        if (data == null) return null;

        Trip trip = new Trip();
        trip.setId(data.getKey());

        for (DataSnapshot d : data.getChildren()) {
            if ("name".equals(d.getKey())) {
                trip.setName(d.getValue(String.class));
            }else if ("destination".equals(d.getKey())) {
                trip.setDestination(d.getValue(String.class));
            }else if (d.getKey().equals("participants")) {
                List<String> participants = new ArrayList<>();
                for (DataSnapshot traveler : d.getChildren()) {
                    participants.add(traveler.getValue().toString());
                }
                trip.setParticipants(participants);
            }
            else if(d.getKey().equals("initDate")){
                trip.setInitDate(d.getValue(Date.class));
            }
            else if(d.getKey().equals("finalDate")){
                trip.setFinalDate(d.getValue(Date.class));
            }
            else if(d.getKey().equals("userId")){
                trip.setUserId(d.getValue(String.class));
            }

        }
        return trip;
    }

    @Override
    public String getObjectReference() {
        return "Trip";
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
