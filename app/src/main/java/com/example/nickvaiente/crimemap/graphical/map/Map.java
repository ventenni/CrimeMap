package com.example.nickvaiente.crimemap.graphical.map;

import android.graphics.Color;
import android.util.Log;

import com.example.nickvaiente.crimemap.MainActivity;
import com.example.nickvaiente.crimemap.OSM.OpenStreetMap;
import com.example.nickvaiente.crimemap.QPS.QueenslandPoliceService;
import com.example.nickvaiente.crimemap.QPS.entity.offence.OffenceInfo;
import com.example.nickvaiente.crimemap.QPS.entity.offence.Result;
import com.example.nickvaiente.crimemap.R;

import org.apache.commons.lang3.ArrayUtils;
import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.infowindow.MarkerInfoWindow;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.example.nickvaiente.crimemap.MainActivity.getAppContext;

/**
 * Created by Tae's Puter on 12/05/2017.
 */

public class Map {

    MapView mapView;
    List<GeoPoint> points = new ArrayList<GeoPoint>();

    // Initializing the mapview and passes coordinates to setLocation()
    public Map(MapView mapView, double latitude, double longitude) {
        this.mapView = mapView;
        setLocation(latitude, longitude);

    }

    // Creates the map based on coordinates (default or suburb entered)
    public void setLocation(double latitude, double longitude){

        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);

        GeoPoint startPoint = new GeoPoint (latitude, longitude);
        IMapController mapController = mapView.getController();
        mapController.setZoom(17);
        mapController.setCenter(startPoint);

        Marker startMarker = new Marker(mapView);
        startMarker.setPosition(startPoint);
//        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        mapView.getOverlays().add(startMarker);

        mapView.invalidate();
    }

    // Adds the offence markers to the map based on location
    public void addMarkers() {
        if (QueenslandPoliceService.getInstance().getOffenceBoundary() != null) {

            java.util.Map<String,String> offenceType = new HashMap<String,String>();
            offenceType.put("1", "Homicide");
            offenceType.put("8", "Assault");
            offenceType.put("14","Robbery");
            offenceType.put("17","Other Offences Against the Person");
            offenceType.put("21","Unlawful Entry");
            offenceType.put("27","Arson");
            offenceType.put("28","Other Property Damage");
            offenceType.put("29","Unlawful Use of Motor Vehicle");
            offenceType.put("30","Other Theft");
            offenceType.put("35","Fraud");
            offenceType.put("39","Handling Stolen Goods");
            offenceType.put("45","Drug Offence");
            offenceType.put("47","Liquor (excl. Drunkenness)");
            offenceType.put("51","Weapons Act Offences");
            offenceType.put("52","Good Order Offence");
            offenceType.put("54","Traffic and Related Offences");
            offenceType.put("55","Other");
            offenceType.put("true", "Solved");
            offenceType.put("false", "Unsolved");

//        Assigning offence types to different marker colours (eg, 1 = Homicide)
            int[] redMarker = {1,8,27};
            int[] yellowMarker = {14,17,21,28,35,45,51};


//         Gathers offences for suburb and assigns them to indiviual markers. If there are individual markers
//         at the same coordinates they are placed into a single cluster marker.
            for (Result result : QueenslandPoliceService.getInstance().getOffenceBoundary().getResult()) {
                Cluster cluster = new Cluster(MainActivity.getAppContext());
                String geometryWKT = result.getGeometryWKT();
                double latitude = QueenslandPoliceService.getInstance().getOffenceCoordinates(geometryWKT, 1);
                double longitude = QueenslandPoliceService.getInstance().getOffenceCoordinates(geometryWKT, 0);

//                displayPerimeter(geometryWKT);

                for (OffenceInfo offence : result.getOffenceInfo()) {
                    //GeoPoint testStart = new GeoPoint(testLat, testLon);
                    GeoPoint point = new GeoPoint(latitude, longitude);


//                Creates new individual marker
                    Marker newMark = new Marker(mapView);
                    if (ArrayUtils.contains(redMarker, offence.getQpsOffenceCode())){
                        newMark.setIcon(getAppContext().getResources().getDrawable(R.mipmap.blue_red, null));
                    } else if (ArrayUtils.contains(yellowMarker, offence.getQpsOffenceCode())){
                        newMark.setIcon(getAppContext().getResources().getDrawable(R.mipmap.blue_yellow, null));
                    } else {
                        newMark.setIcon(getAppContext().getResources().getDrawable(R.mipmap.shield_marker, null));
                    }
                    newMark.setPosition(point);
//                    newMark.setInfoWindow(null);
//
//                Adds offence info to marker bubble
                    newMark.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                    newMark.setTitle("Offence: " + offenceType.get(offence.getQpsOffenceCode() + ""));
                    newMark.setSnippet("Status: " + offenceType.get(offence.getSolved() + ""));

                    SimpleDateFormat dt = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
                    SimpleDateFormat dt1 = new SimpleDateFormat("EEE, d MMM yyyy");
                    try {
                        Date date = dt.parse(offence.getReportDate().replace("T", " "));
                        newMark.setSubDescription("Date Reported: " + dt1.format(date).toString());
                    }
                    catch (ParseException e){
                        Log.i("Print", "Parse Exception in Map. DateParsing");
                    }
                    newMark.setTextLabelFontSize(12);
//                    newMark.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
//                        @Override
//                        public boolean onMarkerClick(Marker marker, MapView mapView) {
//                            Log.i("Print", "Marker pressed");
//                            return false;
//                        }
//                    });
//            mapView.getOverlays().add(newMark);


                    cluster.addMarker(newMark);

//            crimeMarkers.add(newMark);
                }
                mapView.getOverlays().add(cluster.getCluster());
            }
            mapView.invalidate();
        }
    }


//  Outline the suburb perimeter
    public void displayPerimeter(String geometryWKT){
//        String geometryWKT = QueenslandPoliceService.getInstance().getOffenceBoundary().getResult().get(0).getGeometryWKT();
        int size = (geometryWKT.length() - geometryWKT.replaceAll(" ", "").length()) / 2;
//        if (OpenStreetMap.getInstance().getResult() != null) {
            Polyline border = new Polyline();
            List<GeoPoint> points = new ArrayList<GeoPoint>();

        for(int i = 0; i < size; i += 2){
            GeoPoint point = new GeoPoint(QueenslandPoliceService.getInstance().getOffenceCoordinates(geometryWKT, i + 1), QueenslandPoliceService.getInstance().getOffenceCoordinates(geometryWKT, i));
            points.add(point);
        }
        GeoPoint point = new GeoPoint(QueenslandPoliceService.getInstance().getOffenceCoordinates(geometryWKT, 1), QueenslandPoliceService.getInstance().getOffenceCoordinates(geometryWKT, 0));

//            for (List<String> coordinate : OpenStreetMap.getInstance().getResult().getPolygonpoints()) {
//                GeoPoint point = new GeoPoint(Double.parseDouble(coordinate.get(1)), Double.parseDouble(coordinate.get(0)));
//                points.add(point);
//            }
//            GeoPoint point = new GeoPoint(Double.parseDouble(OpenStreetMap.getInstance().getResult().getPolygonpoints().get(0).get(1)), Double.parseDouble(OpenStreetMap.getInstance().getResult().getPolygonpoints().get(0).get(0)));

            points.add(point);
            border.setPoints(points);
            border.setColor(Color.DKGRAY);
//            border.setColor(Color.RED);
            border.setWidth(1f);
//        Adds polygon overlay
            mapView.getOverlays().add(border);
            mapView.invalidate();
        }
//    }
}
