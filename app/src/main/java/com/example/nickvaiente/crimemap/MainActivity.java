package com.example.nickvaiente.crimemap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nickvaiente.crimemap.OSM.LocationInfo;
import com.example.nickvaiente.crimemap.OSM.OpenStreetMap;
import com.example.nickvaiente.crimemap.OSM.RetrieveLocationJSONTask;
import com.example.nickvaiente.crimemap.QPS.QueenslandPoliceService;
import com.example.nickvaiente.crimemap.QPS.entity.offence.OffenceBoundary;

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

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utils.QpsApiAccess;

import static java.lang.String.format;


public class MainActivity extends Activity {
    private static final String NAME_URL = "https://data.police.qld.gov.au/api/boundary?name=%s&returngeometry=true&maxresults=%s";
    private static final String LOCATION_URL = "https://data.police.qld.gov.au/api/boundary?latitude=%s&longitude=%s&maxresults=%s";
    private static final String OFFENCE_URL = "https://data.police.qld.gov.au/api/boundary?name=%s&returngeometry=true&maxresults=%s";
    private String searchInput;
    public String latitude = "-28.002373";
    public String longitude = "153.4145987";
    public static Context context;
    // Placeholder location - Griffith Library
    final GeoPoint startPoint = new GeoPoint(-27.962592, 153.379886);

    final String mapquestApi = "EH5HAxcHJmAN9sf02T6PJA2VDCJ9Tgru"; // MAKE PRIVATE AS WELL?

    MapView map;

    public static Context context;
    GoogleApiClient mGoogleApiClient;

    @Override
    public void onCreate(Bundle savedInstanceState) {

//      Disable Strict Mode
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);
//        QueenslandPoliceService.getInstance().getOffenceCoordinates(0);
//        QueenslandPoliceService.getInstance().getOffenceCoordinates(1);
        setContentView(R.layout.activity_main);
        MainActivity.context = getApplicationContext();

//        get_location();
        map = (MapView) findViewById(R.id.map);
        newMap(map, startPoint);
        addMarkers(map);
        addingWaypoints(map, mapquestApi, startPoint);
        createDrawer();
        MainActivity.context = getApplicationContext();

    }

//    This function sets the initial map with the placeholder location
    void newMap(MapView map, GeoPoint startpoint) {
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);

        final EditText searchBox = (EditText) findViewById(R.id.searchBox);
        final Button searchButton = (Button) findViewById(R.id.searchButton);

        // Specifies where the map will load
        IMapController mapController = map.getController();
        mapController.setZoom(17);
        mapController.setCenter(startpoint);

        MapView mapView = (MapView) findViewById(R.id.map);
        Map map = new Map(mapView, Double.parseDouble(latitude), Double.parseDouble(longitude));
        // Sets the marker for the starting coordinates
        Marker startMarker = new Marker(map);
        startMarker.setTitle("Start point");
        startMarker.setPosition(startpoint);
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        map.getOverlays().add(startMarker);

//        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.showSoftInput(searchBox, InputMethodManager.SHOW_IMPLICIT);
//        searchBox.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus) {
//
//                }
//            }
//        });
//        searchBox.setImeActionLabel("Search", KeyEvent.KEYCODE_ENTER);


        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Read location from search box
                searchInput = searchBox.getText().toString().trim().replaceAll("[^a-zA-Z]", "");
                searchBox.setText(searchInput);
                //User input validation (using suburb name to search OSM and QPS)g
                OpenStreetMap.getInstance().performSearch(searchInput);

                if(OpenStreetMap.getInstance().getResult() != null) {
                    latitude = OpenStreetMap.getInstance().getResult().getLat();
                    longitude = OpenStreetMap.getInstance().getResult().getLon();

                    QueenslandPoliceService.getInstance().performSearch(latitude, longitude);
//                QueenslandPoliceService.getInstance().performSearch(searchInput);
                    int sleepDuration = 0;
                    while (QueenslandPoliceService.getInstance().getOffenceBoundary() == null && sleepDuration < 10) {
                        String loading;
                        try {
                            TimeUnit.SECONDS.sleep(2);
                        }
                        catch (InterruptedException e){
                            Log.d("Print", "Interrupted exception in main");
                        }
                        if (sleepDuration % 3 == 0){
                            loading = "Loading.  ";
                        } else if (sleepDuration % 3 == 0){
                            loading = "Loading.. ";
                        } else {
                            loading = "Loading...";
                        }
                        showToast(loading, 0);
                        sleepDuration += 2;
                    }
                    OffenceBoundary boundary = QueenslandPoliceService.getInstance().getOffenceBoundary();
                    for(int i = 0; i < boundary.getResultCount(); i++){
                        double latitude = QueenslandPoliceService.getInstance().getOffenceCoordinates(boundary.getResult().get(i).getGeometryWKT(), 0);
                        double longitude = QueenslandPoliceService.getInstance().getOffenceCoordinates(boundary.getResult().get(i).getGeometryWKT(), 1);
                    }
                    MapView mapView = (MapView) findViewById(R.id.map);
                    Map map = new Map(mapView, Double.parseDouble(latitude), Double.parseDouble(longitude));

                } else {
                    showToast("No Results. Timed Out :(", 1);
                }
            }
        });

    }

    public static Context getAppContext() {
        return MainActivity.context;
    }

    public static void showToast (String message, int duration) {
        //Display toast message. LENGTH_SHORT = 0, LENGTH_LONG = 1.
        if (duration < 1) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }
    }
