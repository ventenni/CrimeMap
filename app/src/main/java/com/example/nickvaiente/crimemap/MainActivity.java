package com.example.nickvaiente.crimemap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nickvaiente.crimemap.OSM.LocationInfo;
import com.example.nickvaiente.crimemap.OSM.OpenStreetMap;
import com.example.nickvaiente.crimemap.OSM.RetrieveLocationJSONTask;
import com.example.nickvaiente.crimemap.QPS.QueenslandPoliceService;
import com.example.nickvaiente.crimemap.QPS.entity.offence.OffenceBoundary;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utils.QpsApiAccess;

import static java.lang.String.format;


public class MainActivity extends Activity {
    private static final String NAME_URL = "https://data.police.qld.gov.au/api/boundary?name=%s&returngeometry=true&maxresults=%s";
    private static final String LOCATION_URL = "https://data.police.qld.gov.au/api/boundary?latitude=%s&longitude=%s&maxresults=%s";
    private static final String OFFENCE_URL = "https://data.police.qld.gov.au/api/boundary?name=%s&returngeometry=true&maxresults=%s";
    private String searchInput;
    public String latitude = "-28.002373";
    public String longitude = "153.4145987";
    public static Context context;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        QueenslandPoliceService.getInstance().getOffenceCoordinates(0);
//        QueenslandPoliceService.getInstance().getOffenceCoordinates(1);
        setContentView(R.layout.activity_main);
        MainActivity.context = getApplicationContext();

        final EditText searchBox = (EditText) findViewById(R.id.searchBox);
        final Button searchButton = (Button) findViewById(R.id.searchButton);

        MapView mapView = (MapView) findViewById(R.id.map);
        Map map = new Map(mapView, Double.parseDouble(latitude), Double.parseDouble(longitude));

//        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.showSoftInput(searchBox, InputMethodManager.SHOW_IMPLICIT);
//        searchBox.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus) {
//
//                }
//            }
//        });
//        searchBox.setImeActionLabel("Search", KeyEvent.KEYCODE_ENTER);


        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Read location from search box
                searchInput = searchBox.getText().toString().trim().replaceAll("[^a-zA-Z]", "");
                searchBox.setText(searchInput);
                //User input validation (using suburb name to search OSM and QPS)g
                OpenStreetMap.getInstance().performSearch(searchInput);

                if(OpenStreetMap.getInstance().getResult() != null) {
                    latitude = OpenStreetMap.getInstance().getResult().getLat();
                    longitude = OpenStreetMap.getInstance().getResult().getLon();

                    QueenslandPoliceService.getInstance().performSearch(latitude, longitude);
//                QueenslandPoliceService.getInstance().performSearch(searchInput);
                    int sleepDuration = 0;
                    while (QueenslandPoliceService.getInstance().getOffenceBoundary() == null && sleepDuration < 10) {
                        String loading;
                        try {
                            TimeUnit.SECONDS.sleep(2);
                        }
                        catch (InterruptedException e){
                            Log.d("Print", "Interrupted exception in main");
                        }
                        if (sleepDuration % 3 == 0){
                            loading = "Loading.  ";
                        } else if (sleepDuration % 3 == 0){
                            loading = "Loading.. ";
                        } else {
                            loading = "Loading...";
                        }
                        showToast(loading, 0);
                        sleepDuration += 2;
                    }
                    OffenceBoundary boundary = QueenslandPoliceService.getInstance().getOffenceBoundary();
                    for(int i = 0; i < boundary.getResultCount(); i++){
                        double latitude = QueenslandPoliceService.getInstance().getOffenceCoordinates(boundary.getResult().get(i).getGeometryWKT(), 0);
                        double longitude = QueenslandPoliceService.getInstance().getOffenceCoordinates(boundary.getResult().get(i).getGeometryWKT(), 1);
                    }
                    MapView mapView = (MapView) findViewById(R.id.map);
                    Map map = new Map(mapView, Double.parseDouble(latitude), Double.parseDouble(longitude));

                } else {
                    showToast("No Results. Timed Out :(", 1);
                }
            }
        });

    }

    public static Context getAppContext() {
        return MainActivity.context;
    }

    public static void showToast (String message, int duration) {
        //Display toast message. LENGTH_SHORT = 0, LENGTH_LONG = 1.
        if (duration < 1) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }
    }

}