package com.example.nickvaiente.crimemap.graphical.map;

import android.os.AsyncTask;

import org.osmdroid.bonuspack.routing.MapQuestRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;

import static com.example.nickvaiente.crimemap.R.id.map;

/**
 * Created by nickvaiente on 16/5/17.
 */

public class Routes extends AsyncTask<String, Void, Void> {
    private String mapquestApi;
    private GeoPoint start;
    private GeoPoint finish;
    private ArrayList<GeoPoint> waypoints;
    private RoadManager roadManager;

    public Routes() {
        this.start = new GeoPoint(-27.926252, 153.382916);
        this.finish = new GeoPoint(-27.926252, 153.384916);
        this.mapquestApi = "EH5HAxcHJmAN9sf02T6PJA2VDCJ9Tgru"; // MAKE PRIVATE AS WELL?
        this.waypoints = new ArrayList<GeoPoint>();
        this.roadManager = new MapQuestRoadManager(mapquestApi);
    }

    @Override
    protected Void doInBackground(String... params) {
        this.roadManager.addRequestOption("routeType=pedestrian");

        this.waypoints.add(start);
        this.waypoints.add(finish);

        // Adds road overlay
        Road road = roadManager.getRoad(waypoints);
        Polyline roadOverlay = RoadManager.buildRoadOverlay(road);

//        return roadOverlay;
        return null;

    }

    private String getMapQuestAPI() {
        return this.mapquestApi;
    }
}
//    public Polyline overlayRoutes(GeoPoint start, GeoPoint finish) {
//
//        this.roadManager.addRequestOption("routeType=pedestrian");
//
//        this.waypoints.add(start);
//        this.waypoints.add(finish);
//
//        // Adds road overlay
//        Road road = roadManager.getRoad(waypoints);
//        Polyline roadOverlay = RoadManager.buildRoadOverlay(road);
//
//        return roadOverlay;
//    }


//      This makes a call to Mapquest as stated in the osmbonuspack tutorial
//      Mapquest supports changing routes so they are suited for bicycle or pedestrians
//      The route is set to pedestrian mode
//      Mapquest API Key - EH5HAxcHJmAN9sf02T6PJA2VDCJ9Tgru
