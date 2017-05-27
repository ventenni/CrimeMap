package com.example.nickvaiente.crimemap.graphical.map;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;

import com.example.nickvaiente.crimemap.R;

import org.osmdroid.bonuspack.clustering.RadiusMarkerClusterer;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.padding;
import static com.example.nickvaiente.crimemap.MainActivity.getAppContext;
import static java.security.AccessController.getContext;

/**
 * Created by nickvaiente on 13/5/17.
 */

public class Cluster {
    private RadiusMarkerClusterer crimeMarkers;
    private List<Marker> markers;
    private Bitmap clusterIcon;
    private Drawable greenIcon;
    private Drawable yellowIcon;
    private Drawable redIcon;

    public Cluster(Context context) {
        this.crimeMarkers = new RadiusMarkerClusterer(context);
//        this.clusterIcon = ((BitmapDrawable) clusterIconD).getBitmap();
//        this.crimeMarkers.setIcon(clusterIcon);
        this.crimeMarkers.getTextPaint().setTextSize(12 * context.getResources().getDisplayMetrics().density);
        this.markers = new ArrayList<Marker>();
        this.greenIcon = context.getDrawable(R.mipmap.green);
        this.yellowIcon = context.getDrawable(R.mipmap.yellow);
        this.redIcon = context.getDrawable(R.mipmap.red);
        crimeMarkers.setMaxClusteringZoomLevel(25);
    }

    public void addMarker(Marker marker) {
        this.markers.add(marker);
    }

    public RadiusMarkerClusterer getCluster(){
        int size = markers.size();

        if (size <= 10) {
            this.clusterIcon = ((BitmapDrawable) this.greenIcon).getBitmap();
        } else if (size > 10 && size <= 20) {
            this.clusterIcon = ((BitmapDrawable) this.yellowIcon).getBitmap();
        } else {
            this.clusterIcon = ((BitmapDrawable) this.redIcon).getBitmap();
        }
        this.crimeMarkers.setIcon(clusterIcon);

        for (Marker marker : markers) {
            crimeMarkers.add(marker);
        }
        return this.crimeMarkers;
    }
}
