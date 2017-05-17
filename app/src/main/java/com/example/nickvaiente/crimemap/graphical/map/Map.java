package com.example.nickvaiente.crimemap.graphical.map;

import com.example.nickvaiente.crimemap.MainActivity;
import com.example.nickvaiente.crimemap.QPS.QueenslandPoliceService;
import com.example.nickvaiente.crimemap.QPS.entity.offence.OffenceInfo;
import com.example.nickvaiente.crimemap.QPS.entity.offence.Result;
import com.example.nickvaiente.crimemap.R;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import static com.example.nickvaiente.crimemap.MainActivity.getAppContext;

/**
 * Created by Tae's Puter on 12/05/2017.
 */

public class Map {

    MapView mapView;

    public Map(MapView mapView, double latitude, double longitude) {
        this.mapView = mapView;
        setLocation(latitude, longitude);

    }

    public void setLocation(double latitude, double longitude){

        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);

        GeoPoint startPoint = new GeoPoint (latitude, longitude);
        IMapController mapController = mapView.getController();
        mapController.setZoom(15);
        mapController.setCenter(startPoint);

        Marker startMarker = new Marker(mapView);
        startMarker.setPosition(startPoint);
//        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        mapView.getOverlays().add(startMarker);

        mapView.invalidate();
    }

    public void addMarkers() {

        Cluster cluster = new Cluster(MainActivity.getAppContext());

        for (Result result : QueenslandPoliceService.getInstance().getOffenceBoundary().getResult()) {
            String geometryWKT = result.getGeometryWKT();
            double latitude = QueenslandPoliceService.getInstance().getOffenceCoordinates(geometryWKT, 1);
            double longitude = QueenslandPoliceService.getInstance().getOffenceCoordinates(geometryWKT, 0);

            for(OffenceInfo offence : result.getOffenceInfo()) {
                //GeoPoint testStart = new GeoPoint(testLat, testLon);
                GeoPoint point = new GeoPoint(latitude, longitude);

                Marker newMark = new Marker(mapView);
                newMark.setIcon(getAppContext().getResources().getDrawable(R.drawable.new_marker_logo, null));
                newMark.setPosition(point);
                newMark.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                newMark.setTitle("Possible buried treasure");
//            mapView.getOverlays().add(newMark);

                cluster.addMarker(newMark);
//            crimeMarkers.add(newMark);
            }
        }
        mapView.getOverlays().add(cluster.getCluster());
        mapView.invalidate();
    }
}
