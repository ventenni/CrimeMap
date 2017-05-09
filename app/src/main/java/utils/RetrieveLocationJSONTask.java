package utils;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.protocol.HTTP;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Array;

import entity.location_search.LocationInfo;

/**
 * Created by Tae's Puter on 7/05/2017.
 */

public class RetrieveLocationJSONTask extends AsyncTask<String,Void,Void>
{
    private static final String ADDRESS_URL = "http://nominatim.openstreetmap.org/search/%s?format=json&addressdetails=1&limit=1&polygon_svg=1";

    @Override
    protected Void doInBackground(String... params) {
        RestTemplate restTemplate = new RestTemplate(true);

        try {
            //Retrieve jason object from http request
            Log.i("Print", "testing");

            //get json object
            LocationInfo[] jasonObject = restTemplate.getForObject(params[0], LocationInfo[].class);

            //Retrieve latitude
            Log.i("Print", "lat: " + jasonObject[0].getLat());

            //Retrieve longitude
//            Log.i("Print", "lon: " + jasonObject.getLon());

        } catch(Exception e) {
            Log.e("Print", e.getMessage());
        }
        return null;
    }
}
