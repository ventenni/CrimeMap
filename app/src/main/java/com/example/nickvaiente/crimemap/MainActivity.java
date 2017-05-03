package com.example.nickvaiente.crimemap;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

import apiservice.LocationService;
import apiservice.OkHttp;
import entity.geography.Message;
import retrofit.ApiService;
import retrofit.RetroClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;

import static java.lang.String.format;

import java.util.Map;
import java.util.*;


public class MainActivity extends Activity {
    private static final String NAME_URL = "https://data.police.qld.gov.au/api/boundary?name=%s&returngeometry=true&maxresults=%s";
    private static final String LOCATION_URL = "https://data.police.qld.gov.au/api/boundary?latitude=%d&longitude=%d&maxresults=";

    private static final String ADDRESS_URL = "http://nominatim.openstreetmap.org/search/%s?format=json&addressdetails=1&limit=1&polygon_text=1";
//    private static final String ADDRESS_END_URL = "/%s?format=json&addressdetails=1&limit=1&polygon_text=1";
    private EditText searchBox = null;
    private Button searchButton = null;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MapView map = (MapView) findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);

        GeoPoint startPoint = new GeoPoint(-27.962592, 153.379886);
        IMapController mapController = map.getController();
        mapController.setZoom(18);
        mapController.setCenter(startPoint);

        Marker startMarker = new Marker(map);
        startMarker.setPosition(startPoint);
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        map.getOverlays().add(startMarker);

        map.invalidate();

        //SEARCH FUNCTION
//            new RetrieveFeedTask().execute(ADDRESS_URL);
        searchBox = (EditText) findViewById(R.id.searchBox);
        searchButton = (Button) findViewById(R.id.searchButton);

        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //Read location from search box
                String searchInput = searchBox.getText().toString().trim();
                if (searchInput.length() > 0) {
                    try {
//                        Log.i("Message", searchInput);
                        //Search OSM for matching locations and return a maximum result of 1
//                        RestTemplate restTemplate = new RestTemplate();
                        String address = searchInput;
                        String url = format(ADDRESS_URL, address);

                        // issue the Get request
                        OkHttp example = new OkHttp();
                        String getResponse = example.doGetRequest(url);
                        Log.i("Print", getResponse);




//                        String JSonString = readURL(url);
//                        JSONArray s = JSONArray.fromObject(JSonString);
//                        JSONObject Data =(JSONObject)(s.getJSONObject(0));
//                        System.out.println(Data.get("name"));
//                        restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter());
//                        LocationService resultObject = restTemplate.getForObject(url, LocationService.class); //CRASHES HERE GRRRRRRRRRR
//                        Log.i("Message", resultObject.getLat());
//                        if (resultObject.length > 0)
//                            Log.i("Message", "Found match");
//                        else
//                            Log.i("Message", "No matches found");





//                        //Creating an object of our api interface
//                        ApiService api = RetroClient.getApiService();
//
//                        /**
//                         * Calling JSON
//                         */
//                        Call<LocationService> call = api.getMyJSON();
//
//                        /**
//                         * Enqueue Callback will be call when get response...
//                         */
//                        call.enqueue(new Callback<LocationService>() {
//                            @Override
//                            public void onResponse(Call<LocationService> call, Response<LocationService> response) {
//
//                                if (response.isSuccessful()) {
//                                    /**
//                                     * Got Successfully
//                                     */
//                                    String lat = response.body().getLat();
//                                    String lon = response.body().getLon();
//                                    Log.i("Message", lat + " kkkk " + lon);
//                                }
//                            }
//
//                            @Override
//                            public void onFailure(Call<LocationService> call, Throwable t) {
//                                Log.i("Message", "Shit aint working couz");
//                            }
//                        });






//                        if(addresses.size() > 0) {
//                            address = addresses.get(0);
//                            latitude = address.getLatitude();
//                            longitude = address.getLongitude();
//
//                            //Search QPS for the location and return the crime data
//
//
//                            //Display location on the OSM map and show map markers
//                            MapView map = (MapView) findViewById(R.id.map);
//                            map.setTileSource(TileSourceFactory.MAPNIK);
//                            map.setBuiltInZoomControls(true);
//                            map.setMultiTouchControls(true);
//
//                            GeoPoint startPoint = new GeoPoint(latitude, longitude);
//                            IMapController mapController = map.getController();
//                            mapController.setZoom(18);
//                            mapController.setCenter(startPoint);
//
//                            Marker startMarker = new Marker(map);
//                            startMarker.setPosition(startPoint);
//                            startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
//                            map.getOverlays().add(startMarker);
//
//                            map.invalidate();
//                        }
                    } catch(Exception e) {
//                        e.getMessage();
                        e.printStackTrace();
                    }
                }
            }
        });


//        RestTemplate restTemplate = new RestTemplate();
//
//        String name = "Rock";
//        Double latitude = -27.5;
//        Double longitude = 153.0;
//        int maxResults = 10;
//        Log.i("Message", "Hello");
//
//        String url = format(NAME_URL, name, maxResults);
//        Message message = restTemplate.getForObject(url, Message.class);
//        Log.i("Message", message.getResultCount().toString());
//
//        url = format(LOCATION_URL, latitude, longitude, maxResults);
//        Message message2 = restTemplate.getForObject(url, Message.class);
//        System.out.println(message.getResultCount());
    }


}