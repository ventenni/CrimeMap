package com.example.nickvaiente.crimemap;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.clustering.RadiusMarkerClusterer;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {

//      Disable Strict Mode
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final MapView map = (MapView) findViewById(R.id.map);
        newMap(map);
        addingWaypoints(map);

        ////      Code below is used to make a side menu.
////      MaterialDrawer - Mike Penz - Github

        new DrawerBuilder().withActivity(this).build();
//


//        safest route
//        compare
//        about
//        help

//        // if you want to update the items at a later time it is recommended to keep it in a variable
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName(R.string.Home);
        SecondaryDrawerItem item2 = new SecondaryDrawerItem().withIdentifier(2).withName(R.string.Compare);
        SecondaryDrawerItem item3 = new SecondaryDrawerItem().withIdentifier(3).withName(R.string.About);
//        Log.i("Tag", "error");
//
//        // create the drawer and remember the `Drawer` result object
        Drawer result = new DrawerBuilder()
                .withActivity(this)
                .addDrawerItems(
                        item1,
                        new DividerDrawerItem(),
                        item2,
                        item3
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
//                    This function is activated when 'Compare' is pressed in the side menu.
//                    It will hide the search field, and show the route fields and button.
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        Log.i("marker-View", view.toString());
                        Log.i("marker-position", String.valueOf(position));
                        Log.i("marker-drawerItem", drawerItem.toString());

                        EditText start = (EditText) MainActivity.this.findViewById(R.id.start);
                        EditText finish = (EditText) MainActivity.this.findViewById(R.id.end);
                        EditText search = (EditText) MainActivity.this.findViewById(R.id.search);
                        Button route_button = (Button) MainActivity.this.findViewById(R.id.route_button);

//                        If 'Home' is pressed, set the text fields back to normal
//                        and reload the map at the original point.
                        if (position == 0) {
                            if (start.getVisibility() == View.VISIBLE) {
                                start.setVisibility(View.GONE);
                                finish.setVisibility(View.GONE);
                                route_button.setVisibility(View.GONE);
                                search.setVisibility(View.VISIBLE);
                                newMap(map);

                            }
                        }
                        else if (position == 2) {
                            if (start.getVisibility() == View.VISIBLE) {
                                start.setVisibility(View.GONE);
                                finish.setVisibility(View.GONE);
                                route_button.setVisibility(View.GONE);
                                search.setVisibility(View.VISIBLE);
                            } else {
                                start.setVisibility(View.VISIBLE);
                                finish.setVisibility(View.VISIBLE);
                                route_button.setVisibility(View.VISIBLE);
                                search.setVisibility(View.GONE);
                            }
                        }
                        else if (position == 3){
                            Log.i("marked", "about");
                        } else{
                            Log.i("else", "else");
                        }
                        return true;
                    }
                })
                .build();



