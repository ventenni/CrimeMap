package com.example.nickvaiente.crimemap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
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
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nickvaiente.crimemap.OSM.OpenStreetMap;
import com.example.nickvaiente.crimemap.QPS.QueenslandPoliceService;
import com.example.nickvaiente.crimemap.graphical.graphs.MainGraphs;
import com.example.nickvaiente.crimemap.graphical.map.Map;
import com.example.nickvaiente.crimemap.graphical.map.Routes;
import com.example.nickvaiente.crimemap.graphical.pages.AboutLayoutActivity;
import com.example.nickvaiente.crimemap.graphical.pages.HelpLayoutActivity;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.infowindow.InfoWindow;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static android.widget.Toast.LENGTH_SHORT;

public class MainActivity extends Activity {

    public static final int NUMBER_OF_CRIMES = 17;
    final String mapquestApi = "EH5HAxcHJmAN9sf02T6PJA2VDCJ9Tgru"; // MAKE PRIVATE AS WELL?

    public static Context context;
    private com.mikepenz.materialdrawer.Drawer slideMenu;
    private String searchInput;
    private LocationManager locationManager;
    private String latitude = "-28.002373";
    private String longitude = "153.4145987";
    private MapView mapView;
    private Boolean statisticsOpen = false;
    OpenStreetMap test1 = OpenStreetMap.getInstance();
    QueenslandPoliceService test2 = QueenslandPoliceService.getInstance();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        try {
            if (Build.VERSION.SDK_INT >= 23) {
                int timer;
                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    Log.i("permission" + " 0", "tom");
                } else {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
//                    timer = 0;
//                    while (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && timer < 10) {
//                        TimeUnit.SECONDS.sleep(1);
//                        timer++;
//                    }
                }

                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    Log.i("permission" + " 0", "tom");
                } else {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
//                    timer = 0;
//                    while (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && timer < 10) {
//                        TimeUnit.SECONDS.sleep(1);
//                        timer++;
//                    }
                }

                if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    Log.i("permission" + " 0", "tom");
                } else {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
//                    timer = 0;
//                    while (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && timer < 10) {
//                        TimeUnit.SECONDS.sleep(1);
//                        timer++;
//                    }
                }

                if (checkSelfPermission(Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED) {
                    Log.i("permission" + " 0", "tom");
                } else {
                    requestPermissions(new String[]{Manifest.permission.INTERNET}, 1);
//                    timer = 0;
//                    while (checkSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED && timer < 10) {
//                        TimeUnit.SECONDS.sleep(1);
//                        timer++;
//                    }
                }
            }
