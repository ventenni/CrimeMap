package com.example.nickvaiente.crimemap;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.springframework.web.client.RestTemplate;

import entity.geography.Message;

import static java.lang.String.format;


public class MainActivity extends Activity {
        private static final String NAME_URL = "https://data.police.qld.gov.au/api/boundary?name=%s&returngeometry=true&maxresults=%s";
        private static final String LOCATION_URL = "https://data.police.qld.gov.au/api/boundary?latitude=%d&longitude=%d&maxresults=";


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

        RestTemplate restTemplate = new RestTemplate();

        String name = "Rock";
        Double latitude = -27.5;
        Double longitude = 153.0;
        int maxResults = 10;
        Log.i("Message", "Hello");

        String url = format(NAME_URL, name, maxResults);
        Message message = restTemplate.getForObject(url, Message.class);
        Log.i("Message", message.getResultCount().toString());

        url = format(LOCATION_URL, latitude, longitude, maxResults);
        Message message2 = restTemplate.getForObject(url, Message.class);
        System.out.println(message.getResultCount());
    }


}