package com.example.nickvaiente.crimemap;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

/**
 * Created by Tae's Puter on 12/05/2017.
 */

public class Map {

    public Map(MapView map, double latitude, double longitude) {
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);

        GeoPoint startPoint = new GeoPoint (latitude, longitude);
        IMapController mapController = map.getController();
        mapController.setZoom(18);
        mapController.setCenter(startPoint);

        Marker startMarker = new Marker(map);
        startMarker.setPosition(startPoint);
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        map.getOverlays().add(startMarker);

        map.invalidate();

    }

    public void showMap(double latitude, double longitude) {

    }




}
