package dev.wildtraveling.Service;

import java.util.ArrayList;
import java.util.List;

import dev.wildtraveling.Domain.Trip;
import dev.wildtraveling.Util.Repository;
import dev.wildtraveling.Util.Service;

/**
 * Created by pere on 3/31/17.
 */
public class TripService extends Service<Trip> {

    private int loaded=0;
    private int loadNeed=1;
    private String currentTrip;

    public TripService(Repository<Trip>repository){
        super(repository);
    }

    public Trip save(Trip item){
            if(repository==null){
            throw new UnsupportedOperationException("Service needs to be created with the DishRepository to function");
            }
            Trip trip=repository.insert(item);
            return trip;
    }

    public List<Trip> getTripsByUser(String currentUser) {
        List<Trip> trips = new ArrayList<>();
        for(Trip t: repository.all()){
            if(t.getUserId().equals(currentUser)){
                trips.add(t);
            }
        }
        return trips;
    }

    public String getCurrentTrip() {
        return currentTrip;
    }

    public void setCurrentTrip(String currentTrip) {
        this.currentTrip = currentTrip;
    }

    public Trip getTripById(String currentTrip) {
        for (Trip t: repository.all()){
            if(t.getId().equals(currentTrip)){
                return t;
            }
        }
        return null;
    }
}