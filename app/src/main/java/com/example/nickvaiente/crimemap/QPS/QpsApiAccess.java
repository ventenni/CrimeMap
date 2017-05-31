package com.example.nickvaiente.crimemap.QPS;

import android.os.AsyncTask;
import android.util.Log;

import com.example.nickvaiente.crimemap.OSM.OpenStreetMap;
import com.example.nickvaiente.crimemap.QPS.Geography.RetrieveGeographyJSONTask;
import com.example.nickvaiente.crimemap.QPS.Offence.RetrieveOffenceJSONTask;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.example.nickvaiente.crimemap.QPS.QueenslandPoliceService;
import com.example.nickvaiente.crimemap.QPS.entity.geography.Success;
import com.example.nickvaiente.crimemap.QPS.entity.offence.OffenceBoundary;

import static java.lang.String.format;

/**
 * Created by cherr on 3/05/2017.
 */

public class QpsApiAccess extends AsyncTask<String,Void,Void>
{
    private static final String NAME_URL = "https://data.police.qld.gov.au/api/boundary?name=%s&returngeometry=true&maxresults=%s";
    // API to obtain the boundary ID from Latitude/Longitude
    private static final String LOCATION_URL = "https://data.police.qld.gov.au/api/boundary?latitude=%s&longitude=%s&maxresults=%s";
    // API to obtain offence info from suburb ID
    public static final String OFFENCE_URL = "https://data.police.qld.gov.au/api/qpsmeshblock?boundarylist=%s&startdate=%s&enddate=%s&offences=%s";

    int maxResults = 3;


//   Getting all of the offences from QPS for a given location.
    @Override
    protected Void doInBackground(String... params) {
        if(android.os.Debug.isDebuggerConnected())
            android.os.Debug.waitForDebugger();
        String url = format(LOCATION_URL, params[0], params[1], maxResults);
//        String url = format(NAME_URL, params[0], maxResults);

        RetrieveGeographyJSONTask retrieveGeographyJSONTask = new RetrieveGeographyJSONTask();
        retrieveGeographyJSONTask.doInBackground(url);

        Success successResults = QueenslandPoliceService.getInstance().getSuccess();
        String boundaryList = getBoundaryList(successResults);
        long endDate = System.currentTimeMillis() / 1000L;


        long startDate = endDate - QueenslandPoliceService.getInstance().getTimePeriod();
        String filters = QueenslandPoliceService.getInstance().getOffenceTypes();

        String offenceUrl = format(OFFENCE_URL, boundaryList, startDate, endDate, filters);

        RetrieveOffenceJSONTask retrieveOffenceJSONTask = new RetrieveOffenceJSONTask();

        //Gets success results - including polygon boundary for an offence
        retrieveOffenceJSONTask.doInBackground(offenceUrl);

        OffenceBoundary offenceBoundary = QueenslandPoliceService.getInstance().getOffenceBoundary();
        if(offenceBoundary != null){
            Log.i("OffenceCount", offenceBoundary.getResultCount().toString());
        } else {
            Log.i("OffenceBoundary", "No crimes found");
        }
        return null;
    }

    private String getBoundaryList(Success success) {
//        List<Integer> ids = new ArrayList<>();

        if(success.getResult().size() != 0){
//            ids.add(success.getResult().get(1).getQldSuburbId());
//            ids.add(success.getResult().get(0).getQldPostcodeId());
////            ids.add(null);
//            ids.add(success.getResult().get(2).getLocalGovtAreaId());
////            ids.add(success.getResult().get(0).getQpsAreaId());
//            ids.add(null);
            String result = "1_" + success.getResult().get(1).getQldSuburbId();
//            for (int i = 0; i < 4; i++){
//                if(ids.get(i) != null){
//                    int id = i+1;
//                    result = result + id + "_" + ids.get(i)+",";
//                }
//            }
//            result = result.substring(0,result.length()-1);
            return result;
        } else {
            Log.e("Geography", "Success is empty");
            return "";
        }
    }
}
