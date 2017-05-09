package com.example.nickvaiente.crimemap.QPS.Geography;

import android.util.Log;

import org.springframework.web.client.RestTemplate;

import javax.net.ssl.HostnameVerifier;

import entity.geography.Success;
import entity.offence.OffenceBoundary;

import static java.lang.String.format;

/**
 * Created by cherr on 3/05/2017.
 */

public class RetrieveGeographyJSONTask
{
    private static final String NAME_URL = "https://data.police.qld.gov.au/api/boundary?name=%s&returngeometry=true&maxresults=%s";
    private static final String LOCATION_URL = "https://data.police.qld.gov.au/api/boundary?latitude=%d&longitude=%d&maxresults=";
    public static final String OFFENCE_URL = "https://data.police.qld.gov.au/api/qpsmeshblock?boundarylist=1_6989,1_6428&startdate=1364466223&enddate=1372415023&offences=8,14";

    private RestTemplate restTemplate;

    public Success doInBackground(String... params) {
        restTemplate = new RestTemplate(true);
        try {
            //this line crashes my Android VM but try it on you machines and see if it works.
            Success success = restTemplate.getForObject(params[0], Success.class);
            Log.i("Result", success.getResult().get(0).getLGA());

            //OffenceBoundary class example
            OffenceBoundary offenceBoundary = restTemplate.getForObject(OFFENCE_URL, OffenceBoundary.class);
            Log.i("OffenceBoundary", offenceBoundary.getResult().get(0).getOffenceInfo().get(0).getPostcode());
            return success;
        } catch(
                Exception ex)

        {
            Log.e("Error", ex.getMessage());
            ex.printStackTrace();
        }
        return null;
    }
}
