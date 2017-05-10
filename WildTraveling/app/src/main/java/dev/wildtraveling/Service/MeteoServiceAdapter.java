package dev.wildtraveling.Service;

import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
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
import java.util.Calendar;
import java.util.List;

import dev.wildtraveling.Domain.FoursquareVenue;
import dev.wildtraveling.Domain.dayMeteoPrevision;
import dev.wildtraveling.R;
import dev.wildtraveling.Util.Util;

/**
 * Created by pere on 5/8/17.
 */
public class MeteoServiceAdapter {

    private String API_KEY = "3f32887a7aa565a2661a44e98c26a818";

    private Double latitude;
    private Double longitude;

    private Boolean finish = false;
    private String temp;

    private String daily;
    private List<dayMeteoPrevision> results;

    public List<dayMeteoPrevision> getForecast(LatLng coord){
        this.latitude = coord.latitude;
        this.longitude = coord.longitude;
        //new MeteoCall().execute();
        //temp = makeCall("http://api.openweathermap.org/data/2.5/weather?lat="+this.latitude+"&lon="+this.longitude+"&APPID=" + API_KEY + "&units=metric");
        //forecast = makeCall("http://api.openweathermap.org/data/2.5/forecast?lat="+this.latitude+"&lon="+this.longitude+"&APPID=" + API_KEY + "&units=metric");
        daily = makeCall("http://api.openweathermap.org/data/2.5/forecast/daily?lat="+this.latitude+"&lon="+this.longitude+"&APPID=" + API_KEY + "&units=metric");

        if (daily == null) {
            finish = true;
        } else {
            results = parseDaylyWeather(daily);
            //Util.setMeteo(results);
            finish = true;
        }
        return results;
    }

    public Boolean getStatus() {
        if(finish) return true;
        return false;
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

    private static List<dayMeteoPrevision> parseDaylyWeather(final String response) {

        dayMeteoPrevision temp = new dayMeteoPrevision();
        List<dayMeteoPrevision> m = new ArrayList<>();
        try {
            // make an jsonObject in order to parse the response
            JSONObject jsonObject = new JSONObject(response);


            if(jsonObject.has("list")){
                JSONArray array = jsonObject.getJSONArray("list");
                for(int i = 0; i < array.length(); i++){
                    dayMeteoPrevision poi = new dayMeteoPrevision();
                    if(array.getJSONObject(i).has("temp")){
                        poi.setTemp_min(array.getJSONObject(i).getJSONObject("temp").getDouble("min"));
                        poi.setTemp_max(array.getJSONObject(i).getJSONObject("temp").getDouble("max"));
                    }
                    if(array.getJSONObject(i).has("weather")){
                        poi.setWeather_type(array.getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("main"));
                        poi.setWeather_description(array.getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("description"));
                    }
                    if(array.getJSONObject(i).has("speed")){
                        poi.setWind_speed(array.getJSONObject(i).getDouble("speed"));
                    }
                    poi.setHumidity(array.getJSONObject(i).getInt("humidity"));

                    m.add(poi);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return m;

    }

    public int setMeteoIcon(String type){
        if(type.equals("Clear")){
            return R.drawable.sun;
        } else if(type.equals("Clouds")){
            return R.drawable.cloud;
        } else if(type.equals("Rain")){
            return R.drawable.rain;
        } else if(type.equals("Snow")){
            return R.drawable.snow;
        } else if(type.equals("Few Clouds")){
            return R.drawable.sun_cloud;
        }
        return R.drawable.sun_cloud;

    }
}
