package dev.wildtraveling.Util;

import android.content.Context;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import dev.wildtraveling.Domain.FoursquareVenue;
import dev.wildtraveling.Domain.dayMeteoPrevision;

/**
 * Created by pere on 4/12/17.
 */
public final class Util {

    public static final String EMAIL = "pereberge@gmail.com";
    public static final String PASSWORD = "eslokete";
    private static Integer loaded = 0;
    private static List<FoursquareVenue> venues = new ArrayList<>();
    private static LatLng currentLocation;
    private static LatLng currentRouteStart;
    private static FoursquareVenue currentVenue;
    private static Boolean finishSearch = false;

    public static dayMeteoPrevision getMeteo() {
        return meteo;
    }

    public static void setMeteo(dayMeteoPrevision meteo) {
        Util.meteo = meteo;
    }

    private static dayMeteoPrevision meteo;

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

    public static List<FoursquareVenue> sortVenues(Double key) {
        List<FoursquareVenue> a = new ArrayList<>();
        List<FoursquareVenue> b = new ArrayList<>();
        List<FoursquareVenue> c = new ArrayList<>();
        List<FoursquareVenue> d = new ArrayList<>();
        List<FoursquareVenue> nulls = new ArrayList<>();

        for(FoursquareVenue v : venues){
            if(v.getPriceRank()==1 || v.getPriceRank() == 1.0){
                a.add(v);
            } else if(v.getPriceRank()==2 || v.getPriceRank() == 2.0){
                b.add(v);
            } else if(v.getPriceRank()==3 || v.getPriceRank() == 3.0){
                c.add(v);
            } else if(v.getPriceRank()==4 || v.getPriceRank() == 4.0){
                d.add(v);
            } else if(v.getPriceRank()==0 || v.getPriceRank() == 0.0){
                nulls.add(v);
            }
        }
        List<FoursquareVenue> res = new ArrayList<>();

        if(key == 0.0 || key == 0){ //bag packer
            res.addAll(a);
            res.addAll(b);
            res.addAll(c);
            res.addAll(d);
            res.addAll(nulls);
        } else if(key == 1.0 || key == 1){ //poser
            res.addAll(b);
            res.addAll(a);
            res.addAll(c);
            res.addAll(d);
            res.addAll(nulls);
        } else if(key == 2.0 || key == 2){ //intermediate
            res.addAll(c);
            res.addAll(b);
            res.addAll(d);
            res.addAll(a);
            res.addAll(nulls);
        } else if(key == 3.0 || key == 3){ //luxury
            res.addAll(d);
            res.addAll(c);
            res.addAll(b);
            res.addAll(a);
            res.addAll(nulls);
        }
        return res;
    }

    public static void setNewVenues() {
        venues.clear();
        venues = new ArrayList<>();
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    public static FoursquareVenue getCurrentVenue() {
        return currentVenue;
    }

    public static void setCurrentVenue(FoursquareVenue currentVenue) {
        Util.currentVenue = currentVenue;
    }

    public static LatLng getCurrentRouteStart() {
        return currentRouteStart;
    }

    public static void setCurrentRouteStart(LatLng currentRouteStart) {
        Util.currentRouteStart = currentRouteStart;
    }


    public static Boolean getFinishSearch() {
        return finishSearch;
    }

    public static void setFinishSearch(Boolean finishSearch) {
        Util.finishSearch = finishSearch;
    }

    public static String getCompleteAddressString(double LATITUDE, double LONGITUDE, Context context) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strAdd;
    }

    public static String getCountryCode(double latitude, double longitude, Context context){
        String strAdd = "";
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                strAdd = returnedAddress.getCountryCode();
                System.out.println("codi del pais: "+strAdd);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strAdd;
    }


    public static String getMonthName(String month) {
        int m = Integer.parseInt(month);
        String[] months = {"Jan", "Feb", "Mar", "Apl", "May", "Jun", "Jul", "Aug", "Sep", "Oct","Nov","Dec"};
        return months[m];
    }

    public static Integer getHalfScreenWidht(Point size){
        int screenWidth = size.x;
        int screenHeight = size.y;
        int halfScreenWidth = (int)(screenWidth *0.5);
        int quarterScreenWidth = (int)(halfScreenWidth * 0.5);

        return halfScreenWidth;
    }
}
