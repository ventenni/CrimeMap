package com.example.nickvaiente.crimemap;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import utils.QpsApiAccess;
import utils.RetrieveLocationJSONTask;

import static java.lang.String.format;


public class MainActivity extends Activity {
    private static final String NAME_URL = "https://data.police.qld.gov.au/api/boundary?name=%s&returngeometry=true&maxresults=%s";
    private static final String LOCATION_URL = "https://data.police.qld.gov.au/api/boundary?latitude=%s&longitude=%s&maxresults=%s";
    private static final String OFFENCE_URL = "https://data.police.qld.gov.au/api/boundary?name=%s&returngeometry=true&maxresults=%s";
    private static final String ADDRESS_URL = "http://nominatim.openstreetmap.org/search/%s?format=json&addressdetails=1&limit=1&polygon_svg=1";

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

        //LOCATION SEARCH FUNCTION
        searchBox = (EditText) findViewById(R.id.searchBox);
        searchButton = (Button) findViewById(R.id.searchButton);

        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //Read location from search box
                String searchInput = searchBox.getText().toString().trim();
                if (searchInput.length() > 0) {
                    try {
                        //Search OSM for matching locations and return a maximum result of 1
                        String address = searchInput;
                        String url = format(ADDRESS_URL, address);
                        new RetrieveLocationJSONTask().execute(url);
                        new QpsApiAccess().execute(searchInput);
                    } catch(Exception e) {
                        e.getMessage();
                    }
                }
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