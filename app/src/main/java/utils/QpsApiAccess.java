package utils;

import android.os.AsyncTask;
import android.util.Log;

import com.example.nickvaiente.crimemap.QPS.Geography.RetrieveGeographyJSONTask;
import com.example.nickvaiente.crimemap.QPS.Offence.RetrieveOffenceJSONTask;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import entity.geography.Success;
import entity.offence.OffenceBoundary;

import static java.lang.String.format;

/**
 * Created by cherr on 3/05/2017.
 */

public class QpsApiAccess extends AsyncTask<String,Void,Void>
{
    private static final String NAME_URL = "https://data.police.qld.gov.au/api/boundary?name=%s&returngeometry=true&maxresults=%s";
    private static final String LOCATION_URL = "https://data.police.qld.gov.au/api/boundary?latitude=%s&longitude=%s&maxresults=%s";
    public static final String OFFENCE_URL = "https://data.police.qld.gov.au/api/qpsmeshblock?boundarylist=%s&startdate=%s&enddate=%s&offences=%s";

    String name = "Rock";
    Double latitude = -27.5;
    Double longitude = 153.0;
    int maxResults = 1;

    @Override
    protected Void doInBackground(String... params) {
        String url = format(NAME_URL, params[0], maxResults);
//        String url2 = format(LOCATION_URL, latitude, longitude, maxResults);
        RetrieveGeographyJSONTask retrieveGeographyJSONTask = new RetrieveGeographyJSONTask();
        Success success = retrieveGeographyJSONTask.doInBackground(url);
//        Success success = new Success();
//        List<Result> list= new ArrayList<>();
//        Result result = new Result();
//        result.setQldPostcodeId(6989);
//        list.add(result);
//        success.setResult(list);

        String boundaryList = getBoundaryList(success);
        long endDate = System.currentTimeMillis() / 1000L;
        long startDate = getStartDate(-28);
        String filters = "1";

        String offenceUrl = format(OFFENCE_URL, boundaryList, startDate, endDate, filters);

        RetrieveOffenceJSONTask retrieveOffenceJSONTask = new RetrieveOffenceJSONTask();
        OffenceBoundary offenceBoundary = retrieveOffenceJSONTask.doInBackground(offenceUrl);

        Log.i("OffenceBoundary", offenceBoundary.getResult().get(0).getOffenceInfo().get(0).getOffenceCount().toString());
        return null;
    }

    private String getBoundaryList(Success success) {
        List<Integer> ids = new ArrayList<>();
        ids.add(success.getResult().get(0).getQldSuburbId());
        ids.add(success.getResult().get(0).getQldPostcodeId());
        ids.add(success.getResult().get(0).getLocalGovtAreaId());
        ids.add(success.getResult().get(0).getQpsAreaId());
        String result = "";
        for (int i = 0; i < 4; i++){
            if(ids.get(i) != null){
                int id = i+1;
                result = result + id + "_" + ids.get(i)+",";
            }
        }
        result = result.substring(0,result.length()-1);
        return result;
    }

    public long getStartDate(int days)
    {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, days);
        return c.getTime().getTime() / 1000L;
    }
}
