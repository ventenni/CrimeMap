package com.example.nickvaiente.crimemap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nickvaiente.crimemap.OSM.OpenStreetMap;
import com.example.nickvaiente.crimemap.QPS.QueenslandPoliceService;
import com.example.nickvaiente.crimemap.graphical.graphs.MainGraphs;
import com.example.nickvaiente.crimemap.graphical.map.Map;
import com.example.nickvaiente.crimemap.graphical.pages.AboutLayoutActivity;
import com.example.nickvaiente.crimemap.graphical.pages.HelpLayoutActivity;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.util.concurrent.TimeUnit;

public class MainActivity extends Activity {

    final String mapquestApi = "EH5HAxcHJmAN9sf02T6PJA2VDCJ9Tgru"; // MAKE PRIVATE AS WELL?

    private com.mikepenz.materialdrawer.Drawer slideMenu;
    private String searchInput;
    private String latitude = "-28.002373";
    private String longitude = "153.4145987";
    private MapView mapView;
    public static Context context;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainActivity.context = getApplicationContext();
        //    public static Context context;

        long defaultTimePeriod = 15778463;
        String defaultOffences = "1,8,14,17,21,27,28,29,30,35,39,45,47,51,52,54,55";

        final EditText searchBox = (EditText) findViewById(R.id.searchBox);
        final ImageButton toggleButton = (ImageButton) findViewById(R.id.toggleButton);
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        final Button searchButton = (Button) findViewById(R.id.searchButton);
        final Button routeButton = (Button) findViewById(R.id.routeButton);
        final Button compareButton = (Button) findViewById(R.id.compareButton);
        final ImageButton filterButton = (ImageButton) findViewById(R.id.filterButton);
        final SeekBar timePeriod = (SeekBar) findViewById(R.id.timePeriod);
        final Button submitFilterButton = (Button) findViewById(R.id.submitFilterButton);

        final LinearLayout searchMenu = (LinearLayout) findViewById(R.id.searchMenu);
        final LinearLayout filterMenu = (LinearLayout) findViewById(R.id.filterMenu);
//        OpenStreetMap.getInstance();
        QueenslandPoliceService.getInstance().setOffenceTypes(defaultOffences);
        QueenslandPoliceService.getInstance().setTimePeriod(defaultTimePeriod);
        mapView = (MapView) findViewById(R.id.map);
        // Placeholder location - Griffith Library
        final Map map = new Map(mapView, -27.962592, 153.379886);
        mapView.getOverlays().clear();
//        addingWaypoints(mapView, mapquestApi, startPoint);

        createDrawer();

        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slideMenu.openDrawer();
            }
        });

        searchBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    Log.i("Print","Start enter pressed on Search");
                    searchBox.clearFocus();
                    //performSearch(map);
                    Log.i("Print","End enter pressed on Search");
                }
                return false;
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.i("Print", "Start searchButton onClickListerner");
                searchBox.clearFocus();
                performSearch(map);
                Log.i("Print", "End searchButton onClickListerner");
            }
        });

        filterButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                slideMenu.getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            //Hide the search box and search button when user clicks the filter button
            changeView(3);
            }
        });

        submitFilterButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            String offenceTypes = "";

            CheckBox[] checkBoxes = new CheckBox[17];
            checkBoxes[0] = (CheckBox) findViewById(R.id.checkBox1); //Homicide
            checkBoxes[1] = (CheckBox) findViewById(R.id.checkBox8); //Assault
            checkBoxes[2] = (CheckBox) findViewById(R.id.checkBox14); //Robbery
            checkBoxes[3] = (CheckBox) findViewById(R.id.checkBox17); //Other Offences Against the Person
            checkBoxes[4] = (CheckBox) findViewById(R.id.checkBox21); //Unlawful Entry
            checkBoxes[5] = (CheckBox) findViewById(R.id.checkBox27); //Arson
            checkBoxes[6] = (CheckBox) findViewById(R.id.checkBox28); //Other Property Damage
            checkBoxes[7] = (CheckBox) findViewById(R.id.checkBox29); //Unlawful Use of Motor Vehicle
            checkBoxes[8] = (CheckBox) findViewById(R.id.checkBox30); //Other Theft
            checkBoxes[9] = (CheckBox) findViewById(R.id.checkBox35); //Fraud
            checkBoxes[10] = (CheckBox) findViewById(R.id.checkBox39); //Handling Stolen Goods
            checkBoxes[11] = (CheckBox) findViewById(R.id.checkBox45); //Drug Offence
            checkBoxes[12] = (CheckBox) findViewById(R.id.checkBox47); //Liquor (excl. Drunkenness)
            checkBoxes[13] = (CheckBox) findViewById(R.id.checkBox51); //Weapons Act Offences
            checkBoxes[14] = (CheckBox) findViewById(R.id.checkBox52); //Good Order Offence
            checkBoxes[15] = (CheckBox) findViewById(R.id.checkBox54); //Traffic and Related Offences
            checkBoxes[16] = (CheckBox) findViewById(R.id.checkBox55); //Other

            int offenceID[] = {1, 8, 14, 17, 21, 27, 28, 29, 30, 35, 39, 45, 47, 51, 52, 54, 55};

            for (int i = 0; i < 17; i++) {
                if (checkBoxes[i].isChecked()) {
                    offenceTypes += offenceID[i] + ",";
                }
            }

            offenceTypes = offenceTypes.substring(0, offenceTypes.length() - 1);

            QueenslandPoliceService.getInstance().setOffenceTypes(offenceTypes);

            //Hide the search box and search button when user clicks the filter button
