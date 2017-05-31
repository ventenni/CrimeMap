package com.example.nickvaiente.crimemap.QPS;

import android.os.AsyncTask;
import android.util.Log;

import com.example.nickvaiente.crimemap.QPS.Geography.RetrieveGeographyJSONTask;
import com.example.nickvaiente.crimemap.QPS.Offence.RetrieveOffenceJSONTask;

import com.example.nickvaiente.crimemap.QPS.entity.geography.Success;
import com.example.nickvaiente.crimemap.QPS.entity.offence.OffenceBoundary;

import static java.lang.String.format;

/**
 * Created by cherr on 3/05/2017.
 */

public class QpsApiAccess extends AsyncTask<String,Void,Void>
{
    // API to obtain the boundary ID from Latitude/Longitude
    private static final String LOCATION_URL = "https://data.police.qld.gov.au/api/boundary?latitude=%s&longitude=%s&maxresults=%s";
    // API to obtain offence info from suburb ID
    public static final String OFFENCE_URL = "https://data.police.qld.gov.au/api/qpsmeshblock?boundarylist=%s&startdate=%s&enddate=%s&offences=%s";
    // Default max results for location results
    public static final int MAX_RESULTS = 3;

//   Getting all of the offences from QPS for a given location.
    @Override
    protected Void doInBackground(String... params) {
        if(android.os.Debug.isDebuggerConnected())
            android.os.Debug.waitForDebugger();
        String url = format(LOCATION_URL, params[0], params[1], MAX_RESULTS);

        //  Retrieves and sets values from API calls and sets the QPS Suburb ID to boundaryList
        RetrieveGeographyJSONTask retrieveGeographyJSONTask = new RetrieveGeographyJSONTask();
        retrieveGeographyJSONTask.doInBackground(url);
        Success successResults = QueenslandPoliceService.getInstance().getSuccess();
        String boundaryList = getBoundaryList(successResults);

        // sets the end date to current time
        long endDate = System.currentTimeMillis() / 1000L;

        // sets the start date to whatever is set in the QPS singleton instance
        long startDate = endDate - QueenslandPoliceService.getInstance().getTimePeriod();

        // sets the offence types according to what's been set in the filter
        String filters = QueenslandPoliceService.getInstance().getOffenceTypes();

        String offenceUrl = format(OFFENCE_URL, boundaryList, startDate, endDate, filters);
        RetrieveOffenceJSONTask retrieveOffenceJSONTask = new RetrieveOffenceJSONTask();

        //Gets success results - including polygon boundary for an offence
        retrieveOffenceJSONTask.doInBackground(offenceUrl);

        OffenceBoundary offenceBoundary = QueenslandPoliceService.getInstance().getOffenceBoundary();
        if (offenceBoundary == null) {
            Log.i("OffenceBoundary", "No crimes found");
        }
        return null;
    }

    private String getBoundaryList(Success success) {
        if(success.getResult().size() != 0){
            // 1_ is syntax to identify the following number as a suburb ID
            String result = "1_" + success.getResult().get(1).getQldSuburbId();
            return result;
        } else {
            Log.e("Geography", "Success is empty");
            return "";
        }
    }
}
