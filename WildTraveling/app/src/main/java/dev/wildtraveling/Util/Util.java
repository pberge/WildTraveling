package dev.wildtraveling.Util;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import dev.wildtraveling.Service.FoursquareVenue;

/**
 * Created by pere on 4/12/17.
 */
public final class Util {

    private static Integer loaded = 0;
    private static List<FoursquareVenue> venues = new ArrayList<>();
    private static LatLng currentLocation;

    public static String obtainDateString(Date date) {
        String result = "";
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        if (calendar.get(Calendar.DAY_OF_MONTH) < 10) day = "0" + day;
        if (calendar.get(Calendar.MONTH) < 10) month = "0" + month;
        result += " " + day + "/" + month + "/" + year + "\n";

        return result;
    }

    public static Integer getLoaded() { return loaded; }

    public static void increaseLoaded() { ++loaded; }

    public static LatLng getLocationFromAddress(String address, Context context) {
        Geocoder coder = new Geocoder(context);
        List<Address> results = null;
        LatLng coordinates = null;
        try {
            try {
                results = coder.getFromLocationName(address, 5);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (results == null) {
                return null;
            }
            Address location = results.get(0);
            coordinates = new LatLng(location.getLatitude(), location.getLongitude());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return coordinates;
    }

    public static List<FoursquareVenue> getVenues() {
        return venues;
    }

    public static void setVenues(List<FoursquareVenue> venues) {
        Util.venues = venues;
    }

    public static LatLng getCurrentLocation() {
        return currentLocation;
    }

    public static void setCurrentLocation(LatLng currentLocation) {
        Util.currentLocation = currentLocation;
    }

    public static List<FoursquareVenue> sortVenues(int key) {
        List<FoursquareVenue> a = new ArrayList<>();
        List<FoursquareVenue> b = new ArrayList<>();
        List<FoursquareVenue> c = new ArrayList<>();
        List<FoursquareVenue> d = new ArrayList<>();
        List<FoursquareVenue> nulls = new ArrayList<>();

        for(FoursquareVenue v : venues){
            if(v.getPriceRank()==1){
                a.add(v);
            } else if(v.getPriceRank()==2){
                b.add(v);
            } else if(v.getPriceRank()==3){
                c.add(v);
            } else if(v.getPriceRank()==4){
                d.add(v);
            } else if(v.getPriceRank()==0){
                nulls.add(v);
            }
        }

        List<FoursquareVenue> res = new ArrayList<>();
        res.addAll(a);
        res.addAll(b);
        res.addAll(c);
        res.addAll(d);
        res.addAll(nulls);

        return res;
    }
}
