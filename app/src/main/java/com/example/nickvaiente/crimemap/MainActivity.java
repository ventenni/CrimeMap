package com.example.nickvaiente.crimemap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nickvaiente.crimemap.OSM.OpenStreetMap;
import com.example.nickvaiente.crimemap.QPS.QueenslandPoliceService;
import com.example.nickvaiente.crimemap.graphical.map.Map;
import com.example.nickvaiente.crimemap.graphical.pages.AboutLayoutActivity;
import com.example.nickvaiente.crimemap.graphical.pages.HelpLayoutActivity;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.util.concurrent.TimeUnit;

public class MainActivity extends Activity {

    final String mapquestApi = "EH5HAxcHJmAN9sf02T6PJA2VDCJ9Tgru"; // MAKE PRIVATE AS WELL?

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
        final Button searchButton = (Button) findViewById(R.id.searchButton);
        final Button filterButton = (Button) findViewById(R.id.filterButton);
        final SeekBar timePeriod = (SeekBar) findViewById(R.id.timePeriod);
        final Button submitFilterButton = (Button) findViewById(R.id.submitFilterButton);

        final LinearLayout searchMenu = (LinearLayout) findViewById(R.id.searchMenu);
        final LinearLayout filterMenu = (LinearLayout) findViewById(R.id.filterMenu);

        QueenslandPoliceService.getInstance().setOffenceTypes(defaultOffences);
        QueenslandPoliceService.getInstance().setTimePeriod(defaultTimePeriod);
        mapView = (MapView) findViewById(R.id.map);
        // Placeholder location - Griffith Library
        final Map map = new Map(mapView, -27.962592, 153.379886);

//        addingWaypoints(mapView, mapquestApi, startPoint);

        createDrawer();

        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Read location from search box
                searchInput = searchBox.getText().toString().trim().replaceAll("[^a-zA-Z ]", "");
                //User input validation (using suburb name to search OSM and QPS)g
                OpenStreetMap.getInstance().performSearch(searchInput);

                if (OpenStreetMap.getInstance().getResult() != null) {
                    searchBox.setText(searchInput);
                    latitude = OpenStreetMap.getInstance().getResult().getLat();
                    longitude = OpenStreetMap.getInstance().getResult().getLon();

                    QueenslandPoliceService.getInstance().performSearch(latitude, longitude);
                    int sleepDuration = 0;
                    while (QueenslandPoliceService.getInstance().getOffenceBoundary() == null && sleepDuration < 10) {
                        String loading;
                        try {
                            TimeUnit.SECONDS.sleep(2);
                        } catch (InterruptedException e) {
                            Log.d("Print", "Interrupted exception in main");
                        }
                        if (sleepDuration % 3 == 0) {
                            loading = "Loading.  ";
                        } else if (sleepDuration % 3 == 0) {
                            loading = "Loading.. ";
                        } else {
                            loading = "Loading...";
                        }
                        showToast(loading, 0);
                        sleepDuration += 2;
                    }
                    //clears existing map markers
                    mapView.getOverlays().clear();
                    map.setLocation(Double.parseDouble(latitude), Double.parseDouble(longitude));
                    map.addMarkers();
                } else {
                    showToast("No Results. Timed Out :(", 1);
                }

            }
        });

        filterButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Hide the search box and search button when user clicks the filter button
                if (filterMenu.getVisibility() == View.GONE) {
                    filterMenu.setVisibility(View.VISIBLE);
                    searchMenu.setVisibility(View.GONE);
                }
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
                if (filterMenu.getVisibility() == View.VISIBLE) {
                    filterMenu.setVisibility(View.GONE);
                    searchMenu.setVisibility(View.VISIBLE);
                }
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

                String[] timePeriodValues = {"1 Week", "1 Month", "3 Months", "6 Months", "1 Year", "2 Years", "3 Years", "4 Years", "5 Years", "10 Years"};
                long[] epochValue = {604800, 2629743, 7889229, 15778458, 31556926, 63113852, 94670778, 126227704, 157784630, 315569260};

                labelTimePeriod.setText(timePeriodValues[progress]);

                QueenslandPoliceService.getInstance().setTimePeriod(epochValue[progress]);

            }
        });

    }

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

    void createDrawer() {
        new DrawerBuilder().withActivity(MainActivity.this).build();

        // if you want to update the items at a later time it is recommended to keep it in a variable
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName(R.string.Home);
        SecondaryDrawerItem item2 = new SecondaryDrawerItem().withIdentifier(2).withName(R.string.Route);
        SecondaryDrawerItem item3 = new SecondaryDrawerItem().withIdentifier(3).withName(R.string.About);
        SecondaryDrawerItem item4 = new SecondaryDrawerItem().withIdentifier(4).withName(R.string.Help);


        // create the SideMenuDrawer and remember the `SideMenuDrawer` result object
        com.mikepenz.materialdrawer.Drawer result = new DrawerBuilder()
                .withActivity(MainActivity.this)
                .addDrawerItems(
                        item1,
                        item2,
                        item3,
                        item4
                )
                .withOnDrawerItemClickListener(new com.mikepenz.materialdrawer.Drawer.OnDrawerItemClickListener() {
                    //              This function is activated when 'Compare' is pressed in the side menu.
//              It will hide the search field, and show the route fields and button.
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        Log.i("marker-View", view.toString());
                        Log.i("marker-position", String.valueOf(position));
                        Log.i("marker-drawerItem", drawerItem.toString());

                        EditText start = (EditText) MainActivity.this.findViewById(R.id.start);
                        EditText finish = (EditText) MainActivity.this.findViewById(R.id.end);
                        LinearLayout searchMenu = (LinearLayout) MainActivity.this.findViewById(R.id.searchMenu);
                        Button route_button = (Button) MainActivity.this.findViewById(R.id.route_button);

//              If 'Home' is pressed, set the text fields back to normal
//              and reload the mapView at the original point.
                        if (position == 0) { // If position is "Home"
                            if (start.getVisibility() == View.VISIBLE) {
                                start.setVisibility(View.GONE);
                                finish.setVisibility(View.GONE);
                                route_button.setVisibility(View.GONE);
                                searchMenu.setVisibility(View.VISIBLE);
//                                newMap(mapView);
                            }
                        } else if (position == 1) { // if position is "safest route"
//                            Following If/else statements determine whether the text fields are
//                            visible or not and switches them on/off.
                            if (start.getVisibility() == View.VISIBLE) {
                                start.setVisibility(View.GONE);
                                finish.setVisibility(View.GONE);
                                route_button.setVisibility(View.GONE);
                                searchMenu.setVisibility(View.VISIBLE);
                            } else {
                                start.setVisibility(View.VISIBLE);
                                finish.setVisibility(View.VISIBLE);
                                route_button.setVisibility(View.VISIBLE);
                                searchMenu.setVisibility(View.GONE);
                            }
                        } else if (position == 2) { // If position is "About"
                            Intent myIntent = new Intent(view.getContext(), AboutLayoutActivity.class);
                            startActivityForResult(myIntent, 0);
                            Log.i("marked", "about");
                        } else if (position == 3) { // Else the button pressed was "Help"
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

}
