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

import utils.QpsApiAccess;

import static java.lang.String.format;

/**
 * Created by Tae's Puter on 9/05/2017.
 */

public class QueenslandPoliceService{
    private static QueenslandPoliceService instance = new QueenslandPoliceService();
    private static final String LOCATION_URL = "https://data.police.qld.gov.au/api/boundary?latitude=%s&longitude=%s&maxresults=%s";
    private Success success = null;
    private OffenceBoundary offenceBoundary = null;


    public QueenslandPoliceService() {

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
    }

    public OffenceBoundary getOffenceBoundary() {
        return offenceBoundary;
    }

    public void setOffenceBoundary(OffenceBoundary offenceBoundary) {
        this.offenceBoundary = offenceBoundary;
    }
}







