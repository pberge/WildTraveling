package dev.wildtraveling.Activity;

import android.Manifest;
import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;



import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

import dev.wildtraveling.Library.AbstractRouting;
import dev.wildtraveling.Library.Route;
import dev.wildtraveling.Library.RouteException;
import dev.wildtraveling.Library.Routing;
import dev.wildtraveling.Library.RoutingListener;
import dev.wildtraveling.R;
import dev.wildtraveling.Domain.FoursquareVenue;
import dev.wildtraveling.Util.Util;

public class mapsActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    protected GoogleMap map;
    protected LatLng start;
    protected LatLng end;
    TextView starting;
    TextView destination;
    ImageButton swap;
    ImageButton car;
    ImageButton bus;
    ImageButton walk;
    TextView time;
    TextView distance;
    private AbstractRouting.TravelMode routeType = AbstractRouting.TravelMode.WALKING;
    private static final String LOG_TAG = "MyActivity";
    protected GoogleApiClient mGoogleApiClient;
    private ProgressDialog progressDialog;
    private List<Polyline> polylines;
    private static final int[] COLORS = new int[]{R.color.ruta1,R.color.ruta2,R.color.ruta3,R.color.ruta4,R.color.ruta5};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Route");
        setSupportActionBar(toolbar);
*/
        android.support.v7.app.ActionBar a = getSupportActionBar();
        a.setTitle("Route");

        polylines = new ArrayList<>();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        MapsInitializer.initialize(this);
        mGoogleApiClient.connect();

        //Get start and end
        FoursquareVenue venue = Util.getCurrentVenue();
        end = new LatLng(venue.getLatitude(),venue.getLongitute());
        start = Util.getCurrentRouteStart();

        time = (TextView) findViewById(R.id.time);
        distance = (TextView) findViewById(R.id.distance);
        starting = (TextView) findViewById(R.id.startPoint);
        destination = (TextView) findViewById(R.id.endPoint);
        swap = (ImageButton) findViewById(R.id.swap_vertical);
        car = (ImageButton) findViewById(R.id.carButton);
        bus = (ImageButton) findViewById(R.id.busButton);
        walk = (ImageButton) findViewById(R.id.walkButton);

        starting.setText(Util.getCompleteAddressString(Util.getCurrentRouteStart().latitude,Util.getCurrentRouteStart().longitude, getApplicationContext()));
        destination.setText(venue.getLocation());

        swap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String auxStart = starting.getText().toString();
                String auxEnd = destination.getText().toString();
                starting.setText(auxEnd);
                destination.setText(auxStart);
                LatLng aux1 = start;
                LatLng aux2 = end;
                start = aux2;
                end = aux1;
                sendRequest();
            }
        });

        car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                routeType = AbstractRouting.TravelMode.DRIVING;
                sendRequest();
            }
        });
        bus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                routeType = AbstractRouting.TravelMode.TRANSIT;
                sendRequest();
            }
        });
        walk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                routeType = AbstractRouting.TravelMode.WALKING;
                sendRequest();
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            getSupportFragmentManager().beginTransaction().replace(R.id.map, mapFragment).commit();
        }

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
                map.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
                    @Override
                    public void onCameraChange(CameraPosition position) {
                        LatLngBounds bounds = map.getProjection().getVisibleRegion().latLngBounds;
                    }
                });

                CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(18.013610, -77.498803));
                CameraUpdate zoom = CameraUpdateFactory.zoomTo(16);

                map.moveCamera(center);
                map.animateCamera(zoom);

                LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0, new android.location.LocationListener() {
                            @Override
                            public void onLocationChanged(Location location) {
                                CameraUpdate center = CameraUpdateFactory.newLatLng(Util.getCurrentRouteStart());
                                CameraUpdate zoom = CameraUpdateFactory.zoomTo(16);

                                map.moveCamera(center);
                                map.animateCamera(zoom);
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
                        });


                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        3000, 0, new android.location.LocationListener() {
                            @Override
                            public void onLocationChanged(Location location) {
                                CameraUpdate center = CameraUpdateFactory.newLatLng(Util.getCurrentRouteStart());
                                CameraUpdate zoom = CameraUpdateFactory.zoomTo(16);


                                map.moveCamera(center);
                                map.animateCamera(zoom);
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
                        });
                sendRequest();
            }
        });
    }
    public void sendRequest() {
        if(Util.isOnline(getApplicationContext()))
        {
            route();
        }
        else
        {
            Toast.makeText(this, "No internet connectivity", Toast.LENGTH_SHORT).show();
        }
    }

    public void route()
    {
        if(start==null || end==null)
        {
            if(start==null)
            {
                if(starting.getText().length()>0)
                {
                    starting.setError("Choose location from dropdown.");
                }
                else
                {
                    Toast.makeText(this,"Please choose a starting point.",Toast.LENGTH_SHORT).show();
                }
            }
            if(end==null)
            {
                if(destination.getText().length()>0)
                {
                    destination.setError("Choose location from dropdown.");
                }
                else
                {
                    Toast.makeText(this,"Please choose a destination.",Toast.LENGTH_SHORT).show();
                }
            }
        }
        else
        {
            progressDialog = ProgressDialog.show(this, "Please wait.",
                    "Fetching route information.", true);

            Routing routing = new Routing.Builder()
                    .travelMode(routeType)
                    .withListener(new RoutingListener() {
                        @Override
                        public void onRoutingFailure(RouteException e) {
                            progressDialog.dismiss();
                            if(e != null) {
                                Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }else {
                                Toast.makeText(getApplicationContext(), "Something went wrong, Try again", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onRoutingStart() {

                        }

                        @Override
                        public void onRoutingSuccess(List<Route> route, int shortestRouteIndex) {
                            routeTypeColor();
                            progressDialog.dismiss();
                            CameraUpdate center = CameraUpdateFactory.newLatLng(start);
                            CameraUpdate zoom = CameraUpdateFactory.zoomTo(16);

                            map.moveCamera(center);


                            if(polylines.size()>0) {
                                for (Polyline poly : polylines) {
                                    poly.remove();
                                }
                            }

                            polylines = new ArrayList<>();
                            //add route(s) to the map.
                            for (int i = 0; i <route.size(); i++) { //agafar nomes la ruta més ràpida

                                //In case of more than 5 alternative routes
                                int colorIndex = i % COLORS.length;

                                PolylineOptions polyOptions = new PolylineOptions();
                                polyOptions.color(getResources().getColor(COLORS[colorIndex]));
                                polyOptions.width(10); //10 + i * 3
                                polyOptions.addAll(route.get(i).getPoints());
                                Polyline polyline = map.addPolyline(polyOptions);
                                polylines.add(polyline);

                                time.setText(route.get(i).getDurationText());
                                distance.setText(route.get(i).getDistanceText());
                            }

                            // Start marker
                            MarkerOptions options = new MarkerOptions();
                            options.position(start);
                            options.icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker_grey));
                            map.addMarker(options);

                            // End marker
                            options = new MarkerOptions();
                            options.position(end);
                            options.icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker_orange));
                            map.addMarker(options);
                        }

                        @Override
                        public void onRoutingCancelled() {

                        }
                    })
                    .alternativeRoutes(true)
                    .waypoints(start, end)
                    .build();
            routing.execute();
        }
    }

    private void routeTypeColor() {
        if(routeType.equals(AbstractRouting.TravelMode.WALKING)){
            walk.setColorFilter(getResources().getColor(R.color.colorAccent));
            car.setColorFilter(getResources().getColor(R.color.colorPrimary));
            bus.setColorFilter(getResources().getColor(R.color.colorPrimary));
        } else if (routeType.equals(AbstractRouting.TravelMode.DRIVING)){
            walk.setColorFilter(getResources().getColor(R.color.colorPrimary));
            car.setColorFilter(getResources().getColor(R.color.colorAccent));
            bus.setColorFilter(getResources().getColor(R.color.colorPrimary));
        } else {
            walk.setColorFilter(getResources().getColor(R.color.colorPrimary));
            car.setColorFilter(getResources().getColor(R.color.colorPrimary));
            bus.setColorFilter(getResources().getColor(R.color.colorAccent));
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.v(LOG_TAG, connectionResult.toString());
    }

    @Override
    public void onConnected(Bundle bundle) {
    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}