//    this function creates the Route using place holder locations
    void addingWaypoints(MapView map, String mapquestApi, GeoPoint startPoint) {

        //      This makes a call to Mapquest as stated in the osmbonuspack tutorial
        //      Mapquest supports changing routes so they are suited for bicycle or pedestrians
        //      The route is set to pedestrian mode
        //      Mapquest API Key - EH5HAxcHJmAN9sf02T6PJA2VDCJ9Tgru

        RoadManager roadManager = new MapQuestRoadManager(mapquestApi);
        roadManager.addRequestOption("routeType=pedestrian");

        //      Placeholder route locations - Griffith Library to Harbour Town
        GeoPoint extraPoint = new GeoPoint(-27.926252, 153.382916);

        ArrayList<GeoPoint> waypoints = new ArrayList<GeoPoint>();
        waypoints.add(startPoint);
        waypoints.add(extraPoint);

//        Adds road overlay
        Road road = roadManager.getRoad(waypoints);
        Polyline roadOverlay = RoadManager.buildRoadOverlay(road);
        map.getOverlays().add(roadOverlay);

        map.invalidate();

    }

    void addMarkers(MapView map) {
        // TESTING MULTIPLE MARKERS -------------------- WILL EVENTUALLY DELETE
        double[] lat = new double[11];
        double[] lon = new double[11];

        double testLat;
        double testLon;

        lat[0] = -27.961073;
        lon[0] = 153.379700;

        lat[1] = -27.961073;
        lon[1] = 153.383700;

        lat[2] = -27.961073;
        lon[2] = 153.384100;


        // Following code will cluster the markers when zoomed out
//        RadiusMarkerClusterer crimeMarkers = new RadiusMarkerClusterer(this);
//        crimeMarkers.getTextPaint().setTextSize(25 * this.getResources().getDisplayMetrics().density);

        Drawable clusterIconG = getResources().getDrawable(R.mipmap.new_marker_logo_green, null);
        Drawable clusterIconY = getResources().getDrawable(R.mipmap.new_marker_logo_yellow, null);
        Drawable clusterIconR = getResources().getDrawable(R.mipmap.new_marker_logo_red, null);

        Cluster cluster = new Cluster(this);

//        Bitmap clusterIcon = ((BitmapDrawable) clusterIconD).getBitmap();

//        crimeMarkers.setIcon(clusterIcon);

        for (int i = 0; i < 3; i++) {
            testLat = lat[i];
            testLon = lon[i];
            //GeoPoint testStart = new GeoPoint(testLat, testLon);
            GeoPoint testPoint = new GeoPoint(testLat, testLon);

            Marker newMark = new Marker(map);
            newMark.setIcon(getResources().getDrawable(R.drawable.new_marker_logo, null));
            newMark.setPosition(testPoint);
            newMark.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            newMark.setTitle("Possible buried treasure");
//            map.getOverlays().add(newMark);

            cluster.addMarker(newMark);
//            crimeMarkers.add(newMark);

        }

        map.getOverlays().add(cluster.getCluster());

        map.invalidate();

    }

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
    //// Code below is used to make a side menu.
    //// MaterialDrawer - Mike Penz - Github
    void createDrawer() {
        new DrawerBuilder().withActivity(this).build();

        // if you want to update the items at a later time it is recommended to keep it in a variable
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName(R.string.Home);
        SecondaryDrawerItem item2 = new SecondaryDrawerItem().withIdentifier(2).withName(R.string.Route);
        SecondaryDrawerItem item3 = new SecondaryDrawerItem().withIdentifier(3).withName(R.string.About);
        SecondaryDrawerItem item4 = new SecondaryDrawerItem().withIdentifier(4).withName(R.string.Help);


        // create the drawer and remember the `Drawer` result object
        Drawer result = new DrawerBuilder()
                .withActivity(this)
                .addDrawerItems(
                        item1,

                        item2,
                        item3,
                        item4
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
//              This function is activated when 'Compare' is pressed in the side menu.
//              It will hide the search field, and show the route fields and button.
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        Log.i("marker-View", view.toString());
                        Log.i("marker-position", String.valueOf(position));
                        Log.i("marker-drawerItem", drawerItem.toString());

                        EditText start = (EditText) MainActivity.this.findViewById(R.id.start);
                        EditText finish = (EditText) MainActivity.this.findViewById(R.id.end);
                        EditText search = (EditText) MainActivity.this.findViewById(R.id.search);
                        Button route_button = (Button) MainActivity.this.findViewById(R.id.route_button);

//              If 'Home' is pressed, set the text fields back to normal
//              and reload the map at the original point.
                        if (position == 0) { // If position is "Home"
                            if (start.getVisibility() == View.VISIBLE) {
                                start.setVisibility(View.GONE);
                                finish.setVisibility(View.GONE);
                                route_button.setVisibility(View.GONE);
                                search.setVisibility(View.VISIBLE);
//                                newMap(map);
                            }
                        } else if (position == 2) { // if position is "safest route"
//                            Following If/else statements determine whether the text fields are
//                            visible or not and switches them on/off.
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
                        } else if (position == 3) { // If position is "About"
                            Intent myIntent = new Intent(view.getContext(), AboutLayoutActivity.class);
                            startActivityForResult(myIntent, 0);
                            Log.i("marked", "about");
                        } else if (position == 4) { // Else the button pressed was "Help"
                            Intent myIntent = new Intent(view.getContext(), HelpLayoutActivity.class);
                            Log.i("marker", "HELP");
                            startActivityForResult(myIntent, 0);
                            Log.i("marker", "HELP");
                        } else {
                            Log.i("Marker", "Nothing");
                        }
                        return true;
                    }
                })
                .build();
    }

    public static Context getAppContext() { return MainActivity.context;}
}