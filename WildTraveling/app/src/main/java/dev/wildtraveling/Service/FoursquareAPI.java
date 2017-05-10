package dev.wildtraveling.Service;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import dev.wildtraveling.Domain.FoursquareVenue;

/**
 * Created by pere on 4/24/17.
 */
public interface FoursquareAPI {

    List<FoursquareVenue> generateVenuesFromCity(double latitude, double longitude);

    List<FoursquareVenue> getCurrentVenues();

    List<FoursquareVenue> getVenuesCategory(LatLng coord, String category);

}