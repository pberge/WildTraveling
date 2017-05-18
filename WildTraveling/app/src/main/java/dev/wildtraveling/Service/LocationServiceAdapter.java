package dev.wildtraveling.Service;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;

import com.google.android.gms.maps.model.LatLng;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import dev.wildtraveling.Domain.FoursquareVenue;
import dev.wildtraveling.Util.Util;

/**
 * Created by pere on 4/24/17.
 */
public class LocationServiceAdapter implements FoursquareAPI {

    private final String CLIENT_ID = "XPAG4JS3CS4TB3T3JNZCIC3JXRUISI1BKORJLB1WKLCLR2P4";
    private final String CLIENT_SECRET = "MS3FNETWTKVVBEVGYSFYSWHHILBXUFACU1ONJBCGM0S5LNOJ";
    private final String v="20170428";

    private String latitude;
    private String longitude;

    private String currentSearch;
    private Boolean finish = false;

    private List<FoursquareVenue> venueList = new ArrayList<>();
    private String temp;

    private Context context;

    public void setContext(Context context){
        this.context = context;
    }


    @Override
    public List<FoursquareVenue> generateVenuesFromCity(double latitude, double longitude) {
        this.latitude = String.valueOf(latitude);
        this.longitude = String.valueOf(longitude);
        new FoursquareCall().execute();
        this.currentSearch = "no";
        return venueList;
    }

    @Override
    public List<FoursquareVenue> getVenuesCategory(LatLng coord, String category) {
        this.latitude = String.valueOf(coord.latitude);
        this.longitude = String.valueOf(coord.longitude);
        this.currentSearch = category;
        new FoursquareCall().execute();
        Util.setVenues(venueList);
        System.out.println("Mida venue list despres de getVenuesCategory: " + venueList.size());
        Util.setFinishSearch(true);
        return venueList;
    }

    @Override
    public List<FoursquareVenue> getCurrentVenues() {
        return venueList;
    }

    public Boolean getStatus() {
        if(finish) return true;
        return false;
    }

    public List<FoursquareVenue> getVenuesFromQuery(LatLng coord, String museum) {
        return null;
    }

    public List<FoursquareVenue> getHospital(LatLng coord) {
        this.latitude = String.valueOf(coord.latitude);
        this.longitude = String.valueOf(coord.longitude);
        venueList = new ArrayList<>();
        Util.setFinishSearch(false);
        new HospitalCall().execute();
        Util.setVenues(venueList);
        return venueList;
    }

    private class HospitalCall extends AsyncTask<View, Void, String> {


        @Override
        protected String doInBackground(View... urls) {
            // make Call to the url
            temp = makeCall("https://api.foursquare.com/v2/venues/search?"+
                    "&client_id=" + CLIENT_ID +
                    "&client_secret=" + CLIENT_SECRET +
                    "&ll=" + latitude + "," + longitude+
                    "&v="+v+
                    "&query=hospital"+
                    "&oauth_token=DBRXKVUS1HG52CRZK5ERIKQ42BP4UPL11Q2N0PQUCCZZ3A4R");
            return "";
        }

        @Override
        protected void onPreExecute() {
            // we can start a progress bar here
        }

        @Override
        protected void onPostExecute(String result) {
            if (temp == null) {
                // we have an error to the call
                // we can also stop the progress bar
                finish = true;
                System.out.println("TEMP ES NULL");
            } else {
                // all things went right
                // parseFoursquare venues search result
                venueList = parseFoursquareSearch(temp);
                Util.setVenues(venueList);
                Util.setFinishSearch(true);
                finish = true;
            }
        }
    }

    private class FoursquareCall extends AsyncTask<View, Void, String> {


        @Override
        protected String doInBackground(View... urls) {
            // make Call to the url
            if(!currentSearch.equals("no")) {
                temp = makeCall("https://api.foursquare.com/v2/venues/explore?"+
                        "&client_id=" + CLIENT_ID +
                        "&client_secret=" + CLIENT_SECRET +
                        "&ll=" + latitude + "," + longitude+
                        "&v="+v+
                        "&section="+currentSearch+
                        "&oauth_token=DBRXKVUS1HG52CRZK5ERIKQ42BP4UPL11Q2N0PQUCCZZ3A4R");
                return "";
            }else{
                System.out.println("Entra a cerca per coordenades solament");
                temp = makeCall("https://api.foursquare.com/v2/venues/explore?client_id=" + CLIENT_ID +
                        "&client_secret=" + CLIENT_SECRET + "&v="+v+"&ll=" + latitude + "," + longitude);
                return "";
            }
        }

        @Override
        protected void onPreExecute() {
            // we can start a progress bar here
        }

        @Override
        protected void onPostExecute(String result) {
            if (temp == null) {
                // we have an error to the call
                // we can also stop the progress bar
                finish = true;
                System.out.println("TEMP ES NULL");
            } else {
                // all things went right
                // parseFoursquare venues search result
                venueList = parseFoursquare(temp);
                Util.setVenues(venueList);
                finish = true;
            }
        }
    }

