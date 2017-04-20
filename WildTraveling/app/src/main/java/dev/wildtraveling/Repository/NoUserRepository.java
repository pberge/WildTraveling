package dev.wildtraveling.Repository;

import android.content.Context;

import com.google.firebase.database.DataSnapshot;

import dev.wildtraveling.Domain.NoUser;
import dev.wildtraveling.Util.FirebaseRepository;

/**
 * Created by pere on 4/1/17.
 */
public class NoUserRepository extends FirebaseRepository<NoUser> {

    public NoUserRepository(Context context) {
        super(context);
    }

    @Override
    protected NoUser convert(DataSnapshot data) {
        if (data == null) return null;

        NoUser traveler = new NoUser();
        traveler.setId(data.getKey());

        for (DataSnapshot d : data.getChildren()) {
            if ("name".equals(d.getKey())) {
                traveler.setName(d.getValue(String.class));
            } else if ("email".equals(d.getKey())) {
                traveler.setEmail(d.getValue(String.class));
            }
        }
        return traveler;
    }

    @Override
    public String getObjectReference() {
        return "NoUser";
    }
}