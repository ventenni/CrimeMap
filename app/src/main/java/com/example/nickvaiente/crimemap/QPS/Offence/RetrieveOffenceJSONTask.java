package com.example.nickvaiente.crimemap.QPS.Offence;

import android.util.Log;

import org.springframework.web.client.RestTemplate;

import entity.geography.Success;
import entity.offence.OffenceBoundary;

import static java.lang.String.format;

/**
 * Created by cherr on 3/05/2017.
 */

public class RetrieveOffenceJSONTask
{
    private static final String NAME_URL = "https://data.police.qld.gov.au/api/boundary?name=%s&returngeometry=true&maxresults=%s";
    private static final String LOCATION_URL = "https://data.police.qld.gov.au/api/boundary?latitude=%d&longitude=%d&maxresults=";
    public static final String OFFENCE_URL = "https://data.police.qld.gov.au/api/qpsmeshblock?boundarylist=1_6989,1_6428&startdate=1364466223&enddate=1372415023&offences=8,14";

    public OffenceBoundary doInBackground(String... params) {
        RestTemplate restTemplate = new RestTemplate(true);
        try {
            //OffenceBoundary class example
            OffenceBoundary offenceBoundary = restTemplate.getForObject(params[0], OffenceBoundary.class);
            Log.i("OffenceBoundary", offenceBoundary.getResult().get(0).getOffenceInfo().get(0).getPostcode());
            return offenceBoundary;
        } catch(
                Exception ex)

        {
            Log.e("Error", ex.getMessage());
            ex.printStackTrace();
        }
        return null;
    }
}
