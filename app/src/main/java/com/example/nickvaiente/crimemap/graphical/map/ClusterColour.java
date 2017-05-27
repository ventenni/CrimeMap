package com.example.nickvaiente.crimemap.graphical.map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.example.nickvaiente.crimemap.R;

import org.osmdroid.bonuspack.clustering.RadiusMarkerClusterer;
import org.osmdroid.bonuspack.clustering.StaticCluster;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

/**
 * Created by Ken on 5/19/2017.
 */

public class ClusterColour extends RadiusMarkerClusterer {
    private Drawable greenIcon;
    private Drawable yellowIcon;
    private Drawable redIcon;

    public ClusterColour(Context context) {
        super(context);
        this.greenIcon = context.getDrawable(R.mipmap.green);
        this.yellowIcon = context.getDrawable(R.mipmap.yellow);
        this.redIcon = context.getDrawable(R.mipmap.red);
    }

    @Override public Marker buildClusterMarker(StaticCluster cluster, MapView mapView) {
        Marker m = new Marker(mapView);

        m.setPosition(cluster.getPosition());
        m.setInfoWindow(null);
        m.setAnchor(mAnchorU, mAnchorV);

        Bitmap finalIcon = Bitmap.createBitmap(mClusterIcon.getWidth(), mClusterIcon.getHeight(), mClusterIcon.getConfig());
        int size = cluster.getSize();
        if (size <= 10) {
            finalIcon = ((BitmapDrawable) this.greenIcon).getBitmap();
        } else if (size > 10 && size <= 20) {
            finalIcon = ((BitmapDrawable) this.yellowIcon).getBitmap();
        } else {
            finalIcon = ((BitmapDrawable) this.redIcon).getBitmap();
        }
        Canvas iconCanvas = new Canvas(finalIcon);
        iconCanvas.drawBitmap(mClusterIcon, 0, 0, null);
        String text = "" + cluster.getSize();
        int textHeight = (int) (mTextPaint.descent() + mTextPaint.ascent());
        iconCanvas.drawText(text,
                mTextAnchorU * finalIcon.getWidth(),
                mTextAnchorV * finalIcon.getHeight() - textHeight / 2,
                mTextPaint);
        m.setIcon(new BitmapDrawable(mapView.getContext().getResources(), finalIcon));

        return m;
    }
}