//        Button route_button = (Button) findViewById();

    }
    void newMap(MapView map){
//        MapView map = (MapView) findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);

        GeoPoint startPoint = new GeoPoint(-27.962592, 153.379886);

        //      Specifies where the map will load
        IMapController mapController = map.getController();
        mapController.setZoom(17);
        mapController.setCenter(startPoint);

        Marker startMarker = new Marker(map);
        startMarker.setTitle("Start point");
        startMarker.setPosition(startPoint);
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        map.getOverlays().add(startMarker);


        map.invalidate();
    }

    void addingWaypoints(MapView map) {
        //      This makes a call to Mapquest as stated in the osmbonuspack tutorial
        //      Mapquest supports changing routes so they are suited for bicycle or pedestrians
        //      The route is set to pedestrian mode
        //      Mapquest API Key - EH5HAxcHJmAN9sf02T6PJA2VDCJ9Tgru
        RoadManager roadManager = new MapQuestRoadManager("EH5HAxcHJmAN9sf02T6PJA2VDCJ9Tgru");
        roadManager.addRequestOption("routeType=pedestrian");

        GeoPoint startPoint = new GeoPoint(-27.962592, 153.379886);
        GeoPoint extraPoint = new GeoPoint(-27.926252, 153.382916);

        ArrayList<GeoPoint> waypoints = new ArrayList<GeoPoint>();
        waypoints.add(startPoint);
        waypoints.add(extraPoint);

//        Adds road overlay
        Road road = roadManager.getRoad(waypoints);
        Polyline roadOverlay = RoadManager.buildRoadOverlay(road);
        map.getOverlays().add(roadOverlay);

        map.invalidate();


//        This adds the node icons to each new road for the route.
//        Add a node icon to mipmap folder and use that for node markers
//        Drawable nodeIcon = getResources().getDrawable(R.mipmap.marker, null);
//        for (int i = 0; i < road.mNodes.size(); i++) {
//            RoadNode node = road.mNodes.get(i);
//            Marker nodeMarker = new Marker(map);
//            nodeMarker.setPosition(node.mLocation);
//            nodeMarker.setIcon(nodeIcon);
//            nodeMarker.setTitle("Step " + i);
////          Below code adds travel instructions to each marker
//            nodeMarker.setSnippet(node.mInstructions);
//            nodeMarker.setSubDescription(Road.getLengthDurationText(this, node.mLength, node.mDuration));
//            Drawable icon = getResources().getDrawable(R.mipmap.marker, null);
//            nodeMarker.setImage(icon);
//            map.getOverlays().add(nodeMarker);
//        }

//        map.invalidate();
    }


//    FUNCTIONS MADE - START ---------------------------------------------

//        MapView map = (MapView) findViewById(R.id.map);
//        map.setTileSource(TileSourceFactory.MAPNIK);
//        map.setBuiltInZoomControls(true);
//        map.setMultiTouchControls(true);

//      Sets the marker coordinates
//        GeoPoint startPoint = new GeoPoint(-27.962592, 153.379886);
//        GeoPoint extraPoint = new GeoPoint(-27.926252, 153.382916);
//        GeoPoint testPoint[] = new GeoPoint[3];


//      Specifies where the map will load
//        IMapController mapController = map.getController();
//        mapController.setZoom(17);
//        mapController.setCenter(startPoint);

// TESTING MULTIPLE MARKERS -------------------- WILL EVENTUALLY DELETE
//        double[] lat = new double[3];
//        double[] lon = new double[3];
//
//        lat[0] = -27.961073;
//        lon[0] = 153.379700;
//
//        lat[1] = -27.961073;
//        lon[1] = 153.383700;
//
//        lat[2] = -27.961073;
//        lon[2] = 153.384100;

//        for (int i = 0; i < 3; i++) {
//            double testLat = lat[i];
//            double testLon = lon[i];
//            //GeoPoint testStart = new GeoPoint(testLat, testLon);
//            testPoint[i] = new GeoPoint(testLat, testLon);
//
//            Marker newMark = new Marker(map);
//            //newMark.setPosition(testStart);
////            newMark.setPosition(testPoint[i]);
//            newMark.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
//            newMark.setTitle("Possible buried treasure");
//            map.getOverlays().add(newMark);
//        }


//      This adds markers when the app loads
//        Marker startMarker = new Marker(map);
//        startMarker.setTitle("Start point");

//        startMarker.setPosition(startPoint);
//        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
//
//        Marker extraMarker = new Marker(map);
//        extraMarker.setPosition(extraPoint);
//        extraMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);

//        map.getOverlays().add(startMarker);
//        map.getOverlays().add(extraMarker);


