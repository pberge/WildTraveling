package dev.wildtraveling.Util;

import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.provider.Settings;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by pere on 4/28/17.
 */
public class MyLocListener implements LocationListener {
    @Override
    public void onLocationChanged(Location location) {
        System.out.println("Change location");
        if(location!=null){
            System.out.println("Change location no null");
            LatLng latLng = new LatLng(location.getAltitude(),location.getLongitude());
            Util.setCurrentLocation(latLng);
            System.err.println("LATITUD: "+location.getLatitude());
            System.err.println("LONGITUD: "+location.getLongitude());
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {
    }
}