//        }
//        catch (InterruptedException e){
//            Log.i("Print", "Interrupted Exception in Main permissions section");
//        }
//        OpenStreetMap.getInstance().loadSuburbBoundary(getAssets());
        setContentView(R.layout.activity_main);
        MainActivity.context = getApplicationContext();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //    public static Context context;

        long defaultTimePeriod = 15778463;
        String defaultOffences = "1,8,14,17,21,27,28,29,30,35,39,45,47,51,52,54,55";
        updateGPS();

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

        final FloatingActionButton statisticsButton = (FloatingActionButton) findViewById(R.id.statisticsButton);
        final FloatingActionButton statisticsBackButton = (FloatingActionButton) findViewById(R.id.statisticsBackButton);
        final FloatingActionButton gpsButton = (FloatingActionButton) findViewById(R.id.gpsButton);

        mapView = (MapView) findViewById(R.id.mapImage);
        mapView.getOverlays().clear();
        final Map map = new Map(mapView, Double.parseDouble(latitude), Double.parseDouble(longitude));

        QueenslandPoliceService.getInstance().setOffenceTypes(defaultOffences);
        QueenslandPoliceService.getInstance().setTimePeriod(defaultTimePeriod);
        displayMarkers(map);

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

            CheckBox[] checkBoxes = new CheckBox[NUMBER_OF_CRIMES];
            checkBoxes[0] = (CheckBox) findViewById(R.id.checkBox1);   //Homicide R
            checkBoxes[1] = (CheckBox) findViewById(R.id.checkBox8);   //Assault R
            checkBoxes[2] = (CheckBox) findViewById(R.id.checkBox14);  //Robbery Y
            checkBoxes[3] = (CheckBox) findViewById(R.id.checkBox17);  //Other Offences Against People Y
            checkBoxes[4] = (CheckBox) findViewById(R.id.checkBox21);  //Unlawful Entry Y
            checkBoxes[5] = (CheckBox) findViewById(R.id.checkBox27);  //Arson R
            checkBoxes[6] = (CheckBox) findViewById(R.id.checkBox28);  //Other Property Damage Y
            checkBoxes[7] = (CheckBox) findViewById(R.id.checkBox29);  //Unlawful Use of Motor Vehicle W
            checkBoxes[8] = (CheckBox) findViewById(R.id.checkBox30);  //Other Theft W
            checkBoxes[9] = (CheckBox) findViewById(R.id.checkBox35);  //Fraud Y
            checkBoxes[10] = (CheckBox) findViewById(R.id.checkBox39); //Handling Stolen Goods W
            checkBoxes[11] = (CheckBox) findViewById(R.id.checkBox45); //Drug Offence Y
            checkBoxes[12] = (CheckBox) findViewById(R.id.checkBox47); //Liquor (excl. Drunkenness) W
            checkBoxes[13] = (CheckBox) findViewById(R.id.checkBox51); //Weapons Act Offences Y
            checkBoxes[14] = (CheckBox) findViewById(R.id.checkBox52); //Good Order Offence W
            checkBoxes[15] = (CheckBox) findViewById(R.id.checkBox54); //Traffic and Related Offences W
            checkBoxes[16] = (CheckBox) findViewById(R.id.checkBox55); //Other W

            int offenceID[] = {1, 8, 14, 17, 21, 27, 28, 29, 30, 35, 39, 45, 47, 51, 52, 54, 55};

            for (int i = 0; i < NUMBER_OF_CRIMES; i++) {
                if (checkBoxes[i].isChecked()) {
                    offenceTypes += offenceID[i] + ",";
                }
            }

            if (offenceTypes.length() > 0){
                offenceTypes = offenceTypes.substring(0, offenceTypes.length() - 1);
                QueenslandPoliceService.getInstance().setOffenceTypes(offenceTypes);
                if (slideMenu.getCurrentSelectedPosition() == 1 && statisticsOpen){
                    changeView(4);
                } else {
                    changeView(slideMenu.getCurrentSelectedPosition());
                }
                if(slideMenu.getCurrentSelectedPosition() == 1){
                    displayMarkers(map);
                }
                if(slideMenu.getCurrentSelectedPosition() == 2){
                    performCompare();
                }
                slideMenu.getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            } else {
                showToast("Please select an offence type.", 0);
            }


                //Hide the search box and search button when user clicks the filter button