////      This makes a call to Mapquest as stated in the osmbonuspack tutorial
////      Mapquest supports changing routes so they are suited for bicycle or pedestrians
////      The route is set to pedestrian mode
////      Mapquest API Key - EH5HAxcHJmAN9sf02T6PJA2VDCJ9Tgru
//        RoadManager roadManager = new MapQuestRoadManager("EH5HAxcHJmAN9sf02T6PJA2VDCJ9Tgru");
//        roadManager.addRequestOption("routeType=pedestrian");
//
//
//        ArrayList<GeoPoint> waypoints = new ArrayList<GeoPoint>();
//        waypoints.add(startPoint);
//        waypoints.add(extraPoint);
//
////        Adds road overlay
//        Road road = roadManager.getRoad(waypoints);
//        Polyline roadOverlay = RoadManager.buildRoadOverlay(road);
//        map.getOverlays().add(roadOverlay);
//
//
////        This adds the node icons to each new road for the route.
////        Add a node icon to mipmap folder and use that for node markers
//        Drawable nodeIcon = getResources().getDrawable(R.mipmap.marker, null);
//        for (int i = 0; i < road.mNodes.size(); i++) {
//            RoadNode node = road.mNodes.get(i);
//            Marker nodeMarker = new Marker(map);
//            nodeMarker.setPosition(node.mLocation);
//            nodeMarker.setIcon(nodeIcon);
//            nodeMarker.setTitle("Step " + i);
////          Below code adds travel instructions to each marker
//            nodeMarker.setSnippet(node.mInstructions);
//            nodeMarker.setSubDescription(Road.getLengthDurationText(this, node.mLength, node.mDuration));
//            Drawable icon = getResources().getDrawable(R.mipmap.marker, null);
//            nodeMarker.setImage(icon);
//            map.getOverlays().add(nodeMarker);
//        }





//FUNCTIONS MADE - FINISH -------------------------------------------------


//
////      OpenStreetMaps Places Of Interest (POI) with Nominatim
//        NominatimPOIProvider poiProvider = new NominatimPOIProvider("");
//        ArrayList<POI> pois = poiProvider.getPOICloseTo(startPoint, "fuel", 50, 0.1);
//
//        RadiusMarkerClusterer poiMarkers = new RadiusMarkerClusterer(this);
//        map.getOverlays().add(poiMarkers);
//
//        Drawable clusterIconD = getResources().getDrawable(R.drawable.green_cluster_logo, null);
//        Bitmap clusterIcon = ((BitmapDrawable) clusterIconD).getBitmap();
//
//        poiMarkers.setIcon(clusterIcon);
////      Below sets the POI marker with the title, description etc. It also uses the shield from
////      the CrimeMap logo as a marker.
//        Drawable poiIcon = getResources().getDrawable(R.mipmap.shield_marker, null);
//        for (POI poi : pois) {
//            Marker poiMarker = new Marker(map);
//            poiMarker.setTitle(poi.mType);
//            poiMarker.setSnippet(poi.mDescription);
//            poiMarker.setPosition(poi.mLocation);
//            poiMarker.setIcon(poiIcon);
//            if (poi.mThumbnail != null) {
////                poiItem.setImage(new BitmapDrawable(poi.mThumbnail));
////                poiMarker.setImage(new BitmapDrawable(R.mipmap.shield_marker, poi.mThumbnail));
//                poiMarker.setImage(getDrawable(R.mipmap.shield_marker));
//            }
//            poiMarkers.add(poiMarker);
//        }
//
////        RadiusMarkerClusterer poiMarkers = new RadiusMarkerClusterer(this);
//
////        Drawable clusterIconD = getResources().getDrawable(R.drawable.green_cluster_logo);
//
//
//        map.invalidate();
//
//////      Code below is used to make a side menu.
//////      MaterialDrawer - Mike Penz - Github
//
//        new DrawerBuilder().withActivity(this).build();
////
////        // if you want to update the items at a later time it is recommended to keep it in a variable
//        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName(R.string.Compare);
//        SecondaryDrawerItem item2 = new SecondaryDrawerItem().withIdentifier(2).withName(R.string.About);
////        Log.i("Tag", "error");
////
////        // create the drawer and remember the `Drawer` result object
//        Drawer result = new DrawerBuilder()
//                .withActivity(this)
//
//                .addDrawerItems(
//                        item1,
//                        new DividerDrawerItem(),
//                        item2
//                )
//                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
//                    @Override
//                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
//                        // do something with the clicked item :D
//                        return true;
//                    }
//                })
//                .build();


    }
//}




//   Compiling all code into functions to tidy it up.
//   Up to the routes from A to B