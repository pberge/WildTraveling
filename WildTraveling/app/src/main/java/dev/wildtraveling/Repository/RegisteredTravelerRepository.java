package dev.wildtraveling.Repository;

import android.content.Context;
import android.net.Uri;

import com.google.firebase.database.DataSnapshot;

import dev.wildtraveling.Domain.Person;
import dev.wildtraveling.Domain.RegisteredTraveler;
import dev.wildtraveling.Util.FirebaseRepository;

/**
 * Created by pere on 4/10/17.
 */
public class RegisteredTravelerRepository extends FirebaseRepository<RegisteredTraveler> {

public RegisteredTravelerRepository(Context context) {
        super(context);
        }

@Override
protected RegisteredTraveler convert(DataSnapshot data) {
        if (data == null) return null;

        RegisteredTraveler traveler = new RegisteredTraveler();
        traveler.setId(data.getKey());

        for (DataSnapshot d : data.getChildren()) {
                if ("name".equals(d.getKey())) {
                traveler.setName(d.getValue(String.class));
                } else if ("email".equals(d.getKey())) {
                        traveler.setEmail(d.getValue(String.class));
                }else if ("emergencyContact".equals(d.getKey())) {
                        traveler.setEmergencyContact(d.getValue(String.class));
                }
        }
        return traveler;
        }

@Override
public String getObjectReference() {
        return "RegisteredTraveler";
        }
        }