//            if (filterMenu.getVisibility() == View.VISIBLE) {
//                filterMenu.setVisibility(View.GONE);
//                searchMenu.setVisibility(View.VISIBLE);
//            }
            changeView(slideMenu.getCurrentSelectedPosition());
            slideMenu.getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }
        });

        timePeriod.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                TextView labelTimePeriod = (TextView) findViewById(R.id.labelTimePeriod);

                String[] timePeriodValues = {"1 Week", "1 Month", "3 Months", "6 Months", "1 Year", "2 Years", "3 Years", "4 Years", "5 Years"};
                long[] epochValue = {604800, 2629743, 7889229, 15778458, 31556926, 63113852, 94670778, 126227704, 157784630};

                labelTimePeriod.setText(timePeriodValues[progress]);

                QueenslandPoliceService.getInstance().setTimePeriod(epochValue[progress]);

            }
        });

        compareButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                LineChart lineChart = (LineChart) findViewById(R.id.lineChart);
                lineChart.fitScreen();

                Compare compare1 = new Compare();
                Compare compare2 = new Compare();

                EditText compareBox1 = (EditText) findViewById(R.id.compareBox1);
                EditText compareBox2 = (EditText) findViewById(R.id.compareBox2);

                //Retrieve location info results
                String compareInput1 = compareBox1.getText().toString().trim().replaceAll("[^a-zA-Z ]", "");
                compareBox1.setText(compareInput1);
                OpenStreetMap.getInstance().resetInstance();
                OpenStreetMap.getInstance().performSearch(compareInput1);
                compare1.setLocationResult(OpenStreetMap.getInstance().getResult());
                //Retrieve offence info results  ************Are we using the search input or lat lon for the QPS search
                QueenslandPoliceService.getInstance().resetInstance();
                QueenslandPoliceService.getInstance().performSearch(OpenStreetMap.getInstance().getResult().getLat(), OpenStreetMap.getInstance().getResult().getLon());
                qpsWaitTimer();
                compare1.setOffenceResult(QueenslandPoliceService.getInstance().getOffenceBoundary());


                String compareInput2 = compareBox2.getText().toString().trim().replaceAll("[^a-zA-Z ]", "");
                compareBox2.setText(compareInput2);
                OpenStreetMap.getInstance().resetInstance();
                OpenStreetMap.getInstance().performSearch(compareInput2);
                compare2.setLocationResult(OpenStreetMap.getInstance().getResult());
                QueenslandPoliceService.getInstance().resetInstance();
                QueenslandPoliceService.getInstance().performSearch(OpenStreetMap.getInstance().getResult().getLat(), OpenStreetMap.getInstance().getResult().getLon());
                qpsWaitTimer();
                compare2.setOffenceResult(QueenslandPoliceService.getInstance().getOffenceBoundary());

                compare1.plotData();
                compare2.plotData();

                MainGraphs mainGraphs = new MainGraphs();
                mainGraphs.createLineChart(lineChart, compare1, compare2);
//                MainGraphs mainGraphs2 = new MainGraphs();
//                setContentView(R.layout.activity_piechart);
//                pieChart = (PieChart) findViewById(R.id.pieChart);
//                mainGraphs2.createPieChart(pieChart);
//
//                MainGraphs mainGraphs3 = new MainGraphs();
//                setContentView(R.layout.activity_barchart);
//                barChart = (BarChart) findViewById(R.id.barChart);
//                mainGraphs3.createBarChart(barChart);

