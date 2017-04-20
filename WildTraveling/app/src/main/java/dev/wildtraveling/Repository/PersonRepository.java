package dev.wildtraveling.Repository;

import android.content.Context;

import com.google.firebase.database.DataSnapshot;

import dev.wildtraveling.Domain.Person;
import dev.wildtraveling.Util.FirebaseRepository;

/**
 * Created by pere on 4/10/17.
 */
public class PersonRepository extends FirebaseRepository<Person> {

    public PersonRepository(Context context) {
        super(context);
    }

    @Override
    protected Person convert(DataSnapshot data) {
        if (data == null) return null;

        Person traveler = new Person();
        traveler.setId(data.getKey());

        for (DataSnapshot d : data.getChildren()) {
            if ("name".equals(d.getKey())) {
                traveler.setName(d.getValue(String.class));
            } else if ("phone".equals(d.getKey())) {
                traveler.setPhone(d.getValue(String.class));
            }
        }
        return traveler;
    }

    @Override
    public String getObjectReference() {
        return "Person";
    }
}
