package com.example.nickvaiente.crimemap.QPS;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.nickvaiente.crimemap.OSM.OpenStreetMap;
import com.example.nickvaiente.crimemap.QPS.entity.geography.Success;
import com.example.nickvaiente.crimemap.QPS.entity.offence.OffenceBoundary;
import com.example.nickvaiente.crimemap.R;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utils.QpsApiAccess;

import static java.lang.String.format;

/**
 * Created by Tae's Puter on 9/05/2017.
 */

public class QueenslandPoliceService{
    private static QueenslandPoliceService instance = new QueenslandPoliceService();
    private static final String LOCATION_URL = "https://data.police.qld.gov.au/api/boundary?latitude=%s&longitude=%s&maxresults=%s";
    private Success success;
    private OffenceBoundary offenceBoundary;


    public QueenslandPoliceService() {
        this.success = null;
        this.offenceBoundary = null;
    }

    public static QueenslandPoliceService getInstance() {
        return instance;
    }

    public void performSearch(String latitude, String longitude){
        new QpsApiAccess().execute(latitude, longitude);
    }

//    public void performSearch(String name){
//        new QpsApiAccess().execute(name);
//    }


    public Success getSuccess() {
        return success;
    }

    public void setSuccess(Success success) {
        this.success = success;
//        this.success.getAdditionalProperties().putAll(success.getAdditionalProperties());
//        this.success.setMessage(success.getMessage());
//        this.success.getResult().addAll(success.getResult());
//        this.success.setResultCount(success.getResultCount());
//        this.success.setSuccess(success.getSuccess());
    }

    public OffenceBoundary getOffenceBoundary() {
        return this.offenceBoundary;
    }

    public void setOffenceBoundary(OffenceBoundary offenceBoundary) {
        this.offenceBoundary = offenceBoundary;
        if(this.offenceBoundary == null){

        }
    }

    public double getOffenceCoordinates(String geometryWKT, int index){
        // Every even index is a longitude
        // Every odd index is a latitude
        String polygon = geometryWKT;
        Pattern latNlon = Pattern.compile("(-?)[0-9]*\\.[0-9]*");
        Matcher match = latNlon.matcher(polygon);
        String coordinate = "";
        // Increments index position to find Lat and Long
        for(int i = 0; i <= index; i++) {
            match.find();
        }
            coordinate = match.group();
            Log.d("Print matchLat", coordinate);

        return Double.parseDouble(coordinate);
    }
}