//



                //Display graph of suburb comparison
                //pass whatever parameters the graphs need...



            }
        });

    }

    public static Context getAppContext() {
        return MainActivity.context;
    }

    public static void showToast(String message, int duration) {
        //Display toast message. LENGTH_SHORT = 0, LENGTH_LONG = 1.
        if (duration < 1) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }
    }


//        This adds the node icons to each new road for the route.
//        Add a node icon to mipmap folder and use that for node markers
//        Drawable nodeIcon = getResources().getDrawable(R.mipmap.marker, null);
//        for (int i = 0; i < road.mNodes.size(); i++) {
//            RoadNode node = road.mNodes.get(i);
//            Marker nodeMarker = new Marker(mapView);
//            nodeMarker.setPosition(node.mLocation);
//            nodeMarker.setIcon(nodeIcon);
//            nodeMarker.setTitle("Step " + i);
////          Below code adds travel instructions to each marker
//            nodeMarker.setSnippet(node.mInstructions);
//            nodeMarker.setSubDescription(Road.getLengthDurationText(this, node.mLength, node.mDuration));
//            Drawable icon = getResources().getDrawable(R.mipmap.marker, null);
//            nodeMarker.setImage(icon);
//            mapView.getOverlays().add(nodeMarker);
//        }

    //        mapView.invalidate();

    private void createDrawer() {
        new DrawerBuilder().withActivity(MainActivity.this).build();

        // if you want to update the items at a later time it is recommended to keep it in a variable
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName(R.string.Home);
        SecondaryDrawerItem item2 = new SecondaryDrawerItem().withIdentifier(2).withName(R.string.Compare);
        SecondaryDrawerItem item3 = new SecondaryDrawerItem().withIdentifier(2).withName(R.string.Route);
        SecondaryDrawerItem item4 = new SecondaryDrawerItem().withIdentifier(3).withName(R.string.About);
        SecondaryDrawerItem item5 = new SecondaryDrawerItem().withIdentifier(4).withName(R.string.Help);


        // create the SideMenuDrawer and remember the `SideMenuDrawer` result object
        slideMenu = new DrawerBuilder()
                .withActivity(MainActivity.this)
                .addDrawerItems(
                        item1,
                        item2,
                        item3,
                        item4,
                        item5
                )
                .withOnDrawerItemClickListener(new com.mikepenz.materialdrawer.Drawer.OnDrawerItemClickListener() {
                    //              This function is activated when 'Compare' is pressed in the side menu.
//              It will hide the search field, and show the route fields and button.
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        Log.i("marker-View", view.toString());
                        Log.i("marker-position", String.valueOf(position));
                        Log.i("marker-drawerItem", drawerItem.toString());



//              If 'Home' is pressed, set the text fields back to normal
//              and reload the mapView at the original point.
                        if (position == 0) { // If position is "Home"
                            changeView(0);
                        } else if (position == 1){
                            changeView(1);
                        } else if (position == 2) { // if position is "safest route"
//                            Following If/else statements determine whether the text fields are
//                            visible or not and switches them on/off.
                            changeView(2);
                        } else if (position == 3) { // If position is "About"
                            Intent myIntent = new Intent(view.getContext(), AboutLayoutActivity.class);
                            startActivityForResult(myIntent, 0);
                            Log.i("marked", "about");
                        } else if (position == 4) { // Else the button pressed was "Help"
                            Intent myIntent = new Intent(view.getContext(), HelpLayoutActivity.class);
                            startActivityForResult(myIntent, 0);
                            Log.i("marker", "HELP");
                        }
                        slideMenu.closeDrawer();
                        return true;
                    }
                })
                .build();

    }

    private void performSearch(Map map){
        EditText searchBox = (EditText) findViewById(R.id.searchBox);
        //                progressBar.setVisibility(View.VISIBLE);
//                searchButton.setVisibility(View.GONE);
        //Read location from search box
        searchInput = searchBox.getText().toString().trim().replaceAll("[^a-zA-Z ]", "");
        //User input validation (using suburb name to search OSM and QPS)g
        OpenStreetMap.getInstance().resetInstance();
        Log.i("Print", "Before OSM perform");
        OpenStreetMap.getInstance().performSearch(searchInput);
        Log.i("Print", "After OSM perform");

        if (OpenStreetMap.getInstance().getResult() != null) {
            searchBox.setText(searchInput);
            latitude = OpenStreetMap.getInstance().getResult().getLat();
            longitude = OpenStreetMap.getInstance().getResult().getLon();
            QueenslandPoliceService.getInstance().resetInstance();
            Log.i("Print", "Before QPS perform");
            QueenslandPoliceService.getInstance().performSearch(latitude, longitude);
            qpsWaitTimer();
            Log.i("Print", "After QPS timer");
            //clears existing map markers
            mapView.getOverlays().clear();
            map.setLocation(Double.parseDouble(latitude), Double.parseDouble(longitude));
            map.addMarkers();
        } else {
            showToast("No Results. Timed Out :(", 1);
        }
//                progressBar.setVisibility(View.GONE);
//                searchButton.setVisibility(View.VISIBLE);
    }

    private void qpsWaitTimer(){
        int sleepDuration = 0;
        while (QueenslandPoliceService.getInstance().getOffenceBoundary() == null && sleepDuration < 10) {
            String loading;
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                Log.d("Print", "Interrupted exception in main");
            }
//            if (sleepDuration % 3 == 0) {
//                loading = "Loading.  ";
//            } else if (sleepDuration % 3 == 0) {
//                loading = "Loading.. ";
//            } else {
//                loading = "Loading...";
//            }
//            showToast(loading, 0);
            sleepDuration += 1;
        }
    }

    private void changeView(int page) {
        Button searchButton = (Button) findViewById(R.id.searchButton);
        Button routeButton = (Button) findViewById(R.id.routeButton);
        Button compareButton = (Button) findViewById(R.id.compareButton);

        LinearLayout topMenu = (LinearLayout) findViewById(R.id.topMenu);
        LinearLayout filterMenu = (LinearLayout) findViewById(R.id.filterMenu);

        LinearLayout searchMenu = (LinearLayout) findViewById(R.id.searchMenu);
        LinearLayout compareMenu = (LinearLayout) findViewById(R.id.compareMenu);
        LinearLayout routeMenu = (LinearLayout) findViewById(R.id.routeMenu);

        LineChart lineChart = (LineChart) findViewById(R.id.lineChart);

        if (page == 0) { // If page is "Home"
            topMenu.setVisibility(View.VISIBLE);
            filterMenu.setVisibility(View.GONE);

            searchButton.setVisibility(View.VISIBLE);
            compareButton.setVisibility(View.GONE);
            routeButton.setVisibility(View.GONE);

            searchMenu.setVisibility(View.VISIBLE);
            compareMenu.setVisibility(View.GONE);
            routeMenu.setVisibility(View.GONE);

            lineChart.setVisibility(View.GONE);
            mapView.setVisibility(View.VISIBLE);
        } else if (page == 1) { // If page is "Compare"
            topMenu.setVisibility(View.VISIBLE);
            filterMenu.setVisibility(View.GONE);

            searchButton.setVisibility(View.GONE);
            compareButton.setVisibility(View.VISIBLE);
            routeButton.setVisibility(View.GONE);

            searchMenu.setVisibility(View.GONE);
            compareMenu.setVisibility(View.VISIBLE);
            routeMenu.setVisibility(View.GONE);

            lineChart.setVisibility(View.VISIBLE);
            mapView.setVisibility(View.GONE);
        } else if (page == 2) { // if page is "safest route"
            topMenu.setVisibility(View.VISIBLE);
            filterMenu.setVisibility(View.GONE);

            searchButton.setVisibility(View.GONE);
            compareButton.setVisibility(View.GONE);
            routeButton.setVisibility(View.VISIBLE);

            searchMenu.setVisibility(View.GONE);
            compareMenu.setVisibility(View.GONE);
            routeMenu.setVisibility(View.VISIBLE);

            lineChart.setVisibility(View.GONE);
            mapView.setVisibility(View.VISIBLE);
        } else if (page == 3) { // if page is "safest route"
            topMenu.setVisibility(View.GONE);
            filterMenu.setVisibility(View.VISIBLE);

            searchButton.setVisibility(View.GONE);
            compareButton.setVisibility(View.GONE);
            routeButton.setVisibility(View.GONE);

            searchMenu.setVisibility(View.GONE);
            compareMenu.setVisibility(View.GONE);
            routeMenu.setVisibility(View.GONE);

            lineChart.setVisibility(View.GONE);
            mapView.setVisibility(View.GONE);
        }
    }


//    void addingWaypoints(MapView map, GeoPoint startPoint) {
//
//        Routes route = new Routes();
//        //      Placeholder route locations - Griffith Library to Harbour Town
//        GeoPoint extraPoint = new GeoPoint(-27.926252, 153.382916);
//
//        Polyline roadOverlay = route.overlayRoutes(startPoint, extraPoint);
////        Adds road overlay
//        map.getOverlays().add(roadOverlay);
//
//        map.invalidate();
//
//    }
}
