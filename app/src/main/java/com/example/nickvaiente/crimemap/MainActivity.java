package com.example.nickvaiente.crimemap;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.location.NominatimPOIProvider;
import org.osmdroid.bonuspack.location.POI;
import org.osmdroid.bonuspack.routing.MapQuestRoadManager;
import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.bonuspack.routing.RoadNode;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.FolderOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;


public class MainActivity extends Activity {

    @Override public void onCreate(Bundle savedInstanceState) {
//      Disable Strict Mode
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MapView map = (MapView) findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);



//      Sets the marker coordinates
        GeoPoint startPoint = new GeoPoint(-27.962592, 153.379886);
        GeoPoint extraPoint = new GeoPoint(-27.961073, 153.383700);
        GeoPoint testPoint[] = new GeoPoint[3];


//      Specifies where the map will load
        IMapController mapController = map.getController();
        mapController.setZoom(17);
        mapController.setCenter(startPoint);

// TESTING MULTIPLE MARKERS -------------------- WILL EVENTUALLY DELETE
        double[] lat = new double[3];
        double[] lon = new double[3];

        lat[0] = -27.961073;
        lon[0] = 153.379700;

        lat[1] = -27.961073;
        lon[1] = 153.383700;

        lat[2] = -27.961073;
        lon[2] = 153.384100;

        for (int i = 0; i < 3; i++) {
            double testLat = lat[i];
            double testLon = lon[i];
            //GeoPoint testStart = new GeoPoint(testLat, testLon);
            testPoint[i] = new GeoPoint(testLat, testLon);

            Marker newMark = new Marker(map);
            //newMark.setPosition(testStart);
            newMark.setPosition(testPoint[i]);
            newMark.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            map.getOverlays().add(newMark);
        }


//      This adds markers when the app loads
        Marker startMarker = new Marker(map);
        startMarker.setTitle("Start point");

        startMarker.setPosition(startPoint);
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);

        Marker extraMarker = new Marker(map);
        extraMarker.setPosition(extraPoint);
        extraMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);

        map.getOverlays().add(startMarker);
        map.getOverlays().add(extraMarker);


//      This makes a call to Mapquest as stated in the osmbonuspack tutorial
//      Mapquest supports changing routes so they are suited for bicycle or pedestrians
//      The route is set to pedestrian mode
//      Mapquest API Key - EH5HAxcHJmAN9sf02T6PJA2VDCJ9Tgru
        RoadManager roadManager = new MapQuestRoadManager("EH5HAxcHJmAN9sf02T6PJA2VDCJ9Tgru");
        roadManager.addRequestOption("routeType=pedestrian");


        ArrayList<GeoPoint> waypoints = new ArrayList<GeoPoint>();
        waypoints.add(startPoint);
        waypoints.add(extraPoint);

//        Adds road overlay
        Road road = roadManager.getRoad(waypoints);
        Polyline roadOverlay = RoadManager.buildRoadOverlay(road);
        map.getOverlays().add(roadOverlay);


//        This adds the node icons to each new road for the route.
//        Add a node icon to mipmap folder and use that for node markers
        Drawable nodeIcon = getResources().getDrawable(R.mipmap.marker, null);
        for (int i = 0; i < road.mNodes.size(); i++){
            RoadNode node = road.mNodes.get(i);
            Marker nodeMarker = new Marker(map);
            nodeMarker.setPosition(node.mLocation);
            nodeMarker.setIcon(nodeIcon);
            nodeMarker.setTitle("Step " + i);
            map.getOverlays().add(nodeMarker);
        }


        map.invalidate();
    }


}