//            if (filterMenu.getVisibility() == View.VISIBLE) {
//                filterMenu.setVisibility(View.GONE);
//                searchMenu.setVisibility(View.VISIBLE);
//            }
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
                performCompare();
            }
        });

        statisticsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeView(4);
                BarChart barChart = (BarChart) findViewById(R.id.homeBarChart);

                try {
                    Compare homeBarChartStats = new Compare();
                    homeBarChartStats.setLocationResult(OpenStreetMap.getInstance().getResult());
                    homeBarChartStats.setOffenceResult(QueenslandPoliceService.getInstance().getOffenceBoundary());

                    homeBarChartStats.plotData();

                    MainGraphs homeLineChart = new MainGraphs();
                    MainGraphs homeBarChart = new MainGraphs();
                    TextView labelHomeBarChartTitle = (TextView) findViewById(R.id.labelHomeLineChart);
                    TextView labelHomeBarChart = (TextView) findViewById(R.id.labelBarChart3);
                    labelHomeBarChartTitle.setText(QueenslandPoliceService.getInstance().getOffenceBoundary().getResult().get(0).getOffenceInfo().get(0).getSuburb());
                    labelHomeBarChart.setText("Offences in " + QueenslandPoliceService.getInstance().getOffenceBoundary().getResult().get(0).getOffenceInfo().get(0).getSuburb());

                    LineChart homeLineChartView = (LineChart) findViewById(R.id.homeLineChart);
                    homeLineChart.createLineChart(homeLineChartView, homeBarChartStats);
                    homeLineChartView.getLegend().setEnabled(false);

                    BarChart homeBarChartView = (BarChart) findViewById(R.id.homeBarChart);
                    homeBarChart.createBarChart(homeBarChartView, homeBarChartStats);
                }
                catch (NullPointerException e){

                }
            }
        });

        statisticsBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeView(1);
            }
        });

        gpsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateGPS();
                displayMarkers(map);
            }
        });

    }

    public static Context getAppContext() {
        return MainActivity.context;
    }

    public static void showToast(String message, int duration) {
        //Display toast message. LENGTH_SHORT = 0, LENGTH_LONG = 1.
        if (duration < 1) {
            Toast.makeText(context, message, LENGTH_SHORT).show();
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
//                        item3,
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
                        if (position == 1) { // If position is "Home"
                            changeView(1);
                        } else if (position == 2){
                            changeView(2);
                        }
//                        else if (position == 2) { // if position is "safest route"
////                            Following If/else statements determine whether the text fields are
////                            visible or not and switches them on/off.
//                            changeView(2);
//                        }
                        else if (position == 3) { // If position is "About"
                            Intent myIntent = new Intent(view.getContext(), AboutLayoutActivity.class);
                            startActivityForResult(myIntent, 0);
                            Log.i("marked", "about");
                            slideMenu.setSelection(0);
                        } else if (position == 4) { // Else the button pressed was "Help"
                            Intent myIntent = new Intent(view.getContext(), HelpLayoutActivity.class);
                            startActivityForResult(myIntent, 0);
                            Log.i("marker", "HELP");
                            slideMenu.setSelection(0);
                        }
                        slideMenu.closeDrawer();
                        return true;
                    }
                })
                .withHeader(R.layout.header_layout)
                .build();


    }

    private void performSearch(Map map){
        EditText searchBox = (EditText) findViewById(R.id.searchBox);
        //                progressBar.setVisibility(View.VISIBLE);
//                searchButton.setVisibility(View.GONE);
        //Read location from search box
        searchInput = searchBox.getText().toString().trim();//.replaceAll("[^a-zA-Z ]", "");
        //User input validation (using suburb name to search OSM and QPS)g
        OpenStreetMap.getInstance().resetInstance();
        Log.i("Print", "Before OSM perform");
        OpenStreetMap.getInstance().performSearch(searchInput);
        Log.i("Print", "After OSM perform");

        if (OpenStreetMap.getInstance().getResult() != null) {
            if (OpenStreetMap.getInstance().getResult().getAddress().getState().equalsIgnoreCase("QLD") || OpenStreetMap.getInstance().getResult().getAddress().getState().equalsIgnoreCase("queensland")) {
                searchBox.setText(searchInput);
                latitude = OpenStreetMap.getInstance().getResult().getLat();
                longitude = OpenStreetMap.getInstance().getResult().getLon();
                String suburb = OpenStreetMap.getInstance().getResult().getAddress().getSuburb();
                List<List<String>> polygonPoints = OpenStreetMap.getInstance().getResult().getPolygonpoints();
                //pass bounding box coords to the function that displays the suburb perimeter
                displayMarkers(map);
            }else{
                showToast("Enter Queensland Suburb", 1);
            }

        } else {
            showToast("No Results. Timed Out :(", 1);
        }
//                progressBar.setVisibility(View.GONE);
//                searchButton.setVisibility(View.VISIBLE);
    }

    private void performCompare(){

        Compare compare1 = new Compare();
        Compare compare2 = new Compare();

        EditText compareBox1 = (EditText) findViewById(R.id.compareBox1);
        EditText compareBox2 = (EditText) findViewById(R.id.compareBox2);

        if(compareBox1.getText().toString().length() > 0 && compareBox2.getText().toString().length() > 0) {
            try {
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

                try {
                    MainGraphs lineGraph = new MainGraphs();
                    TextView labelLineChart = (TextView) findViewById(R.id.labelLineChart);
                    labelLineChart.setText(compare1.getOffenceResult().getResult().get(0).getOffenceInfo().get(0).getSuburb() + " & " + compare2.getOffenceResult().getResult().get(0).getOffenceInfo().get(0).getSuburb());
                    LineChart lineGraphView = (LineChart) findViewById(R.id.lineChart);
                    lineGraph.createLineChart(lineGraphView, compare1, compare2);
                    lineGraphView.fitScreen();

                    MainGraphs barChart1 = new MainGraphs();
                    TextView labelBarChart1 = (TextView) findViewById(R.id.labelBarChart1);
                    labelBarChart1.setText("Offences in " + compare1.getOffenceResult().getResult().get(0).getOffenceInfo().get(0).getSuburb());
                    BarChart barChartView1 = (BarChart) findViewById(R.id.barChart1);
                    barChart1.createBarChart(barChartView1, compare1);

                    MainGraphs barChart2 = new MainGraphs();
                    TextView labelBarChart2 = (TextView) findViewById(R.id.labelBarChart2);
                    labelBarChart2.setText("Offences in " + compare2.getOffenceResult().getResult().get(0).getOffenceInfo().get(0).getSuburb());
                    BarChart barChartView2 = (BarChart) findViewById(R.id.barChart2);
                    barChart2.createBarChart(barChartView2, compare2);
                } catch (IndexOutOfBoundsException e) {
                    showToast("No Results :(", 0);
                }
            } catch (NullPointerException e) {
                showToast("Enter suburbs to compare", 0);
            }
        }
        else {
            showToast("Enter suburbs to compare", 0);
        }

//                MainGraphs pieChart1 = new MainGraphs();
//                PieChart pieChartView1 = (PieChart) findViewById(R.id.pieChartSuburb1);
//                pieChart1.createPieChart(pieChartView1, compare1);
//
//                MainGraphs pieChart2 = new MainGraphs();
//                PieChart pieChartView2 = (PieChart) findViewById(R.id.pieChartSuburb2);
//                pieChart2.createPieChart(pieChartView2, compare2);
//
//                MainGraphs mainGraphs3 = new MainGraphs();
//                setContentView(R.layout.activity_barchart);
//                barChart = (BarChart) findViewById(R.id.barChart);
//                mainGraphs3.createBarChart(barChart);
    }

    private void displayMarkers(Map map){
        QueenslandPoliceService.getInstance().resetInstance();
        Log.i("Print", "Before QPS perform");
        QueenslandPoliceService.getInstance().performSearch(latitude, longitude);
        qpsWaitTimer();
        Log.i("Print", "After QPS timer");
        //clears existing map markers
        mapView.getOverlays().clear();
        InfoWindow.closeAllInfoWindowsOn(mapView);
        map.setLocation(Double.parseDouble(latitude), Double.parseDouble(longitude));
        map.addMarkers();
        //Not using atm because polypoint from OSM is inaccurate and the result is too big causing some searches to not work
        //map.displayPerimeter();
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

        ScrollView graphs = (ScrollView) findViewById(R.id.graphs);

        RelativeLayout homeBarChartMenu = (RelativeLayout) findViewById(R.id.homeBarChartMenu);

        if (page == 1) { // If page is "Home"
            topMenu.setVisibility(View.VISIBLE);
            filterMenu.setVisibility(View.GONE);

            searchButton.setVisibility(View.VISIBLE);
            compareButton.setVisibility(View.GONE);
            routeButton.setVisibility(View.GONE);

            searchMenu.setVisibility(View.VISIBLE);
            compareMenu.setVisibility(View.GONE);
            routeMenu.setVisibility(View.GONE);

            graphs.setVisibility(View.GONE);
            homeBarChartMenu.setVisibility(View.GONE);
            statisticsOpen = false;
            mapView.setVisibility(View.VISIBLE);
        } else if (page == 2) { // If page is "Compare"
            topMenu.setVisibility(View.VISIBLE);
            filterMenu.setVisibility(View.GONE);

            searchButton.setVisibility(View.GONE);
            compareButton.setVisibility(View.VISIBLE);
            routeButton.setVisibility(View.GONE);

            searchMenu.setVisibility(View.GONE);
            compareMenu.setVisibility(View.VISIBLE);
            routeMenu.setVisibility(View.GONE);

            graphs.setVisibility(View.VISIBLE);
            homeBarChartMenu.setVisibility(View.GONE);
            statisticsOpen = false;
            mapView.setVisibility(View.GONE);
        }
//        else if (page == 2) { // if page is "safest route"
//            topMenu.setVisibility(View.VISIBLE);
//            filterMenu.setVisibility(View.GONE);
//
//            searchButton.setVisibility(View.GONE);
//            compareButton.setVisibility(View.GONE);
//            routeButton.setVisibility(View.VISIBLE);
//
//            searchMenu.setVisibility(View.GONE);
//            compareMenu.setVisibility(View.GONE);
//            routeMenu.setVisibility(View.VISIBLE);
//
//            graphs.setVisibility(View.GONE);
//            homeBarChartMenu.setVisibility(View.GONE);
//            statisticsOpen = false;
//            mapView.setVisibility(View.VISIBLE);
//        }
        else if (page == 3) { // if page is "filter"
            topMenu.setVisibility(View.GONE);
            filterMenu.setVisibility(View.VISIBLE);

            searchButton.setVisibility(View.GONE);
            compareButton.setVisibility(View.GONE);
            routeButton.setVisibility(View.GONE);

            searchMenu.setVisibility(View.GONE);
            compareMenu.setVisibility(View.GONE);
            routeMenu.setVisibility(View.GONE);

            graphs.setVisibility(View.GONE);
            homeBarChartMenu.setVisibility(View.GONE);
            mapView.setVisibility(View.GONE);
        } else if (page == 4) { // if page is "home page statistics"
            topMenu.setVisibility(View.VISIBLE);
            filterMenu.setVisibility(View.GONE);

            searchButton.setVisibility(View.INVISIBLE);
            compareButton.setVisibility(View.GONE);
            routeButton.setVisibility(View.GONE);

            searchMenu.setVisibility(View.GONE);
            compareMenu.setVisibility(View.GONE);
            routeMenu.setVisibility(View.GONE);

            graphs.setVisibility(View.GONE);
            homeBarChartMenu.setVisibility(View.VISIBLE);
            statisticsOpen = true;
            mapView.setVisibility(View.GONE);
        }
    }

    public void updateGPS() throws SecurityException{
        Boolean enabled = true;
        int timer = 0;
        if (Build.VERSION.SDK_INT >= 23) {
            enabled = false;
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                enabled = true;
            }
            else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                timer = 0;
                while (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED || timer < 10) {
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    }
                    catch (InterruptedException e){
                        Log.i("Print", "Interrupted Exception in updateGPS");
                    }
                    timer++;
                }
            }
        }
        if (enabled) {
            Boolean test1 = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location locationNet = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            List<String> providers = locationManager.getProviders(true);
            Location bestLocation = null;
            for (String provider : providers) {
                Location l = locationManager.getLastKnownLocation(provider);
                if (l == null) {
                    continue;
                }
                if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                    // Found best last known location: %s", l);
                    bestLocation = l;
                }
            }
            if (bestLocation != null) {
                latitude = bestLocation.getLatitude() + "";
                longitude = bestLocation.getLongitude() + "";
            }
        }

        //        long GPSLocationTime = 0;
        //        if (null != locationGPS) { GPSLocationTime = locationGPS.getTime(); }
        //
        //        long NetLocationTime = 0;
        //
        //        if (null != locationNet) {
        //            NetLocationTime = locationNet.getTime();
        //        }
        //
        //        if ( 0 < GPSLocationTime - NetLocationTime ) {
        //            latitude = locationGPS.getLatitude() + "";
        //            longitude = locationGPS.getLongitude() + "";
        //        }
        //        else {
        //            latitude = locationNet.getLatitude() + "";
        //            longitude = locationNet.getLongitude() + "";
        //        }

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
