package com.example.nickvaiente.crimemap.OSM;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.nickvaiente.crimemap.R;

import java.util.concurrent.TimeUnit;

import static com.example.nickvaiente.crimemap.MainActivity.getAppContext;
import static com.example.nickvaiente.crimemap.MainActivity.showToast;

/**
 * Created by Tae's Puter on 9/05/2017.
 */

public class OpenStreetMap extends Activity{
    private static OpenStreetMap instance = new OpenStreetMap();
    private String ADDRESS_URL = "http://nominatim.openstreetmap.org/search/%s Queensland?countrycodes=AU&format=json&addressdetails=1&limit=1&polygon_svg=1";
    private LocationInfo result;
    private ProgressBar spinner;

    public OpenStreetMap() {
        this.result = null;

    }

    public static OpenStreetMap getInstance() {
        return instance;
    }

    public void setResult(LocationInfo result) {
        this.result = result;
    }

    public LocationInfo getResult() {
        return result;
    }

    public void performSearch(String searchInput) {
        if (searchInput.length() > 0) {
            try {

                Log.i("Message", "Testing");
                //Search OSM for matching locations and return a maximum result of 1
                String url = String.format(ADDRESS_URL, searchInput);
                new RetrieveLocationJSONTask().execute(url);
                int sleepDuration = 0;

                while (result == null && sleepDuration < 10) {
                    String searching;
                    TimeUnit.SECONDS.sleep(1);
                    if (sleepDuration % 3 == 0){
                        searching = "Searching.  ";
                    } else if (sleepDuration % 3 == 0){
                        searching = "Searching.. ";
                    } else {
                        searching = "Searching...";
                    }
                    showToast(searching, 0);
                    sleepDuration++;
                }

                if (result != null) {
                    String lat = getResult().getLat();
                    String lon = getResult().getLon();

                    Log.i("Print", lat);
                    Log.i("Print", lon);
                }
            } catch(Exception e) {
                e.getMessage();
            }
        }

    }


}