    public static String makeCall(String url) {

        // string buffers the url
        StringBuffer buffer_string = new StringBuffer(url);
        String replyString = "";

        // instanciate an HttpClient
        HttpClient httpclient = new DefaultHttpClient();
        // instanciate an HttpGet
        HttpGet httpget = new HttpGet(buffer_string.toString());

        try {
            // get the responce of the httpclient execution of the url
            HttpResponse response = httpclient.execute(httpget);
            InputStream is = response.getEntity().getContent();

            // buffer input stream the result
            BufferedInputStream bis = new BufferedInputStream(is);
            ByteArrayBuffer baf = new ByteArrayBuffer(20);
            int current = 0;
            while ((current = bis.read()) != -1) {
                baf.append((byte) current);
            }
            // the result as a string is ready for parsing
            replyString = new String(baf.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
        // trim the whitespaces
        return replyString.trim();
    }

    private List<FoursquareVenue> parseFoursquareSearch(String response) {
        ArrayList<FoursquareVenue> temp = new ArrayList<FoursquareVenue>();
        try {
            JSONObject jsonObject = new JSONObject(response);

            if (jsonObject.has("response")) {
                if (jsonObject.getJSONObject("response").has("venues")) {

                    JSONArray jsonArray = jsonObject.getJSONObject("response").getJSONArray("venues");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        FoursquareVenue poi = new FoursquareVenue();
                        if (jsonArray.getJSONObject(i).has("name")) {
                            poi.setName(jsonArray.getJSONObject(i).getString("name"));
                            if (jsonArray.getJSONObject(i).has("location")) {
                                if (jsonArray.getJSONObject(i).getJSONObject("location").has("address")) {
                                    poi.setLocation(jsonArray.getJSONObject(i).getJSONObject("location").getString("address"));
                                    if (jsonArray.getJSONObject(i).getJSONObject("location").has("lat")) {
                                        poi.setLatitude(jsonArray.getJSONObject(i).getJSONObject("location").getDouble("lat"));
                                    }
                                    if (jsonArray.getJSONObject(i).getJSONObject("location").has("lng")) {
                                        poi.setLongitute(jsonArray.getJSONObject(i).getJSONObject("location").getDouble("lng"));
                                    }
                                    if (jsonArray.getJSONObject(i).getJSONObject("location").has("distance")) {
                                        poi.setDistance(jsonArray.getJSONObject(i).getJSONObject("location").getInt("distance"));
                                    }
                                    temp.add(poi);
                                }
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<FoursquareVenue>();
        }
        System.out.println("Mida hospital list despres de getVenuesCategory: "+temp.size());

        return temp;
    }

    private static ArrayList<FoursquareVenue> parseFoursquare(final String response) {

        ArrayList<FoursquareVenue> temp = new ArrayList<FoursquareVenue>();
        try {

            // make an jsonObject in order to parse the response
            JSONObject jsonObject = new JSONObject(response);

            if (jsonObject.has("response")) {
                if (jsonObject.getJSONObject("response").has("groups")) {

                    JSONArray jsonArray = jsonObject.getJSONObject("response").getJSONArray("groups").getJSONObject(0).getJSONArray("items");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        FoursquareVenue poi = new FoursquareVenue();
                        if (jsonArray.getJSONObject(i).getJSONObject("venue").has("name")) {
                            poi.setName(jsonArray.getJSONObject(i).getJSONObject("venue").getString("name"));
                            if (jsonArray.getJSONObject(i).getJSONObject("venue").has("location")) {
                                if (jsonArray.getJSONObject(i).getJSONObject("venue").getJSONObject("location").has("address")) {
                                    if (jsonArray.getJSONObject(i).getJSONObject("venue").getJSONObject("location").has("city")) {
                                        poi.setLocation(jsonArray.getJSONObject(i).getJSONObject("venue").getJSONObject("location").getString("address") + ", " + jsonArray.getJSONObject(i).getJSONObject("venue").getJSONObject("location").getString("city"));
                                    }
                                    if (jsonArray.getJSONObject(i).getJSONObject("venue").getJSONObject("location").has("lat")) {
                                        poi.setLatitude(jsonArray.getJSONObject(i).getJSONObject("venue").getJSONObject("location").getDouble("lat"));
                                    }
                                    if (jsonArray.getJSONObject(i).getJSONObject("venue").getJSONObject("location").has("lng")) {
                                        poi.setLongitute(jsonArray.getJSONObject(i).getJSONObject("venue").getJSONObject("location").getDouble("lng"));
                                    }
                                    if (jsonArray.getJSONObject(i).getJSONObject("venue").has("categories")) {
                                        if (jsonArray.getJSONObject(i).getJSONObject("venue").getJSONArray("categories").length() > 0) {
                                            if (jsonArray.getJSONObject(i).getJSONObject("venue").getJSONArray("categories").getJSONObject(0).has("icon")) {
                                                poi.setCategory(jsonArray.getJSONObject(i).getJSONObject("venue").getJSONArray("categories").getJSONObject(0).getString("name"));
                                                poi.setIconURL(jsonArray.getJSONObject(i).getJSONObject("venue").getJSONArray("categories").getJSONObject(0).getJSONObject("icon").toString());
                                            }
                                        }
                                    }
                                    if (jsonArray.getJSONObject(i).getJSONObject("venue").has("price")) {
                                        poi.setPriceRank(jsonArray.getJSONObject(i).getJSONObject("venue").getJSONObject("price").getDouble("tier"));
                                    } else{
                                        poi.setPriceRank(0.0);
                                    }
                                    temp.add(poi);
                                }
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<FoursquareVenue>();
        }
        System.out.println("Mida venue list despres de getVenuesCategory: "+temp.size());

        return temp;

    }


}