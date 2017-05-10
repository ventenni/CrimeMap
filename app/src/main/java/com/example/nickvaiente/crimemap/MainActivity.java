package com.example.nickvaiente.crimemap;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.nickvaiente.crimemap.OSM.LocationInfo;
import com.example.nickvaiente.crimemap.OSM.OpenStreetMap;
import com.example.nickvaiente.crimemap.OSM.RetrieveLocationJSONTask;
import com.example.nickvaiente.crimemap.QPS.QueenslandPoliceService;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import utils.QpsApiAccess;

import static java.lang.String.format;


public class MainActivity extends Activity {
    private static final String NAME_URL = "https://data.police.qld.gov.au/api/boundary?name=%s&returngeometry=true&maxresults=%s";
    private static final String LOCATION_URL = "https://data.police.qld.gov.au/api/boundary?latitude=%s&longitude=%s&maxresults=%s";
    private static final String OFFENCE_URL = "https://data.police.qld.gov.au/api/boundary?name=%s&returngeometry=true&maxresults=%s";
    private String searchInput;
    public String latitude = "-28.002373";
    public String longitude = "153.4145987";

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText searchBox = (EditText) findViewById(R.id.searchBox);
        final Button searchButton = (Button) findViewById(R.id.searchButton);

        MapView map = (MapView) findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);

        GeoPoint startPoint = new GeoPoint(Double.parseDouble(latitude), Double.parseDouble(longitude));
        IMapController mapController = map.getController();
        mapController.setZoom(18);
        mapController.setCenter(startPoint);

        Marker startMarker = new Marker(map);
        startMarker.setPosition(startPoint);
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        map.getOverlays().add(startMarker);

        map.invalidate();

        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Read location from search box
                searchInput = searchBox.getText().toString().trim();
                //User input validation (using suburb name to search OSM and QPS)
//                if (searchInput)


                OpenStreetMap.getInstance().performSearch(searchInput);
                latitude = OpenStreetMap.getInstance().getResult().getLat();
                longitude = OpenStreetMap.getInstance().getResult().getLon();

                QueenslandPoliceService.getInstance().performSearch(latitude, longitude);
//                QueenslandPoliceService.getInstance().performSearch(searchInput);

                MapView map = (MapView) findViewById(R.id.map);
                map.setTileSource(TileSourceFactory.MAPNIK);
                map.setBuiltInZoomControls(true);
                map.setMultiTouchControls(true);

                GeoPoint startPoint = new GeoPoint(Double.parseDouble(latitude), Double.parseDouble(longitude));
                IMapController mapController = map.getController();
                mapController.setZoom(18);
                mapController.setCenter(startPoint);

                Marker startMarker = new Marker(map);
                startMarker.setPosition(startPoint);
                startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                map.getOverlays().add(startMarker);

                map.invalidate();
            }
        });


//        String name = "Rock";
//        Double latitude = -27.5;
//        Double longitude = 153.0;
//        int maxResults = 1;
//        Log.i("Message","Hello");
//        String url = format(NAME_URL, name, maxResults);
//        String url2 = format(LOCATION_URL, latitude, longitude, maxResults);
//        String url3 = format(OFFENCE_URL, name, maxResults);
//
//        new RetrieveGeographyJSONTask().execute(url, url2, url3);
    }
}