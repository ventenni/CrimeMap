package com.example.nickvaiente.crimemap.OSM;

import android.os.AsyncTask;
import android.util.Log;

import org.springframework.web.client.RestTemplate;


/**
 * Created by Tae's Puter on 7/05/2017.
 */

public class RetrieveSuggestions extends AsyncTask<String,Void,Void>
{
//    private static final String ADDRESS_URL = "http://nominatim.openstreetmap.org/search/%s?format=json&addressdetails=1&limit=1&polygon_svg=1";
//    private static final String ADDRESS_URL = "http://nominatim.openstreetmap.org/search?q=%s+Queensland&format=json&polygon=1&addressdetails=1";

    @Override
    protected Void doInBackground(String... params) {
        RestTemplate restTemplate = new RestTemplate(true);
        LocationInfo[] jsonObject;

        try {
            //Retrieve json object from http request
            Log.i("Print", "testing RetrieveLocationJsonTask1");

            //get json object
            jsonObject = restTemplate.getForObject(params[0], LocationInfo[].class);



        } catch(Exception e) {
            Log.e("Print", e.getMessage());
        }
        return null;
    }
}
