package com.example.nickvaiente.crimemap.OSM;

import android.app.Activity;
import android.content.res.AssetManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.nickvaiente.crimemap.MainActivity;
import com.example.nickvaiente.crimemap.R;

import org.xml.sax.Parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.databind.*;

import static com.example.nickvaiente.crimemap.MainActivity.context;
import static com.example.nickvaiente.crimemap.MainActivity.getAppContext;
import static com.example.nickvaiente.crimemap.MainActivity.showToast;

/**
 * Created by Tae's Puter on 9/05/2017.
 */

public class OpenStreetMap extends Activity{
    private static OpenStreetMap instance = new OpenStreetMap();
    private String ADDRESS_URL = "http://nominatim.openstreetmap.org/search/%s Queensland?countrycodes=AU&format=json&addressdetails=1&limit=1";//&polygon=1";
//    private String ADDRESS_URL = "http://nominatim.openstreetmap.org/search?q=%s+Queensland&format=json&polygon=1&addressdetails=1";
    private LocationInfo result;
    private List<Address> suggestions = new ArrayList<>();

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

                Log.i("Print", "PerformSearch in OSM");
                //Search OSM for matching locations and return a maximum result of 1
                String url = String.format(ADDRESS_URL, searchInput);
                Log.i("Print", "Using url " + url );
                new RetrieveLocationJSONTask().execute(url);
                int sleepDuration = 0;

                while (result == null && sleepDuration < 10) {
//                    String searching;
                    TimeUnit.SECONDS.sleep(1);
//                    if (sleepDuration % 3 == 0){
//                        searching = "Searching.  ";
//                    } else if (sleepDuration % 3 == 0){
//                        searching = "Searching.. ";
//                    } else {
//                        searching = "Searching...";
//                    }
//                    showToast(searching, 0);
                    sleepDuration += 1;
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

    public void performSuggestion(String searchInput) {
        if (searchInput.length() > 2) {
            try {

                Log.i("Print", "PerformSuggestion in OSM");
                //Search OSM for matching locations and return a maximum result of 1
                String url = String.format("http://nominatim.openstreetmap.org/search/%s Queensland?countrycodes=AU&format=json&addressdetails=1&limit=5&polygon_svg=1", searchInput);
                Log.i("Print", "Using url " + url );
                new RetrieveLocationJSONTask().execute(url);
                int sleepDuration = 0;

                while (result == null && sleepDuration < 20) {
//                    String searching;
                    TimeUnit.SECONDS.sleep(1);
//                    if (sleepDuration % 3 == 0){
//                        searching = "Searching.  ";
//                    } else if (sleepDuration % 3 == 0){
//                        searching = "Searching.. ";
//                    } else {
//                        searching = "Searching...";
//                    }
//                    showToast(searching, 0);
                    sleepDuration += 1;
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

    public void resetInstance(){
        this.result = null;
    }

//    public void loadSuburbBoundary(AssetManager assetManager){
//        ObjectMapper mapper = new ObjectMapper();
//
//        try {
//            InputStream inputStream = assetManager.open("qldSuburbBoundary.json");
//            boundary = mapper.readValue(inputStream, SuburbBoundary.class);
//        }
//        catch(FileNotFoundException e){
//            Log.i("Print","FileNotFoundException in loadSuburbBoundary - OpenStreetMap");
//        }
//        catch(IOException e){
//            Log.i("Print","IOException in loadSuburbBoundary - OpenStreetMap");
//        }
//    }


}






