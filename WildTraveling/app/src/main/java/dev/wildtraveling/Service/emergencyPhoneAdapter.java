package dev.wildtraveling.Service;

import com.google.android.gms.maps.model.LatLng;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import dev.wildtraveling.Domain.dayMeteoPrevision;
import dev.wildtraveling.R;

/**
 * Created by pere on 5/13/17.
 */
public class emergencyPhoneAdapter {

    private String API_KEY = "3f32887a7aa565a2661a44e98c26a818";

    private Double latitude;
    private Double longitude;

    private Boolean finish = false;

    private String res;
    private String phone;

    public String getEmergencyPhone(LatLng coord, String code){
        this.latitude = coord.latitude;
        this.longitude = coord.longitude;
        res = makeCall("http://emergencynumberapi.com/api/country/"+code);

        if (res == null) {
            finish = true;
            System.out.println("info del pais es null");
        } else {
            System.out.println("info del pais NO es null");
            System.out.println(res);
            phone = parseCountryInfo(res);
            System.out.println("phone es: "+phone);
            finish = true;
        }
        return phone;
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

    private String parseCountryInfo(String response) {
        System.out.println("parse country info");
        try {
            JSONObject jsonObject = new JSONObject(response);
            System.out.println("abans de data");
            if(jsonObject.has("data")){
                System.out.println("entra a data");
                JSONObject obj = jsonObject.getJSONObject("data");
                if(obj.getBoolean("member_112")){
                    return "112";
                } else if(!obj.getJSONObject("police").getJSONArray("all").getJSONObject(0).equals("")){
                    return obj.getJSONObject("police").getJSONArray("all").getJSONObject(0).toString();
                } else if(!obj.getJSONObject("ambulance").getJSONArray("all").getJSONObject(0).equals("")){
                    return obj.getJSONObject("ambulance").getJSONArray("all").getJSONObject(0).toString();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return "";

    }

}

