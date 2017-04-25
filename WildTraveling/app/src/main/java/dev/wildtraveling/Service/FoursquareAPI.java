package dev.wildtraveling.Service;

import java.util.List;

/**
 * Created by pere on 4/24/17.
 */
public interface FoursquareAPI {

    List<FoursquareVenue> generateVenuesFromCity(double latitude, double longitude);

    List<FoursquareVenue> getCurrentVenues();

}